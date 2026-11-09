package memoryjuggle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SetManager {

    // In-memory copy of the database. Every change hits the DB first, then this list.
    private final ArrayList<CardSet> sets = new ArrayList<>();

    public SetManager() {
        load();
    }

    public ArrayList<CardSet> getSets() {
        return sets;
    }

    public CardSet findSet(String name) {
        for (CardSet set : sets) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        return null;
    }

    // Runs an INSERT/UPDATE/DELETE and returns the generated key (0 if none).
    private static int exec(String sql, Object... args) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    // shortcut: reuse IllegalArgumentException so the screens' existing catch shows the message
    private static IllegalArgumentException dbError(SQLException e) {
        return new IllegalArgumentException("Database error: " + e.getMessage());
    }

    private void load() {
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT set_id, set_name FROM card_sets ORDER BY set_id");
             PreparedStatement ps = c.prepareStatement(
                     "SELECT pair_id, word, match_word FROM card_pairs WHERE set_id = ? ORDER BY pair_id")) {

            while (rs.next()) {
                CardSet set = new CardSet(rs.getString("set_name"));
                set.setId(rs.getInt("set_id"));

                ps.setInt(1, set.getId());
                try (ResultSet pr = ps.executeQuery()) {
                    while (pr.next()) {
                        CardPair p = new CardPair(pr.getString("word"), pr.getString("match_word"));
                        p.setId(pr.getInt("pair_id"));
                        set.getPairs().add(p);
                    }
                }
                sets.add(set);
            }
        } catch (SQLException e) {
            System.err.println("Could not load sets: " + e.getMessage());
        }
    }

    public boolean addSet(CardSet s) {
        if (s == null || findSet(s.getName()) != null) {
            return false;
        }
        try {
            s.setId(exec("INSERT INTO card_sets (set_name) VALUES (?)", s.getName()));
            sets.add(s);
            return true;
        } catch (SQLException e) {
            System.err.println("Could not add set: " + e.getMessage());
            return false;
        }
    }

    public boolean removeSet(CardSet s) {
        if (s == null || !sets.contains(s)) {
            return false;
        }
        try {
            exec("DELETE FROM card_sets WHERE set_id = ?", s.getId());   // pairs go too (ON DELETE CASCADE)
            sets.remove(s);
            return true;
        } catch (SQLException e) {
            System.err.println("Could not delete set: " + e.getMessage());
            return false;
        }
    }

    public void renameSet(CardSet set, String newName) {
        newName = newName == null ? "" : newName.trim();
        if (newName.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        CardSet other = findSet(newName);
        if (other != null && other != set) {
            throw new IllegalArgumentException("That name is already used.");
        }
        try {
            exec("UPDATE card_sets SET set_name = ? WHERE set_id = ?", newName, set.getId());
        } catch (SQLException e) {
            throw dbError(e);
        }
        set.setName(newName);
    }

    public void addPair(CardSet set, String a, String b) {
        set.validatePair(a, b, -1);
        a = a.trim();
        b = b.trim();
        try {
            CardPair p = new CardPair(a, b);
            p.setId(exec("INSERT INTO card_pairs (set_id, word, match_word) VALUES (?, ?, ?)",
                    set.getId(), a, b));
            set.getPairs().add(p);
        } catch (SQLException e) {
            throw dbError(e);
        }
    }

    public void updatePair(CardSet set, int index, String a, String b) {
        set.validatePair(a, b, index);
        a = a.trim();
        b = b.trim();
        CardPair p = set.getPairs().get(index);
        try {
            exec("UPDATE card_pairs SET word = ?, match_word = ? WHERE pair_id = ?", a, b, p.getId());
        } catch (SQLException e) {
            throw dbError(e);
        }
        p.setCardA(a);
        p.setCardB(b);
    }

    public void removePair(CardSet set, int index) {
        CardPair p = set.getPairs().get(index);
        try {
            exec("DELETE FROM card_pairs WHERE pair_id = ?", p.getId());
        } catch (SQLException e) {
            throw dbError(e);
        }
        set.removePair(index);
    }

    public ArrayList<CardSet> search(String query) {
        ArrayList<CardSet> result = new ArrayList<>();
        if (query == null) {
            return result;
        }
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT set_id FROM card_sets WHERE set_name LIKE ?")) {
            ps.setString(1, "%" + query + "%");
            ArrayList<Integer> ids = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt(1));
                }
            }
            for (CardSet set : sets) {
                if (ids.contains(set.getId())) {
                    result.add(set);
                }
            }
        } catch (SQLException e) {
            System.err.println("Search failed: " + e.getMessage());
        }
        return result;
    }
}
