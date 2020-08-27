package runner.base;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

public class RunnerObjControl extends AbstractControl {

    private final Vector3f dir;
    public RunnerObjControl(Vector3f dir) {
        this.dir = dir;
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f pos = this.spatial.getLocalTranslation();
        this.spatial.setLocalTranslation(pos.add(dir.mult(tpf)));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // nothing
    }
}
