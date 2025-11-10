package com.uavguard.app;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Controller {

    @FXML
    private Circle base;

    @FXML
    private Circle knob;

    private double centerX;
    private double centerY;
    private double radius;

    @FXML
    public void initialize() {
        radius = base.getRadius();
        centerX = knob.getCenterX();
        centerY = knob.getCenterY();
    }

    @FXML
    public void onMouseReleased(MouseEvent e) {
        knob.setCenterX(centerX);
        knob.setCenterY(centerY);
    }

    @FXML
    public void onMouseDragged(MouseEvent e) {
        double dx = e.getX();
        double dy = e.getY();

        if (-100 <= dx && dx <= 100 && -100 <= dy && dy <= 100) {
            knob.setCenterX(dx);
            knob.setCenterY(dy);
        }

        System.out.println("X: " + dx + " Y: " + dy);
    }
}

// double distance = Math.sqrt(dx * dx + dy * dy);
// if (distance > radius) {
//     dx = (dx / distance) * radius;
//     dy = (dy / distance) * radius;
// }

// knob.setCenterX(centerX + dx);
// knob.setCenterY(centerY + dy);

// <Pane fx:id="joystick">
//     <Circle fx:id="base" radius="100"/>
//     <Circle fx:id="knob" radius="30"/>
// </Pane>
// <Pane fx:id="joystick">
//     <Circle fx:id="base" layoutX="100" layoutY="100" radius="100"/>
//     <Circle fx:id="knob" onMouseReleased="#onMouseReleased" onMouseDragged="#onMouseDragged" layoutX="100" layoutY="100" radius="30"/>
// </Pane>
