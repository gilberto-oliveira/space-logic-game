package br.edu.unifesspa.mc.logicgame.game.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;
import br.edu.unifesspa.mc.logicgame.framework.image.Animation;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItemList;
import br.edu.unifesspa.mc.logicgame.framework.input.GameAction;
import br.edu.unifesspa.mc.logicgame.framework.input.KeyManager;
import br.edu.unifesspa.mc.logicgame.framework.sound.MidiPlayer;
import br.edu.unifesspa.mc.logicgame.framework.state.GameState;
import br.edu.unifesspa.mc.logicgame.framework.state.GameStateManager;
import br.edu.unifesspa.mc.logicgame.game.GameSettings;
import br.edu.unifesspa.mc.logicgame.game.model.Blown;
import br.edu.unifesspa.mc.logicgame.game.model.Enemy;
import br.edu.unifesspa.mc.logicgame.game.model.Player;
import br.edu.unifesspa.mc.logicgame.game.model.Shot;
import br.edu.unifesspa.mc.logicgame.game.model.Sprite;

public class FirstGameState implements GameState {

    private String stateChange;
    private GameState nextState;

    private Player player;
    private int score;

    private ArrayList<Enemy> enemies;
    private ArrayList<Blown> blowns;
    private ArrayList<Shot> outputs;

    private KeyManager keyManager;
    private GameAction moveUp;
    private GameAction moveDown;
    private GameAction moveLeft;
    private GameAction moveRight;
    private GameAction shooter;
    private GameAction exit;

    // parallax scrolling
    private Sprite background;
    private int scrollingOffset;

    // generated positions
    private Random random;

    private MidiPlayer midiPlayer;

    private int amountShotsOne;
    private int amountShotsZero;
    private long outputTime;
    private long currentTime;
    private long startTime;

    public FirstGameState(GameState nextState) {
        this.nextState = nextState;
    }

    @Override
    public String getName() {
        return "_FirstState";
    }

    @Override
    public String checkForStateChange() {
        return stateChange;
    }

    @Override
    public void setup() {
        keyManager = new KeyManager(ScreenManager.getInstance().getFullScreenWindow());

        moveUp = new GameAction("moveUp");
        moveDown = new GameAction("moveDown");
        moveLeft = new GameAction("rotateLeft");
        moveRight = new GameAction("rotateRight");
        shooter = new GameAction("shooter", true);
        exit = new GameAction("exit", true);

        keyManager.map(KeyEvent.VK_UP, moveUp);
        keyManager.map(KeyEvent.VK_DOWN, moveDown);
        keyManager.map(KeyEvent.VK_LEFT, moveLeft);
        keyManager.map(KeyEvent.VK_RIGHT, moveRight);
        keyManager.map(KeyEvent.VK_F, shooter);
        keyManager.map(KeyEvent.VK_ESCAPE, exit);

        blowns = new ArrayList<>();
        outputs = new ArrayList<>();
        random = new Random();

        amountShotsOne = 0;
        amountShotsZero = 0;

        outputTime = 0;
        startTime = System.currentTimeMillis();
        currentTime = startTime;

        midiPlayer = new MidiPlayer();

        setupBackground();
        setupPlayer();
        setupEnemies();

        midiPlayer.play(GameSettings.BACK_SOUND);

        score = 0;
    }

