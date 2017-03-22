package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.impls.CollectionSklad;
import sample.objects.Supplier;


public class SupplierController {
    @FXML
    Button butCancelDialog;
    @FXML
    Button butOkDialog;
    @FXML
    TextField txtCompany;
    @FXML
    TextField txtAddress;
    @FXML
    TextField txtPhone;

    private Supplier supplier;
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

    public void actionSave(ActionEvent actionEvent) { //нажата ОК
        if (txtCompany.getText().length()<=1 || txtAddress.getText().length()<=1 || txtPhone.getText().length()<=1) {
            System.out.println("Нужно заполнить все поля!");
        } else {
            supplier.setCompany(txtCompany.getText());
            supplier.setAddress(txtAddress.getText());
            supplier.setPhone(txtPhone.getText());
            if (isEdit){
                collectionSklad.update(supplier);//для изменения записи в БД
            }
            actionClose(actionEvent);
        }

    }

    //=========================================================================
    public void setSupplier(Supplier supplier) {
        if (supplier == null) { //добавляем нового поставщика
            supplier = new Supplier();
            this.supplier = supplier;
            txtCompany.setText(supplier.getCompany());
            txtAddress.setText(supplier.getAddress());
            txtPhone.setText(supplier.getPhone());
        }
        this.supplier = supplier;
        txtCompany.setText(supplier.getCompany());
        txtAddress.setText(supplier.getAddress());
        txtPhone.setText(supplier.getPhone());
    }


    public Supplier getSupplier() {
        return supplier;
    }

    public void setCollectionSklad(CollectionSklad collectionSklad) {
        this.collectionSklad = collectionSklad;
        System.out.println("сет коллекцион");
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }
}
