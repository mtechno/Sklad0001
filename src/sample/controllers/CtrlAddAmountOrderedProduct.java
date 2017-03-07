package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.impls.CollectionSklad;
import sample.objects.Order;
import sample.objects.OrderedProduct;
import sample.objects.Product;

import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CtrlAddAmountOrderedProduct {
    @FXML
    Button butCancelDialog;
    @FXML
    Button butOkDialog;
    @FXML
    TextField txtAmountProduct;
    @FXML
    Label lbNameProduct;

    private OrderedProduct orderedProduct;
    private CollectionSklad collectionSklad;


    @FXML
    public void initialize() {

    }

    //=====================CLICK BUTTONS===============================================
    public void actionClose(ActionEvent actionEvent) {//НАЖАТА ОТМЕНА
        Node source = (Node) actionEvent.getSource(); //узнаем нажатый компонент
        Stage stage = (Stage) source.getScene().getWindow(); //у него узнаем сцену, у сцены - окно
        stage.hide(); //прячем окно
    }

    public void actionSave(ActionEvent actionEvent) { //нажата ОК
        orderedProduct.setAmount(Integer.parseInt(txtAmountProduct.getText()));
//        collectionSklad.update(orderedProduct);//для изменения записи в БД
        actionClose(actionEvent);
    }

    //=========================================================================
    public void setOrderedProduct(OrderedProduct orderedProduct) {
        this.orderedProduct = orderedProduct;
        lbNameProduct.setText(orderedProduct.getOrderedProduct().getName());
        txtAmountProduct.setText(""+orderedProduct.getAmount());
    }


    public OrderedProduct getOrderedProduct() {
         return orderedProduct;
    }

    public void setCollectionSklad(CollectionSklad collectionSklad) {
        this.collectionSklad = collectionSklad;
        System.out.println("сет коллекцион");
    }


}
