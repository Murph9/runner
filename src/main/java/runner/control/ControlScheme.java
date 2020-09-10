package runner.control;

import java.util.LinkedList;
import java.util.List;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

public class ControlScheme implements IControlScheme {
    
    public int maxCount() {
        return KEY_NAMES.size() / 2;
    }

    public void listenFor(InputManager im, int runnerNum, PlayerControl control) {
        var keys = getForNum(runnerNum);
        im.addMapping(runnerNum + "_Left", new KeyTrigger(keys[0].key));
        im.addMapping(runnerNum + "_Right", new KeyTrigger(keys[1].key));
        im.addListener(control, runnerNum + "_Left", runnerNum + "_Right");
    }
    
    public void deListenFor(InputManager im, int runnerNum, PlayerControl control) {
        im.deleteMapping(runnerNum + "_Left");
        im.deleteMapping(runnerNum + "_Right");
        im.removeListener(control);
    }
    private KeyMap[] getForNum(int runnerNum) {
        return new KeyMap[] {KEY_NAMES.get(runnerNum * 2), KEY_NAMES.get(runnerNum * 2 + 1)};
    }
    public String getControlsFor(int runnerNum) {
        var a = getForNum(runnerNum);
        return a[0].text + "   |   " + a[1].text;
    }
    
    public MoveDir onAction(int runnerNum, String name, boolean keyPressed, float tpf) {
        if (name.equals(runnerNum+"_Left") && keyPressed)
            return MoveDir.Left;
        if (name.equals(runnerNum + "_Right") && keyPressed)
            return MoveDir.Right;

        return MoveDir.None;
    }


    // to handle their viewports
    public static final List<KeyMap> KEY_NAMES = new LinkedList<>();
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