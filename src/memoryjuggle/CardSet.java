
package memoryjuggle;

import java.util.ArrayList;

public class CardSet {
    private String name;
    private ArrayList<CardPair> pairs;

    public CardSet(String name) {
        this.name = name;
        this.pairs = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<CardPair> getPairs() {
        return pairs;
    }

    public void removePair(int index) {
        if (index >= 0 && index < pairs.size()) {
            pairs.remove(index);
        }
    }

    public boolean addPair(String a, String b) {

        // Reject blank inputs
        if (a == null || b == null ||
            a.trim().isEmpty() || b.trim().isEmpty()) {
            return false;
        }

        // Reject delimiter
        if (a.contains("|") || b.contains("|")) {
            return false;
        }

        // Reject identical words
        if (a.equalsIgnoreCase(b)) {
            return false;
        }

      // Check for existing words
    for (CardPair pair : pairs) {
    if (pair.getCardA().equalsIgnoreCase(a) ||
        pair.getCardA().equalsIgnoreCase(b) ||
        pair.getCardB().equalsIgnoreCase(a) ||
        pair.getCardB().equalsIgnoreCase(b)) {
        return false;
    }
    }

        // Add valid pair
        pairs.add(new CardPair(a.trim(), b.trim()));
        return true;
    }
}
