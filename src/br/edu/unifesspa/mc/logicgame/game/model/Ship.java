package br.edu.unifesspa.mc.logicgame.game.model;

import java.util.ArrayList;

import br.edu.unifesspa.mc.logicgame.framework.image.Animation;

public class Ship extends Sprite {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_DEAD = 1;
    public static final int DEFAULT_LIFE = 2;

    private int state;
    private int life;

    private ArrayList<Shot> shots;

    public Ship(Animation anim) {
        super(anim);
        this.state = STATE_NORMAL;
        this.life = DEFAULT_LIFE;
        shots = new ArrayList<>();
    }

    @Override
    public void update() {
        super.update();
        if (life < 0) {
            state = STATE_DEAD;
        }
    }

    public boolean isDead() {
        return (state == STATE_DEAD);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void addShot(Shot shot) {
        shots.add(shot);
    }

    public void removeShot(int index) {
        shots.remove(index);
    }

    public ArrayList<Shot> getShots() {
        return shots;
    }

}
