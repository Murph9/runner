package runner.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

public class NumControlScheme implements IControlScheme {
    
    private final Map<Integer, IControlSchemeListener> listeners = new HashMap<>();
    private int currentNum;

    // https://wiki.jmonkeyengine.org/docs/3.3/core/input/combo_moves.html

    public NumControlScheme() {
        currentNum = 0;
    }

    @Override
    public void init(InputManager im) {
        im.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        im.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        im.addListener(this, "Left", "Right");
    }

    @Override
    public void remove(InputManager im) {
        im.removeListener(this);
        im.deleteMapping("Left");
        im.deleteMapping("Right");
    }

    public int maxCount() {
        return KEY_NAMES.size();
    }

    private KeyMap getForNum(int runnerNum) {
        return KEY_NAMES.get(runnerNum);
    }
    public String getControlsFor(int runnerNum) {
        return getForNum(runnerNum).text;
    }
    
    public static final List<KeyMap> KEY_NAMES = new LinkedList<>();
    static {
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_1, "1"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_2, "2"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_3, "3"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_4, "4"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_5, "5"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_6, "6"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_7, "7"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_8, "8"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_9, "9"));
        KEY_NAMES.add(new KeyMap(KeyInput.KEY_0, "0"));
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed)
            return;

        if (name.equals("Left"))
            listeners.get(this.currentNum).doMove(MoveDir.Left);
        else if (name.equals("Right"))
            listeners.get(this.currentNum).doMove(MoveDir.Right);
        else {
            try {
                int a = Integer.parseInt(name);
                this.currentNum = a;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void addControl(InputManager im, int runnerNum, IControlSchemeListener controlListener) {
        listeners.put(runnerNum, controlListener);

        im.addMapping(runnerNum + "", new KeyTrigger(getForNum(runnerNum).key));
        im.addListener(this, runnerNum + "");
    }

    @Override
    public void removeControl(InputManager im, int runnerNum) {
        listeners.remove(runnerNum);
        im.deleteMapping(runnerNum + "");
    }
}
