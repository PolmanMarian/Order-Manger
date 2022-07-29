package com.example.assigment3;

import com.example.assigment3.DataAccess.ClientDAO;
import com.example.assigment3.DataAccess.OrderDAO;
import com.example.assigment3.DataAccess.ProductDAO;
import com.example.assigment3.Model.Client;
import com.example.assigment3.Model.Order;
import com.example.assigment3.Model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Warehouse extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Warehouse");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        List<Product> products = new ProductDAO().getAll();
        for(Product product : products){
            System.out.println(product);
        }

        Client client = new ClientDAO().findById(1);
        System.out.println(client);
        Client client2 = new Client(4,"VASILEEEE","Somcuta Mare");
        new ClientDAO().update(client2);
        new ClientDAO().delete(4);
        launch();

    }
}