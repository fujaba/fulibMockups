# Scenario PartyApp GUI

## GUI

There is a WebApp with id PartyApp and with description "Party App".

## First Page

There are Content with id nameField, locationField, whenField and
                  with description "input Name?", "input Where?", "input When?".

There is a Content with id nextButton and with description "button next".

There is a Page with id firstPage
            and with description "Basics | button People | button Items"
            and with content nameField, locationField, whenField, nextButton.

## Add Person Page

There are Content with id personNameField, page3Button and
                  with description "input Name?", "button add".

There is a Page with id addPerson
            and with description "Add Person"
            and with content personNameField, page3Button.

## People Page

There is a Content with id addToPeoples, albertLine
               and with description "button add",
                                    "Albert | + 0.00 Euro".
There is a Content with id carliLine
               and with description "Carli | + 0.00 Euro".
There is a Page with id peoplePage
            and with description "button Basics |  People | button Items"
            and with content addToPeoples, albertLine.

## Add Item Page

There are Content with id itemNameField, itemPriceField, itemOwnerField, itemPageAddButton and
                  with description "input Name?",
                                   "input 0.00 Euro?",
                                   "input Who?",
                                   "button add".

There is a Page with id addItem
            and with description "Add Item"
            and with content itemNameField, itemPriceField, itemOwnerField, itemPageAddButton.

## Items Page

There is a Content with id addToItems, beerLine
               and with description "button add",
                                    "Beer | 12.00 Euro | Albert".
There is a Page with id itemsPage
            and with description "button Basics |  button People | Items"
            and with content addToItems, beerLine.

## Model

There is a PartyService with id partyMan.

There is a Party theParty.

## Scenario

PartyApp has content firstPage.
![PartyApp](app01.html)

// There is a User with name Alice.
// Better: Alice writes ...
We write XMas into value of nameField.
![PartyApp](app02.html)

We write "SE-Lab" into value of locationField.
![PartyApp](app03.html)

We write "Today 20:00" into value of whenField.
![PartyApp](app04.html)

We write value of whenField into partyDate.
We call writePartyData on partyMan with currentParty theParty
and with name "XMas" and with location "SE-Lab" and with date partyDate.
WritePartyData writes "XMas" into name of theParty.
WritePartyData writes "SE-Lab" into location of theParty.
WritePartyData writes date into date of theParty.
WritePartyData answers with "OK".

PartyApp has content addPerson.
![PartyApp](app05.html)

We write "Albert" into value of personNameField.
![PartyApp](app06.html)

// I would like to write:
// Page3Button calls addPerson on partyMan with name "Albert" from value of personNameField.
// oder:
// There is a User Alice.
// Alice calls click on page3Button.
// Click calls addPerson on partyMan ...
We call buildPerson on partyMan with currentParty theParty
and with pname "Albert".
BuildPerson creates a Participant newPerson  with name pname.
TheParty has participants newPerson.
BuildPerson answers with newPerson.

PartyApp has content peoplePage.
![PartyApp](app07.html)

We write "" into value of personNameField.
PartyApp has content addPerson.
![PartyApp](app08.html)

We write "Carli" into value of personNameField.
![PartyApp](app09.html)

We call buildPerson on partyMan with currentParty theParty
and with pname "Carli".
![theParty](theParty.svg)

We write carliLine into content of peoplePage.
PartyApp has content peoplePage.
![PartyApp](app10.html)

PartyApp has content addItem.
![PartyApp](app11.html)

We write "Beer" into value of itemNameField.
![PartyApp](app12.html)

We write "12.00 Euro" into value of itemPriceField.
![PartyApp](app13.html)

We write "Albert" into value of itemOwnerField.
![PartyApp](app14.html)

PartyApp has content itemsPage.
![PartyApp](app15.html)

We write "" into value of itemNameField.
We write "" into value of itemPriceField.
We write "" into value of itemOwnerField.
PartyApp has content addItem.
![PartyApp](app16.html)

We write "Wine" into value of itemNameField.
We write "10.00 Euro" into value of itemPriceField.
We write "Carli" into value of itemOwnerField.
![PartyApp](app17.html)

There is a Content with id wineLine and with description "Wine | 10.00 Euro | Carli".
We write wineLine into content of itemsPage.
PartyApp has content itemsPage.
![PartyApp](app18.html)

We write "Albert | + 1.00 Euro" into description of albertLine.
We write "Carli | - 1.00 Euro" into description of carliLine.
PartyApp has content peoplePage.
![PartyApp](app19.html)

## Mockup

![PartyApp](app00.mockup.html)
![PartyApp](app00.tables.html)
