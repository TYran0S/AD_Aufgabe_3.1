package aufgabe3_1;

import java.util.*;

public class TestFrame {
    final public static String TESTDATA = "GR21_TEST.tsp";  // TSP Datei
    final public static int BESTSOLUTION = 2707;            // Beste bislang bekannte Loesung
    final public static int CITIES = 21;                    // Anzahl der Staedte
    final public static int ANTS = 10;                      // Anzahl der Ameisen
    final public static int CYCLES = 100000;                // Anzahl der Durchlaeufe
    final public static int STARTNODE = 1;                  // Startpunkt der Ameise
    final public static double ALPHA = 1.0;                 // Einfluss der Pheromonene
    final public static double BETA = 5.0;                  // Einfluss der Weglaenge
    final public static double RHO = 0.5;                   // Evaporationskonstante
    final public static double Q = 1.0;                     // Pheromon-Menge eines Connection-Updates
    final public static boolean BRUTEFORCE = false;         // BruteForce oder ACO waehlen

    public static void testBruteForce() {
        System.out.println(".::AUSWERTUNG FUER DIE BERECHNUNG MIT DEM BRUTEFORCE ALGORITHMUS::.\n");
        System.out.println("TESTDATEI: " + TESTDATA + "\nSTAEDTE: " + CITIES + "\n\nBERECHNUNG WIRD DURCHGEFUEHRT...");
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        List<Node> nodes = Parser.initNodes(connections);
        List<Node> nodePermutations = BruteForce.calculatePermutation(nodes, nodes.size()-1, connections);
        System.out.println("\nKUERZESTE GEFUNDENE STRECKE: " + BruteForce.calcWayLength(nodePermutations, connections));
    }

    public static void testWegberechnung() {
        System.out.println("\n\n::Test: Berechnung der Weglaenge ueber Nodes::");

        ACO testColony = new ACOImpl();
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        List<Node> nodes = Parser.initNodes(connections);

        List<Integer> visitedNodes = new ArrayList<Integer>();
        visitedNodes.add(nodes.get(0).ID);
        visitedNodes.add(nodes.get(1).ID);
        visitedNodes.add(nodes.get(2).ID);
        visitedNodes.add(nodes.get(3).ID);
        visitedNodes.add(nodes.get(4).ID);
        visitedNodes.add(nodes.get(0).ID);

        int testlength = testColony.length(visitedNodes, connections);
        System.out.printf("Gesamtlaenge = %d", testlength);
    }

    public static void testBewegungDerAmeise() {
        System.out.println("\n\n::Test: Bewegung einer Ameise::");

        ACO testColony = new ACOImpl();
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        List<Node> nodes = Parser.initNodes(connections);

        List<Integer> visitedNodes = new ArrayList<Integer>();
        visitedNodes.add(nodes.get(0).ID);

        // Ameise auf Node 0 plazieren
        Ant testAnt = new Ant(0, visitedNodes);
        System.out.println(testAnt.visitedNodes);

        // Ameise nach Node 1 bewegen
        testColony.move(testAnt, nodes.get(1));
        System.out.println(testAnt.visitedNodes);

        // Ameise nach Node 2 bewegen
        testColony.move(testAnt, nodes.get(2));
        System.out.println(testAnt.visitedNodes);

        // Ameise nach Node 4 bewegen
        testColony.move(testAnt, nodes.get(4));
        System.out.println(testAnt.visitedNodes);
    }

    public static void testBesuchteStaedte() {
        System.out.println("\n\n::Test: ob die Ameise schon alle Staedte besucht hat::");

        ACO testColony = new ACOImpl();
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        List<Node> nodes = Parser.initNodes(connections);

        List<Integer> visitedNodes = new ArrayList<Integer>();
        visitedNodes.add(nodes.get(0).ID);
        visitedNodes.add(nodes.get(1).ID);
        visitedNodes.add(nodes.get(2).ID);

        Ant testVisitAnt = new Ant(0, visitedNodes);

        System.out.println("Noch nicht alles Besucht: " + testColony.tourFinished(testVisitAnt, 5));

        testColony.move(testVisitAnt, nodes.get(3));
        testColony.move(testVisitAnt, nodes.get(4));

        System.out.println("Schon Alle Besucht: " + testColony.tourFinished(testVisitAnt, 5));
    }

