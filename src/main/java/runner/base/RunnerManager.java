package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.post.filters.FogFilter;

import runner.saving.RecordManager;

public class RunnerManager {
    //to handle their viewports
    private static final List<KeyMap> KEY_NAMES = new LinkedList<>();
    static {
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_A, "A"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_D, "D"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_LEFT, "<-"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_RIGHT, "->"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_J, "J"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_L, "L"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_V, "V"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_N, "N"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_F, "F"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_H, "H"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_1, "1"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_3, "3"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_5, "5"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_7, "7"));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_0, "0"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_EQUALS, "="));

        KEY_NAMES.add(new KeyMap(KeyInput.KEY_NUMPAD4, "Num4"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_NUMPAD6, "Num6"));
    }

    private static final float SPEED_MULT = 1.75f;
    private final int count;
    private RunnerUi ui;
    private List<Runner> runners;

    protected static float speedByDistance(float dist) {
        return SPEED_MULT * _speedByDistance(dist);
    }
    private static float _speedByDistance(float dist) {
        if (dist < 0)
            return 1;
        if (dist < 100)
            return 1 + (dist) / 50;
        if (dist < 200)
            return 3 + (dist - 100) / 100;
        return 4 + (dist - 200) / 200;
    }

    public RunnerManager(int count) {
        this.count = (int)FastMath.clamp(count, 1, KEY_NAMES.size()/2); //can't make more than your key combos
        runners = new LinkedList<>();
    }

    private Vector3f calcOffsetFor(int i) {
        return new Vector3f(i*10f, 0, 0); //just far enough that they can't see each other
    }

    private void reset(Application app) {
        var highScore = RecordManager.getRecord(this.count);

        ui = new RunnerUi(this, highScore);
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
        var fpp = new FilterPostProcessor(app.getAssetManager());
        FogFilter fog = new FogFilter(new ColorRGBA(0f, 0f, 0f, .5f), 2f, 80f);
        fpp.addFilter(fog);
        app.getViewPort().addProcessor(fpp);

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

                fpp = new FilterPostProcessor(app.getAssetManager());
                fpp.addFilter(fog);
                view_n.addProcessor(fpp);
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

        float score = getDistance();
        RecordManager.saveRecord(this.count, score);
        ui.gameOver(score);
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