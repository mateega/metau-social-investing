package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.example.project.fragments.ChatFragment;
import com.example.project.fragments.DepositFragment;
import com.example.project.fragments.OverviewFragment;
import com.example.project.fragments.SettingsFragment;
import com.example.project.fragments.TradeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN ACTIVITY";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    private FirebaseFirestore db;
    String groupId;
    String groupName;

    protected List<String> members;
    protected MemberAdapter memberAdapter;
    protected List<String> investors;
    protected InvestorAdapter investorAdapter;
    protected ArrayList<Map<String, Object>> messages;
    protected ChatAdapter chatAdapter;
    protected List<ArrayList<String>> holdings;
    protected HoldingAdapter holdingAdapter;
    HashMap<String, String> groupData;

    public String memberCount;
    public String groupAssetCount;
    public String personalAssetCount;
    public String recentTrade;

    public String userId;

    HashMap<String, Object> newUserTrade;

    int currentFragPosition;
    int newFragPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Fade());
        getWindow().setExitTransition(new Fade());
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        members = new ArrayList<>();
        investors = new ArrayList<>();
        memberAdapter = new MemberAdapter(this, members);
        investorAdapter = new InvestorAdapter(this, investors);

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages, groupId);

        holdings = new ArrayList<>();
        holdingAdapter = new HoldingAdapter(this, holdings, groupId);

        groupData = new HashMap<>();

        userId = getIntent().getStringExtra("userId");

        memberCount = getIntent().getStringExtra("memberCount");
        groupAssetCount = getIntent().getStringExtra("groupAssetCount");
        personalAssetCount = getIntent().getStringExtra("personalAssetCount");
        recentTrade = getIntent().getStringExtra("recentTrade");

        newUserTrade = new HashMap<String, Object>();

        currentFragPosition = 1;
        newFragPosition = 1;

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.actionGroupOverview:
                        newFragPosition = 1;
                        fragment = new OverviewFragment();
                        break;
                    case R.id.actionDeposit:
                        newFragPosition = 2;
                        fragment = new DepositFragment();
                        break;
                    case R.id.actionChat:
                        newFragPosition = 3;
                        fragment = new ChatFragment();
                        break;
                    case R.id.actionTrade:
                        newFragPosition = 4;
                        fragment = new TradeFragment();
                        break;
                    case R.id.actionSettings:
                        newFragPosition = 5;
                        fragment = new SettingsFragment();
                        break;
                    default:
                        newFragPosition = 1;
                        fragment = new OverviewFragment();
                        break;
                }
                if (currentFragPosition == newFragPosition) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.no_transition,
                                    R.anim.no_transition,
                                    R.anim.no_transition,
                                    R.anim.no_transition
                            )
                            .replace(R.id.flContainer, fragment).commit();
                } else if (currentFragPosition > newFragPosition) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_left,
                                    R.anim.slide_in_left,
                                    R.anim.slide_out_left
                            )
                            .replace(R.id.flContainer, fragment).commit();
                } else {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_right,
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_right
                            )
                            .replace(R.id.flContainer, fragment).commit();
                }
                currentFragPosition = newFragPosition;
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.actionGroupOverview);

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("notification_topic");
        Log.i(TAG, "user joined notification topic");

        String deepLink = getIntent().getStringExtra("deepLink");
        if (deepLink !=null) {
            Fragment fragment;
            switch (deepLink) {
                case "www.metau.page.link/chat":
                    fragment = new ChatFragment();
                    break;
                case "www.metau.page.link/overview":
                    fragment = new OverviewFragment();
                    break;
                default:
                    fragment = new OverviewFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        }

        db = FirebaseFirestore.getInstance();

        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");

        pullMemberList();
        pullChat();
        pullHoldings();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    ///////////////////////////////////////////
    //   FUNCTIONS BELOW FOR CHAT FRAGMENT   //
    ///////////////////////////////////////////

    public ArrayList<Map<String, Object>> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Map<String, Object>> messages) {
        this.messages = messages;
    }

    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    public void setChatAdapter(ChatAdapter chatAdapter) {
        this.chatAdapter = chatAdapter;
    }

    public void pullChat() {
        db.enableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
        DocumentReference docRef = db.collection("chats").document(groupId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getMetadata().isFromCache()) {
                    Log.i(TAG, "CALLED DATA FROM CACHE");
                } else {
                    Log.i(TAG, "CALLED FIREBASE DATABASE -- CHATS");
                }
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + data);
                        ArrayList<Map<String, Object>> messagesMap = (ArrayList) data.get("messages");
                        messages.addAll(messagesMap);
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        db.disableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    ///////////////////////////////////////////
    // FUNCTIONS BELOW FOR SETTINGS FRAGMENT //
    ///////////////////////////////////////////

    public MemberAdapter getMemberAdapter() {
        return memberAdapter;
    }

    public void setMemberAdapter(MemberAdapter memberAdapter) {
        this.memberAdapter = memberAdapter;
    }

    public InvestorAdapter getInvestorAdapter() {
        return investorAdapter;
    }

    public void setInvestorAdapter(InvestorAdapter investorAdapter) {
        this.investorAdapter = investorAdapter;
    }

    public void pullMemberList() {
        DocumentReference docRef = db.collection("groups").document(groupId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getMetadata().isFromCache()) {
                    Log.i(TAG, "CALLED DATA FROM CACHE");
                } else {
                    Log.i(TAG, "CALLED FIREBASE DATABASE -- GROUPS");
                }
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, "DocumentSnapshot data: " + data);
                        ArrayList dbMembers = (ArrayList) data.get("members");
                        ArrayList dbInvestors = (ArrayList) data.get("traders");

                        if (dbMembers != null || dbMembers.get(0).equals("")) {
                            members.addAll(dbMembers);
                            memberAdapter.notifyDataSetChanged();
                        }
                        if (dbInvestors != null || dbInvestors.get(0).equals("")) {
                            investors.addAll(dbInvestors);
                            investorAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        db.disableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    ///////////////////////////////////////////
    // FUNCTIONS BELOW FOR DEPOSIT FRAGMENT  //
    ///////////////////////////////////////////

    public void setPersonalAssets(String personalAssets) {
        personalAssetCount = personalAssets;
    }

    ///////////////////////////////////////////
    //  FUNCTIONS BELOW FOR TRADE FRAGMENT   //
    ///////////////////////////////////////////

    public HashMap<String, Object> getNewUserTrade() {
        return newUserTrade;
    }

    public void setNewUserTrade(HashMap<String, Object> newUserTrade) {
        this.newUserTrade = newUserTrade;
    }


    ///////////////////////////////////////////
    // FUNCTIONS BELOW FOR OVERVIEW FRAGMENT //
    ///////////////////////////////////////////

    public HashMap<String, String> getGroupData() {
        HashMap<String,String> groupData = new HashMap<String, String>();
        groupData.put("memberCount", memberCount);
        groupData.put("groupAssets", groupAssetCount);
        groupData.put("personalAssets", personalAssetCount);
        groupData.put("recentTrade", recentTrade);
        return groupData;
    }

    public List<ArrayList<String>> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<ArrayList<String>> holdings) {
        this.holdings = holdings;
    }

    public HoldingAdapter getHoldingAdapter() {
        return holdingAdapter;
    }

    public void setHoldingAdapter(HoldingAdapter holdingAdapter) {
        this.holdingAdapter = holdingAdapter;
    }

    public void pullHoldings() {
        DocumentReference docRef = db.collection("groups").document(groupId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().getMetadata().isFromCache()) {
                    Log.i(TAG, "CALLED DATA FROM CACHE");
                } else {
                    Log.i(TAG, "CALLED FIREBASE DATABASE -- GROUPS");
                }
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        setHoldingsFromActivity(data);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setHoldingsFromActivity(Map<String, Object> data) {
        ArrayList<Map<String, Object>> trades = (ArrayList) data.get("trades");
        HashMap<String, ArrayList<String>> tradesMap = new HashMap<String, ArrayList<String>>();
        // trade array list format: [ticker, qnt. in crypto, qnt. in USD]

        for (int i = 0; i < trades.size(); i++) {
            Map<String, Object> trade = trades.get(i);
            String tradeDirection = trade.get("direction").toString();
            Double tradeLot = Double.valueOf(trade.get("lot").toString());
            Double tradePrice = Double.valueOf(trade.get("price").toString());
            String tradeTicker = trade.get("ticker").toString();

            if (tradesMap.containsKey(tradeTicker)) {
                ArrayList<String> oldTrade = tradesMap.get(tradeTicker);
                String oldLot = oldTrade.get(1);
                String oldSizeUSD = oldTrade.get(2);

                String newTradeLot;
                String newTradeSizeUSD;
                ArrayList<String> tradeList = new ArrayList<String>();
                if (tradeDirection.equals("buy")) {
                    newTradeLot = String.valueOf(Double.parseDouble(oldLot) + tradeLot);
                    newTradeSizeUSD = String.valueOf(Double.parseDouble(oldSizeUSD) + (tradeLot * tradePrice));
                } else {
                    newTradeLot = String.valueOf(Double.parseDouble(oldLot) - tradeLot);
                    newTradeSizeUSD = String.valueOf(Double.parseDouble(oldSizeUSD) - (tradeLot * tradePrice));
                }

                tradeList.add(tradeTicker);
                tradeList.add(newTradeLot);
                tradeList.add(newTradeSizeUSD);
                tradesMap.replace(tradeTicker, tradeList);

            } else {
                if (tradeDirection.equals("buy")) {
                    ArrayList<String> tradeList = new ArrayList<String>();
                    String tradeSizeUSD = String.valueOf(tradeLot * tradePrice);
                    tradeList.add(tradeTicker);
                    tradeList.add(String.valueOf(tradeLot));
                    tradeList.add(tradeSizeUSD);
                    tradesMap.put(tradeTicker, tradeList);
                }
                // I don't believe an else statement is needed here since a group must buy an asset
                // before they sell it. This buy will come earlier in the trade list
            }
        }
        holdings.addAll(tradesMap.values());
        holdingAdapter.notifyDataSetChanged();
    }
}