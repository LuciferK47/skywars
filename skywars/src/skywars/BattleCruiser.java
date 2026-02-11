package skywars;

public class BattleCruiser extends EnemyShip {
    public BattleCruiser(Square spawnTile) {
        super(spawnTile);
    }

    @Override
    public String toString() {
        return "Battle Cruiser";
    }
}
