package skywars;

import java.util.ArrayList;
import java.util.List;

public class MasterShip extends Ship {
    public enum ShipMode {
        DEFENSIVE, OFFENSIVE
    }

    private ShipMode tactic = ShipMode.DEFENSIVE;
    private boolean alive = true;

    public MasterShip(Square spawnTile) {
        super(spawnTile);
    }

    public ShipMode getMode() {
        return tactic;
    }

    public void setMode(ShipMode tactic) {
        this.tactic = tactic;
    }

    public boolean isAlive() {
        return alive;
    }

    private int getDeathThreshold() {
        return tactic == ShipMode.DEFENSIVE ? 2 : 3;
    }

    @Override
    public void move(Grid board) {
        if (!alive) return;
        Square next = board.getRandomNeighbor(currentTile);
        setCurrentSquare(next); // only move — no logic yet
    }

    public void resolveConflict(Engine game) {
        if (!alive || currentTile == null) return;

        List<EnemyShip> raidersHere = new ArrayList<>();
        for (Ship ship : new ArrayList<>(currentTile.getOccupants())) {
            if (ship instanceof EnemyShip && ship.getCurrentSquare() == currentTile) {
                raidersHere.add((EnemyShip) ship);
            }
        }

        if (raidersHere.size() >= getDeathThreshold()) {
            alive = false;
        } else if (raidersHere.size() == 1) {
            EnemyShip raider = raidersHere.get(0);
            currentTile.removeShip(raider);
            raider.currentTile = null;
            game.removeRaider(raider);
            EngineEvents.recordDestruction(raider);
        }
    }

    @Override
    public String toString() {
        return "Master Ship";
    }
}
