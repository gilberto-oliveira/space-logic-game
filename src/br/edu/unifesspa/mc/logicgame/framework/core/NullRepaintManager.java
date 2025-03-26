package br.edu.unifesspa.mc.logicgame.framework.core;

import javax.swing.RepaintManager;
import javax.swing.JComponent;

public class NullRepaintManager extends RepaintManager {

    public static void install() {
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }

    @Override
    public void addInvalidComponent(JComponent c) {
    }

    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
    }

    @Override
    public void markCompletelyDirty(JComponent c) {
    }

    @Override
    public void paintDirtyRegions() {
    }

}
