package runner.base;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.style.ElementId;

import runner.helper.Screen;
import runner.saving.RecordManager;

public class MainMenu extends AbstractAppState {
    
    private Screen screen;
    private Application app;
    private Node rootNode;

    private Container mainWindow;
    private RunnerManager rm;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
        screen = new Screen(app.getContext().getSettings());
        rootNode = new Node("UI node");
        ((SimpleApplication)app).getGuiNode().attachChild(rootNode);
      
        initMainWindow(rootNode);

        super.initialize(stateManager, app);
    }

    @SuppressWarnings("unchecked") // button unchecked vargs
    private void initMainWindow(Node rootNode) {
        mainWindow = new Container();
        mainWindow.setPreferredSize(new Vector3f(screen.getWidth() / 2, screen.getHeight() / 1.4f, 0));
        var l = mainWindow.addChild(new Label("Runner", new ElementId("title")));
        l.setTextHAlignment(HAlignment.Center);
        l.setTextVAlignment(VAlignment.Center);

        for (int i = 1; i < 6; i++) {
            Button button = mainWindow.addChild(new Button("Start (" + i + ")"));
            button.setTextHAlignment(HAlignment.Center);
            button.setTextVAlignment(VAlignment.Center);
            final int count = i;
            button.addClickCommands(new Command<Button>() {
                @Override
                public void execute(Button source) {
                    mainWindow.removeFromParent();
                    startup(count);
                }
            });

            l = mainWindow.addChild(new Label("HS: " + (int)RecordManager.getRecord(i), new ElementId("small")), 1);
            l.setTextVAlignment(VAlignment.Center);
        }
        rootNode.attachChild(mainWindow);

        screen.centerMe(mainWindow);
    }

    private void startup(int count) {
        if (rm != null)
            rm.cleanup(app);

        // init game
        rm = new RunnerManager(this, count);
        rm.initOnce(app);
    }

    public void quit() {
        rootNode.attachChild(mainWindow);
    }
}
