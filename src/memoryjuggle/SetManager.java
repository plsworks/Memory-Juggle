package memoryjuggle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SetManager {

    private final String filename;
    private final ArrayList<CardSet> sets = new ArrayList<>();

    public SetManager(String filename) {
        this.filename = filename;
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

    public boolean addSet(CardSet s) {
        if (s == null || findSet(s.getName()) != null) {
            return false;
                }
        sets.add(s);
        save();
        return true;
    }

    public boolean removeSet(CardSet s) {
        if (s == null || !sets.remove(s)) {
            return false;
       
    }
    save();
    return true;
    }

    private void load() {
     File file = new File(filename);
      if (!file.exists()) {
            return;
        }
      
     try(BufferedReader reader = new BufferedReader(new FileReader(file))){
         String line;
           while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length != 3) {
                    continue;
                }

            String setName = parts[0].trim();
                String cardA   = parts[1].trim();
                String cardB   = parts[2].trim();
          
          if (setName.isEmpty()) {
                    continue;
                }
                 CardSet set = findSet(setName);
                if (set == null) {
                    set = new CardSet(setName);
                    sets.add(set);
                }

                if (cardA.isEmpty() && cardB.isEmpty()) {
                    continue;
                }
            set.getPairs().add(new CardPair(cardA, cardB));

           }

     }
     catch(IOException e){
         System.err.println("Could not load " + filename + ": " + e.getMessage());
     }
    }

    public void save() {
        try(PrintWriter prnt = new PrintWriter(new File(filename))) {
            for(CardSet set : sets){
                  if (set.getPairs().isEmpty()) {
                   prnt.println(set.getName() + "||");
                    continue;
                }
            for(CardPair pair : set.getPairs()){
                 prnt.println(set.getName() + "|"
                              + pair.getCardA() + "|"
                              + pair.getCardB());
            }
            }
        }
        catch (IOException e){
            System.err.println("Could not save " + filename + ": " + e.getMessage());
        }   
    }

    public ArrayList<CardSet> search(String query) {
         ArrayList<CardSet> result = new ArrayList<>();
        if (query == null) {
            return result;
        }
        String nice = query.toLowerCase();
        for (CardSet set : sets) {
            if (set.getName().toLowerCase().contains(nice)) {
                result.add(set);
            }
        }
        return result;
    }
    }
