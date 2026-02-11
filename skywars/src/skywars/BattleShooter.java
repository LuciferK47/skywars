package skywars;

public class BattleShooter extends EnemyShip {
    public BattleShooter(Square spawnTile) {
        super(spawnTile);
    }

    @Override
    public String toString() {
        return "Battle Shooter";
    }
}
