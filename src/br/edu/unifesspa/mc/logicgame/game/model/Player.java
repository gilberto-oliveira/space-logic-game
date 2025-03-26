package br.edu.unifesspa.mc.logicgame.game.model;

import br.edu.unifesspa.mc.logicgame.framework.image.Animation;

public class Player extends Ship {

    public Player(Animation anim) {
        super(anim);
        this.setLife(DEFAULT_LIFE * 2);
    }

}
