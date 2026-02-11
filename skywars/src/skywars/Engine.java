package skywars;

import java.awt.Component;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

public class Engine {
    private Grid board;
    private MasterShip master;
    private List<EnemyShip> raiders;
    private Random rng;
    private List<EngineObserver> observers;
    private MasterShip.ShipMode currentMode = MasterShip.ShipMode.DEFENSIVE;

    public Engine() {
        this.board = new Grid();
        board.setEngine(this);
        this.raiders = new ArrayList<>();
        this.rng = new Random();
        this.observers = new ArrayList<>();
        startNewEngine();
    }

    public void saveEngineToFile(Component parent) {
        String name = JOptionPane.showInputDialog(parent, "Enter a name for your save:");
        if (name == null || name.trim().isEmpty()) return;

        try {
            new File("saves").mkdirs(); // create saves/ folder if not exists
            FileWriter writer = new FileWriter("saves/" + name.trim() + ".json");

            EngineState state = new EngineState();
            Square masterSq = master.getCurrentSquare();
            state.masterRow = masterSq.getRow();
            state.masterCol = masterSq.getCol();
            state.masterMode = master.getMode().name();

            List<EngineState.EnemyData> raiderDataList = new ArrayList<>();
            for (EnemyShip raider : raiders) {
                EngineState.EnemyData ed = new EngineState.EnemyData();
                ed.row = raider.getCurrentSquare().getRow();
                ed.col = raider.getCurrentSquare().getCol();
                ed.type = raider.getClass().getSimpleName();
                raiderDataList.add(ed);
            }

            state.raiders = raiderDataList;

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(state, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void loadEngineFromFile(Component parent) {
        File folder = new File("saves");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            JOptionPane.showMessageDialog(parent, "No saved games found.");
            return;
        }

        String[] fileNames = Arrays.stream(files)
            .map(File::getName)
            .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(
            parent,
            "Select a save to load:",
            "Load Engine",
            JOptionPane.PLAIN_MESSAGE,
            null,
            fileNames,
            fileNames[0]
        );

        if (selected == null) return;

        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader("saves/" + selected);
            EngineState state = gson.fromJson(reader, EngineState.class);
            reader.close();

            for (int row = 0; row < Grid.SIZE; row++) {
                for (int col = 0; col < Grid.SIZE; col++) {
                    board.getSquare(row, col).getOccupants().clear();
                }
            }

            raiders.clear();
            master = null;

            Square masterSq = board.getSquare(state.masterRow, state.masterCol);
            master = new MasterShip(masterSq);
            master.setMode(MasterShip.ShipMode.valueOf(state.masterMode));

            for (EngineState.EnemyData ed : state.raiders) {
                Square sq = board.getSquare(ed.row, ed.col);
                EnemyShip raider;
                switch (ed.type) {
                    case "BattleCruiser": raider = new BattleCruiser(sq); break;
                    case "BattleShooter": raider = new BattleShooter(sq); break;
                    default: raider = new BattleStar(sq); break;
                }
                raiders.add(raider);
            }

            notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   

    public void startNewEngine() {
        for (int row = 0; row < Grid.SIZE; row++) {
            for (int col = 0; col < Grid.SIZE; col++) {
                Square tile = board.getSquare(row, col);
                tile.getOccupants().clear();
            }
        }
    
        raiders.clear();
        master = null;
    
        int row = rng.nextInt(Grid.SIZE);
        int col = rng.nextInt(Grid.SIZE);
        Square spawnTile = board.getSquare(row, col);
    
        master = new MasterShip(spawnTile);
        master.setMode(currentMode);
    
        notifyObservers();
    }
    

    public void moveTurn() {
        if (!master.isAlive()) return;

        // Step 1: Move master and all raiders
        master.move(board);
        for (EnemyShip e : raiders) {
            e.move(board);
        }

        // Step 2: Resolve conflict (AFTER all occupants have moved)
        master.resolveConflict(this);

        // Step 3: Spawn new raider sometimes
        if (rng.nextInt(3) == 0) {
            spawnRandomEnemy();
        }

        // Step 4: Remove dead raiders
        raiders.removeIf(e -> e.getCurrentSquare() == null);

        notifyObservers();
    }

    private void spawnRandomEnemy() {
        Square spawn = board.getTopLeft();
        EnemyShip raider;
        int type = rng.nextInt(3);
        switch (type) {
            case 0: raider = new BattleCruiser(spawn); break;
            case 1: raider = new BattleShooter(spawn); break;
            default: raider = new BattleStar(spawn); break;
        }
        raiders.add(raider);
    }

    public boolean isEngineOver() {
        return !master.isAlive();
    }

    public Grid getGrid() {
        return board;
    }

    public MasterShip getMasterShip() {
        return master;
    }

    public List<EnemyShip> getRaiders() {
        return raiders;
    }

    public void removeRaider(EnemyShip e) {
        raiders.remove(e);
    }

    public void setMasterMode(MasterShip.ShipMode tactic) {
        this.currentMode = tactic;
        if (master != null) {
            master.setMode(tactic);
        }
    }
    
    public void addObserver(EngineObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (EngineObserver obs : observers) {
            obs.onEngineStateChanged();
        }
    }
}
