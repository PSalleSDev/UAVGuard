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
        centerX = base.getLayoutX();
        centerY = base.getLayoutY();
        radius = base.getRadius();
    }

    @FXML
    public void onMouseReleased(MouseEvent e) {
        knob.setLayoutX(centerX);
        knob.setLayoutY(centerY);
    }

    @FXML
    public void onMouseDragged(MouseEvent e) {
        double dx = e.getX() - centerX;
        double dy = e.getY() - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > radius) {
            dx = (dx / distance) * radius;
            dy = (dy / distance) * radius;
        }

        knob.setCenterX(centerX + dx);
        knob.setCenterY(centerY + dy);

        System.out.println("X: " + dx + " Y: " + dy);
    }
}

// <Pane fx:id="joystick">
//     <Circle fx:id="base" radius="100"/>
//     <Circle fx:id="knob" radius="30"/>
// </Pane>
// <Pane fx:id="joystick">
//     <Circle fx:id="base" layoutX="100" layoutY="100" radius="100"/>
//     <Circle fx:id="knob" onMouseReleased="#onMouseReleased" onMouseDragged="#onMouseDragged" layoutX="100" layoutY="100" radius="30"/>
// </Pane>
