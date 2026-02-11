package skywars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    public static final int SIZE = 4;
    private Square[][] tiles;
    private Random rng = new Random();

    public Grid() {
        tiles = new Square[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                tiles[row][col] = new Square(row, col);
            }
        }
    }

    private Engine game;
    public void setEngine(Engine game) {
        this.game = game;
    }
    public Engine getEngine() {
        return game;
    }


    public Square getSquare(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) return null;
        return tiles[row][col];
    }

    public List<Square> getNeighborSquares(int row, int col) {
        List<Square> neighbors = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                Square s = getSquare(row + dr, col + dc);
                if (s != null) neighbors.add(s);
            }
        }
        return neighbors;
    }

    public Square getRandomNeighbor(Square tile) {
        List<Square> neighbors = getNeighborSquares(tile.getRow(), tile.getCol());
        return neighbors.isEmpty() ? tile : neighbors.get(rng.nextInt(neighbors.size()));
    }

    public Square getTopLeft() {
        return tiles[0][0];
    }

    public void printGrid() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                System.out.print(tiles[row][col] + " ");
            }
            System.out.println();
        }
    }
}
