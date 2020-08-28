package runner.helper;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Geo {
    public static Geometry createBox(AssetManager am, Vector3f size, ColorRGBA colour) {
        Box box = new Box(size.x, size.y, size.z);
        Geometry gBox = new Geometry("box", box);
        gBox.setLocalTranslation(0, 25, -0.1f);
        var matBox = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        matBox.setColor("Color", new ColorRGBA(0.2f, 0.2f, 0.2f, 0));
        gBox.setMaterial(matBox);
        return gBox;
    }
}