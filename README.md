###Odpalane z cmd: 
Gra pozwala na uruchomienie w dwóch trybach (z poziomu AIProjectWorkspace\DiceWarsGame\out\production\DiceWarsGame). Pierwszy służy jedynie do tworzenia grafów i zapisania ich do pliku. Drugi do faktycznego uruchomienia gry na wczytanym grafie.



###Tryb 1. Tworzenie grafu
```
java -cp "<ścieżka gdzie jest jFuzzyLogic.jar i json-simple-1.1.1.jar>*"; com.example.main.MainWindow 1 nrOfVertices maxNrOfEdges ścieżkaDoWynikowegoPlikuZGrafem
```
#####P.S Jeżeli używacie linuxa to zamiast średnika jest dwukropek!
```
java -cp "/home/szymonidas/repos/AIProject/DiceWarsGame/*": com.example.main.MainWindow 1 10 5 outputGraph.txt
```

np.
```
cd C:\Moje Dokumenty\AIProjectWorkspace\DiceWarsGame\out\production\DiceWarsGame
java -cp "../../../*"; com.Example.main.MainWindow 1 10 5 outputGraph.txt
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

TODO:
To cudo powinno przyjmować jeszcze jako parametr typy agentów i ścieżkę do nich.
