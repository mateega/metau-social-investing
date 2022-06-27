Original App Design Project - README Template
===

# SOCIAL-INVESTING

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Investing is crypto is hard. Socail-investing aims to make crypto investing easier by allowing users to invest together through groups. Investing groups will invest together by pooling members' cash. Users will have the ability to vote on trade and chat with each other in-app.

### App Evaluation
- **Category:** Finance, social.
- **Mobile:** Mobile-first android App. No website planned.
- **Story:** Investing in crypto is hard. Invest smarter by investing with your friends.
- **Market:** New crypto investors, college students and young adults.
- **Habit:** Users will see notifications to see where their portolio is at or when there is a major change in one of their investments. Users in theory would open the app multiple times a day to check their portolio because of these notifications.
- **Scope:**

0.1: user can join a single investing group, users can deposit paper money to the group, any user can trade on behalf of the group.

0.2: multiple investing groups to join, investing vs non-investing member distinction.

0.3: chat for each group.

[Stretch] 0.4: users can deposit real money, add APIs to trade real money.

[Stretch] 0.5: ability to DM other users, push notifications.

[Stretch] 0.6: explore page to see other groups, news section.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* user can login
* user can create a new account
* user can join multiple investing groups
* user can deposit paper money into a group
* user can make a trade on behalf of the group
* user can be either an investing or non-investing member (ability to make trades or not)
* user can chat others in the group

**Optional Nice-to-have Stories**

* user can deposit real money
* user can trade real money
* user can DM other users
* user can view explore page to find new groups
* user can view news section

### 2. Screen Archetypes

* login page
    *  user can login
*  registration page
    *   user can create a new account
*  groups page
    *  user can join multiple investing groups
    *  user can view multiple investing groups
*  group overview page
    *  overview of the group and its returns
*  deposit page
    *  user can deposit paper money into a group
*  trade page
    *  user can make a trade on behalf of the group
*  group setting page
    *  user can be either an investing or non-investing member (ability to make trades or not)
    *  ability to tongle members and investing or non-investing
*  chat page
    *  user can chat others in the group

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* group overview page
* deposit page
* trade page
* group setting page
* chat page

**Flow Navigation** (Screen to Screen)

* login page
    * groups page
* registration page
    * groups page
* groups page
    * group overview page
    * deposit page
    * trade page
    * group setting page
    * chat page
* group overview, deposit, trade, group setting, chat
    * <>group overview, deposit, trade, group setting, chat<>

## Wireframes

https://github.com/mateega/metau-social-investing/blob/main/wireframe.pdf

## Schema 

### Models
User

| Property | Type | Description |
| -------- | -------- | -------- |
| objectId     | String     | email of user -- unique id for the user     |
| name     | String     | user's display name (first and/or last)     |
| password     | String     | password of users account     |
| email     | String     | email of user     |
| profilePicture     | String     | url to the profile picture of user     |
| assets     | Map<String, Number>     | amount of assets user has in total and different funds (eg: <total:10000, fund_1:2000>)


Group

| Property | Type | Description |
| -------- | -------- | -------- |
| objectId     | String     | unqiue id for the group, name of group in lowercase with spaces as underscores     |
| name     | String     | name of group/fund     |
| assets     | Number     | Total assets in fund pooled from users    |
| members     | ArrayList<String>     | list of non-investing members in group     |
| investors     | ArrayList<String?     | list of investing members in group     |
| trades     | ArrayList<Map<String, Object>>     | map of data for each trade -- direction (String, "buy" or "sell"), lot (Number), price (Number), ticker (String), time (Timestamp), trader (String)   |


Chat

| Property | Type | Description |
| -------- | -------- | -------- |
| objectId     | String     | unique id for a group's chat, same value as Group objectId     |
| messages     | ArrayList<Message>     | list of the group's messages     |


Message

| Property | Type | Description |
| -------- | -------- | -------- |
| objectId     | String     | unique id for the message, simply incremented from the previous message (eg: "17")     |
| user     | String     | id of user who sent message     |
| body     | String     | body text of the message     |
| time | TimeStamp | time that the message was sent |


### Networking
For my project I will be using Firebase instead of Parse. After reading more about Firebase and talking with the instructor, I thought that it would be more of a challenge to implement a new database. I also think that Firebase will be helpful to know for future projects. For the login and registration pages below, Firebase authentication is used. I intent to implement email/password login, Google login, and Facebook login.

Within Firebase, I chose to go with Cloud Firestore over Realtime Databse since data is stored as collections of documents. This leads to complex, hierarchical data being easier to organize at scale compared to Realtime Databases which used one large JSON tree to store data. Both Realtime Database and Cloud Firestore are NoSQL Databases.


* login page
    *  (Read/GET) check for user account
*  registration page
    *   (Create/POST) add user account
*  groups page
    *  (Read/GET) query all groups to display group name and asset amount
*  group overview page
    *  (Read/GET) query specific group to get member count, assets count and most recent trade
    *  (Read/GET) query user to get personal asset count
*  deposit page
    *  (Read/GET) query user to get asset count
    *  (Update/PUT) update user's asset count
*  chat page
    *  (Read/GET) query specific chat to get messages
    *  (Create/POST) create a new message
*  trade: search page
    *  no network request needed, only dealing with external (non-database) API(s)
*  trade: info page
    * no network request needed, only dealing with external (non-database) API(s)
*  trade: payment page
    *  (Create/POST) create a new trade for a group
*  group setting page
    *  (Read/GET) query group to get members and investors
    *  (Delete/DELETE) remove user from group if they would like to leave



## Plan of action
1. ~~Schema design~~
2. ~~Set up database (Parse)~~
   1. ~~Users~~
      1. ~~Deposited amount~~
   2. ~~Groups~~
      1. ~~Users~~
      2. ~~Trades~~
      3. ~~Chat~~
3. ~~Add 3 groups to the database~~
4. ~~Configure login~~
5. ~~Configure sign up~~
6. Set up search functionality (API used)
7. Set up trade payment functionality (user buys coin and result is sent to database)
8. ~~Configure group invest amount on overview page~~
9. ~~Configure most recent trade on overview page~~
10. ~~Configure user depositing feature~~
11. ~~Configure personal invest amount on overview page (based on how much the user has deposited in total)~~
12. ~~Set up member count on overview page~~
13. ~~List group members in settings page~~
14. Toggle between investing and non-investing member in settings page [settings page complete]
15. Set up trade info page (TradingView API used) 
~~16. Give trade recommendations on trade page (optional, if I feel that the trade search page is too blank)~~
17. Set up chat for each group
18. Make custom charts for the trade:info page using past data