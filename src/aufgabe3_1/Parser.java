package aufgabe3_1;

import java.io.*;
import java.util.*;

public class Parser {

    // Startwert der Pheromone pro Connection
    final public static int pheromon = 0;

    /**
     * Liest eine (symmetrische) TSP Datei ein und uebertraegt die linke
     * untere Dreiecksmatrix in ein int Array.
     * Erfordert Nullen als Trennzeichen (siehe Aufbau der TSP Dateien).
     * 
     * @param filePath  String des Dateinamens / Dateipfades
     * @return int      Array mit den Laengen der TSP Datei
     */
    public static int[] parseTestFile(String filePath) {

        int[] resultArray = null;
        String line; // Zu verarbeitende Zeile
        String lineSplit[]; // Werte ohne Trennzeichen
        List<String> list = new LinkedList<String>(); // Alle Werte ohne Trennzeichen

        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {

                if((line.trim().charAt(0) == '0')) {
                    while(!(line.equals("EOF"))) {

                        // Unnoetige Whitespaces und Tabs am Anfang und Ende entfernen
                        line = line.trim();

                        // line bei jedem Trennzeichen " " aufsplitten und die
                        // String Fragmente einzeln im lineSplit Array speichern
                        lineSplit = line.split(" ");

                        // Nur Werte ohne Trennzeichen in Array abspeichern
                        for(String element : lineSplit) {
                            if(!element.isEmpty()) {
                                list.add(element);
                            } // if
                        } // for

                        // Naechste Zeile holen
                        line = br.readLine().trim();
                    } // while

                } // if
            } // while

            resultArray = new int[list.size()];

            // Liste nach (int) resultArray parsen
            for(int i = 0; i < resultArray.length; i++) {
                resultArray[i] = Integer.parseInt(list.get(i));
            } // for

            br.close(); // BufferedReader Stream schliessen

        } catch (Exception e) {
            System.err.println(e);
            System.err.println("TSP Datei konnte nicht eingelesen werden!");
            System.exit(0);
        } // catch

        return resultArray;
    }

    /**
     * Erzeugt eine ArrayList von Connections
     * 
     * @param array Array mit den Entfernungen zwischen den Nodes
     * @return      Eine ArrayList von Connections
     */
    public static List<Connection> initConnections(int[] array) {

        List<Connection> connectionList = new ArrayList<Connection>();
        int cityID1 = 1;
        int cityID2 = 1;
        int connectionID = 1;

        for (int i = 0; i < array.length; i++) {

            if (array[i] == 0) {
                cityID1++;
                cityID2 = 1;
            } else {
                if (cityID1 == cityID2) {
                    cityID2++;
                } // if

                List<Integer> cities = new ArrayList<Integer>();
                cities.add(cityID1);
                cities.add(cityID2);

                connectionList.add(new Connection(connectionID, array[i], pheromon, cities));
                cityID2++;
                connectionID++;
            } // else
        } // for

        return connectionList;
    }

    /**
     * Erzeugt eine ArrayListe aus Nodes
     * 
     * @param connections   ArrayListe von Connections
     * @return              ArrayList von Nodes
     */
    public static List<Node> initNodes(List<Connection> connections) {

        List<Node> nodeList = new ArrayList<Node>();
        Set<Integer> connectionSet = new HashSet<Integer>();

        for (int i = 0; i < connections.size(); i++) {
            connectionSet.addAll(connections.get(i).cities);
        } // for

        for (int i = 0; i < connectionSet.size(); i++) {

            List<Connection> connectionList = new ArrayList<Connection>();

            for (int j = 0; j < connections.size(); j++) {

                if (connections.get(j).cities.contains(i + 1)) {
                    connectionList.add(connections.get(j));
                } // if
            } // for

            nodeList.add(new Node(i + 1, connectionList));
        } // for

        return nodeList;
    }

}