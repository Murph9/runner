package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Runner extends AbstractAppState {
    
    // Base instance of a runner game
    private final Vector3f startPos;

    private final List<Geometry> objects;
    private int characterPos;
    private Geometry player;
    
    public Runner(Vector3f startPos) {
        this.startPos = startPos;
        this.objects = new LinkedList<>();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.characterPos = 0;

        Box b = new Box(0.5f, 0.5f, 0.5f);
        Geometry g = new Geometry("thing", b);
        var mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1,1,1,0.08f));
        g.setMaterial(mat);

        player = g.clone();
        player.setLocalTranslation(startPos);

        ((SimpleApplication)app).getRootNode().attachChild(player);

        for (int i = 0; i < 10; i++) {
            var g2 = g.clone();
            objects.add(g2);
            g2.setLocalTranslation(FastMath.nextRandomInt(-1, 1)*2, FastMath.nextRandomInt(2, 15)*2, 0);
            ((SimpleApplication) app).getRootNode().attachChild(g2);
        }

        app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        app.getInputManager().addListener(listener, "Left", "Right");
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        System.out.println(characterPos);
        player.setLocalTranslation(startPos.add(characterPos, 0, 0));

        for (Geometry g: objects) {
            g.setLocalTranslation(g.getLocalTranslation().add(0, -1*tpf, 0));
        }
    }

    private final ActionListener listener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Left") && !keyPressed) {
                characterPos--;
            }
            if (name.equals("Right") && !keyPressed) {
                characterPos++;
            }
        }
    };
}
