# Infi-Hausaufgaben: Überblick
Aufgabenstellungen & eventuelle Zusatzinformationen 

<br>

## *H01_KundenArtikelBestellung:*

> ### **Aufgabe**:
> programmiere folgende Aufgabe mit JAVA+Sqlite
>
> * [X] erzeuge eine Kundentabelle mit (id, name, email)
> * [X] erzeuge eine Artikeltabelle mit (id, bezeichnung, preis)
> * [X] autoincrement verwenden
> * [X] erzeuge alle notwendigen Methoden um die Tabellen anzulegen
und zu befüllen
> * [X] trenne Kunden- und Artikelbereiche in verschiedene Klassen
> * [X] erzeuge eine Bestelltabelle mit (kundenID, artikelID, anzahl)
    (verwende Foreign Keys!!)
> * [X] erzeuge alle notwendigen Methoden um einen Artikel zu bestellen
> * [X] erzeuge eine Methode um die Bestellung von einem Kunden anzuzeigen

<br>

Später wurde die Aufgabe noch erweitert: 
>Aufgabe Kunde-Artikel erweitern.
>als Methoden zu implementieren:
> 1. * [X] Bestellung soll durch Angabe der BestellID gelöscht werden können
> 2. * [X] Bestellung soll durch Angabe der BestellID und Anzahl der bestellten Artikel geupdatet werden.
> 3. * [X] Erweitern Sie die Artikeltabelle mit einer Lagerbestandspalte
> 4. * [X] Bei einer Bestellung, sollte der Lagerbestand geprüft werden. Ist dieser geringer, als die gewünschte Bestellmenge, wird die bestellung nicht durchgeführt.
> 
>weitere Erweiterungen kommen...
>wer vorarbeiten möchte...
>
> 5. * [X] Lagerbestand eines Artikels muss nach Bestellung angepasst werden können

<br>

als letztes wurde dann alles von SQLite auf MySQL umgeschrieben

<br>
<br>

## *H02_Testaufgabe:*

Es handelt sich hierbei um das Beispiel im 2. Test, indem Schueler den Klassenzugeteilt werden sollen
> ### **Aufgabe:**
> Programmiere folgende Aufgabe mit JAVA + SQLITE:
>
> Eine Schule benötigt eine Schülerverwaltung.
>
> * [X] Entwerfen Sie die CREATE Statments für folgende Tabellen:
>    * „alle“ Schüler
>    * „alle“ Klassen
>
>   wählen Sie sinnvolle Spalten und Spaltentypen.
>
> * [X] Entwerfen Sie das SQL-Statement für die Erzeugen der Schüler-zu-Klasse Tabelle. Diese Tabelle soll auch das Datum der Zuordnungszeitpunktes speichern. Vergessen Sie nicht Vorkehrungen gegen Dateninkonsistenzen einzubauen.
>
> * [X] Schreiben Sie eine java-Methode, welcher (Connection, Klassenname, Schülername) als Parameter bekommt, und diesen Datensatz dann in die Tabelle speichert.
>
> * [X] rufen Sie diese Methode exemplarisch in der main auf

<br><br>

## *H03_Autoverleih*

Ist die Aufgabe des Nachtestes und gleicht daher der 2. Hausaufgabe bzw. der 1. Testaufgabe.

Anschließend wurde die Aufgabe erweitert:

> ### **Aufgabe:**
> * [X] schreibe die Aufgabe auf Prepared Statements um

<br><br>

## *H04_CsvEinlesen*
Ist die aktuelle Hausaufgabe
> ### **Aufgabe:**
>
> * [X] Erzeuge eine CSV-Datei von Schülerinformationen
> * [X] Überlege die passende TabellenStruktur zu der CSV-Datei
> * [X] erzeuge automatisch die Tabelle mittels JAVA
> * [X]  lese mit SCANNER, zeileweise die CSV-Datei und schreib zeileweise in die neue Tabelle
> * [X]  mach das Ganze nochmal für die Klassenräume
>
> * [X] selektiere die insertierten Schüler+Klassen
>
>Fertigstellung 2022.04.04

<br>

Außerdem habe ich eine zusätzliche Funktion eingebaut. Und zwar ist es möglich, in einer CSV-Datei mehrere Tabellen anzulegen. Dafür gibt es jedoch Voraussetzungen: 

- Primary keys werdem mit  "#" angegeben
- Foreign keys werden mit "\*" angegeben. Hinter dem "\*" steht dann die refernzierte Tabelle
- zwischen den Tabellen muss 1 leere Zeile sein

<br>

die benutzte CSV-Datei ist im Ordner.
