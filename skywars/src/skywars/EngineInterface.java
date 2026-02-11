package skywars;

import java.awt.*;
import javax.swing.*;

public class EngineInterface extends JFrame implements EngineObserver {
    private Engine game;
    private JLabel[][] boardLabels;
    private JLabel infoBar;
    private JComboBox<String> tacticSelector;
    private Timer messageTimer;

    public EngineInterface() {
        super("Sky Wars");
        this.game = new Engine();
        game.addObserver(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel - Controls
        JPanel topPanel = new JPanel();
        JButton startButton = new JButton("Start New Engine");
        JButton moveButton = new JButton("Next Move");
        JButton saveButton = new JButton("Save Engine");
        JButton loadButton = new JButton("Load Engine");
        tacticSelector = new JComboBox<>(new String[]{"Defensive", "Offensive"});

        startButton.addActionListener(e -> {
            game.startNewEngine();
        });

        moveButton.addActionListener(e -> {
            game.moveTurn();

            String eventMessage = EngineEvents.getAndClearLastDestruction();
            if (eventMessage != null) {
                showTemporaryMessage(eventMessage, 3000);
            }

            if (game.isEngineOver()) {
                JOptionPane.showMessageDialog(this, "Engine Over! Master ship destroyed.");
            }
        });

        saveButton.addActionListener(e -> game.saveEngineToFile(this));
        loadButton.addActionListener(e -> game.loadEngineFromFile(this));

        tacticSelector.addActionListener(e -> {
            if (tacticSelector.getSelectedIndex() == 0) {
                game.setMasterMode(MasterShip.ShipMode.DEFENSIVE);
            } else {
                game.setMasterMode(MasterShip.ShipMode.OFFENSIVE);
            }
        });

        topPanel.add(startButton);
        topPanel.add(moveButton);
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(new JLabel("Mode:"));
        topPanel.add(tacticSelector);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Grid
        JPanel boardPanel = new JPanel(new GridLayout(Grid.SIZE, Grid.SIZE));
        boardLabels = new JLabel[Grid.SIZE][Grid.SIZE];
        for (int row = 0; row < Grid.SIZE; row++) {
            for (int col = 0; col < Grid.SIZE; col++) {
                boardLabels[row][col] = new JLabel(".", SwingConstants.CENTER);
                boardLabels[row][col].setPreferredSize(new Dimension(60, 60));
                addStyle(boardLabels[row][col]);
                boardPanel.add(boardLabels[row][col]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Bottom Panel - Status
        infoBar = new JLabel("Status: Ready");
        add(infoBar, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    @Override
    public void onEngineStateChanged() {
        Grid board = game.getGrid();

        for (int row = 0; row < Grid.SIZE; row++) {
            for (int col = 0; col < Grid.SIZE; col++) {
                Square tile = board.getSquare(row, col);
                boardLabels[row][col].setText(tile.toString());
            }
        }

        String status = game.isEngineOver() ? "Engine Over" : "Master is Alive";
        infoBar.setText("Status: " + status);

        System.out.println("---- GRID STATE ----");
        for (int row = 0; row < Grid.SIZE; row++) {
            for (int col = 0; col < Grid.SIZE; col++) {
                Square tile = game.getGrid().getSquare(row, col);
                System.out.printf("[%d,%d]: %s%n", row, col, tile.toString());
            }
        }
    }

    private void showTemporaryMessage(String message, int milliseconds) {
        infoBar.setText("Status: " + message);
        if (messageTimer != null && messageTimer.isRunning()) {
            messageTimer.stop();
        }
        messageTimer = new Timer(milliseconds, e -> {
            String status = game.isEngineOver() ? "Engine Over" : "Master is Alive";
            infoBar.setText("Status: " + status);
        });
        messageTimer.setRepeats(false);
        messageTimer.start();
    }

    private void addStyle(JLabel label) {
        label.setFont(new Font("Times New Roman", 0, 13));
        label.setForeground(Color.YELLOW);         // make text visible
        label.setBackground(Color.BLACK);          // background color
        label.setOpaque(true);                     // must be true to show background
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
}
