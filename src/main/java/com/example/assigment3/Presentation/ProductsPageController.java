package com.example.assigment3.Presentation;
import com.example.assigment3.DataAccess.ClientDAO;
import com.example.assigment3.DataAccess.ProductDAO;
import com.example.assigment3.Model.Client;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ProductsPageController implements Initializable {

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Client, Integer> idCol;

    @FXML
    private TableColumn<Client, String> nameCol;

    @FXML
    private TableColumn<Client, Integer> quantityCol;

    @FXML
    private TableColumn<Client, Integer> priceCol;

    @FXML
    private TextField name;

    @FXML
    private TextField quantity;

    @FXML
    private TextField price;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productsTable.setEditable(true);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        productsTable.setItems(FXCollections.observableList(new ProductDAO().getAll()));
    }

    @FXML
    public void onEditName(TableColumn.CellEditEvent<Client,String> productStringCellEditEvent){

        Product product = productsTable.getSelectionModel().getSelectedItem();
        product.setName(productStringCellEditEvent.getNewValue());

        new ProductDAO().update(product);
    }

    @FXML
    public void onEditQuantity(TableColumn.CellEditEvent<Client,Integer> productStringCellEditEvent){

        Product product = productsTable.getSelectionModel().getSelectedItem();
        product.setQuantity(productStringCellEditEvent.getNewValue());

        new ProductDAO().update(product);
    }

    @FXML
    public void onEditPrice(TableColumn.CellEditEvent<Client,Integer> productStringCellEditEvent){

        Product product = productsTable.getSelectionModel().getSelectedItem();
        product.setPrice(productStringCellEditEvent.getNewValue());
        new ProductDAO().update(product);
    }

    @FXML
    public void onAddProduct(){

        Product product = new Product(name.getText(),Integer.parseInt(price.getText()),Integer.parseInt(quantity.getText()));
        name.clear();
        quantity.clear();
        price.clear();

        new ProductDAO().insert(product);
        productsTable.setItems(FXCollections.observableList(new ProductDAO().getAll()));

    }

    @FXML
    public void onDelete(){
        ObservableList<Product> singleProduct;
        singleProduct=productsTable.getSelectionModel().getSelectedItems();

        for(Product product: singleProduct){
            new ProductDAO().delete(product.getId());
        }

        productsTable.setItems(FXCollections.observableList(new ProductDAO().getAll()));
    }

    @FXML
    public void onHome() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Warehouse.primaryStage.setScene(scene);
        Warehouse.primaryStage.show();
    }

}
