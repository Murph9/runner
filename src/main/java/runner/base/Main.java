package runner.base;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.ConstantVerifierState;
import com.simsilica.lemur.GuiGlobals;

/**@author murph9 */
public class Main extends SimpleApplication {

    public static final String PROJECT_VERSION = "v0.0.2 (2020-09-22)";
    public static boolean IS_DEBUG = true;

    public static void main(String... args) {
        IS_DEBUG = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
        System.out.println("Program isdebug: " + IS_DEBUG);
        
        Main main = new Main();
        main.setDisplayStatView(IS_DEBUG); // shows the triangle count and stuff
        main.start();
    }
    
    public Main() {
        super(new ConstantVerifierState(),
                new StatsAppState(),
                new MainMenu());
    }

    @Override
    public void simpleInitApp() {
        inputManager.setCursorVisible(true);
        inputManager.deleteMapping(INPUT_MAPPING_EXIT); // no esc close pls
        
        setPauseOnLostFocus(true);

        // initialize Lemur (the GUI manager)
        GuiGlobals.initialize(this);
        LemurStyle.load(getContext().getSettings());
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}


