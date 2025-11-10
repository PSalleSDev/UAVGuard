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
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist > radius) {
            dx = (dx / dist) * radius;
            dy = (dy / dist) * radius;
        }

        knob.setLayoutX(centerX + dx);
        knob.setLayoutY(centerY + dy);

        double normX = dx / radius;
        double normY = dy / radius;
        System.out.println("X: " + normX + " Y: " + normY);
    }
}
