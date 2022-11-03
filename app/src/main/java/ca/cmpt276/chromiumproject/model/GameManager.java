package ca.cmpt276.chromiumproject.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GameManager class has a list of GameConfigs.
 * Contains methods to access and update this list
 * Use the iterator to iterate through the list. Use ByIndex methods to access the list via index values.
 * Singleton support.
 */

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

    public List<GameConfig> getGameConfigs() {
        return gameConfigs;
    }

    public void setGameConfigByIndex(int indexOfGame, GameConfig editedGame) {
        gameConfigs.set(indexOfGame, editedGame);
    }

    @NonNull
    @Override
    public Iterator<GameConfig> iterator() {
        return gameConfigs.iterator();
    }

    public void deleteGameConfig(GameConfig targetConfig) {
        gameConfigs.remove(targetConfig);
    }
}
