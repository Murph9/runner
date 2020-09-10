package runner.base;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import runner.control.IControlScheme;
import runner.control.PlayerControl;
import runner.generator.ObjGenerator;
import runner.generator.Pattern;
import runner.helper.Geo;
import runner.helper.H;

public class Runner extends AbstractAppState {
    
    // Base instance of a runner game
    private final RunnerManager manager;
    private final Vector3f startPos;
    private final int num;
    private final IControlScheme control;
    private final Node rootNode;

    private SimpleApplication app;
    
    private final BoxMover mover;
    private float placedDistance;
    
    private Geometry player;
    private Geometry gFloor;

    private ObjGenerator generator;
    
    public Runner(RunnerManager manager, int num, Vector3f startPos, IControlScheme control) {
        this.manager = manager;
        this.num = num;
        this.rootNode = new Node("root node");
        this.startPos = startPos;
        this.control = control;
        this.mover = new BoxMover();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.mover.setSpeed(1);

        this.app = (SimpleApplication)app;
        ((SimpleApplication)app).getRootNode().attachChild(rootNode);
        rootNode.setLocalTranslation(startPos);
        
        // floor
        gFloor = Geo.createBox(app.getAssetManager(), new Vector3f(2f, 30f, 0.1f), new ColorRGBA(0.2f, 0.2f, 0.2f, 0));
        gFloor.setLocalTranslation(0, 25f, -0.1f);
        rootNode.attachChild(gFloor);
        
        // player
        player = Geo.createBox(app.getAssetManager(), new Vector3f(0.4f, 0.4f, 0.1f), ColorRGBA.Black);
        player.setName("player");
        player.setLocalTranslation(new Vector3f());
        player.getMaterial().setColor("Color", H.randomColourHSV());
        player.addControl(new PlayerControl(app, num, new Vector3f(), control));
        rootNode.attachChild(player);

        control.listenFor(app.getInputManager(), num, player.getControl(PlayerControl.class));

        //start the box generator
        generator = new ObjGenerator();
        placePattern(generator.getStart(25), 5);

        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (player != null)
            player.getControl(PlayerControl.class).setEnabled(enabled);

        mover.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    public float getDistance() {
        return mover.getDistance();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        // update all speeds
        float distance = mover.getDistance();
        mover.setSpeed(RunnerManager.speedByDistance(distance));
        mover.update(tpf);

        //check for collisions
        CollisionResults results = new CollisionResults();
        BoundingVolume box = player.getWorldBound();
        for (Geometry g: mover.getBoxes()) {
            if (g.collideWith(box, results) > 0) {
                stopAllThings();
                manager.gameOver();
            }
        }
                
        // generate new boxes
        if (placedDistance < distance + 20) {
            placePattern(generator.getNext(), 20);
        }
    }

    @Override
    public void cleanup() {
        control.deListenFor(app.getInputManager(), num, player.getControl(PlayerControl.class));

        player.removeFromParent();
        player.removeControl(PlayerControl.class);

        gFloor.removeFromParent();

        mover.cleanup();
        super.cleanup();
    }

	public Spatial getRootNode() {
		return rootNode;
    }

    private void placePattern(Pattern pattern, float yPos) {
        placedDistance += pattern.getLength();
        
        for (var b: pattern.boxes) {
            rootNode.attachChild(mover.placeBox(app, b.length, b.xPos.pos, yPos + b.yPos));
        }

        //rootNode.attachChild(mover.placeLine(app, yPos, -1.5f, 1.5f));
        //TODO fix starting pattern not having the correct length
    }

    private void stopAllThings() {
        player.getControl(PlayerControl.class).setEnabled(false);
        mover.stopAll();
    }
}
