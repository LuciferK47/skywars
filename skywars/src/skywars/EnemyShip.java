package skywars;

import java.util.Random;

public abstract class EnemyShip extends Ship {
    private Random rng = new Random();

    public EnemyShip(Square spawnTile) {
        super(spawnTile);
    }

    @Override
    public void move(Grid board) {
        if (currentTile == null) return; // ✅ prevents ghost occupants from moving
        Square nextSquare = board.getRandomNeighbor(currentTile);
        setCurrentSquare(nextSquare);
    }

    // Each subclass will override this
    @Override
    public abstract String toString();
}
