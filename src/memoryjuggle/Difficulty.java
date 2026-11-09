package memoryjuggle;

public class Difficulty {
    private String difficulty;
    private int rows;
    private int columns;
    private int pairs;
    private int previewMs;
    private int startSwaps;    // visible swaps after the preview, before play
    private int attemptSwaps;  // visible swaps after every attempted pair
    private int swapMs;        // time between swaps

    private void set(String difficulty, int rows, int columns, int pairs, int previewMs,
                     int startSwaps, int attemptSwaps, int swapMs) {
        if (pairs != (rows * columns) / 2) {
            throw new IllegalArgumentException("pairs must equal rows*columns/2");
        }
        this.difficulty = difficulty;
        this.rows = rows;
        this.columns = columns;
        this.pairs = pairs;
        this.previewMs = previewMs;
        this.startSwaps = startSwaps;
        this.attemptSwaps = attemptSwaps;
        this.swapMs = swapMs;
    }

    public int getRows()           { return rows; }
    public int getColumns()        { return columns; }
    public String getDifficulty()  { return difficulty; }
    public int getRequiredPairs()  { return pairs; }
    public int getPreviewMs()      { return previewMs; }
    public int getStartSwaps()     { return startSwaps; }
    public int getAttemptSwaps()   { return attemptSwaps; }
    public int getSwapMs()         { return swapMs; }

    public void easy()       { set("Easy",       2, 4,  4, 3000, 0, 0, 0); }
    public void medium()     { set("Medium",     3, 4,  6, 2500, 0, 0, 0); }
    public void hard()       { set("Hard",       4, 4,  8, 2000, 0, 0, 0); }
    public void extreme()    { set("Extreme",    4, 5, 10, 1000, 6, 0, 500); }
    public void impossible() { set("Impossible", 4, 6, 12,  500, 8, 2, 400); }
}
