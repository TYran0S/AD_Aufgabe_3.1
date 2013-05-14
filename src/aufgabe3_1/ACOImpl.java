package aufgabe3_1;

import java.util.*;

public class ACOImpl implements ACO {
    /**
     * Berechnet die Laenge des Weges, den die Ameise gegangen ist
     * 
     * @param visitedNodes          Liste der besuchten Staedte als Integer
     * @param connectionList        Liste aller Connections
     * @return                      Laenge des Weges der diese Staedte miteinander verbindet
     */
    // FIXME:   Fuer die Laengenberechnung werden die zwei Cities, die an einer
    //          Connection haengen, in der ArrayList direkt via Index abgefragt.
    //          Haengen in der Implementierung MEHR als zwei Cities an einer
    //          Connection, dann muss die if-Abfrage angepasst werden.
    public int length(List<Integer> visitedNodes, List<Connection> connectionList) {
        int length = 0;

        // Durchlaufe alle Nodes
        for (int i = 1; i < visitedNodes.size(); i++) {

            // Durchlaufe fuer jeweiligen Node alle Connections 
            for (int j = 0; j < connectionList.size(); j++) {

                // Die Connection suchen, die zwei besuchte Nodes miteinander verbindet
                if((visitedNodes.get(i) == connectionList.get(j).cities.get(0) && visitedNodes.get(i - 1) == connectionList.get(j).cities.get(1))
                || (visitedNodes.get(i) == connectionList.get(j).cities.get(1) && visitedNodes.get(i - 1) == connectionList.get(j).cities.get(0))) {

                    length += connectionList.get(j).length;
                } // if
            } // for
        } // for

        return length;
    }

    /**
     * Erzeugt eine ArrayList von Ants
     * 
     * @param antCount      Anzahl der Ameisen
     * @param startPosition Startpunkt der Ameisen
     * @return              ArrayList mit Ameisen
     */
    public List<Ant> createAnts(int antCount, Node startPosition) {
        List<Ant> ants = new ArrayList<Ant>();
        List<Integer> visitedNodes = new ArrayList<Integer>();

        visitedNodes.add(startPosition.ID);

        for(int i=0; i<antCount; i++) {
            ants.add(new Ant(i+1, visitedNodes));
        }

        return ants;
    }

    /**
     * Bewegt eine Ameise zu einem neuen Node
     * 
     * @param ant   Ameise die bewegt werden soll
     * @param node  Node zu dem sich die Ameise bewegen soll
     * @return      Ameise an der neuen Position
     */
    public Ant move(Ant ant, Node node) {
        List<Integer> nodeList = ant.visitedNodes;
        nodeList.add(node.ID);

        return new Ant(ant.ID, nodeList);
    }

    /**
     * Erzeugt den Startpunkt als neues Ziel fuer die Heimkehr der Ameise
     * 
     * @param ant   Ameise, die nach Hause geschickt werden soll
     * @return      Ameise mit Positionsdaten fuer die Heimkehr
     */
    public Ant goHome(Ant ant) {
        List<Integer> nodeList = ant.visitedNodes;

        if (ant.visitedNodes.get(ant.visitedNodes.size() - 1) != ant.visitedNodes.get(0)) {
            nodeList.add(ant.visitedNodes.get(0));
        }

        return new Ant(ant.ID, nodeList);
    }

    /**
     * Loescht die Liste der besuchten Nodes aus der Ameise
     * 
     * @param ant   Ameise, bei der die Liste geloescht werden soll
     * @return      Neue, saubere Ameise
     */
    public Ant clear(Ant ant) {
        List<Integer> nodeList = new ArrayList<Integer>();
        nodeList.add(ant.visitedNodes.get(0));

        return new Ant(ant.ID, nodeList);
    }

    /**
     * Prueft, ob die Ameise ihre Tour beenden und den Rueckweg antreten muss
     * 
     * @param ant       Ameise, die geprueft werden soll
     * @param numCities Anzahl der Staedte
     * @return          Status der Tour
     */
    public boolean tourFinished(Ant ant, int numCities) {

        // Wenn visitedNodes + Rueckweg groessergleich numCities + 1 ist
        if (ant.visitedNodes.size() >= (numCities + 1)) {
            return true; // true zurueckgeben
        }
        return false; // sonst false
    }

    /**
     * Updatet den Pheromonwert einer Connection (erfordert Update der Nodes)
     * 
     * @param connection    Connection, die geupdatet werden soll
     * @param alpha         Wert um den Pheromone veraendert werden sollen
     * @return              Geupdatete Connection
     */
    public Connection updatePheromones(Connection oldConnection, double q) {
        double newPheromon = 0;

        newPheromon = (oldConnection.pheromon + q);

        if (newPheromon < 0 ) {
            newPheromon = 0; // Negative Pheromon-Werte verhindern
        }

        return new Connection(oldConnection.ID, oldConnection.length, newPheromon, oldConnection.cities);
    }

    /**
     * Verringert die Pheromonwerte aller Connections (erfordert Update der Nodes)
     * 
     * @param oldList   Liste aller aktueller Connections
     * @param rho       Evaporations-Koeffizient
     * @return          Neue Liste aller Connections mit reduziertem Pheromonwert
     */
    public List<Connection> evaporate(List<Connection> oldList, double rho) {
        List<Connection> newPheromones = new ArrayList<Connection>();

        for(int i=0; i<oldList.size(); i++) {
            newPheromones.add(updatePheromones(oldList.get(i), rho));
        } // for

        return newPheromones;
    }

