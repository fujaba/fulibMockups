# Scenario Model Classes.

There are Elements
with id   e1,   e2,
with text "E1", "E2".

There are Contents
with id          c1,     c2,
with description "C1",   "C2",
with elements    e1, e2, e1, e2,
with value       "v1",   "v2".

There is a SpriteBoard with id b1.
There are sprites with id s1, s2
and with icon car and home
and with x 4.0 and 16.0
and with y 12.0 and 6.0.

There are Pages
with id          p1,     p2,
with description "P1",   "P2". 
// with content     c1, c2, c1, c2.


There is a WebApp
with id          App,
with description "Desc.",
with content     p1, p2.

B1 has content s1 and s2.
p1 has content c1 and board.
p2 has content c2 and board.

There is a HomeAndCarSpritesApp
with id          ShroomApp,
with description "Shroom Wars".

There is a page with id mainPage.
ShroomApp has content mainPage.

There is a SpriteBoard with id mainBoard.
There is a Content with id headLine and with description "button move car".
MainPage has content headLine and mainBoard.
There are sprites with id car1, home2
and with icon car and home
and with x 4.0 and 16.0
and with y 12.0 and 6.0.

MainBoard has content car1 and home2.


![ShroomApp](shroomwars.svg)

![ShroomApp](shroomwars.html)
