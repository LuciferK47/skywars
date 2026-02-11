package skywars;

public abstract class Ship {
    protected Square currentTile;

    public Ship(Square spawnTile) {
        this.currentTile = spawnTile;
        this.currentTile.addShip(this);
    }

    public Square getCurrentSquare() {
        return currentTile;
    }

    public void setCurrentSquare(Square nextTile) {
        if (currentTile != null) {
            currentTile.removeShip(this);
        }
        currentTile = nextTile;
        nextTile.addShip(this);
    }

    // Called every time a move is triggered
    public abstract void move(Grid board);

    @Override
    public abstract String toString(); // Used for printing or GUI display
}
