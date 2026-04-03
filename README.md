# TO-DO (Practicum 2)
De soort uitwerking (totaal, nominaal, defensief) is hetzelfde als in practicum 1. Voor werken met bindingen tussen mappen en items is het defensief. Maak je eigen exceptions indien nodig.

BELANGRIJK: De items (File, Directory, Link) delen nu heel veel functionaliteit, namelijk bepaalde attributen en methodes (move, getRoot, isDirectOrIndirectChildOf, getTotalDiskUsage, ...)
--> Maak een abstracte klasse Item waarvan ze overerven???

- [x] Fouten feedback practicum 1 aanpassen ⚡⚡ (Loïck)

## Superklasse (Item.java) _BEIDE_
- [x] Aanmaken
- [x] Gedeelde oude implementatie van File, Directory en Link hier inbrengen (CTRL+C, CTRL+V)
- [x] File correct doen overerven van nieuwe superklasse en enkel eigen nieuwe methodes/attributen meegeven

## Bestanden (File.java)
- [x] File types (met Enum) ⚡⚡

## Mappen (Directory.java) _CASPER_
- [x] Subklasse van Item
- [x] Representatie (attributen) ⚡
- [x] Constructoren ⚡
- [x] Bidirectionele associatie met bestanden ⚡⚡⚡
- [x] Methode: getNbItems() ⚡
- [x] Methode: getItemAt(Int positie) ⚡
- [x] Methode: getItem(String naam) ⚡
- [x] Methode: hasAsItem(File file / Folder folder / Link link) ⚡
- [x] Methode: makeRoot() ⚡
- [x] Methode: containsDiskItemWithName(String naam) ⚡
- [x] Afhandeling van schrijfrecht ⚡⚡
- [x] Punt uit naam halen in deze subklasse ⚡

## Links (Link.java) _LOÏCK_
- [x] Subklasse van Item
- [x] Representatie (attributen) ⚡
- [x] Constructoren ⚡
- [x] Unidirectionele associatie met bestanden of mappen ⚡⚡⚡
- [x] Correcte afhandeling van verwijderde links/bestanden/mappen ⚡⚡⚡⚡

## Lijst van nodige constructoren _BEIDE_
- [x] File(dir, name, size, writable, type)
- [x] File(dir, name, type)
- [x] Link(dir, name, linkedItem)
- [x] Directory(dir, name, writable)
- [x] Directory(dir, name)
- [x] Directory(name, writable)
- [x] Directory(name)

## Algemeen (dingen voor in Item.java) _BEIDE_
- [x] Bestanden, folders en links verwijderen (als schrijfbaar en leeg) ⚡⚡
- [x] Bijhouden van creation/modification data voor alle items ⚡
- [x] Uitwerken van hasOverlappingUsePeriod(.) voor alle items ⚡
- [x] Methode: move(.) ⚡
- [x] Methode: getRoot() ⚡⚡⚡
- [x] Methode: isDirectOrIndirectChildOf(.) ⚡⚡
- [x] Methode: getTotalDiskUsage() ⚡
- [x] Methode: getAbsolutePath() ⚡⚡
- [x] Default names per soort ⚡⚡
- [x] Deletion checks voor alle publieke methodes en methodes die andere Items als input nemen ⚡⚡⚡
- [x] Superveel tests maken (voor elke post-conditie, na project klaar) ⚡⚡⚡⚡⚡


