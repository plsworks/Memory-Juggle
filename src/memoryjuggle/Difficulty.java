/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package memoryjuggle;

/**
 *
 * @author trist
 */
public class Difficulty{
    private int rows;
    private int columns;
    private String difficulty;

    public void easy(){
        this.rows = 4;
        this.columns = 4;
        this.difficulty = "easy";
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public String getDifficulty() {
        return difficulty;
    }
    
}
