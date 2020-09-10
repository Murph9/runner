package runner.control;

import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;

public interface IControlScheme extends ActionListener {
    void init(InputManager im);
    void remove(InputManager im);

    void addControl(InputManager im, int num, IControlSchemeListener controlListener);
    void removeControl(InputManager im, int num);

    int maxCount();
    String getControlsFor(int runnerNum);
}