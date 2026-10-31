/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package memoryjuggle;

import java.util.ArrayList;

/**
 *
 * @author trist
 */
public class MemoryJuggle {
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<CardPair> myCards = new ArrayList<>();

        myCards.add(new CardPair("Apple", "Mansanas"));
        myCards.add(new CardPair("Dog", "Aso"));
        myCards.add(new CardPair("Cat", "Pusa"));
        myCards.add(new CardPair("Cat", "Pusa"));
        
        
        
        Difficulty diff = new Difficulty();
        diff.easy();

        // Pass myCards to MatchCards
        MatchCards session = new MatchCards(myCards, diff);



    }
    
}
