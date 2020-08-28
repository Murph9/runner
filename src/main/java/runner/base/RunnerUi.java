package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;

import runner.helper.Screen;

public class RunnerUi extends AbstractAppState {
    
    private List<String> keysList = new LinkedList<>();
    private List<Container> containers = new LinkedList<>();

    public RunnerUi() {
        
    }

    public void addKeyCombo(String left, String right) {
        keysList.add(left + " | " + right);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        Screen screen = new Screen(app.getContext().getSettings());
        Node guiNode = ((SimpleApplication)app).getGuiNode();
        int width = app.getCamera().getWidth();
        int height = app.getCamera().getHeight();

        int count = keysList.size();
        for (int i = 0; i < count; i++) {
            String keys = keysList.get(i);
            Container myWindow = new Container();
            guiNode.attachChild(myWindow);
            myWindow.addChild(new Label(keys));
            containers.add(myWindow);

            screen.centeredAt(myWindow, new Vector3f(i*width / count + width / (2 * count), height - 50, 0));
        }

        super.initialize(stateManager, app);
    }
}
