
package memoryjuggle;

import java.util.ArrayList;
import java.util.Collections;

public class CardSet {
    private int id;   // set_id in the database (0 until saved)
    private String name;
    private ArrayList<CardPair> pairs;

    public CardSet(String name) {
        this.name = name;
        this.pairs = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    // Throws IllegalArgumentException if the pair is not allowed.
    // ignoreIndex = the row being edited (-1 when adding), so it doesn't clash with itself.
    public void validatePair(String a, String b, int ignoreIndex) {
        if (a == null || b == null || a.trim().isEmpty() || b.trim().isEmpty()) {
            throw new IllegalArgumentException("Both fields are required.");
        }
        a = a.trim();
        b = b.trim();
        if (a.equalsIgnoreCase(b)) {
            throw new IllegalArgumentException("Word and match must be different.");
        }
        for (int i = 0; i < pairs.size(); i++) {
            if (i == ignoreIndex) continue;
            CardPair p = pairs.get(i);
            if (p.getCardA().equalsIgnoreCase(a) || p.getCardA().equalsIgnoreCase(b)
                    || p.getCardB().equalsIgnoreCase(a) || p.getCardB().equalsIgnoreCase(b)) {
                throw new IllegalArgumentException("One of those words is already used in this set.");
            }
        }
    }

public ArrayList<CardPair> getRandomPairs(int n) {
    if (n > pairs.size()) {
        throw new IllegalArgumentException(
            "Requested " + n + " pairs, but this set only has " + pairs.size());
    }
    ArrayList<CardPair> copy = new ArrayList<>(pairs);
    Collections.shuffle(copy);
    return new ArrayList<>(copy.subList(0, n));
}
}