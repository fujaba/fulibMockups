# Group Account

## Object Model

There is a GroupApp.

There are participants with name Alice and Bob and Carli and Dora.

There are social-events with name SE BBQ and SE X-Mas.

GroupApp has events and is app of SE BBQ and SE X-Mas.

There is a buy-item with name meat and with price 12.23.
There is a buy-item with name beer and with price 13.42.

SE BBQ has items and is event of meat and beer.

Alice has items and is owner of meat and beer. 

## GUI

There is a UI with id login and with description "page Group App". 

There is a UI with id demo
and with description "button - gui01.html | 1 | button > gui02.html || ---".

There is a UI with id title
and with description "Famous Group Account".

Login has content demo and title. 

There is a UI with id button bar and with parent login 
and with description "button login gui01.html | button items | button overview gui13.html".

There is a UI with id nameIn and with parent login 
and with description "input user_name name? || input party party?".

There is a UI with id loginButton and with parent login 
and with description "button ok gui04.html".

![login](gui01.html.png)

There is a parameter with key user_name and with value "Alice".
User_name has owner login.
Demo has description  "button < gui01.html | 2 | button > gui03.html || ---".

![login](gui02.html.png)

There is a parameter with key party and with value "SE-BBQ" and with owner login.
Demo has description  "button < gui02.html | 3 | button > gui04.html || ---".

![login](gui03.html.png)

There is a UI with id items and with description "page Group App". 
Items has content demo, title and button bar. 
Title has description "SE-BBQ".
Demo has description  "button < gui03.html | 4 | button > gui05.html || ---".

There is a UI with id addItems and with parent items
and with description "button add gui05.html".

![items](gui04.html.png)

There is a UI with id add_item and with description "page Group App". 
Add_item has content demo, title and button bar. 
Demo has description  "button < gui04.html | 5 | button > gui06.html || ---".

There is a UI with id itemInputs and with parent add_item
and with description "input item_name item? || input price 0.00? || input owner owner?".

There is a UI with id itemOK and with parent add_item
and with description "button ok gui09.html".

![add_item](gui05.html.png)

There is a parameter with key item_name and with value "beer" and with owner add_item.
Demo has description  "button < gui05.html | 6 | button > gui07.html || ---".

![add_item](gui06.html.png)

There is a parameter with key price and with value "12.00" and with owner add_item.
Demo has description  "button < gui06.html | 7 | button > gui08.html || ---".

![add_item](gui07.html.png)

There is a parameter with key owner and with value "Bob" and with owner add_item.
Demo has description  "button < gui07.html | 8 | button > gui09.html || ---".

![add_item](gui08.html.png)

AddItems has description "button add gui10.html".
There is a UI with id beer_item and with parent items
and with description "cell beer | cell 12.00 | cell Bob".
Demo has description  "button < gui08.html | 9 | button > gui10.html || ---".

![items](gui09.html.png)

Item_name has value "". 
Price has value "".
Owner has value "".
ItemOK has description "button ok gui12.html".
Demo has description  "button < gui09.html | 10 | button > gui11.html || ---".

![add_item](gui10.html.png)

Item_name has value "meat". 
Price has value "14.00".
Owner has value "Alice".
Demo has description  "button < gui10.html | 11 | button > gui12.html || ---".

![add_item](gui11.html.png)

There is a UI with id meat_item and with parent items
and with description "cell meat | cell 14.00 | cell Alice".
Demo has description  "button < gui11.html | 12 | button > gui13.html || ---".

![items](gui12.html.png)

There is a UI with id overview. 
Overview has content demo, title, button bar. 
Demo has description  "button < gui12.html | 13 | button - gui13.html || ---".

There is a UI with id results and with parent overview
and with description "cell Total: | cell 26.00 || cell Share: | cell 13.00".

There is a UI with id saldi and with parent overview
and with description "cell Alice: | cell +1.00 || cell Bob: | cell -1.00".

![overview](gui13.html.png)