    /**
     * Ermittelt den Weg, welchen die Ameise gehen soll
     * 
     * @param ant   Ameise, fuer die der Weg ermittelt werden soll
     * @param node  Position der Ameise
     * @return      Pfad, welchen die Ameise gehen soll
     */
    // FIXME:   Es sollte beachtet werden, dass das Probabilities Array auf der
    //          Anzahl der Staedte basiert, damit man nicht aus dem Index laeuft.
    //          Ausserdem sollte man beachten, dass es immer eine Connection
    //          weniger in einem Node gibt, als Nodes insgesamt vorhanden sind,
    //          weil der eigene Node keine Connection zu sich selbst braucht.
    public double[] findPath(Ant ant, Node node, int numCities, double alpha, double beta) {
        double[] probabilities = new double[numCities];
        double nenner = 0.0;

        for (int i = 0; i < node.trails.size(); i++) {

            Connection currentConnection = node.trails.get(i); // Aktuelle Connections
            List<Integer> currentCities = currentConnection.cities; // Aktuelle Cities der Connections

            // Wenn die Staedte in currentCities noch nicht besucht worden sind, Nenner berechnen
            if (!(ant.visitedNodes.containsAll(currentCities))) {

                nenner += Math.pow(node.trails.get(i).pheromon, alpha) * Math.pow(1.0 / node.trails.get(i).length, beta);

                probabilities[i] = 1.0; // probabilities Index zur spaeteren Berechnung mit 1.0 taggen
            } // if
        } // for

        // Fuer getaggte Probabilities den Zaehler und das Endergebnis berechnen
        for (int j = 0; j < probabilities.length; j++) {
            if (probabilities[j] == 1.0) {

                double zaehler = Math.pow(node.trails.get(j).pheromon, alpha) * Math.pow(1.0 / node.trails.get(j).length, beta);

                probabilities[j] = zaehler / nenner;
            } // if
        } // for

        /*
         * Das Probabilities Array hat genauso viele Eintraege wie es Nodes gibt.
         * Jeder Node hat jedoch eine Connection weniger, als es Nodes gibt, da
         * die Connection zum Node, auf dem man gerade steht, nicht enthalten ist.
         * Nach der obigen Berechnung der Probabilities muss der ausgelassene
         * Node im Probabilities Array so verschoben werden, dass die uebrigen
         * Eintrage wieder indexrichtig im Array stehen und die Probabilities
         * zu den zugehoerigen Nodes passen.
         */
        for (int i = probabilities.length - 1; i > 0; i--) {
            if (i >= node.ID) {
                probabilities[i] = probabilities[i - 1];
                probabilities[i - 1] = 0;
            } // if
        } // for

        return probabilities;
    }

    /**
     * Waehlt aus allen Probabilities nach "gewichteter Wahrscheinlichkeit" einen Wert aus
     * 
     * @param   nodes Liste mit den verfuegbaren Nodes
     * @param   array Array mit den Wahrscheinlichkeiten (Probabilities)
     * @return  Node, der auf Basis der Wahrscheinlichkeiten zufaellig gewaehlt wurde
     */
    // FIXME:   Es gilt zu beachten, dass das Probabilities Array so gross ist,
    //          wie die Anzahl an Nodes / Staedten. Die Probabilities sind im
    //          Array indexrichtig zu den Staedten / Nodes abgespeichert.
    //          Dies sollte beachtet werden, um nicht aus dem Index zu springen.
    public Node randomPathChoice(List<Node> nodes, double[] array){
        double random = (new Random().nextDouble()); // Neuer Random double Wert
        double[] copyArray = array.clone(); // Kopie vom Original Array erstellen
        Arrays.sort(copyArray); // Kopie vom Original Array aufsteigend sortieren
        double sumProbabilities = 0.0; // Aufsummierte Probabilities
        double foundProbability = 0.0; // Probability, die kleinergleich Random ist
        int resultIndex = -1; // Index von foundProbability im Original Array

        /*
         * Schritt [1]: Prinzip: Gewichtete Wahrscheinlichkeit
         * Aufsteigend sortiertes Probabilities Array durchlaufen
         * und die Wahrscheinlichkeiten in sumProbabilities aufsummieren,
         * bis die Summe groesser ist, als der random-Wert.
         * Der Wahrscheinlichkeits-Wert im aktuellen Index entspricht dann dem
         * nach dem random-Wert gewichteten Zufallswert aller Wahrscheinlichkeiten
         * im Probability Array und wird in foundProbability gespeichert.
         * 
         * Schritt [2]: Im originalen Array das Vorkommen von foundProbability
         * finden und den Index in resultIndex speichern.
         * 
         * Schritt [3]: In der Node Liste den indexrichtigen Node, passend zum
         * gefundenen Probability-Wert im uebergebenem Array finden und
         * ausgeben.
         */

        // Schritt [1]:
        for (int i = 0; i < copyArray.length; i++) {
            sumProbabilities += copyArray[i];
            if (sumProbabilities > random) {
                foundProbability = copyArray[i]; // Probability-Wert speichern
                i = copyArray.length; // for-Schleife abbrechen lassen
            }
        }

        // Schritt [2]:
        for (int i = 0; i < array.length; i++) {
            if (array[i] == foundProbability) {
                resultIndex = i; // Index von foundProbability im original Array
                i = array.length; // for-Schleife abbrechen lassen
            } // if
        } // for

        // Schritt [3]:
        for (int i = 0; i < nodes.size(); i++) {
            if ((resultIndex != -1) && (nodes.get(i).ID == resultIndex + 1)) {
                return nodes.get(i); // Gefundenen, indexrichtigen Node ausgeben 
            } // if
        } // for

        return null;
    }

}