package br.edu.unifesspa.mc.logicgame.game.state.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import br.edu.unifesspa.mc.logicgame.framework.core.NullRepaintManager;
import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;
import br.edu.unifesspa.mc.logicgame.framework.image.Animation;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItemList;
import br.edu.unifesspa.mc.logicgame.framework.state.GameState;
import br.edu.unifesspa.mc.logicgame.framework.state.GameStateManager;
import br.edu.unifesspa.mc.logicgame.game.GameSettings;
import br.edu.unifesspa.mc.logicgame.game.model.Sprite;

public class MenuGameState implements ActionListener, GameState {

    private JButton play;
    private JButton quit;

    // parallax scrolling
    private Sprite background;
    private int scrollingOffset;

    // game name image
    private ImageItem gameName;

    // game controls image
    private ImageItem gameControls;

    // description image
    private ImageItem description;

    private String stateChange;
    private GameState nextState;

    public MenuGameState(GameState nextState) {
        this.nextState = nextState;
        stateChange = null;
        NullRepaintManager.install();
        scrollingOffset = 0;
        background = new Sprite(new Animation(0, new ImageItemList<ImageItem>(GameSettings.getInstance().createImage("menu_scrolling.jpg")), 0));
        gameName = GameSettings.getInstance().createImage("game_name.png");
        description = GameSettings.getInstance().createImage("description.png");
        gameControls = GameSettings.getInstance().createImage("control.png");
    }

    @Override
    public void setup() {
        play = createButton("btn_play", "Play");
        quit = createButton("btn_exit", "Quit");

        JFrame frame = ScreenManager.getInstance().getFullScreenWindow();

        GroupLayout layout = new GroupLayout(frame.getContentPane());

        frame.getContentPane().setLayout(layout);

        if (frame.getContentPane() instanceof JComponent) {
            ((JComponent) frame.getContentPane()).setOpaque(false);
        }

        JPanel mainPane = new JPanel();

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup().addGap(frame.getWidth() / 4, frame.getWidth() / 4, frame.getWidth())
                        .addComponent(mainPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(200, Short.MAX_VALUE)));

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGap(frame.getHeight() / 3, frame.getHeight() / 3, frame.getHeight() / 3)
                        .addComponent(mainPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(200, Short.MAX_VALUE)));

        mainPane.setLayout(new GridLayout(2, 1, 5, 5));

        mainPane.add(play);
        mainPane.add(quit);

        mainPane.setBorder(BorderFactory.createLineBorder(Color.RED, 8));

        if (mainPane instanceof JComponent) {
            ((JComponent) mainPane).setOpaque(false);
        }

        frame.validate();
    }

    private JButton createButton(String name, String toolTip) {
        ImageItem item = GameSettings.getInstance().createImage(name + ".png");

        ImageIcon iconRollover = new ImageIcon(item.getImage());

        int w = iconRollover.getIconWidth();
        int h = iconRollover.getIconHeight();

        Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

        BufferedImage image = ScreenManager.getInstance().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        Graphics2D g = (Graphics2D) image.getGraphics();
        Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f);
        g.setComposite(alpha);
        g.drawImage(iconRollover.getImage(), 0, 0, null);
        g.dispose();

        ImageIcon iconDefault = new ImageIcon(image);

        image = ScreenManager.getInstance().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        g = (Graphics2D) image.getGraphics();
        g.drawImage(iconRollover.getImage(), 2, 2, null);
        g.dispose();

        ImageIcon iconPressed = new ImageIcon(image);

        JButton button = new JButton();
        button.addActionListener(this);
        button.setIgnoreRepaint(true);
        button.setFocusable(false);
        button.setToolTipText(toolTip);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(cursor);
        button.setIcon(iconDefault);
        button.setRolloverIcon(iconRollover);
        button.setPressedIcon(iconPressed);

        return button;
    }

    @Override
    public void processLogics() {
        scrollingOffset--;
        if (scrollingOffset < -background.getWidth()) {
            scrollingOffset = 0;
        }
    }

    @Override
    public void renderGraphics(Graphics2D g2d) {
        background.draw(g2d, scrollingOffset, 0);
        background.draw(g2d, scrollingOffset + background.getWidth(), 0);

        gameName.draw(g2d, (ScreenManager.getInstance().getWidth() - gameName.getWidth()) / 2, gameName.getHeight() / 2);

        description.draw(g2d, (ScreenManager.getInstance().getWidth() - description.getWidth()) / 2, ScreenManager.getInstance().getHeight() - description.getHeight());

        gameControls.draw(g2d, (ScreenManager.getInstance().getWidth() - gameControls.getWidth()) / 2, ScreenManager.getInstance().getHeight() - (gameControls.getHeight() * 4));

        JFrame frame = ScreenManager.getInstance().getFullScreenWindow();
        frame.getLayeredPane().paintComponents(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(play)) {
            stateChange = nextState.getName();
        }
        if (evt.getSource().equals(quit)) {
            stateChange = GameStateManager.EXIT_GAME;
        }
    }

    @Override
    public void tearDown() {

    }

    @Override
    public String getName() {
        return "_MenuState";
    }

    @Override
    public String checkForStateChange() {
        return stateChange;
    }

}
