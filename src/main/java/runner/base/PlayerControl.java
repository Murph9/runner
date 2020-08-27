package runner.base;

import com.jme3.app.Application;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class PlayerControl extends AbstractControl {

    private final Vector3f startPos;
    private int pos;

    public PlayerControl(Application app, Vector3f startPos, int keyRight, int keyLeft) {
        this.startPos = startPos;
        app.getInputManager().addMapping("Left", new KeyTrigger(keyRight));
        app.getInputManager().addMapping("Right", new KeyTrigger(keyLeft));
        app.getInputManager().addListener(listener, "Left", "Right");
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.spatial.setLocalTranslation(startPos.add(pos, 0, 0));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // nothing
    }

    private final ActionListener listener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Left") && !keyPressed) {
                pos--;
            }
            if (name.equals("Right") && !keyPressed) {
                pos++;
            }

            pos = Math.max(-1, Math.min(1, pos)); // clamp
        }
    };
    
}