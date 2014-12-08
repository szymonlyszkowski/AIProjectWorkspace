###Odpalane z cmd: 
Gra pozwala na uruchomienie w dwóch trybach (z poziomu AIProjectWorkspace\DiceWarsGame\out\production\DiceWarsGame). Pierwszy służy jedynie do tworzenia grafów i zapisania ich do pliku. Drugi do faktycznego uruchomienia gry na wczytanym grafie.



###Tryb 1. Tworzenie grafu
```
java -cp "<ścieżka gdzie jest jFuzzyLogic.jar i json-simple-1.1.1.jar>*"; com.example.main.MainWindow 1 nrOfVertices maxNrOfEdges ścieżkaDoWynikowegoPlikuZGrafem
```
np.
```
cd C:\Moje Dokumenty\AIProjectWorkspace\DiceWarsGame\out\production\DiceWarsGame
java -cp "../../../*"; com.Example.main.MainWindow 1 10 5 outputGraph.txt
```

#####P.S Jeżeli używacie linuxa to zamiast średnika jest dwukropek!
```
java -cp "/home/szymonidas/repos/AIProject/DiceWarsGame/*": com.example.main.MainWindow 1 10 5 outputGraph.txt
```


###Tryb 2. Uruchamianie gry
```
java -cp "<ścieżka gdzie jest jFuzzyLogic.jar i json-simple-1.1.1.jar>*"; com.example.main.MainWindow 2 ścieżkaDoGrafuWygenerowanewgoWyżej
```
np.
```
cd C:\Moje Dokumenty\AIProjectWorkspace\DiceWarsGame\out\production\DiceWarsGame
java -cp "../../../*"; com.Example.main.MainWindow 2 outputGraph.txt
```

###Tryb 3. Agenci
```
java -cp "<ścieżka gdzie jest jFuzzyLogic.jar i json-simple-1.1.1.jar>*"; com.example.main.MainWindow 3 ścieżkaDoGrafuWygenerowanewgoWyżej agent1Directory.jar agent2Directory.jar
```

Generalnie moim zdaniem program powinien przyjmować 2 jarki. Kazda klasa ktora zaimplementuje interfejs Agent powinna byc odczytana.
Jest tylko problem z odpowiednim spakowaniem tych plikow do jar bo wieksze klasy kompiluja sie do kilku plikow class z ktorych tworzy się jar'a.... komenda to stworzenia jar:
```
jar cvf result.jar /source/to/.class/file(s)
```
np.
```
cd ~repos/AIProject/DiceWarsGame/out/production/DiceWarsGame/
jar cvf result.jar /ai/dicewars/common/Agent.class
```

###Odpalanie z agentem w CLIPSie
```
java -cp "<ścieżka gdzie jest jFuzzyLogic.jar i json-simple-1.1.1.jar>*"; -Djava.library.path=../../.. com.example.main.MainWindow [OPCJE JAK WCZEŚNIEJ]
```
Żeby odpalić bibliotekę CLIPSową poza wczytaniem JARa do projektu trzeba jeszcze ręcznie wskazać mu ścieżkę do pliku dll.
np.
```
java -cp "../../../*"; -Djava.library.path=../../.. com.example.main.MainWindow 2 map1.txt
```