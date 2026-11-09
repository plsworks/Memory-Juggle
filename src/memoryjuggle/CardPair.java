/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memoryjuggle;

/**
 *
 * @author trist
 */
public class CardPair {
    private int id;   // pair_id in the database (0 until saved)
    private String cardA;
    private String cardB;
    CardPair(String a, String b) {
        cardA = a;
        cardB = b;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public void setCardA(String a) { cardA = a; }
    public void setCardB(String b) { cardB = b; }

    public String getCardA() {
        return cardA;
    }
    public String getCardB() {
        return cardB;
    }
    
}
