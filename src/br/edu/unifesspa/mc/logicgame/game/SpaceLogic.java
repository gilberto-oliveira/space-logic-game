package br.edu.unifesspa.mc.logicgame.game;

public class SpaceLogic {

    public SpaceLogic(boolean profile) {
        super();
        new GameCore(profile);
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            new SpaceLogic(args[0].equals("-profile"));
        } else {
            new SpaceLogic(false);
        }
    }

}
