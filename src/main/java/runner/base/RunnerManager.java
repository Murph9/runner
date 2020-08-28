package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;

public class RunnerManager {
    //to handle their viewports
    private static final List<KeyMap> KEY_NAMES = new LinkedList<>();
    static {
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_A, "A"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_D, "D"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_J, "J"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_K, "K"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_LEFT, "<-"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_RIGHT, "->"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_1, "1"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_2, "2"));
    }

    private final int count;
    private List<Runner> runners;

    public RunnerManager(int count) {
        this.count = count;
        runners = new LinkedList<>();
    }

    public void init(Application app) {
        RunnerUi ui = new RunnerUi(this);
        app.getStateManager().attach(ui);

        var cam = app.getCamera();
        cam.setLocation(new Vector3f(0, -5, 8));
        cam.lookAt(new Vector3f(0, 4, 0), Vector3f.UNIT_Y);

        for (int i = 0; i < count; i++) {
            Runner r = new Runner(this, new Vector3f(i*3, 0, 0), KEY_NAMES.get(i * 2).key, KEY_NAMES.get(i * 2 + 1).key);
            runners.add(r);
            app.getStateManager().attach(r);

            ui.addKeyCombo(KEY_NAMES.get(i * 2).text, KEY_NAMES.get(i * 2 + 1).text);
        }
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
    }

	public void start() {
        for (Runner r : runners) {
            r.setEnabled(true);
        }
	}
}

// TODO box generation, probably just comes from a list of patterns
// everything eventually gets faster

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