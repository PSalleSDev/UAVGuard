package com.uavguard.app;

import com.uavguard.plugin.Action;
import com.uavguard.plugin.Movement;
import com.uavguard.plugin.Plugin;
import com.uavguard.utilities.Manager;
import com.uavguard.utilities.Socket;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class Home {

    private Manager manager = new Manager();
    private Plugin plugin;
    private Action command;
    private final Socket socket = new Socket();
    private String ip;
    private volatile boolean running = true;

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

    @FXML
    private ComboBox<String> commandSelect;

    @FXML
    private ImageView cameraView;

    private final double centerX = 100;
    private final double centerY = 100;
    private double lradius;
    private double rradius;

    @FXML
    public void initialize() {
        setIp();
        try {
            manager.load("/home/hasbulla/Documents/UAVGuard/plugins");
            plugin = manager.plugins.get(0);
            command = plugin.getCommand().getActions()[0];
            for (Plugin p : manager.plugins) {
                modelSelect.getItems().add(p.getName());
            }

            for (Action c : plugin.getCommand().getActions()) {
                commandSelect.getItems().add(c.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rradius = rbase.getRadius();
        lradius = lbase.getRadius();

        plugin
            .getVideo()
            .onFrame((byte[] data) -> {
                Platform.runLater(() -> {
                    Image img = new Image(new ByteArrayInputStream(data));
                    cameraView.setImage(img);
                });
            });

        new Thread(() -> {
            while (running) {
                try {
                    byte[] pkt = plugin.getCommand().getPacket();
                    socket.sendPacket(pkt, ip, plugin.getCommand().getPort());
                    Thread.sleep(50);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        })
            .start();

        new Thread(() -> {
            try {
                socket.startServer(
                    plugin.getVideo().getPort(),
                    ip,
                    plugin.getVideo().getStartBytes(),
                    (byte[] data) -> {
                        plugin.getVideo().pushFrame(data);
                    }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        })
            .start();

        lbase
            .sceneProperty()
            .addListener((obs, oldScene, newScene) -> {
                if (oldScene != null && newScene == null) {
                    running = false;
                    socket.stopServer();
                }
            });
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

        plugin.getCommand().setParameter(Movement.ROLL, (int) dx);
        plugin.getCommand().setParameter(Movement.PITCH, -(int) dy);
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

        plugin.getCommand().setParameter(Movement.YAW, (int) dx);
        plugin.getCommand().setParameter(Movement.THROTTLE, -(int) dy);
    }

    @FXML
    public void onMouseReleased(MouseEvent e) {
        rknob.setLayoutX(centerX);
        rknob.setLayoutY(centerY);
        lknob.setLayoutX(centerX);
        lknob.setLayoutY(centerY);

        // resetar todos os eixos
        plugin.getCommand().setParameter(Movement.THROTTLE, 0);
        plugin.getCommand().setParameter(Movement.YAW, 0);
        plugin.getCommand().setParameter(Movement.PITCH, 0);
        plugin.getCommand().setParameter(Movement.ROLL, 0);

        try {
            socket.sendPacket(
                plugin.getCommand().getPacket(),
                ip,
                plugin.getCommand().getPort()
            );
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @FXML
    public void onModelSelect() {
        setIp();

        String selected = modelSelect.getValue();
        for (Plugin p : manager.plugins) {
            if (p.getName().equals(selected)) {
                this.plugin = p;
                command = plugin.getCommand().getActions()[0];

                commandSelect.getItems().clear();
                for (Action c : p.getCommand().getActions()) {
                    commandSelect.getItems().add(c.getName());
                }
            }
        }
    }

    @FXML
    public void onCommandSelect() {
        String selected = commandSelect.getValue();
        for (Action c : plugin.getCommand().getActions()) {
            if (c.getName().equals(selected)) {
                this.command = c;
            }
        }
    }

    @FXML
    public void onSendCommand() {
        try {
            byte[] pkt = command.getPacket();
            socket.sendPacket(pkt, ip, plugin.getCommand().getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setIp() {
        try {
            Process process = Runtime.getRuntime().exec(
                new String[] { "bash", "-c", "ip route show default " }
            );
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
            );

            ip = reader.readLine().trim().split("\\s+")[2];
        } catch (Exception e) {}
    }
}
