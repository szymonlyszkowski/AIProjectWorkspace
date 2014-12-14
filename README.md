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

Program przyjmuje 2 jarki. Klasa z agentem, którą wrzucamy do jara musi nazywać się MyAgent (niezależnie od typu agenta) i implementować interfejs Agent. 
Jeżeli dany agent ma w strukturze jakieś klasy lokalne, to je też musimy dołączyć do jara. Najprowdopodobniej klasy powinny znajdować się w jarze uwzględniając podfoldery (np. com/example/main).

Dodatkowo klasa agenta musi posiadać bezparametrowy default constructor. W przypadku FuzzyAgent, plik .fcl może znajdować się poza jarem (tylko trzeba w klasie podać właściwą ścieżkę do pliku; jak damy samą nazwę pliku, to ów plik powinien sie znajdować w out/production/DiceWarsGame)

W przypadku agentów fuzzy i clips, w klasach (które znajdą się w jarze) powinien być kontruktor bez parametrów, który ma zakodowaną ścieżkę do pliku .fcl lub .clp.

Komenda to stworzenia jar:
```
jar cvf result.jar /source/to/.class/file(s)
```
np.
```
cd ~repos/AIProject/DiceWarsGame/out/production/DiceWarsGame/
jar cvf result.jar /ai/dicewars/common/Agent.class
```

###Tryb 4. Bez GUI
Jeżeli w swoich agentach nie ma żadnego wyświetlania rzeczy, to po odpaleniu komendy konsola wyświetli tylko zwycięzcę.

```
java -cp "<ścieżka gdzie jest jFuzzyLogic.jar i json-simple-1.1.1.jar>*"; com.example.main.MainWindow 4 ścieżkaDoGrafuWygenerowanewgoWyżej agent1Directory.jar agent2Directory.jar
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