    private void setupBackground() {
        scrollingOffset = 0;
        background = new Sprite(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("back.jpg")), 0));
    }

    private void setupPlayer() {
        player = new Player(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_player.png")), 0));
        player.setX((ScreenManager.getInstance().getWidth() - player.getWidth()) / 2);
        player.setY(ScreenManager.getInstance().getHeight() - player.getHeight());
    }

    private void setupEnemies() {
        enemies = new ArrayList<>();

        Enemy.And and = new Enemy.And(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_and.png")), 0));
        and.setX((ScreenManager.getInstance().getWidth() - and.getWidth()) / 6);
        and.setY(0);

        Enemy.Or or = new Enemy.Or(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_or.png")), 0));
        or.setX((ScreenManager.getInstance().getWidth() - or.getWidth()) / 2);
        or.setY(0);

        Enemy.Not not = new Enemy.Not(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_not.png")), 0));
        not.setX((ScreenManager.getInstance().getWidth() - not.getWidth()) / 1.2);
        not.setY(0);

        enemies.add(and);
        enemies.add(or);
        enemies.add(not);
    }

    @Override
    public void processLogics() {
        checkInput();
        updatePlayer();
        updateShots();
        checkCollision();
        updateEnemies();
        updateOutputs();
        updateBlowns();
        updateBackground();
    }

    private void updateOutputs() {
        // calculate the elapsed time
        long elapsedTime = System.currentTimeMillis() - currentTime;
        currentTime += elapsedTime;

        if (!outputs.isEmpty()) {
            for (int i = 0; i < outputs.size(); i++) {
                outputs.get(i).update();
                outputTime += elapsedTime;
                if (outputTime > 1000) { // output time (1 second)
                    outputs.remove(i);
                    outputTime = 0;
                }
            }
        }
    }

    private void updateBlowns() {
        if (!blowns.isEmpty()) {
            for (int i = 0; i < blowns.size(); i++) {
                blowns.get(i).update();

                if (blowns.get(i).isEndFrame()) {
                    blowns.remove(i);
                }
            }
        }
    }

    private void createBlown(double x, double y) {
        ImageItemList<ImageItem> blownImages = new ImageItemList<>(GameSettings.getInstance().createImage("blown/blown1.png"));

        for (int i = 2; i < 13; i++) {
            blownImages.add(GameSettings.getInstance().createImage("blown/blown" + i + ".png"));
        }

        Animation blownAnim = new Animation(GameSettings.getInstance().getUps(), blownImages, .025d);
        blownAnim.setRunning(true);

        Blown blown = new Blown(blownAnim);

        blown.setX(x);
        blown.setY(y);

        blowns.add(blown);

        GameSettings.getInstance().play(GameSettings.BLOWN_SOUND);
    }

    private void createOutput(double x, double y, int outputType) {
        Shot tmpShot = null;

        if (outputType == 1) {
            tmpShot = new Shot.One(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_shot1.png")), 0));
        } else if (outputType == 0) {
            tmpShot = new Shot.Zero(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_shot0.png")), 0));
        }

        tmpShot.setVelocityY(1d);
        tmpShot.setX(x);
        tmpShot.setY(y);

        outputs.add(tmpShot);
    }

    private int updateEnemyLogic(Enemy ship) {
        if ((amountShotsOne != 0) || (amountShotsZero != 0)) {
            if (ship instanceof Enemy.Or) {
                return (amountShotsZero == 2) ? 0 : 1;
            }
            if (ship instanceof Enemy.And) {
                return (amountShotsOne == 2) ? 1 : 0;
            }
            if (ship instanceof Enemy.Not) {
                return (amountShotsOne == 1) ? 0 : 1;
            }
        }
        return 0;
    }

    private void updateBackground() {
        background.update();
        scrollingOffset++;
        if (scrollingOffset > background.getHeight()) {
            scrollingOffset = 0;
        }
    }

    private void updateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.update();

            enemy.setVelocityY((random.nextDouble() * 5) + 1);

            if (enemy.isDead()) {
                createBlown(enemy.getX(), enemy.getY());
                createOutput(enemy.getX(), enemy.getY(), updateEnemyLogic(enemy));
                amountShotsOne = amountShotsZero = 0;
                awakeEnemy(enemy);
                score += 10;
            }

            if (enemy.getY() >= ScreenManager.getInstance().getHeight()) {
                awakeEnemy(enemy);
            }
        }
    }

    private void updatePlayer() {
        player.update();
        if (player.isDead()) {
            stateChange = nextState.getName();
        }
        checkScreenBounds();
    }

    private void updateShots() {
        ArrayList<Shot> shots = player.getShots();

        if (!shots.isEmpty()) {
            for (int i = 0; i < shots.size(); i++) {
                shots.get(i).update();
                if (shots.get(i).getY() <= 0) {
                    shots.remove(i);
                }
            }
        }
    }

    private void checkInput() {
        double velocityY = 0.0d, velocityX = 0.0d;

        if (moveUp.isPressed()) {
            velocityY -= player.getMaxSpeed();
        }

        if (moveDown.isPressed()) {
            velocityY += player.getMaxSpeed();
        }

        if (moveLeft.isPressed()) {
            velocityX -= player.getMaxSpeed();
        }

        if (moveRight.isPressed()) {
            velocityX += player.getMaxSpeed();
        }

        player.setVelocityY(velocityY);
        player.setVelocityX(velocityX);

        if (shooter.isPressed()) {
            toShoot();
        }

        if (exit.isPressed()) {
            stateChange = GameStateManager.EXIT_GAME;
        }
    }

    private void checkScreenBounds() {
        if (player.getX() <= 0) {
            player.setX(0);
        }
        if (player.getY() <= 0) {
            player.setY(0);
        }

        if (player.getX() >= ScreenManager.getInstance().getWidth() - player.getWidth()) {
            player.setX(ScreenManager.getInstance().getWidth() - player.getWidth());
        }
        if (player.getY() >= ScreenManager.getInstance().getHeight() - player.getHeight()) {
            player.setY(ScreenManager.getInstance().getHeight() - player.getHeight());
        }
    }

    private void checkCollision() {
        ArrayList<Shot> shots = player.getShots();

        for (int i = 0; i < enemies.size(); i++) {
            if (player.getBoundaries().intersects(enemies.get(i).getBoundaries())) {
                player.setLife(player.getLife() - 1);
                createBlown(enemies.get(i).getX(), enemies.get(i).getY());
                awakeEnemy(enemies.get(i));
            }

            if (!shots.isEmpty()) {
                for (int j = 0; j < shots.size(); j++) {
                    if (enemies.get(i).getBoundaries().intersects(shots.get(j).getBoundaries())) {
                        if (shots.get(j) instanceof Shot.One) {
                            amountShotsOne++;
                        }
                        if (shots.get(j) instanceof Shot.Zero) {
                            amountShotsZero++;
                        }
                        shots.remove(j);
                        enemies.get(i).setLife(enemies.get(i).getLife() - 1);
                    }
                }
            }
        }
    }

    private void awakeEnemy(Enemy enemy) {
        enemy.setX(random.nextInt(ScreenManager.getInstance().getWidth() - enemy.getWidth()));

        if (enemy.getX() < enemy.getWidth()) {
            enemy.setX(enemy.getWidth());
        }

        enemy.setY(-(random.nextInt(enemy.getHeight())));

        if (enemy instanceof Enemy.Not) {
            enemy.setLife(Enemy.DEFAULT_LIFE - 1);
        } else {
            enemy.setLife(Enemy.DEFAULT_LIFE);
        }

        if (enemy.getState() == Enemy.STATE_DEAD) {
            enemy.setState(Enemy.STATE_NORMAL);
        }
    }

    private void toShoot() {
        Shot shot = null;
        int rand = random.nextInt(10) + 1;

        if ((rand % 2) == 0) {
            shot = new Shot.One(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_shot1.png")), 0));
        } else {
            shot = new Shot.Zero(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("sprite_shot0.png")), 0));
        }

        shot.setX(player.getX() + (shot.getWidth() + (shot.getWidth() / 2)));
        shot.setY(player.getY());

        player.addShot(shot);

        GameSettings.getInstance().play(GameSettings.SHOT_SOUND);
    }

    @Override
    public void renderGraphics(Graphics2D g2d) {
        // draw scrolling background
        background.draw(g2d, 0, scrollingOffset);
        background.draw(g2d, 0, scrollingOffset - background.getHeight());

        drawShots(g2d);
        drawBlowns(g2d);

        // draw all enemies
        drawEnemies(g2d);

        // draw player
        player.draw(g2d);

        drawOutputs(g2d);

        drawHUD(g2d);
    }

    private void drawEnemies(Graphics2D g2d) {
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }
    }

    private void drawHUD(Graphics2D g2d) {
        Graphics2D g = (Graphics2D) g2d.create();
        // style of life bar
        final float DASH[] = {1000.0f};
        final BasicStroke DASHED = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, DASH, 0.0f);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setStroke(DASHED); // set the border size�s rectangle

        // draw life bar
        g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        g.fillRect(20, 20, 50 * player.getLife(), 30);
        g.setColor(Color.RED);
        g.drawRect(20, 20, 200, 30);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 50));
        g.drawString(String.format("%06d", score), ScreenManager.getInstance().getWidth() - 180, 50);

        g.dispose();

    }

    private void drawShots(Graphics2D g2d) {
        ArrayList<Shot> shots = player.getShots();

        if (!shots.isEmpty()) {
            for (Shot shot : shots) {
                shot.draw(g2d, (int) shot.getX(), (int) shot.getY());
            }
        }
    }

    private void drawBlowns(Graphics2D g2d) {
        if (!blowns.isEmpty()) {
            for (Blown blown : blowns) {
                blown.draw(g2d, (int) blown.getX(), (int) blown.getY());
            }
        }
    }

    private void drawOutputs(Graphics2D g2d) {
        if (!outputs.isEmpty()) {
            for (Shot out : outputs) {
                out.draw(g2d, (int) out.getX(), (int) out.getY());
            }
        }
    }

    @Override
    public void tearDown() {
        keyManager.resetAllGameActions();
        keyManager.setComponent(null);
        midiPlayer.stop();
        midiPlayer.close();
        midiPlayer = null;
    }

}
