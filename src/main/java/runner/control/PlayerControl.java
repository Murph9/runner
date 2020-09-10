package runner.control;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class PlayerControl extends AbstractControl implements IControlSchemeListener {

    private final Vector3f startPos;
    private int pos;

    public PlayerControl(Application app, Vector3f startPos) {
        this.startPos = startPos;
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
    public void doMove(MoveDir dir) {
        if (dir == MoveDir.Left) {
            pos--;
        }
        if (dir == MoveDir.Right) {
            pos++;
        }

        pos = Math.max(-1, Math.min(1, pos)); // clamp
    }
}
