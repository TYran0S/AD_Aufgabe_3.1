package aufgabe3_1;

import java.util.*;

public class Connection {
    final public int ID;                // ID der Connection
    final public int length;            // Laenge der Connection
    final public List<Integer> cities;  // Mit dieser Connection verbundene Cities
    final public double pheromon;       // Pheromonkonzentration

    public Connection(int connectionID, int length, double pheromon, List<Integer> cities) {
        this.ID = connectionID;
        this.length = length;
        this.pheromon = pheromon;
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "ConnectionID: " + ID + " | Length: " + length + " | CityID's: "
                + cities.toString() + " | Pheromon: " + pheromon + "\n";
    }

}
