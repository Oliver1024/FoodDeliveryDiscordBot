# DashBot
## Team Orca for 2022 Spring CS5500

**Team Members:** Wen Liao, Sangsang Shao, Kejian Tong, Houming Leng

**Video Presentation:**  _____

**Bot Invitation:**   [link](https://www.discord.com/api/oauth2/authorize?client_id=942640033666580550&permissions=8&scope=bot%20applications.commands)

**Bot Design Documentation:**    [link](https://docs.google.com/document/d/1bOdwu1bHmWbQuZFGMOB2F5kCT1mINrbw/edit)

**Group Shared Folder:** [link](https://drive.google.com/drive/folders/1Jjf900uefhaXrXVbYwHZmeIfLFwHTrH5)

_________

## **Bot Description:**

DashBot is a discord online food ordering bot for users to order foods from different restaurants. It provides several functions such as ordering food, recommending dishes from restaurants, checking order status, checking history orders, and ordering together with other discord members.

**Features Involved:**
Embed Replying Format, Button, Dropdown Selection, Multiple User Interaction

## **Commands:**

For Individual Ordering:
 - **`/neworder`**: start a new order
 - **`/order`**: add dishes into the shopping cart
 - **`/delete`**: delete dishes from current shopping cart
 - **`/deleteorder`:** delete current ongoing order
 - **`/checkout`**: check out the current order

For Group Ordering:
 - **`/grouporder`**: Start a group order which allows all members in the channel to add dishes in the shopping cart
 - **`/groupcheckout`**: Check out a group order

For Info Checking:

 - **`/restaurants`**: Show the list of all restaurants you can order at
 - **`/restaurants cuisine_type`**: Filter restaurants with the given cuisine type
 - **`/showcart`**: show the items in the shopping cart
 - **`/orderstatus`**: show the status of your orders (is preparing/being delivered, delivered)
 - **`/info`**: show the info of the restaurant the user are ordering at
 - **`/info restaurant_name`**: show the info of the given restaurant
 - **`/menu`**: show the menu of the restaurant the user are ordering at
 - **`/menu restaurant_name`**: show the menu of the given restaurant
 - **`/last num_k`**: show last num_k numbers of history orders
 - **`/last num_k restaurant_name`**: show last num_k numbers of history orders at the given restaurant

_____

**Build the Bot Locally:**

    ./gradlew spotlessApply build

**Run the Bot Locally:**
 - replace line 33 in Bot.java file with your bot token.
 - replace line 28 in /service/MongoDBService.java file with this MongoDB link (“mongodb+srv://DdAdimin:5500_orca@cluster0.shnoj.mongodb.net/myFirstDatabase?retryWrites=true&w=majority”) or just use the default link in the file.
_____

**Rule for Code Coverage:**
|line test | branch test |
|--|--|
| >= 80% | >= 80% |
_____
