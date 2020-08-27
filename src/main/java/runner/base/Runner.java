package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import runner.helper.H;

public class Runner extends AbstractAppState {
    
    // Base instance of a runner game
    private final Vector3f startPos;
    private final int leftKey;
    private final int rightKey;

    private final List<Geometry> objects;
    private Geometry player;
    
    public Runner(Vector3f startPos, int left, int right) {
        this.startPos = startPos;
        this.leftKey = left;
        this.rightKey = right;
        this.objects = new LinkedList<>();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        
        Box b = new Box(0.4f, 0.4f, 0.4f);
        Geometry g = new Geometry("thing", b);
        var mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(1,1,1,0.08f));
        g.setMaterial(mat);

        player = g.clone();
        player.setLocalTranslation(startPos);
        player.addControl(new PlayerControl(app, startPos, leftKey, rightKey));

        ((SimpleApplication)app).getRootNode().attachChild(player);

        for (int i = 0; i < 10; i++) {
            var g2 = g.clone();
            g2.setName("box" + i);
            g2.getMaterial().setColor("Color", H.randomColourHSV());
            objects.add(g2);
            g2.setLocalTranslation(FastMath.nextRandomInt(-1, 1), FastMath.nextRandomInt(2, 15)*2, 0);
            g2.addControl(new RunnerObjControl(new Vector3f(0, -1, 0)));
            ((SimpleApplication) app).getRootNode().attachChild(g2);
        }
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        //check for collisions
        CollisionResults results = new CollisionResults();
        BoundingVolume box = player.getWorldBound();
        for (Geometry g: objects) {
            if (g.collideWith(box, results) > 0) {
                System.out.println(g.getName());
                //TODO end
            }
        }
    }
}
