package br.edu.unifesspa.mc.logicgame.game;

import java.awt.Graphics2D;

import br.edu.unifesspa.mc.logicgame.framework.core.LoopSteps;
import br.edu.unifesspa.mc.logicgame.framework.core.MainLoop;
import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;
import br.edu.unifesspa.mc.logicgame.framework.core.profile.ProfilerProxy;
import br.edu.unifesspa.mc.logicgame.framework.input.KeyManager;
import br.edu.unifesspa.mc.logicgame.framework.state.GameStateManager;
import br.edu.unifesspa.mc.logicgame.game.state.ExitGameState;
import br.edu.unifesspa.mc.logicgame.game.state.FirstGameState;
import br.edu.unifesspa.mc.logicgame.game.state.SplashGameState;
import br.edu.unifesspa.mc.logicgame.game.state.ui.MenuGameState;

public class GameCore implements LoopSteps {

    private MainLoop gameLoop;
    private GameStateManager gameStateManager;

    public GameCore(boolean profile) {
        super();

        if (profile) {
            gameLoop = new MainLoop((LoopSteps) ProfilerProxy.newProfiled(this), GameSettings.getInstance().getUps());
        } else {
            gameLoop = new MainLoop(this, GameSettings.getInstance().getUps());
        }

        Thread gameThread = new Thread(gameLoop);
        gameThread.setName("Space Logic Thread");
        gameThread.start();
    }

    @Override
    public void setup() {
        setupScreen();

        ExitGameState exit = new ExitGameState();
        FirstGameState first = new FirstGameState(exit);
        MenuGameState menu = new MenuGameState(first);
        SplashGameState splash = new SplashGameState(menu);

        gameStateManager = new GameStateManager();

        gameStateManager.addState(splash);
        gameStateManager.addState(menu);
        gameStateManager.addState(first);
        gameStateManager.addState(exit);

        new Thread() {

            @Override
            public void run() {
                gameStateManager.setState(splash.getName());
            }

        }.start();
    }

    @Override
    public void processLogics() {
        if (gameStateManager.isDone()) {
            gameLoop.stop();
        } else {
            gameStateManager.processLogics();
        }
    }

    @Override
    public void renderGraphics() {
        Graphics2D g2d = ScreenManager.getInstance().getGraphics();
        gameStateManager.renderGraphics(g2d);
        g2d.dispose();
    }

    @Override
    public void paintScreen() {
        ScreenManager.getInstance().update();
    }

    @Override
    public void tearDown() {
        ScreenManager.getInstance().restoreScreen();
    }

    private void setupScreen() {
        ScreenManager.getInstance().setFullScreen(GameSettings.getInstance().getDisplayMode());
        ScreenManager.getInstance().getFullScreenWindow().setCursor(KeyManager.INVISIBLE_CURSOR);
    }

}
