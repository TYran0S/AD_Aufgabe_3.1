package aufgabe3_1;

import java.util.*;

public class BruteForce {

    public static List<Node> node1 = Parser.initNodes(Parser.initConnections(Parser.parseTestFile(TestFrame.TESTDATA)));

    /**
     * Berechnet alle Permutationen und gibt die mit der kuerzesten Strecke zurueck
     * 
     * @param nodes         Liste ueber alle Cities
     * @param endIndex      endIndex der Liste
     * @param connections   Alle Connections
     * @return              Zweidimensionale ArrayList ueber alle Permutationen der Cities
     */
    public static List<Node> calculatePermutation(List<Node> nodes, int endIndex, List<Connection> connections) {

        if (endIndex == 0) {
            node1 = calcNodes(node1, nodes, connections);
        } else {
            calculatePermutation(nodes, endIndex - 1, connections);

            for (int i = 0; i < endIndex; i++) {
                exchangeNode(nodes, i, endIndex);
                calculatePermutation(nodes, endIndex - 1, connections);
                exchangeNode(nodes, i, endIndex);
            } // for
        } // else
        return node1;
    }

    /**
     * Berechnet die bessere Permutation von zwei Node-Listen
     * 
     * @param nodeBestOld   Liste der alten, besten Nodes
     * @param nodeNew       Liste der neuen Node
     * @param connections   Alle Connections
     * @return              Lister der Nodes
     */
    private static List<Node> calcNodes(List<Node> nodeBestOld, List<Node> nodeNew, List<Connection> connections) {
        List<Node> bestNode = nodeBestOld;

        if ((calcWayLength(nodeNew, connections)) <= (calcWayLength(nodeBestOld, connections))) {
            bestNode = new ArrayList<Node>(nodeNew);
        } // if
        return bestNode;
    }

    /**
     * Berechnet die Laenge fuer eine Node-Permutation
     * 
     * @param tour              Alle zugehoerigen Nodes
     * @param connectionList    Alle Connections
     * @return                  Die Weglaenge
     */
    public static int calcWayLength(List<Node> tour, List<Connection> connectionList) {
        int sum = 0;

        for (int i = 0; i < (tour.size() - 1); i++) {
            sum += findConnection(tour.get(i), tour.get(i + 1), connectionList);
        } // for

        // Vor dem return den Rueckweg hinzu addieren
        return sum + findConnection(tour.get(0), tour.get(tour.size() - 1), connectionList);
    }

    /**
     * Findet die Connection zwischen zwei Nodes und gibt ihre Laenge zurueck
     * 
     * @param one               Der erste Node
     * @param two               Der zweite Node
     * @param connectionList    Alle Connections
     * @return                  Laenge der Connection
     */
    public static int findConnection(Node one, Node two, List<Connection> connectionList) {
        Connection tmp = null;

        for (int i = 0; i < connectionList.size(); i++) {

            // CityID's aus der connectionList fuer mehr Speed zwischenspeichern
            final int tempID1 = connectionList.get(i).cities.get(0);
            final int tempID2 = connectionList.get(i).cities.get(1);

            if ((tempID1 == one.ID || tempID2 == one.ID)
             && (tempID1 == two.ID || tempID2 == two.ID)) {

                tmp = connectionList.get(i);
            } // if
        } // for

        return tmp.length;
    }

    /**
     * Vertauscht Nodes innerhalb einer Liste (Util für Permutationsberechnung)
     * 
     * @param nodeList  Die Liste der Nodes
     * @param index1    Der erste Index
     * @param index2    Der zweite Index
     */
    public static void exchangeNode(List<Node> nodeList, int index1, int index2) {
        Node tmp = nodeList.get(index1);
        nodeList.set(index1, nodeList.get(index2));
        nodeList.set(index2, tmp);
    }

}
