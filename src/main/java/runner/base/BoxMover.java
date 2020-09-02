package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import runner.helper.Geo;
import runner.helper.H;

public class BoxMover {
    
    private final List<Geometry> boxes = new LinkedList<>();
    private final List<Geometry> lines = new LinkedList<>();
    private float boxSpeed;
    private float distance;
    private Vector3f dir;
    private boolean enabled = true;

    protected void update(float tpf) {
        if (!enabled)
            return;

        distance += tpf*boxSpeed;

        this.dir = new Vector3f(0, -boxSpeed, 0);

        // remove passed boxes
        for (Geometry g : boxes) {
            if (g.getLocalTranslation().y < -10) {
                g.removeFromParent();
            }
        }
        // remove passed lines
        for (Geometry g : lines) {
            if (g.getLocalTranslation().y < -10) {
                g.removeFromParent();
            }
        }

        //move boxes
        for (Geometry g : boxes) {
            Vector3f pos = g.getLocalTranslation();
            g.setLocalTranslation(pos.add(dir.mult(tpf)));
        }

        //move debug lines
        for (Geometry g : lines) {
            Vector3f pos = g.getLocalTranslation();
            g.setLocalTranslation(pos.add(dir.mult(tpf)));
        }
    }

    public void setSpeed(float speed) {
        this.boxSpeed = speed;
    }

    public Geometry placeBox(Application app, float yLength, int xPos, float yPos) {
        Geometry g2 = Geo.createBox(app.getAssetManager(), new Vector3f(0.4f, yLength, 0.1f), ColorRGBA.Black);
        g2.getMaterial().setColor("Color", H.randomColourHSV());
        boxes.add(g2);
        g2.setLocalTranslation(xPos, yPos, 0);
        return g2;
    }
    public Geometry placeLine(Application app, float yPos, float xMin, float xMax) {
        Geometry g2 = Geo.createLine(app.getAssetManager(), new Vector3f(0, 0, 0), new Vector3f(xMax - xMin, 0, 0), ColorRGBA.Black);
        g2.getMaterial().setColor("Color", ColorRGBA.White);
        g2.setLocalTranslation(xMin, yPos, 0.1f);
        boxes.add(g2);
        return g2;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

	public List<Geometry> getBoxes() {
		return this.boxes;
	}

	public void cleanup() {
        for (Geometry g : boxes) {
            g.removeFromParent();
        }
	}

	public void stopAll() {
        enabled = false;
	}

	public float getDistance() {
		return this.distance;
	}
}