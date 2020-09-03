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
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.style.ElementId;

import runner.helper.Screen;

public class RunnerUi extends AbstractAppState {
    
    private final RunnerManager manager;
    private final List<String> keysList = new LinkedList<>();
    private final List<Container> containers = new LinkedList<>();
    private final float highScore;

    private Label score;
    private Screen screen;
    private Application app;
    private Node rootNode;

    public RunnerUi(RunnerManager manager, float highScore) {
        this.manager = manager;
        this.highScore = highScore;
    }

    public void addKeyCombo(String left, String right) {
        keysList.add(left + "   |   " + right);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.app = app;
        screen = new Screen(app.getContext().getSettings());
        rootNode = new Node("UI node");
        ((SimpleApplication)app).getGuiNode().attachChild(rootNode);
      
        initMainWindow(rootNode);

        initScoreWindow(rootNode);

        int count = keysList.size();
        for (int i = 0; i < count; i++) {
            String keys = keysList.get(i);
            Container keyWindow = new Container();
            rootNode.attachChild(keyWindow);
            keyWindow.addChild(new Label(keys));
            containers.add(keyWindow);

            screen.centeredAt(keyWindow, new Vector3f(i * screen.getWidth() / count + screen.getWidth() / (2 * count), 50, 0));
        }

        super.initialize(stateManager, app);
    }

    @SuppressWarnings("unchecked") // button unchecked vargs
    private void initMainWindow(Node rootNode) {
        Container mainWindow = new Container();
        mainWindow.setPreferredSize(new Vector3f(screen.getWidth()/3, screen.getHeight()/3, 0));
        var l = mainWindow.addChild(new Label("Runner"));
        l.setTextHAlignment(HAlignment.Center);
        l.setTextVAlignment(VAlignment.Center);
        Button button = mainWindow.addChild(new Button("Go"));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                manager.start();
                mainWindow.removeFromParent();
            }
        });
        rootNode.attachChild(mainWindow);

        screen.centerMe(mainWindow);
    }

    private void initScoreWindow(Node rootNode) {
        Container scoreWindow = new Container();
        var highScore = scoreWindow.addChild(new Label(formatScore(this.highScore), new ElementId("small")));
        highScore.setTextHAlignment(HAlignment.Center);

        score = scoreWindow.addChild(new Label("", new ElementId("title")));
        score.setTextHAlignment(HAlignment.Center);
        
        rootNode.attachChild(scoreWindow);
        screen.topCenterMe(scoreWindow);
    }

    @Override
    public void update(float tpf) {
        if (score != null) {
            score.setText(formatScore(manager.getDistance()));
            screen.topCenterMe((Panel) score.getParent());
        }

        super.update(tpf);
    }

    private String formatScore(float score) {
        return ""+(int)score;
    }

    @SuppressWarnings("unchecked") // button unchecked vargs
	public void gameOver(float distance) {
        ((Panel) score.getParent()).removeFromParent();

        Container scoreWindow = new Container();
        scoreWindow.setPreferredSize(new Vector3f(screen.getWidth() / 3, screen.getHeight() / 3, 0));
        var l = scoreWindow.addChild(new Label("Game over"));
        l.setTextHAlignment(HAlignment.Center);
        l.setTextVAlignment(VAlignment.Center);
        l = scoreWindow.addChild(new Label(formatScore(manager.getDistance())));
        l.setTextHAlignment(HAlignment.Center);
        l.setTextVAlignment(VAlignment.Center);

        Button button = scoreWindow.addChild(new Button("Start again"));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                manager.restart(app);
            }
        });

        button = scoreWindow.addChild(new Button("Main Menu"));
        button.setTextHAlignment(HAlignment.Center);
        button.setTextVAlignment(VAlignment.Center);
        button.addClickCommands(new Command<Button>() {
            @Override
            public void execute(Button source) {
                manager.stop(app);
            }
        });

        rootNode.attachChild(scoreWindow);
        screen.centerMe(scoreWindow);
    }
    
    @Override
    public void cleanup() {
        rootNode.removeFromParent();

        super.cleanup();
    }
}
