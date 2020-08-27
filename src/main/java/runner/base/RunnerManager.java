package runner.base;

import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector3f;

public class RunnerManager {
    //to handle their viewports

    public static void cheapInit(Application app, int count) {
        var cam = app.getCamera();
        cam.setLocation(new Vector3f(0, -5, 8));
        cam.lookAt(new Vector3f(0, 4, 0), Vector3f.UNIT_Y);

        for (int i = 0; i < count; i++) {
            Runner r = new Runner(new Vector3f(), KeyInput.KEY_J, KeyInput.KEY_K);
            app.getStateManager().attach(r);
        }
    }
}
