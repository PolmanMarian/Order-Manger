package com.example.assigment3.Presentation;
import com.example.assigment3.DataAccess.ClientDAO;
import com.example.assigment3.DataAccess.OrderDAO;
import com.example.assigment3.DataAccess.ProductDAO;
import com.example.assigment3.Model.Client;
import com.example.assigment3.Model.Order;
import com.example.assigment3.Model.Product;
import com.example.assigment3.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class OrdersPageController implements Initializable {

    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, Integer> idColClient;
    @FXML
    private TableColumn<Client, String> nameColClient;
    @FXML
    private TableColumn<Client, String> addressColClient;


    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, Integer> idColProduct;
    @FXML
    private TableColumn<Product, String> nameColProduct;
    @FXML
    private TableColumn<Product, Integer> priceColProduct;
    @FXML
    private TableColumn<Product, Integer> quantityColProduct;

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableColumn<Order, Integer> idColOrders;
    @FXML
    private TableColumn<Order, Integer> productColOrders;
    @FXML
    private TableColumn<Order, Integer> clientColOrders;
    @FXML
    private TableColumn<Order, Integer> quantityColOrders;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColClient.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColClient.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColClient.setCellValueFactory(new PropertyValueFactory<>("address"));
        clientsTable.setItems(FXCollections.observableList(new ClientDAO().getAll()));

        idColProduct.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColProduct.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColProduct.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTable.setItems(FXCollections.observableList(new ProductDAO().getAll()));

        ordersTable.setEditable(true);
        idColOrders.setCellValueFactory(new PropertyValueFactory<>("id"));

        clientColOrders.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        productColOrders.setCellValueFactory(new PropertyValueFactory<>("productId"));
        quantityColOrders.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColOrders.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        ordersTable.setItems(FXCollections.observableList(new OrderDAO().getAll()));
    }

    @FXML
    public void onEditQuantity(TableColumn.CellEditEvent<Client,Integer> productStringCellEditEvent){

        Order order = ordersTable.getSelectionModel().getSelectedItem();
        int oldQuantity = order.getQuantity();
        Product product = new ProductDAO().findById(order.getProductId());

        order.setQuantity(productStringCellEditEvent.getNewValue());

        if(oldQuantity != -1)
            product.setQuantity(product.getQuantity()-(order.getQuantity()-oldQuantity));
        else
            product.setQuantity(product.getQuantity()-order.getQuantity());


        if(product.getQuantity() < 0){
            order.setQuantity(-1);
            product.setQuantity(oldQuantity);
        }

        new ProductDAO().update(product);
        new OrderDAO().update(order);

        ordersTable.setItems(FXCollections.observableList(new OrderDAO().getAll()));
        productsTable.setItems(FXCollections.observableList(new ProductDAO().getAll()));


        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("Bills/Bill" + new Random().nextInt()%100000 +".txt");
            fileWriter.write(order.getQuantity()+" x "+ product.getName() + "\n Total:" + order.getQuantity()*product.getPrice() +" RON");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void onAddOrder(){

        ObservableList<Product> singleProduct;
        singleProduct = productsTable.getSelectionModel().getSelectedItems();
        System.out.println(singleProduct.get(0));

        ObservableList<Client> singleClient;
        singleClient = clientsTable.getSelectionModel().getSelectedItems();
        System.out.println(singleClient.get(0));

        Order order = new Order(singleClient.get(0).getId(),singleProduct.get(0).getId(),0);

        new OrderDAO().insert(order);
        ordersTable.setItems(FXCollections.observableList(new OrderDAO().getAll()));

    }

    @FXML
    public void onHome() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Warehouse.primaryStage.setScene(scene);
        Warehouse.primaryStage.show();
    }

}
