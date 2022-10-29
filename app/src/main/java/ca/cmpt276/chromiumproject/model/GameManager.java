package ca.cmpt276.chromiumproject.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameManager implements Iterable<GameConfig>{
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

    public void addNewGameConfig(GameConfig config) {
        gameConfigs.add(config);
    }

    public int getNumGameConfigs() {
        return gameConfigs.size();
    }

    public GameConfig getGameConfigByIndex(int index) {
        return gameConfigs.get(index);
    }

    @NonNull
    @Override
    public Iterator<GameConfig> iterator() {
        return gameConfigs.iterator();
    }
}