    public static void testClearConnection() {
        System.out.println("\n\n::Test: Loeschen der besuchten Connections::");

        ACO testColony = new ACOImpl();
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        List<Node> nodes = Parser.initNodes(connections);

        List<Integer> visitedNodes = new ArrayList<Integer>();
        visitedNodes.add(nodes.get(0).ID);
        visitedNodes.add(nodes.get(1).ID);
        visitedNodes.add(nodes.get(2).ID);

        Ant testAnt = new Ant(0, visitedNodes);

        System.out.println("Armeise vorher: " + testAnt.visitedNodes);

        testAnt = testColony.clear(testAnt);

        System.out.println("Armeise nachher: " + testAnt.visitedNodes);
    }

    public static void testPheromonUpdateOne() {
        System.out.println("\n\n::Test: von einem PheromonUpdateOne::");

        ACO testColony = new ACOImpl();
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));

        System.out.println("Pheromonmenge bei Connection 1 um 1 erhoehen");
        connections.set(0, testColony.updatePheromones(connections.get(0), 1));
        System.out.println(connections);

        System.out.println("Pheromonmenge bei Connection 1 um -3 verringern");
        connections.set(0, testColony.updatePheromones(connections.get(0), -3));
        System.out.println(connections);
    }

    public static void testPheromonUpdateAll() {
        System.out.println("\n\nTest: von einem PheromonUpdateAll::");

        ACO testColony = new ACOImpl();
        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));

        System.out.println("Pheromonmenge am Anfang = 0");
        System.out.println(connections);

        System.out.println("Pheromonmenge ueberall um 2 erhoehen");
        connections = testColony.evaporate(connections, 2);
        System.out.println(connections);

        System.out.println("Pheromonmenge ueberall um 0.5 veringern");
        connections = testColony.evaporate(connections, -0.5);
        System.out.println(connections);
    }

    public static void testFindPath() {
        System.out.println("\nTest der findPath Methode:\n");
        ACO testColony = new ACOImpl();

        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        connections = testColony.evaporate(connections, 2);
        List<Node> nodes = Parser.initNodes(connections);
        System.out.println("Connections :" + connections);
        List<Integer> testConNodes = new ArrayList<Integer>();

        //        testConNodes.add(nodes.get(0).ID);
        //        testConNodes.add(nodes.get(1).ID);
        //        testConNodes.add(nodes.get(2).ID);
        //        testConNodes.add(nodes.get(3).ID);
        testConNodes.add(nodes.get(4).ID);

        Ant testConAnt = new Ant(0, testConNodes);
        System.out.println(testConAnt.visitedNodes);
        // System.out.println(nodes.get(2));

        double[] result = testColony.findPath(testConAnt, nodes.get(4), CITIES, ALPHA, BETA);

        double ergebniss = 0;

        for (double element : result) {
            System.out.print("[" + element + "] ");
            ergebniss += element;
        }
        System.out.println("\nErgebnis " + ergebniss);
    }

    public static void testrandomChoice() {
        System.out.println("\nTest der randomChoice Methode:\n");
        ACO testColony = new ACOImpl();

        List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));
        connections = testColony.evaporate(connections, 2);
        List<Node> nodes = Parser.initNodes(connections);

        List<Integer> testConNodes = new ArrayList<Integer>();

        testConNodes.add(nodes.get(0).ID);
        //        testConNodes.add(nodes.get(1).ID);
        testConNodes.add(nodes.get(2).ID);
        testConNodes.add(nodes.get(3).ID);
        //        testConNodes.add(nodes.get(4).ID);

        Ant testConAnt = new Ant(0, testConNodes);
        System.out.println(testConAnt.visitedNodes);

        double[] result = testColony.findPath(testConAnt, nodes.get(3), CITIES, ALPHA, BETA);
        double ergebnis = 0;
        for (double element : result) {
            System.out.print("[" + element + "] ");
            ergebnis += element;
        }
        System.out.println("\nErgebnis " + ergebnis);

        System.out.println("\nGewaehlter Node: " + testColony.randomPathChoice(nodes, result));
    }

    public static void main(String[] args) {

        /* ***** Einzelne Tests der Methoden von ACOImpl **** */
//        testBruteForce();
//        testWegberechnung();
//        testBewegungDerAmeise();
//        testBesuchteStaedte();
//        testClearConnection();
//        testPheromonUpdateOne();
//        testPheromonUpdateAll();
//        testFindPath();
//        testrandomChoice();


        /* ***** Startzeit fuer die Berechnung der Dauer festhalten **** */
        long startTime = System.currentTimeMillis(); // Startzeit des Algorithmus setzen
        boolean foundBestRoute = false; // Boolean, ob die beste Route gefunden wurde
        StringBuilder durationOutput = new StringBuilder("\n\nBEST SOLUTION WURDE LEIDER NICHT ERREICHT :-("); // Output fuer die Dauer, wenn die beste Route gefunden wurde


        if(BRUTEFORCE) {

            /* ***** BruteForce Algorithmus ausfuehren und Dauer berechnen **** */
            startTime = System.currentTimeMillis(); // Startzeit des Algorithmus setzen
            testBruteForce(); // BruteForce Methode starten
            long duration = (System.currentTimeMillis() - startTime); // Dauer
            System.out.print("DAUER: " + ((duration / 1000) / 3600) + " STUNDEN "
                    + (((duration / 1000) % 3600) / 60) + " MINUTEN "
                    + ((duration / 1000) % 60) + " SEKUNDEN "
                    + (duration % 1000) + " MILLISEKUNDEN\n");

        } else {

            startTime = System.currentTimeMillis(); // Startzeit des Algorithmus setzen

            /* ***** Vorinitialisierung ***** */
            ACO testColony = new ACOImpl();

            // Connections parsen aus der Testdatei
            List<Connection> connections = Parser.initConnections(Parser.parseTestFile(TESTDATA));

            // Connections durch evaporate-Methode mit positivem Pheromon-Wert vorinitialisieren, um bei Wahrscheinlichkeitsberechnung nicht durch 0 zu dividieren
            connections = testColony.evaporate(connections, Q);

            // Geupdatete Connections in die Liste der Nodes erzeugen
            List<Node> nodes = Parser.initNodes(connections);

            // Test-Kolonie von Ameisen erstellen
            List<Ant> antList = testColony.createAnts(ANTS, nodes.get(STARTNODE - 1));

            List<Integer> bestRoute = new ArrayList<Integer>();
            int bestLength = 0;
            int tempLength = 0;
            int oldLength = 0;
            boolean update = false; // Boolean fuer das Update der Konsolenausgabe


            /* ***** Berechnung starten ***** */
            int i = 1; // Laufvariable
            while (i <= CYCLES) {

                for(int a = 0; a < antList.size(); a++) {

                    // Wenn eine Ameise ihre Tour beendet hat, Route und Laenge berechnen
                    if(testColony.tourFinished(antList.get(a), CITIES)) {

                        testColony.goHome(antList.get(a)); // Rueckweg zur Route hinzufuegen

                        tempLength = testColony.length(antList.get(a).visitedNodes, connections); // Laenge berechnen

                        // Bei kuerzerer Laenge die Laenge in bestLength abspeichern und die verwendete Route in bestRoute
                        if(((bestLength - tempLength) > 0) || bestLength == 0) {
                            bestLength = tempLength; // Neue kuerzeste Laenge abspeichern
                            bestRoute = antList.get(a).visitedNodes; // Neue kuerzeste Route abspeichern
                        } // if

                        // Besuchte Nodes der Ameise resetten
                        antList.set(a, testColony.clear(antList.get(a)));

                        // Aktualisierung fuer die Konsolenausgabe resetten
                        update = false;

                    } else {
                        // Wenn eine Ameise ihre Tour noch nicht beendet hat

                        // antList.set(a, (testColony.move(antList.get(a), (testColony.randomPathChoice(nodes, (testColony.findPath(antList.get(a), nodes.get(antList.get(a).visitedNodes.get(antList.get(a).visitedNodes.size() - 1 ) - 1), CITIES, ALPHA, BETA)))))));

                        // Start-Node erzeugen
                        Node startNode = nodes.get(antList.get(a).visitedNodes.get(antList.get(a).visitedNodes.size() - 1) - 1);

                        // Fuer die Connections vom Start-Node die Probabilities erzeugen
                        double[] probabilities = testColony.findPath(antList.get(a), startNode, CITIES, ALPHA, BETA);

                        // Nach Zufallsmuster anhand der Probabilities den naechsten Node ermitteln
                        Node nextNode = testColony.randomPathChoice(nodes, probabilities);

                        // Ameise zum nextNode bewegen, dabei wird eine neue Ameise mit dem nextNode in ihrer visitedNodes Liste erzeugt
                        Ant movedAnt = testColony.move(antList.get(a), nextNode);

                        // Alte Ameise in der Liste durch neue Ameise mit aktualisierter visitedNodes Liste ersetzen
                        antList.set(a, movedAnt);

                        // Benutzte Connection suchen und in tempConnection ablegen
                        Connection tempConnection = null;
                        for(Connection element : connections) {
                            if(element.cities.containsAll(antList.get(a).visitedNodes.subList(antList.get(a).visitedNodes.size() - 2, antList.get(a).visitedNodes.size() - 1))) {
                                tempConnection = element;
                            } // if
                        } // for

                        // Pheromon bei benutzter Connection updaten und an gleicher Stelle wieder in der Connetions Liste abspeichern
                        connections.set((tempConnection.ID - 1), testColony.updatePheromones(tempConnection, Q));

                        // Nodes mit aktualisierten Connections erzeugen
                        nodes = Parser.initNodes(connections);

                        // Aktualisierung fuer die Konsolenausgabe resetten
                        update = false;

                    } // else

                } // for

                // Evaporation der Pheromone an den Connections durchfuehren
                connections = testColony.evaporate(connections, RHO);

                // Nodes mit aktualisierten Connections erzeugen
                nodes = Parser.initNodes(connections);

                // Konsolenausgabe nur bei kuerzerer Laenge aktualisieren
                if(((oldLength - bestLength) > 0) || (oldLength == 0)) {
                    oldLength = bestLength;
                    update = true;
                } // if

                // PrettyPrint
                StringBuilder sb = new StringBuilder("[");
                for(int j = 0; j < bestRoute.size(); j++) {
                    sb.append(bestRoute.get(j));

                    if(j == 0 || bestRoute.get(j) != bestRoute.get(0)) {
                        sb.append(" => ");
                    } // if
                } // for
                sb.append("]");

                if(update && (bestLength != 0) || (i == CYCLES)) {
                    System.out.println("CYCLE: " + i + "\nAMEISEN: " + ANTS + "\nSTAEDTE: " + CITIES + "\nAKTUELLE LAENGE: " + bestLength + "\nBEST SOLUTION: " + BESTSOLUTION + "\nAKTUELLE ROUTE: " + sb + "\n");
                } // if

                // Berechnung des Outputs fuer die Dauer der Berechnung fuer die bekannte BEST_SOLUTION, wenn diese gefunden wurde
                if (bestLength == BESTSOLUTION && !foundBestRoute) {
                    long duration = (System.currentTimeMillis() - startTime); // Dauer
                    durationOutput = new StringBuilder();
                    durationOutput.append("\n\nBEST SOLUTION WURDE ERREICHT :-)\n");
                    durationOutput.append("\n\n.::AUSWERTUNG FUER DIE BERECHNUNG DER BEST SOLUTION::.\n\n");
                    durationOutput.append("ANZAHL DER STAEDTE: " + CITIES + "\n");
                    durationOutput.append("ANZAHL DER AMEISEN: " + ANTS + "\n");
                    durationOutput.append("ANZAHL DER CYCLES: " + CYCLES + "\n");
                    durationOutput.append("BEST SOLUTION GEFUNDEN IN CYCLE: " + i + "\n");
                    durationOutput.append("BENUTZTE ROUTE: " + sb + "\n");
                    durationOutput.append("DAUER: " + ((duration / 1000) / 3600) + " STUNDEN "
                            + (((duration / 1000) % 3600) / 60) + " MINUTEN "
                            + ((duration / 1000) % 60) + " SEKUNDEN "
                            + (duration % 1000) + " MILLISEKUNDEN\n");
                    foundBestRoute = true; // Best Solution gefunden
                } // if

                if(i == CYCLES) {
                    System.out.println(durationOutput);
                } // if

                i++; // Takt hochzaehlen
            } // while
        } // else

    } // main

} // class TestFrame
