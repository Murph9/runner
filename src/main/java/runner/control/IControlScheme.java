package runner.control;

import com.jme3.input.InputManager;

public interface IControlScheme {
    int maxCount();

    void listenFor(InputManager im, int runnerNum, PlayerControl control);
    void deListenFor(InputManager im, int runnerNum, PlayerControl control);
    
    MoveDir onAction(int runnerNum, String name, boolean keyPressed, float tpf);
    String getControlsFor(int runnerNum);
}