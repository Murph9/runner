package runner.control;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;

public interface IControlScheme extends ActionListener {
    void init(InputManager app);
    void addControl(InputManager app, int num, IControlSchemeListener controlListener);
    void removeControl(InputManager app, int num);

    int maxCount();
    String getControlsFor(int runnerNum);
}