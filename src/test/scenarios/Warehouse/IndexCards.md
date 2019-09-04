# IndexCards

There are Places with name f1, f2, f3, m1, m2.
There are Areas with name fast, middle, slow. 
Fast has places and is area of f1, f2, f3.
Middle has places m1, m2. 
There is a Warehouse with name area51.
Area51 has areas and is warehouse of fast, middle, slow. 

There are Palette with id eu100, eu200, eu333 and with items 50.
There are Product with name Pumps, Boots, Sneakers. 
Area51 has products and is warehouse of Pumps, Boots, Sneakers.
Pumps has palettes and is product of eu100, eu200. 
Boots has palettes eu333.  

Eu100 has place and is palette of f1. // may I introduce a new assoc within a method body, too? 

We call store with palette eu200 and with place f2.
Store writes eu200 into palette of f2.

We call store with palette eu333 and with place m1. 

There is the object helper.

There is a Line with id line-fast and with icards fast, places of fast.
There is a Line with id line-middle and with icards middle, places of middle.
There is a Line with id line-slow and with icards slow, places of slow.
There is a Line with id head-line.
We write area51 into helper.
We add helper to icards of head-line. 

There is a Section with id bricks-section and with description "<b>Bricks and Mortar</b>"
and with content head-line, line-fast, line-middle, line-slow. 

There is a Line with id products-line.

We write products of Area51 into helper.
We add helper to icards of products-line.

There is a Line with id palettes-line.
We write eu100, eu200, eu333 into helper.
We add helper to icards of palettes-line.

There is a Section with id stock-section and with description "<b>Dynamic Stock</b>"
and with content products-line, palettes-line.

There is a Page with id desk1 and with content bricks-section and stock-section.

// ![Area51,desk1](out.txt)
![Area51](area51cards.svg) 
![desk1](area51cards.html) 
![Area51](area51.tables.html) 

