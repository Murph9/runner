package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import runner.base.ObjGenerator.Pos;
import runner.helper.Geo;
import runner.helper.H;

public class Runner extends AbstractAppState {
    
    // Base instance of a runner game
    private final RunnerManager manager;
    private final Vector3f startPos;
    private final String playerId;
    private final int leftKey;
    private final int rightKey;
    private final Node rootNode;

    private Geometry baseGeo;

    private float boxSpeed;
    private float distance;
    private int blankRowCount;

    private SimpleApplication app;
    
    private final List<Geometry> objects;
    private Geometry player;
    private Geometry gFloor;

    private ObjGenerator generator;
    
    public Runner(RunnerManager manager, Vector3f startPos, int left, int right) {
        this.manager = manager;
        this.rootNode = new Node("root node");
        this.startPos = startPos;
        this.playerId = startPos.toString(); //PLEASE fix this hack for event handlers
        this.leftKey = left;
        this.rightKey = right;
        this.objects = new LinkedList<>();

        this.boxSpeed = 1;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.baseGeo = Geo.createBox(app.getAssetManager(), new Vector3f(0.4f, 0.4f, 0.1f), ColorRGBA.Black);
        
        this.app = (SimpleApplication)app;
        ((SimpleApplication)app).getRootNode().attachChild(rootNode);
        rootNode.setLocalTranslation(startPos);
        
        // floor
        gFloor = Geo.createBox(app.getAssetManager(), new Vector3f(2f, 30f, 0.1f), new ColorRGBA(0.2f, 0.2f, 0.2f, 0));
        gFloor.setLocalTranslation(0, 25f, -0.1f);
        rootNode.attachChild(gFloor);
        
        // player
        player = baseGeo.clone();
        player.setName("player");
        player.setLocalTranslation(new Vector3f());
        player.getMaterial().setColor("Color", H.randomColourHSV());
        player.addControl(new PlayerControl(app, playerId, new Vector3f(), leftKey, rightKey));
        rootNode.attachChild(player);

        app.getInputManager().addMapping(playerId + "Left", new KeyTrigger(leftKey));
        app.getInputManager().addMapping(playerId + "Right", new KeyTrigger(rightKey));
        app.getInputManager().addListener(player.getControl(PlayerControl.class), playerId + "Left", playerId + "Right");

        //start the box generator
        generator = new ObjGenerator();

        var rows = generator.initalRows();
        for (int i = 0; i < rows.length; i++) {
            placeRow(rows[i], i);
        }

        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (player != null)
            player.getControl(PlayerControl.class).setEnabled(enabled);
        
        for (Geometry g: this.objects)
            g.getControl(RunnerObjControl.class).setEnabled(enabled);

        super.setEnabled(enabled);
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
        
        
        // remove passed boxes
        for (Geometry g: objects) {
            if (g.getLocalTranslation().y < -10) {
                g.removeFromParent();
            }
        }
        
        // generate new boxes
        if (calcFurthestBox() + blankRowCount < 19) {
            var row = generator.getNextRow();
            placeRow(row, 20);
        }

        //update all speeds
        boxSpeed = RunnerManager.speedByDistance(getDistance());
        
        for (Geometry g : objects) {
            g.getControl(RunnerObjControl.class).setDir(new Vector3f(0, -boxSpeed, 0));
        }
    }

    @Override
    public void cleanup() {
        app.getInputManager().deleteMapping(this.playerId + "Left");
        app.getInputManager().deleteMapping(this.playerId + "Right");
        app.getInputManager().removeListener(player.getControl(PlayerControl.class));

        player.removeFromParent();
        player.removeControl(PlayerControl.class);

        gFloor.removeFromParent();

        for (Geometry g : objects) {
            g.removeFromParent();
            g.removeControl(RunnerObjControl.class);
        }

        super.cleanup();
    }

	public Spatial getRootNode() {
		return rootNode;
    }

    private void placeRow(Pos row, int pos) {
        if (row.ifLeft()) {
            placeBox(-1, pos);
        }
        if (row.ifRight()) {
            placeBox(1, pos);
        }
        if (row.ifMiddle()) {
            placeBox(0, pos);
        }

        if (row.ifEmpty()) {
            blankRowCount++;
        } else {
            blankRowCount = 0;
        }
    }

    private void placeBox(int xPos, int yPos) {
        var g2 = baseGeo.clone();
        g2.setName("box");
        g2.getMaterial().setColor("Color", H.randomColourHSV());
        objects.add(g2);
        g2.setLocalTranslation(xPos, yPos, 0);
        g2.addControl(new RunnerObjControl(new Vector3f(0, -boxSpeed, 0)));
        rootNode.attachChild(g2);
    }

    private void stopAllThings() {
        player.getControl(PlayerControl.class).setEnabled(false);
        for (Geometry g : objects) {
            g.getControl(RunnerObjControl.class).setEnabled(false);
        }
    }
    
    private float calcFurthestBox() {
        float distance = 0;
        for (Geometry g : objects) {
            distance = Math.max(distance, g.getLocalTranslation().y);
        }
        return distance;
    }

}
