package br.edu.unifesspa.mc.logicgame.game.state;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;
import br.edu.unifesspa.mc.logicgame.framework.image.Animation;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItemList;
import br.edu.unifesspa.mc.logicgame.framework.input.GameAction;
import br.edu.unifesspa.mc.logicgame.framework.input.KeyManager;
import br.edu.unifesspa.mc.logicgame.framework.state.GameState;
import br.edu.unifesspa.mc.logicgame.framework.state.GameStateManager;
import br.edu.unifesspa.mc.logicgame.game.GameSettings;
import br.edu.unifesspa.mc.logicgame.game.model.Sprite;

public class ExitGameState implements GameState {

    private final long TOTAL_DURATION = 10000L;

    private ImageItem splashSprite;

    private long totalTime;
    private long startTime;
    private long currentTime;

    private KeyManager keyManager;
    private GameAction done;

    private String stateChange;

    // parallax scrolling
    private Sprite background;
    private int scrollingOffset;

    public ExitGameState() {
        done = new GameAction("Done", true);
        background = new Sprite(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("menu_scrolling.jpg")), 0));
        scrollingOffset = 0;
        keyManager = new KeyManager();
        keyManager.map(KeyEvent.VK_ESCAPE, done);
    }

    @Override
    public String getName() {
        return "_ExitState";
    }

    @Override
    public String checkForStateChange() {
        return stateChange;
    }

    @Override
    public void setup() {
        stateChange = null;
        startTime = System.currentTimeMillis();
        currentTime = startTime;
        totalTime = 0;
        keyManager.setComponent(ScreenManager.getInstance().getFullScreenWindow());
        splashSprite = GameSettings.getInstance().createImage("sprite_msg.png");
    }

    @Override
    public void processLogics() {
        long elapsedTime = System.currentTimeMillis() - currentTime;
        currentTime += elapsedTime;

        totalTime += elapsedTime;

        if ((totalTime > TOTAL_DURATION) || (done.isPressed())) {
            stateChange = GameStateManager.EXIT_GAME;
        }

        scrollingOffset--;
        if (scrollingOffset < -background.getWidth()) {
            scrollingOffset = 0;
        }
    }

    @Override
    public void renderGraphics(Graphics2D g2d) {
        background.draw(g2d, scrollingOffset, 0);
        background.draw(g2d, scrollingOffset + background.getWidth(), 0);

        splashSprite.draw(g2d, (ScreenManager.getInstance().getWidth() - splashSprite.getWidth()) / 2, (ScreenManager.getInstance().getHeight() - splashSprite.getHeight()) / 2);
    }

    @Override
    public void tearDown() {
        keyManager.resetAllGameActions();
        keyManager.setComponent(null);
    }

}
