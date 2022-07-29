package com.example.assigment3.Presentation;
import com.example.assigment3.DataAccess.ClientDAO;
import com.example.assigment3.Model.Client;
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
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ClientsPageController implements Initializable {

    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, Integer> idCol;
    @FXML
    private TableColumn<Client, String> nameCol;
    @FXML
    private TableColumn<Client, String> addressCol;
    @FXML
    private TextField name;
    @FXML
    private TextField address;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientsTable.setEditable(true);

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());

        clientsTable.setItems(FXCollections.observableList(new ClientDAO().getAll()));

    }
    @FXML
    public void onEditName(TableColumn.CellEditEvent<Client,String> clientStringCellEditEvent){

        Client client = clientsTable.getSelectionModel().getSelectedItem();
        client.setName(clientStringCellEditEvent.getNewValue());

        new ClientDAO().update(client);
    }
    @FXML
    public void onEditAddress(TableColumn.CellEditEvent<Client,String> clientStringCellEditEvent){

        Client client = clientsTable.getSelectionModel().getSelectedItem();
        client.setAddress(clientStringCellEditEvent.getNewValue());

        new ClientDAO().update(client);
    }
    @FXML
    public void onAddClient(){

        Client client = new Client(name.getText(),address.getText());
        name.clear();
        address.clear();

        new ClientDAO().insert(client);
        clientsTable.setItems(FXCollections.observableList(new ClientDAO().getAll()));

    }
    @FXML
    public void onDelete(){
        ObservableList<Client> singleClient;
        singleClient=clientsTable.getSelectionModel().getSelectedItems();

        for(Client client: singleClient){
            new ClientDAO().delete(client.getId());
        }

        clientsTable.setItems(FXCollections.observableList(new ClientDAO().getAll()));
    }

    @FXML
    public void onHome() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Warehouse.class.getResource("home-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Warehouse.primaryStage.setScene(scene);
        Warehouse.primaryStage.show();
    }
}
