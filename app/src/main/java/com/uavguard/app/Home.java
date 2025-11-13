package com.uavguard.app;

import com.uavguard.plugin.Action;
import com.uavguard.plugin.Plugin;
import com.uavguard.utilities.Manager;
import com.uavguard.utilities.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Home {

    private Manager manager = new Manager();
    private Plugin plugin;

    @FXML
    private Circle lbase;

    @FXML
    private Circle lknob;

    @FXML
    private Circle rbase;

    @FXML
    private Circle rknob;

    @FXML
    private ComboBox<String> modelSelect;

    private final double centerX = 100;
    private final double centerY = 100;
    private double lradius;
    private double rradius;

    @FXML
    public void initialize() {
        try {
            manager.load("/home/hasbulla/Documents/UAVGuard/plugins");
            this.plugin = manager.plugins.get(0);
            for (Plugin p : manager.plugins) {
                modelSelect.getItems().add(p.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rradius = rbase.getRadius();
        lradius = lbase.getRadius();
    }

    @FXML
    public void RightOnMouseDragged(MouseEvent e) {
        var p = rknob.getParent().sceneToLocal(e.getSceneX(), e.getSceneY());

        double dx = p.getX() - centerX;
        double dy = p.getY() - centerY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > rradius) {
            dx = (dx / dist) * rradius;
            dy = (dy / dist) * rradius;
        }

        rknob.setLayoutX(centerX + dx);
        rknob.setLayoutY(centerY + dy);

        plugin.setParameter(Action.ROLL, (int) dx);
        plugin.setParameter(Action.PITCH, -(int) dy);

        try {
            Socket.sendPacket(plugin.getPacket(), plugin.getPort());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @FXML
    public void LeftOnMouseDragged(MouseEvent e) {
        var p = lknob.getParent().sceneToLocal(e.getSceneX(), e.getSceneY());

        double dx = p.getX() - centerX;
        double dy = p.getY() - centerY;

        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > lradius) {
            dx = (dx / dist) * lradius;
            dy = (dy / dist) * lradius;
        }

        lknob.setLayoutX(centerX + dx);
        lknob.setLayoutY(centerY + dy);

        plugin.setParameter(Action.YAW, (int) dx);
        plugin.setParameter(Action.THROTTLE, -(int) dy);

        try {
            Socket.sendPacket(plugin.getPacket(), plugin.getPort());

            for (byte b : plugin.getPacket()) {
                System.out.printf("%02X", b);
            }
            System.out.println();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @FXML
    public void onMouseReleased(MouseEvent e) {
        rknob.setLayoutX(centerX);
        rknob.setLayoutY(centerY);
        lknob.setLayoutX(centerX);
        lknob.setLayoutY(centerY);

        // resetar todos os eixos
        plugin.setParameter(Action.THROTTLE, 0);
        plugin.setParameter(Action.YAW, 0);
        plugin.setParameter(Action.PITCH, 0);
        plugin.setParameter(Action.ROLL, 0);

        try {
            Socket.sendPacket(plugin.getPacket(), plugin.getPort());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @FXML
    public void onModelSelect() {
        String selected = modelSelect.getValue();
        System.out.println("Modelo selecionado: " + selected);
    }
}
