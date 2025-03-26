package br.edu.unifesspa.mc.logicgame.game.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.input.GameAction;
import br.edu.unifesspa.mc.logicgame.framework.input.KeyManager;
import br.edu.unifesspa.mc.logicgame.framework.state.GameState;
import br.edu.unifesspa.mc.logicgame.game.GameSettings;

public class SplashGameState implements GameState {

    private final long TOTAL_DURATION = 5000L;

    private ImageItem splashSprite;

    private long totalTime;
    private long startTime;
    private long currentTime;

    private KeyManager keyManager;
    private GameAction done;

    private String stateChange;
    private GameState nextState;

    public SplashGameState(GameState nextState) {
        this.nextState = nextState;
        done = new GameAction("Done", true);
        keyManager = new KeyManager();
        keyManager.map(KeyEvent.VK_ESCAPE, done);
    }

    @Override
    public void setup() {
        stateChange = null;

        startTime = System.currentTimeMillis();
        currentTime = startTime;
        totalTime = 0;
        keyManager.setComponent(ScreenManager.getInstance().getFullScreenWindow());
        splashSprite = GameSettings.getInstance().createImage("sprite_logo.png");
    }

    @Override
    public void processLogics() {
        // calculate the elapsed time
        long elapsedTime = System.currentTimeMillis() - currentTime;
        currentTime += elapsedTime;

        totalTime += elapsedTime;

        if ((totalTime > TOTAL_DURATION) || (done.isPressed())) {
            stateChange = nextState.getName();
        }
    }

    @Override
    public void renderGraphics(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, ScreenManager.getInstance().getWidth(), ScreenManager.getInstance().getHeight());
        splashSprite.draw(g2d, (ScreenManager.getInstance().getWidth() - splashSprite.getWidth()) / 2, (ScreenManager.getInstance().getHeight() - splashSprite.getHeight()) / 2);
    }

    @Override
    public void tearDown() {
        keyManager.resetAllGameActions();
        keyManager.setComponent(null);
        splashSprite = null;
    }

    @Override
    public String getName() {
        return "_SplashState";
    }

    @Override
    public String checkForStateChange() {
        return stateChange;
    }

}
