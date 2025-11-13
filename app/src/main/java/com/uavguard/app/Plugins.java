package com.uavguard.app;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Plugins {

    @FXML
    private VBox result;

    @FXML
    public void initialize() {
        loadItems();
    }

    private void loadItems() {
        try {
            URL url = new URL(
                "https://raw.githubusercontent.com/PSalleSDev/UAVGuard-Plugins/main/plugins.json"
            );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                System.err.println(
                    "Erro ao buscar plugins: " + conn.getResponseCode()
                );
                return;
            }

            InputStreamReader reader = new InputStreamReader(
                conn.getInputStream()
            );
            Type listType = new TypeToken<List<Item>>() {}.getType();
            List<Item> items = new Gson().fromJson(reader, listType);

            reader.close();
            conn.disconnect();

            for (Item itemData : items) {
                GridPane itemGrid = new GridPane();
                itemGrid.setId("item");

                for (int i = 0; i < 4; i++) {
                    ColumnConstraints col = new ColumnConstraints();
                    col.setPercentWidth(25);
                    itemGrid.getColumnConstraints().add(col);
                }

                Label nameLabel = new Label(itemData.model);
                HBox nameBox = createHBox(nameLabel);
                itemGrid.add(nameBox, 0, 0);

                Label versionLabel = new Label(itemData.version);
                HBox versionBox = createHBox(versionLabel);
                itemGrid.add(versionBox, 1, 0);

                Label statusLabel = new Label(
                    itemData.installed ? "Installed" : "Not installed"
                );

                HBox statusBox = createHBox(statusLabel);
                itemGrid.add(statusBox, 2, 0);

                Button btn = new Button();
                setButtonGraphic(btn, itemData.installed);
                btn.setOnAction(e ->
                    toggleInstall(itemData, btn, statusLabel, statusBox)
                );
                HBox buttonBox = createHBox(btn);
                itemGrid.add(buttonBox, 3, 0);

                result.getChildren().add(itemGrid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HBox createHBox(javafx.scene.Node node) {
        HBox box = new HBox(node);
        box.setAlignment(Pos.CENTER);
        HBox.setHgrow(node, javafx.scene.layout.Priority.ALWAYS);
        return box;
    }

    private void setButtonGraphic(Button btn, boolean installed) {
        try {
            String path = installed ? "icons/remove.xml" : "icons/install.xml";
            javafx.scene.Node graphic = javafx.fxml.FXMLLoader.load(
                getClass().getResource(path)
            );
            btn.setGraphic(graphic);
        } catch (Exception e) {
            e.printStackTrace();
            btn.setText(installed ? "Remove" : "Install"); // fallback
        }
    }

    private void toggleInstall(
        Item item,
        Button btn,
        Label statusLabel,
        HBox statusBox
    ) {
        item.installed = !item.installed;
        statusLabel.setText(item.installed ? "Installed" : "Not installed");
        setButtonGraphic(btn, item.installed);
    }
}
