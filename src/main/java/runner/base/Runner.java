package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import runner.helper.Geo;
import runner.helper.H;

public class Runner extends AbstractAppState {
    
    // Base instance of a runner game
    private final RunnerManager manager;
    private final Vector3f startPos;
    private final int leftKey;
    private final int rightKey;

    private float distance;

    private final List<Geometry> objects;
    private Geometry player;
    
    public Runner(RunnerManager manager, Vector3f startPos, int left, int right) {
        this.manager = manager;
        this.startPos = startPos;
        this.leftKey = left;
        this.rightKey = right;
        this.objects = new LinkedList<>();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        Node rootNode = ((SimpleApplication)app).getRootNode();
        
        // floor
        Geometry gFloor = Geo.createBox(app.getAssetManager(), new Vector3f(2f, 30f, 0.1f), new ColorRGBA(0.2f, 0.2f, 0.2f, 0));
        gFloor.setLocalTranslation(startPos.add(0, 25f, -0.1f));
        rootNode.attachChild(gFloor);
        
        // player
        Geometry g = Geo.createBox(app.getAssetManager(), new Vector3f(0.4f, 0.4f, 0.1f), ColorRGBA.Black);
        player = g.clone();
        player.setName("player");
        player.getMaterial().setColor("Color", H.randomColourHSV());
        player.setLocalTranslation(startPos);
        player.addControl(new PlayerControl(app, startPos, leftKey, rightKey));
        rootNode.attachChild(player);

        for (int i = 0; i < 10; i++) {
            var g2 = g.clone();
            g2.setName("box" + i);
            g2.getMaterial().setColor("Color", H.randomColourHSV());
            objects.add(g2);
            g2.setLocalTranslation(startPos.add(FastMath.nextRandomInt(-1, 1), FastMath.nextRandomInt(2, 15)*2, 0));
            g2.addControl(new RunnerObjControl(new Vector3f(0, -1, 0)));
            rootNode.attachChild(g2);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        player.getControl(PlayerControl.class).setEnabled(enabled);
        for (Geometry g: this.objects) {
            g.getControl(RunnerObjControl.class).setEnabled(enabled);
        }
    }

    public float getDistance() {
        return distance;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        distance += tpf;

        //check for collisions
        CollisionResults results = new CollisionResults();
        BoundingVolume box = player.getWorldBound();
        for (Geometry g: objects) {
            if (g.collideWith(box, results) > 0) {
                stopAllThings();
                manager.gameOver();
            }
        }
    }

    private void stopAllThings() {
        player.getControl(PlayerControl.class).setEnabled(false);
        for (Geometry g: objects) {
            g.getControl(RunnerObjControl.class).setEnabled(false);
        }
    }
}
