package br.edu.unifesspa.mc.logicgame.game;

import java.awt.DisplayMode;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;

import br.edu.unifesspa.mc.logicgame.framework.core.ScreenManager;
import br.edu.unifesspa.mc.logicgame.framework.image.ImageItem;
import br.edu.unifesspa.mc.logicgame.framework.sound.PlayingStreamed;
import br.edu.unifesspa.mc.logicgame.framework.sound.Sound;
import br.edu.unifesspa.mc.logicgame.framework.sound.SoundManager;
import br.edu.unifesspa.mc.logicgame.framework.sound.Streamed;

public class GameSettings {

    public static final String VERSION = "0.0.1 Beta";
    public static final String NAME = "Space Logic";

    public static final DisplayMode DISPLAY_MODES[] = {
        new DisplayMode(1920, 1080, 16, 60),
         new DisplayMode(1920, 1080, 24, 60),
         new DisplayMode(1920, 1080, 32, 60),
         new DisplayMode(1920, 1080, 32, 60),
		
         new DisplayMode(1366, 768, 16, 0),
         new DisplayMode(1366, 768, 24, 0),
         new DisplayMode(1366, 768, 32, 0),
		
         new DisplayMode(1280, 720, 16, 0),
         new DisplayMode(1280, 720, 24, 0),
         new DisplayMode(1280, 720, 32, 0),
        new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(800, 600, 32, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(640, 480, 32, 0)
    };

    private static GameSettings instance;

    private DisplayMode displayMode;
    private int ups = 60;
    private SoundManager soundManager;

    public static final Sound SHOT_SOUND = createSound("swav_shoot");
    public static final Sound BLOWN_SOUND = createSound("swav_blown");
    public static final Sequence BACK_SOUND = createSequence("smidi_back");

    private static Sound createSound(String name) {
        try {
            return new Sound(GameSettings.class.getResource("/assets/sounds/" + name + ".wav"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Sequence createSequence(String name) {
        try {
            return MidiSystem.getSequence(GameSettings.class.getResource("/assets/sounds/" + name + ".midi"));
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ImageItem createImage(String name) {
        return new ImageItem("/assets/images/" + name);
    }

    public final double perUpdate(int valueInASecond) {
        return (double) valueInASecond / ups;
    }

    public final double perUpdate(double valueInASecond) {
        return valueInASecond / ups;
    }

    private GameSettings() {
        displayMode = ScreenManager.getInstance().findFirstCompatibleMode(DISPLAY_MODES);

        System.out.println("Using " + displayMode.getWidth() + " x " + displayMode.getHeight() + " screen resolution ");
        System.out.println("with " + displayMode.getBitDepth() + " bits in bit depth ");

        if (displayMode.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN) {
            ups = displayMode.getRefreshRate();
            System.out.println("and " + displayMode.getRefreshRate() + " refresh rate.");
        }
        System.out.println();
        System.out.println("Updates per second rate: " + ups);

        try {
            soundManager = new SoundManager(SHOT_SOUND.getFormat());
            System.out.println();
            System.out.println("Sounds: " + soundManager.getFormat());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public int getUps() {
        return ups;
    }

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }

    public AudioFormat getSoundFormat() {
        return soundManager.getFormat();
    }

    public PlayingStreamed play(Streamed streamed, boolean loop) {
        return soundManager.play(streamed, loop);
    }

    public PlayingStreamed play(Streamed streamed) {
        return soundManager.play(streamed);
    }

}
