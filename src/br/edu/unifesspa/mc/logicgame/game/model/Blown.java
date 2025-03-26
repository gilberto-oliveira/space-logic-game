package br.edu.unifesspa.mc.logicgame.game.model;

import br.edu.unifesspa.mc.logicgame.framework.image.Animation;

public class Blown extends Sprite {

    public Blown(Animation anim) {
        super(anim);
    }

    @Override
    public void update() {
        anim.update();
    }

    public boolean isEndFrame() {
        return anim.isEndFrame();
    }

}
