package br.edu.unifesspa.mc.logicgame.game.model;

import br.edu.unifesspa.mc.logicgame.framework.image.Animation;

public abstract class Enemy extends Ship {

    public Enemy(Animation anim) {
        super(anim);
    }

    @Override
    public void update() {
        if (this.getLife() == 0) {
            setState(STATE_DEAD);
        }
        this.setY(this.getY() + this.getVelocityY());
    }

    public static class And extends Enemy {

        public And(Animation anim) {
            super(anim);
        }

    }

    public static class Or extends Enemy {

        public Or(Animation anim) {
            super(anim);
        }

    }

    public static class Not extends Enemy {

        public Not(Animation anim) {
            super(anim);
            this.setLife(1);
        }

    }

}
