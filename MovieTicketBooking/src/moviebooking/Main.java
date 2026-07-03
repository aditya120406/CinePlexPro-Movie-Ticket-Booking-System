package moviebooking;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            UITheme.installTooltipStyle();
            UIManager.put("Button.font",            UITheme.FONT_BODY);
            UIManager.put("Label.font",             UITheme.FONT_BODY);
            UIManager.put("TextField.font",         UITheme.FONT_BODY);
            UIManager.put("OptionPane.messageFont", UITheme.FONT_BODY);

            new AppSplashScreen().showSplash();
        });
    }
}
