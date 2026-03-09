# TO-DO (Practicum 2)
De soort uitwerking (totaal, nominaal, defensief) is hetzelfde als in practicum 1. Voor werken met bindingen tussen mappen en items is het defensief. Maak je eigen exceptions indien nodig.

BELANGRIJK: De items (File, Directory, Link) delen nu heel veel functionaliteit, namelijk bepaalde attributen en methodes (move, getRoot, isDirectOrIndirectChildOf, getTotalDiskUsage, ...)
--> Maak een abstracte klasse Item waarvan ze overerven???

- [x] Waar is Wim

## Bestanden (File.java)
- [ ] File types (met Enum)

## Mappen (Directory.java)
- [ ] Representatie (attributen)
- [ ] Constructoren
- [ ] Bidirectionele associatie met bestanden
- [ ] Methode: getNbItems()
- [ ] Methode: getItemAt(Int positie)
- [ ] Methode: getItem(String naam)
- [ ] Methode: hasAsItem(File file / Folder folder / Link link)
- [ ] Methode: makeRoot()
- [ ] Afhandeling van schrijfrecht

## Links (Link.java)
- [ ] Representatie (attributen)
- [ ] Constructoren
- [ ] Unidirectionele associatie met bestanden of mappen
- [ ] Correcte afhandeling van verwijderde links/bestanden/mappen

## Lijst van nodige constructoren
- [ ] File(dir, name, size, writable, type)
- [ ] File(dir, name, type)
- [ ] Link(dir, name, linkedItem)
- [ ] Directory(dir, name, writable)
- [ ] Directory(dir, name)
- [ ] Directory(name, writable)
- [ ] Directory(name)

## Algemeen
- [ ] Bestanden, folders en links verwijderen (als schrijfbaar en leeg)
- [ ] Bijhouden van creation/modification data voor alle items
- [ ] Uitwerken van hasOverlappingUsePeriod(.) voor alle items
- [ ] Methode: move(.)
- [ ] Methode: getRoot()
- [ ] Methode: isDirectOrIndirectChildOf(.)
- [ ] Methode: getTotalDiskUsage()
- [ ] Methode: getAbsolutePath()
