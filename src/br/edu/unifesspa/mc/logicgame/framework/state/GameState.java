package br.edu.unifesspa.mc.logicgame.framework.state;

import java.awt.Graphics2D;

public interface GameState {

    public String getName();

    public String checkForStateChange();

    public void setup();

    public void processLogics();

    public void renderGraphics(Graphics2D g2d);

    public void tearDown();

}
