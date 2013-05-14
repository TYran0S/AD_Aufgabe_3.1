package aufgabe3_1;

import java.util.*;

public class Ant {
    final public int ID;                        // ID der Ameise
    final public List<Integer> visitedNodes;    // von der Ameise besuchte Nodes

    public Ant(int ID, List<Integer> visitedNodes) {
        this.ID = ID;
        this.visitedNodes = visitedNodes;
    }

    @Override
    public String toString() {
        return "Ant-ID: " + ID;
    }

}
