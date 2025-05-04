package com.app.foodguard.utils;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

public class IconManager {
    public static Node getIcon(FontAwesomeIcon iconType, String color, double size) {
        FontAwesomeIconView icon = new FontAwesomeIconView(iconType);
        icon.setFill(Paint.valueOf(color));
        icon.setSize(String.format("%.1fem", size));
        return icon;
    }
}
