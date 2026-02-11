package skywars;

import java.util.List;

public class EngineState {
    public int masterRow;
    public int masterCol;
    public String masterMode;
    public List<EnemyData> raiders;

    public static class EnemyData {
        public String type;
        public int row;
        public int col;
    }
}
