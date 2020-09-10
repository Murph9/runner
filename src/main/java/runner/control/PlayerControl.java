package runner.control;

import com.jme3.app.Application;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class PlayerControl extends AbstractControl implements ActionListener {

    private final int num;
    private final Vector3f startPos;
    private final IControlScheme control;
    private int pos;

    public PlayerControl(Application app, int num, Vector3f startPos, IControlScheme control) {
        this.startPos = startPos;
        this.num = num;
        this.control = control;
    }

    @Override
    protected void controlUpdate(float tpf) {
        this.spatial.setLocalTranslation(startPos.add(pos, 0, 0));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // nothing
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        var result = control.onAction(num, name, keyPressed, tpf);
        if (result == MoveDir.Left) {
            pos--;
        }
        if (result == MoveDir.Right) {
            pos++;
        }

        pos = Math.max(-1, Math.min(1, pos)); // clamp
    }
}
