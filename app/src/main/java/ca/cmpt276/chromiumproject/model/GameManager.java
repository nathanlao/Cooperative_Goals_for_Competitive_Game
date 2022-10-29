package ca.cmpt276.chromiumproject.model;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    List<GameConfig> gameConfigs = new ArrayList<>();

    private static GameManager instance;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

}
