package runner.base;

import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;

public class RunnerManager {
    //to handle their viewports

    private static final int[] keyMappings = new int[] {
            KeyInput.KEY_J, KeyInput.KEY_K,
            KeyInput.KEY_LEFT, KeyInput.KEY_RIGHT,
            KeyInput.KEY_A, KeyInput.KEY_D,
            KeyInput.KEY_1, KeyInput.KEY_2,
    };
    private final int count;

    public RunnerManager(int count) {
        this.count = count;
    }

    public void init(Application app) {
        var cam = app.getCamera();
        cam.setLocation(new Vector3f(0, -5, 8));
        cam.lookAt(new Vector3f(0, 4, 0), Vector3f.UNIT_Y);

        for (int i = 0; i < count; i++) {
            Runner r = new Runner(new Vector3f(), keyMappings[i * 2], keyMappings[i * 2 + 1]);
            app.getStateManager().attach(r);
        }
    }
}

// TODO eventually handle the 12345 + arrows key layout (for jumping and stuff)
// https://wiki.jmonkeyengine.org/docs/3.3/core/input/combo_moves.html