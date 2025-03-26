package br.edu.unifesspa.mc.logicgame.game.model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import br.edu.unifesspa.mc.logicgame.framework.image.Animation;
import br.edu.unifesspa.mc.logicgame.framework.math.Vector2D;

public class Sprite {

    protected Animation anim;
    private Vector2D position = new Vector2D();
    private Vector2D velocity = new Vector2D();

    public Sprite(Animation anim) {
        this.anim = anim;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setX(double x) {
        position.setX(x);
    }

    public void setY(double y) {
        position.setY(y);
    }

    public int getWidth() {
        return anim.getImage().getWidth();
    }

    public int getHeight() {
        return anim.getImage().getHeight();
    }

    public double getVelocityX() {
        return velocity.getX();
    }

    public double getVelocityY() {
        return velocity.getY();
    }

    public void setVelocityX(double vx) {
        velocity.setX(vx);
    }

    public void setVelocityY(double vy) {
        velocity.setY(vy);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public double getMaxSpeed() {
        return 5.0D;
    }

    public BufferedImage getImage() {
        return anim.getImage();
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(anim.getImage(), (int) getX(), (int) getY(), getWidth(), getHeight(), null);
    }

    public void draw(Graphics2D g2d, int x, int y) {
        g2d.drawImage(anim.getImage(), x, y, getWidth(), getHeight(), null);
    }

    public void update() {
        position.setX(position.getX() + velocity.getX());
        position.setY(position.getY() + velocity.getY());
        anim.update();
    }

    public Rectangle2D getBoundaries() {
        return new Rectangle((int) getX(), (int) getY(), getWidth(), getHeight());
    }

}
