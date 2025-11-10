package com.uavguard.app;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Controller {

    @FXML
    private Circle base;

    @FXML
    private Circle knob;

    private final double centerX = 100; // centro desejado na PANE
    private final double centerY = 100;
    private double radius;

    @FXML
    public void initialize() {
        radius = base.getRadius();
    }

    @FXML
    public void onMouseDragged(MouseEvent e) {
        var p = knob.getParent().sceneToLocal(e.getSceneX(), e.getSceneY());

        double dx = p.getX() - centerX;
        double dy = p.getY() - centerY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > radius) {
            dx = (dx / dist) * radius;
            dy = (dy / dist) * radius;
        }

        knob.setLayoutX(centerX + dx);
        knob.setLayoutY(centerY + dy);

        System.out.println("DX: " + dx + " | DY: " + dy);
    }

    @FXML
    public void onMouseReleased(MouseEvent e) {
        knob.setLayoutX(centerX);
        knob.setLayoutY(centerY);
    }
}
