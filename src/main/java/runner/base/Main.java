package runner.base;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.simsilica.lemur.GuiGlobals;

/**@author murph9 */
public class Main extends SimpleApplication {

    public static final String PROJECT_VERSION = "v0.0.1 (2020-08-27)";

    public static void main( String... args ) {
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
        System.out.println("Program isdebug: " + isDebug);
        
        Main main = new Main();
        main.setDisplayStatView(true); // defaults to on, shows the triangle count and stuff
        main.start();
    }
    
    public Main() {
        super(new StatsAppState());
    }

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        inputManager.deleteMapping(INPUT_MAPPING_EXIT); // no esc close pls
        
        setPauseOnLostFocus(true);

        // initialize Lemur (the GUI manager)
        GuiGlobals.initialize(this);

        // init game
        RunnerManager rm = new RunnerManager(1);
        rm.init(this);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}


