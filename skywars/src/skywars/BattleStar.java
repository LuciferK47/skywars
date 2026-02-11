package skywars;

public class BattleStar extends EnemyShip {
    public BattleStar(Square spawnTile) {
        super(spawnTile);
    }

    @Override
    public String toString() {
        return "Battle Star";
    }
}
