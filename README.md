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

0.4: users can deposit real money, add APIs to trade real money.

0.5: ability to DM other users.

0.6: explore page to see other groups, news section.

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
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

## Plan of action
1. Schema design
2. Set up database (Parse)
   1. Users
      1. Deposited amount
   2. Groups
      1. Users
      2. Trades
      3. Chat
3. Add 3 groups to the database 
4. Configure login
5. Configure sign up
6. Set up search functionality (API used)
7. Set up trade payment funcitonality (user buys coin and result is sent to database)
8. Configure group invest amount on overview page
9. Configure most recent trade on overview page
10. Configure user depositing feature
11. Configure personal invest amount on overview page (based on how much the user has deposited in total) [deposit page complete]
12. Set up member count on overview page [overview page complete]
13. List group members in settings page
14. Toggle between investing and non-investing member in settings page [settings page complete]
15. Set up trade info page (TradingView API used) 
16. Give trade recommendations on trade page (optional, if I feel that the trade search page is too blank) [trade pages complete]
17. Set up chat for each group 