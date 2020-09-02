package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;

public class RunnerManager {
    //to handle their viewports
    private static final List<KeyMap> KEY_NAMES = new LinkedList<>();
    static {
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_A, "A"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_D, "D"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_LEFT, "<-"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_RIGHT, "->"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_J, "J"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_K, "K"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_1, "1"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_2, "2"));
    }

    private static final float SPEED_MULT = 1.75f;
    private final int count;
    private RunnerUi ui;
    private List<Runner> runners;

    protected static float speedByDistance(float dist) {
        if (dist < 0)
            return SPEED_MULT;
        if (dist < 100)
            return SPEED_MULT + (dist) / 50 / SPEED_MULT;
        if (dist < 200)
            return 3 * SPEED_MULT + (dist - 100) / 100 / SPEED_MULT;
        return 4 * SPEED_MULT + (dist - 200) / 200 / SPEED_MULT;
    }

    public RunnerManager(int count) {
        this.count = Math.max(1, count);
        runners = new LinkedList<>();
    }

    private Vector3f calcOffsetFor(int i) {
        return new Vector3f(i*10f, 0, 0); //just far enough that they can't see each other
    }

    private void reset(Application app) {
        ui = new RunnerUi(this);
        app.getStateManager().attach(ui);

        for (int i = 0; i < count; i++) {
            Vector3f offset = calcOffsetFor(i);

            Runner r = new Runner(this, offset, KEY_NAMES.get(i * 2).key, KEY_NAMES.get(i * 2 + 1).key);
            runners.add(r);
            app.getStateManager().attach(r);

            ui.addKeyCombo(KEY_NAMES.get(i * 2).text, KEY_NAMES.get(i * 2 + 1).text);
        }
    }

    public void initOnce(Application app) {
        // setup viewports

        // do 1 first, as its the default one
        var offset = calcOffsetFor(0);
        var cam = app.getCamera();
        cam.setLocation(offset.add(0, -5, 8));
        cam.lookAt(offset.add(0, 4, 0), Vector3f.UNIT_Y);

        //then if there is more add more views
        if (count != 1) {
            cam.setViewPort(0, 1/(float)count, 0, 1);
            for (int i = 1; i < count; i++) {
                offset = calcOffsetFor(i);

                cam = cam.clone();
                cam.setLocation(offset.add(0, -5, 8));
                cam.lookAt(offset.add(0, 4, 0), Vector3f.UNIT_Y);
                cam.setViewPort(1 * i / (float) count, 1*(i+1) / (float) count, 0, 1);
                ViewPort view_n = app.getRenderManager().createMainView("View of camera "+i, cam);
                view_n.attachScene(((SimpleApplication)app).getRootNode());
                view_n.setBackgroundColor(ColorRGBA.Black);
                view_n.setClearFlags(true, true, true);
            }
        }

        //init actual game
        reset(app);
    }

    public float getDistance() {
        float distance = 0;
        for (Runner r: runners) {
            distance = Math.max(distance, r.getDistance());
        }
        return distance;
    }

    public void gameOver() {
        for (Runner r: runners) {
            r.setEnabled(false);
        }

        ui.gameOver(getDistance());
    }

	public void start() {
        for (Runner r : runners) {
            r.setEnabled(true);
        }
    }
    
    public void restart(Application app) {
        app.getStateManager().detach(ui);
        ui = null;

        for (Runner r : runners) {
            app.getStateManager().detach(r);
        }
        runners.clear();

        reset(app);
    }
}

// TODO eventually handle the 12345 + arrows key layout (for jumping and stuff)
// https://wiki.jmonkeyengine.org/docs/3.3/core/input/combo_moves.html

class KeyMap {
    public final int key;
    public final String text;
    public KeyMap(int key, String text) {
        this.key = key;
        this.text = text;
    }
}