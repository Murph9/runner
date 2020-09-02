package runner.helper;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;

public class Geo {
    public static Geometry createBox(AssetManager am, Vector3f size, ColorRGBA colour) {
        Box box = new Box(size.x, size.y, size.z);
        Geometry gBox = new Geometry("box", box);
        var matBox = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        matBox.setColor("Color", colour);
        gBox.setMaterial(matBox);
        return gBox;
    }

    public static Geometry createLine(AssetManager am, Vector3f start, Vector3f end, ColorRGBA colour) {
        Line line = new Line(start, end);
        Geometry gLine = new Geometry("line", line);
        var matLine = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        matLine.setColor("Color", colour);
        gLine.setMaterial(matLine);
        return gLine;
    }
}