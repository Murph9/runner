package runner.control;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;

public class PairControlScheme implements IControlScheme {
    
    private final Map<Integer, IControlSchemeListener> listeners = new HashMap<>();

    @Override
    public void init(InputManager im) {
        im.addListener(this);
    }
    @Override
    public void remove(InputManager im) {
        im.removeListener(this);
    }

    @Override
    public void addControl(InputManager im, int runnerNum, IControlSchemeListener controlListener) {
        listeners.put(runnerNum, controlListener);

        var keys = getForNum(runnerNum);
        im.addMapping(runnerNum + "_Left", new KeyTrigger(keys[0].key));
        im.addMapping(runnerNum + "_Right", new KeyTrigger(keys[1].key));
        im.addListener(this, runnerNum + "_Left", runnerNum + "_Right");
    }

    @Override
    public int maxCount() {
        return KEY_NAMES.size() / 2;
    }
    
    private KeyMap[] getForNum(int runnerNum) {
        return new KeyMap[] {KEY_NAMES.get(runnerNum * 2), KEY_NAMES.get(runnerNum * 2 + 1)};
    }
    public String getControlsFor(int runnerNum) {
        var a = getForNum(runnerNum);
        return a[0].text + "   |   " + a[1].text;
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

    private static final Pattern REGEX = Pattern.compile("(\\d)_(.+)");
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        var result = REGEX.matcher(name);
        if (!result.matches() || result.groupCount() < 2)
            return;
        int num = Integer.parseInt(result.group(1));
        String action = result.group(2);

        if (action.equals("Left") && isPressed)
            callListener(num, MoveDir.Left);
        if (action.equals("Right") && isPressed)
            callListener(num, MoveDir.Right);
    }

    private void callListener(int num, MoveDir dir) {
        listeners.get(num).doMove(dir);
    }

    @Override
    public void removeControl(InputManager im, int num) {
        listeners.remove(num);

        im.deleteMapping(num + "_Left");
        im.deleteMapping(num + "_Right");
    }
}