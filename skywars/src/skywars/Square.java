package skywars;

import java.util.ArrayList;
import java.util.List;

public class Square {
    private int row;
    private int col;
    private List<Ship> occupants;

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.occupants = new ArrayList<>();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void addShip(Ship ship) {
        occupants.add(ship);
    }

    public void removeShip(Ship ship) {
        System.out.println("Removing from tile: " + ship.toString());
        occupants.remove(ship);
    }
    

    public List<Ship> getOccupants() {
        return occupants;
    }

    public boolean hasMasterShip() {
        return occupants.stream().anyMatch(s -> s instanceof MasterShip);
    }

    public List<EnemyShip> getEnemyUnits() {
        List<EnemyShip> raiders = new ArrayList<>();
        for (Ship s : occupants) {
            if (s instanceof EnemyShip && s.getCurrentSquare() == this) {
                raiders.add((EnemyShip) s);
            }
        }
        return raiders;
    }

    @Override
    public String toString() {
        if (occupants.isEmpty()) return "";
    
        StringBuilder sb = new StringBuilder("<html>");
        for (Ship ship : occupants) {
            sb.append(ship.toString()).append("<br>");
        }
        sb.append("</html>");
        return sb.toString();
    }
    
}
