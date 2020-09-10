package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.post.filters.FogFilter;

import runner.control.PairControlScheme;
import runner.control.IControlScheme;
import runner.control.NumControlScheme;
import runner.saving.RecordManager;

public class RunnerManager {
    
    public static final String PAIR_CONTROLS = "Pair";
    public static final String NUM_CONTROLS = "Num";

    private static final float SPEED_MULT = 1.75f;
    private final MainMenu menu;
    private final int count;
    private RunnerUi ui;
    private List<Runner> runners;
    private IControlScheme controller;

    protected static float speedByDistance(float dist) {
        return SPEED_MULT * _speedByDistance(dist);
    }
    private static float _speedByDistance(float dist) {
        if (dist < 0)
            return 1;
        if (dist < 100)
            return 1 + (dist) / 50;
        if (dist < 200)
            return 3 + (dist - 100) / 100;
        return 4 + (dist - 200) / 200;
    }

    public RunnerManager(MainMenu menu, int count, String type) {
        this.menu = menu;
        this.runners = new LinkedList<>();

        if (type == PAIR_CONTROLS)
            this.controller = new PairControlScheme();
        else if (type == NUM_CONTROLS)
            this.controller = new NumControlScheme();
        else
            throw new IllegalArgumentException("type");

        this.count = (int) FastMath.clamp(count, 1, controller.maxCount()); // can't make more than your key combos
    }

    private Vector3f calcOffsetFor(int i) {
        return new Vector3f(i * 10f, 0, 0); // just far enough that they can't see each other
    }

    private void reset(Application app) {
        var highScore = RecordManager.getRecord(this.count);

        ui = new RunnerUi(this, highScore);
        app.getStateManager().attach(ui);

        for (int i = 0; i < count; i++) {
            Vector3f offset = calcOffsetFor(i);

            Runner r = new Runner(this, i, offset, controller);
            runners.add(r);
            app.getStateManager().attach(r);

            ui.addKeyCombo(controller.getControlsFor(i));
        }
    }

    public void initOnce(Application app) {
        // setup viewports
        FogFilter fog = new FogFilter(new ColorRGBA(0f, 0f, 0f, .5f), 2f, 80f);
        var cam = app.getCamera();
        int width = cam.getWidth();
        int height = cam.getHeight();

        //add a viewport for every side (yes we keep the original one free)
        for (int i = 0; i < count; i++) {
            var offset = calcOffsetFor(i);

            cam = new Camera(width, height);
            cam.setFrustumPerspective(45f, (float) cam.getWidth() / (count * cam.getHeight()), 1f, 1000f);

            cam.setViewPort(1 * i / (float) count, 1 * (i + 1) / (float) count, 0, 1);
            cam.setLocation(offset.add(0, -5, 8));
            cam.lookAt(offset.add(0, 4, 0), Vector3f.UNIT_Y);
            ViewPort view_n = app.getRenderManager().createMainView("View of camera "+i, cam);
            view_n.attachScene(((SimpleApplication)app).getRootNode());
            view_n.setBackgroundColor(ColorRGBA.Black);
            view_n.setClearFlags(true, true, true);

            var fpp = new FilterPostProcessor(app.getAssetManager());
            fpp.addFilter(fog);
            view_n.addProcessor(fpp);
        }
    
        this.controller.init(app.getInputManager());

        //init actual game
        reset(app);
    }

    public float getDistance() {
        float distance = 0;
        for (Runner r: runners) {
            distance = Math.max(distance, r.getDistance());
        }
        return distance;
    }

    public void gameOver() {
        for (Runner r: runners) {
            r.setEnabled(false);
        }

        float score = getDistance();
        RecordManager.saveRecord(this.count, score);
        ui.gameOver(score);
    }

	public void start() {
        for (Runner r : runners) {
            r.setEnabled(true);
        }
    }
    
    public void restart(Application app) {
        app.getStateManager().detach(ui);
        ui = null;

        for (Runner r : runners) {
            app.getStateManager().detach(r);
        }
        runners.clear();

        reset(app);
    }

    public void stop(Application app) {
        app.getStateManager().detach(ui);
        ui = null;

        for (Runner r : runners) {
            app.getStateManager().detach(r);
        }
        runners.clear();

        controller.remove(app.getInputManager());
        menu.quit();
    }

    public void cleanup(Application app) {
        //remove all but the first one
        boolean first = true;
        for (var viewport: new LinkedList<>(app.getRenderManager().getMainViews())) {
            if (first)
                first = false;
            else
                app.getRenderManager().removeMainView(viewport);
        }
    }
}
