package com.example.assigment3.Presentation;
import com.example.assigment3.Warehouse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class HomePageController {

    @FXML
    public void onClientsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("clients.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Warehouse.primaryStage.setScene(scene);
        Warehouse.primaryStage.show();
    }

    @FXML
    public void onProductsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("products.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Warehouse.primaryStage.setScene(scene);
        Warehouse.primaryStage.show();
    }

    @FXML
    public void onOrdersClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("orders.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Warehouse.primaryStage.setScene(scene);
        Warehouse.primaryStage.show();
    }

}
