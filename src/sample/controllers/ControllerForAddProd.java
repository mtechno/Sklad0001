package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
//import sample.other.BDAdapter;
import sample.impls.CollectionSklad;
import sample.objects.Product;
import sample.objects.Supplier;

import java.sql.SQLException;

public class ControllerForAddProd {
    //Переменные, связанные с fxml файлом
    @FXML
    Button butCancelDialog;
    @FXML
    Button butOkDialog;
    @FXML
    TextField txtName;
    @FXML
    TextField txtAmount;
    @FXML
    TextField txtStorage;
    @FXML
    ComboBox<Supplier> comboSupplier;

    private Product product;
    private CollectionSklad collectionSklad;
    private boolean isEdit = false;


    @FXML
    public void initialize() {

    }

    //=====================CLICK BUTTONS===============================================
    public void actionClose(ActionEvent actionEvent) {//НАЖАТА ОТМЕНА
        Node source = (Node) actionEvent.getSource(); //узнаем нажатый компонент
        Stage stage = (Stage) source.getScene().getWindow(); //у него узнаем сцену, у сцены - окно
        stage.hide(); //прячем окно
    }

    public void actionSave(ActionEvent actionEvent) throws SQLException { //нажата ОК
        if (txtName.getText().length() <= 1 || txtAmount.getText().length() == 0 || txtStorage.getText().length() ==
                0 || comboSupplier.getSelectionModel().getSelectedItem()==null) {
            System.out.println("Нужно заполнить все поля!");
        } else {
            product.setName(txtName.getText());
            product.setAmount(txtAmount.getText());
            product.setStorage(txtStorage.getText());
            product.setSupplier(comboSupplier.getSelectionModel().getSelectedItem());
            if (isEdit){
                collectionSklad.update(product); //для изменения записи в БД
            }
            actionClose(actionEvent);
        }
    }

    //===========================================================================
    public void setProduct(Product product) {
        if (product == null) { //добавляем новый продукт
            product = new Product();
            this.product = product;
            txtName.setText(product.getName());
            txtAmount.setText(product.getAmount());
            txtStorage.setText(product.getStorage());
            comboSupplier.setItems(collectionSklad.getSuppliersArrayList());
        } else {
            this.product = product;
            txtName.setText(product.getName());
            txtAmount.setText(product.getAmount());
            txtStorage.setText(product.getStorage());
            comboSupplier.setValue(product.getSupplier());
            System.out.println("product.getSupplier()" + product.getSupplier());
            comboSupplier.setItems(collectionSklad.getSuppliersArrayList());
            System.out.println("collectionSklad.getSuppliersArrayList()" + collectionSklad.getSuppliersArrayList());

        }

    }

    public Product getProduct() {
        if (product.getName() == "" || product.getAmount() == "" || product.getStorage() == "") {
            System.out.println("Нужно заполнить все поля!");
            return null;
        } else return product;
    }

    public void setCollectionSklad(CollectionSklad collectionSklad) {
        this.collectionSklad = collectionSklad;
        System.out.println("сет коллекцион");
    }

    public CollectionSklad getCollectionSklad() {

        return collectionSklad;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

}
