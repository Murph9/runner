package runner.base;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.Styles;

public class LemurStyle {
    //https://hub.jmonkeyengine.org/t/many-little-lemur-questions/40244/14
    public static final String STYLE_NAME = "my_style";

    public static void load(AppSettings settings) {
        final ColorRGBA base = new ColorRGBA(1, 0.455f, 0.439f, 1f); // FF7470
        final ColorRGBA base2 = new ColorRGBA(1, 0.831f, 0.643f, 1f); // FFD4A4

        Styles styles = GuiGlobals.getInstance().getStyles();

        int fontSize = (int)FastMath.clamp(settings.getWidth() / 15, 36, 130);
        BitmapFont font = GuiGlobals.getInstance().loadFont("Interface/Fonts/nasalization.fnt");
        styles.setDefault(font);

        Attributes attrs;
        QuadBackgroundComponent bg = new QuadBackgroundComponent(new ColorRGBA(0f, 0f, 0f, 1f));
        TbtQuadBackgroundComponent gradient = TbtQuadBackgroundComponent.create(
                "Textures/solid-white.png",
                1, 1, 1, 2, 2,
                1f, false);

        attrs = styles.getSelector(STYLE_NAME);
        attrs.set("fontSize", fontSize);

        // label
        attrs = styles.getSelector("label", STYLE_NAME);
        attrs.set("insets", new Insets3f(2, 2, 0, 2));
        attrs.set("color", base2);

        // title
        attrs = styles.getSelector("title", STYLE_NAME);
        attrs.set("insets", new Insets3f(2, 2, 0, 2));
        attrs.set("color", base2);
        attrs.set("fontSize", fontSize * 1.5f);

        // small
        attrs = styles.getSelector("small", STYLE_NAME);
        attrs.set("insets", new Insets3f(2, 2, 0, 2));
        attrs.set("color", base2);
        attrs.set("fontSize", fontSize / 1.5f);

        // button
        attrs = styles.getSelector("button", STYLE_NAME);
        attrs.set("color", base2);
        attrs.set("background", gradient.clone());
        ((TbtQuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(1,1,1,0.2f));
        attrs.set("insets", new Insets3f(2, 2, 2, 2));
        attrs.set("highlightColor", base);
        attrs.set("focusColor", base);

        // container
        attrs = styles.getSelector("container", STYLE_NAME);
        attrs.set("background", gradient.clone());
        ((TbtQuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(1f, 1f, 1f, 0f));

        // slider
        attrs = styles.getSelector("slider", STYLE_NAME);
        attrs.set("insets", new Insets3f(2,2,2,2));
        attrs.set("background", bg.clone());
        ((QuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(0.25f, 0.5f, 0.5f, 0.5f));

        attrs = styles.getSelector("slider", "button", STYLE_NAME);
        attrs.set("background", bg.clone());
        ((QuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(0.5f, 0.75f, 0.75f, 0.5f));
        attrs.set("insets", new Insets3f(0,0,0,0));

        attrs = styles.getSelector("slider", "slider.thumb.button", STYLE_NAME);
        attrs.set("text", "[]");
        attrs.set("color", new ColorRGBA(0.6f, 0.8f, 0.8f, 0.85f));

        attrs = styles.getSelector("slider", "slider.left.button", STYLE_NAME);
        attrs.set("text", "-");
        attrs.set("background", bg.clone());
        ((QuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(0.5f, 0.75f, 0.75f, 0.5f));
        ((QuadBackgroundComponent)attrs.get("background")).setMargin(5, 0);
        attrs.set("color", new ColorRGBA(0.6f, 0.8f, 0.8f, 0.85f));

        attrs = styles.getSelector("slider", "slider.right.button", STYLE_NAME);
        attrs.set("text", "+");
        attrs.set("background", bg.clone());
        ((QuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(0.5f, 0.75f, 0.75f, 0.5f));
        ((QuadBackgroundComponent)attrs.get("background")).setMargin(4, 0);
        attrs.set("color", new ColorRGBA(0.6f, 0.8f, 0.8f, 0.85f));

        // Tabbed Panel
        attrs = styles.getSelector("tabbedPanel", STYLE_NAME);
        attrs.set("activationColor", new ColorRGBA(0.8f, 0.9f, 1.0f, 0.85f));

        attrs = styles.getSelector("tabbedPanel.container", STYLE_NAME);
        attrs.set("background", null);

        attrs = styles.getSelector("tab.button", STYLE_NAME);
        attrs.set("background", gradient.clone());
        ((TbtQuadBackgroundComponent)attrs.get("background")).setColor(new ColorRGBA(0.25f, 0.5f, 0.5f, 0.5f));
        ((TbtQuadBackgroundComponent)attrs.get("background")).setMargin(6, 4);
        attrs.set("color", new ColorRGBA(0.4f, 0.45f, 0.5f, 0.85f));
        attrs.set("insets", new Insets3f(6,4,0,4));
        
        // checkbox text
        attrs = styles.getSelector("checkbox", STYLE_NAME);
        attrs.set("color", new ColorRGBA(0, 0, 0, 0.85f));

        // Set this as the default style
        GuiGlobals.getInstance().getStyles().setDefaultStyle(STYLE_NAME);
    }
}
