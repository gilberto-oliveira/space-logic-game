package br.edu.unifesspa.mc.logicgame.game.model;

import br.edu.unifesspa.mc.logicgame.framework.image.Animation;

public abstract class Shot extends Sprite {

    public Shot(Animation anim) {
        super(anim);
        super.setVelocityY(super.getMaxSpeed());
    }

    @Override
    public void update() {
        this.setY(this.getY() - this.getVelocityY());
    }

    public static class One extends Shot {

        public One(Animation anim) {
            super(anim);
        }

    }

    public static class Zero extends Shot {

        public Zero(Animation anim) {
            super(anim);
        }

    }

}
