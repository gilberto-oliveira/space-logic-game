package br.edu.unifesspa.mc.logicgame.framework.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;

public class GameStateManager {

    public static final String EXIT_GAME = "_ExitGame";

    private Map<String, GameState> gameStates;
    private GameState currentState;
    private boolean done;

    public GameStateManager() {
        currentState = null;
        gameStates = new HashMap<>();
    }

    public void addState(GameState state) {
        gameStates.put(state.getName(), state);
    }

    public Iterator<GameState> getStates() {
        return gameStates.values().iterator();
    }

    public boolean isDone() {
        return done;
    }

    public void setState(String name) {
        // clean up old state
        if (currentState != null) {
            currentState.tearDown();
        }

        if (name == EXIT_GAME) {
            done = true;
        } else {
            // set new state
            currentState = (GameState) gameStates.get(name);

            if (currentState != null) {
                currentState.setup();
            }
        }
    }

    public void processLogics() {
        // if no state, pause a short time
        if (currentState == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        } else {
            String nextState = currentState.checkForStateChange();
            if (nextState != null) {
                setState(nextState);
            } else {
                currentState.processLogics();
            }
        }
    }

    public void renderGraphics(Graphics2D g) {
        if (currentState != null) {
            currentState.renderGraphics(g);
        } else {
            // if no state, draw the default image to the screen
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, ScreenManager.getInstance().getWidth(), ScreenManager.getInstance().getHeight());
        }
    }

}
