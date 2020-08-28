package runner.base;

import java.util.LinkedList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;

import runner.helper.Screen;

public class RunnerUi extends AbstractAppState {
    
    private final RunnerManager manager;
    private final List<String> keysList = new LinkedList<>();
    private final List<Container> containers = new LinkedList<>();
    
    private Label score;
    private Screen screen;

    public RunnerUi(RunnerManager manager) {
        this.manager = manager;
    }

    public void addKeyCombo(String left, String right) {
        keysList.add(left + " <-- | --> " + right);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        screen = new Screen(app.getContext().getSettings());
        Node guiNode = ((SimpleApplication)app).getGuiNode();
      
        initMainWindow(guiNode);

        initScoreWindow(guiNode);

        int count = keysList.size();
        for (int i = 0; i < count; i++) {
            String keys = keysList.get(i);
            Container keyWindow = new Container();
            guiNode.attachChild(keyWindow);
            keyWindow.addChild(new Label(keys));
            containers.add(keyWindow);

            screen.centeredAt(keyWindow, new Vector3f(i* screen.getWidth() / count + screen.getWidth() / (2 * count), 
                    screen.getHeight() - 50, 0));
        }

        super.initialize(stateManager, app);
    }

    @SuppressWarnings("unchecked") // button unchecked vargs
    private void initMainWindow(Node guiNode) {
        Container mainWindow = new Container();
        mainWindow.addChild(new Label("Runner"));
        Button button = mainWindow.addChild(new Button("Go"), 1);
        button.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                manager.start();
                mainWindow.removeFromParent();
            }
        });
        guiNode.attachChild(mainWindow);

        screen.centerMe(mainWindow);
    }

    private void initScoreWindow(Node guiNode) {
        Container scoreWindow = new Container();
        score = scoreWindow.addChild(new Label(""));
        guiNode.attachChild(scoreWindow);
        screen.topCenterMe(scoreWindow);
    }

    @Override
    public void update(float tpf) {
        if (score != null) {
            score.setText("Score: " + (int)(manager.getDistance()*10));
        }

        screen.topCenterMe((Panel)score.getParent());

        super.update(tpf);
    }
}
