package sample.controllers;

import javafx.beans.property.SimpleStringProperty;
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


public class ControllerForAddOrder {
    //Переменные, связанные с fxml файлом
    @FXML
    Button butCancelDialogAddOrder;
    @FXML
    Button butOkDialogAddOrder;
    @FXML
    Button butClearDialogAddOrder;
    @FXML
    Button butSelectDialogAddOrder;
    @FXML
    TableView<Product> tableAllProductsAddOrder;
    @FXML
    TableView<OrderedProduct> tableSelectedProductsAddOrder;
    @FXML
    private TableColumn<Product, String> nameColumnAll;
    @FXML
    private TableColumn<OrderedProduct, Product> nameColumnSelected;
    @FXML
    private TableColumn<OrderedProduct, String> amountColumnSelected;
    @FXML
    private TableColumn<Product, String> amountColumnAll;

    private Order order;
    private CollectionSklad collectionSklad;
    private Stage stageDialogOrder;
    private boolean isEdit = false;

    //=====================INIT WINDOW===============================================
    public void setListeners() {//слушатель при каждом показе окна
        stageDialogOrder.setOnShowing(new EventHandler<WindowEvent>() { //показали окно
            @Override
            public void handle(WindowEvent windowEvent) {

            }
        });
    }

    public void initOnShown() { //инициализация при каждом показе окошка
        initializeTableAllProducts();
        initializeTableSelectedProducts();
    }

    //=====================INIT TABLES===============================================
    private void initializeTableSelectedProducts() {
        tableSelectedProductsAddOrder.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        nameColumnSelected.setCellValueFactory(new PropertyValueFactory<OrderedProduct, Product>("orderedProduct"));
        amountColumnSelected.setCellValueFactory(new PropertyValueFactory<OrderedProduct, String>("amount"));
        tableSelectedProductsAddOrder.setItems(order.getOrderedProductList());
        System.out.println(order.getOrderedProductList());
    }

    private void initializeTableAllProducts() {
        tableAllProductsAddOrder.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        nameColumnAll.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        amountColumnAll.setCellValueFactory(new PropertyValueFactory<Product, String>("amount"));
        tableAllProductsAddOrder.setItems(collectionSklad.getProductArrayList());
    }

    //=====================CLICK BUTTONS===============================================
    public void clickBtn(ActionEvent actionEvent) throws SQLException {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) {//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;

        System.out.println("кликбтн");
        switch (clickedButton.getId()) {
            case "butDel"://кнопка Удалить
                System.out.println(tableSelectedProductsAddOrder.getItems());
                if (tableSelectedProductsAddOrder.getItems() != null) {
                    //удаляем выделенные элементы
                    for (OrderedProduct orderedProduct : tableSelectedProductsAddOrder.getSelectionModel().getSelectedItems()) {
                        order.removeOrderedProductFromList(orderedProduct);
                    }
                }
                break;
            case "butClear"://кнопка Очистить все
                if (tableSelectedProductsAddOrder.getItems() != null) {
                    order.clearOrderedProductList();
                    System.out.println("Нажата кнопка Clear. Очищен список продуктов у заказа.");
                }
                break;
            case "butSelect"://кнопка Перенос выделенного в таблицу справа
                if (tableAllProductsAddOrder.getItems() != null && tableAllProductsAddOrder.getSelectionModel().getSelectedItems() != null) {
                    //бежим по списку выделенных продуктов из таблицы слева
                    //добавляем к заказу список объектов orderedProduct
                    //TODO после нажатия кнопки SAVE вытащить из БД id и присвоить его объекту orderedProduct
                    //TODO реализовать изменение количества товаров
                    for (Product product : tableAllProductsAddOrder.getSelectionModel().getSelectedItems()) {
                        order.addOrderedProductToList(new OrderedProduct(1, product, 0));
                    }
                    //отображаем список в таблице справа
                    tableSelectedProductsAddOrder.setItems(order.getOrderedProductList());

                    System.out.println("Нажата кнопка Select. У заказа список продуктов=" + order.getOrderedProductList());
                }
                break;
        }
    }

    public void actionClose(ActionEvent actionEvent) {//НАЖАТА ОТМЕНА
        Node source = (Node) actionEvent.getSource(); //узнаем нажатый компонент
        Stage stage = (Stage) source.getScene().getWindow(); //у него узнаем сцену, у сцены - окно
        order = null;
        collectionSklad.deleteProductSelectedListForOrder();//удалили из списка выбранные элементы.
        stage.hide(); //прячем окно
    }

    public void actionSave(ActionEvent actionEvent) { //нажата ОК

        //сохраняем в БД список
        //TODO Реализовать запись в БД списка заказанных товаров у заказа

        System.out.println(new Date(Calendar.getInstance().getTimeInMillis()).toString());

        Date date = new Date();
        Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        System.out.println(format.format(date));

        order.setDate(format.format(date));
//        order.setProductList(collectionSklad.getProductSelectedListForOrder());
        collectionSklad.update(order);//для изменения записи в БД

        Node source = (Node) actionEvent.getSource(); //узнаем нажатый компонент
        Stage stage = (Stage) source.getScene().getWindow(); //у него узнаем сцену, у сцены - окно
        stage.hide(); //прячем окно
        if (tableSelectedProductsAddOrder != null) {
            collectionSklad.deleteProductSelectedListForOrder();//удалили из списка выбранные элементы.
        }

    }

    //===============================================================================
    public void setCollectionSklad(CollectionSklad collectionSklad) {
        this.collectionSklad = collectionSklad;
        System.out.println("сет коллекцион эдд ордер");
    }

    public CollectionSklad getCollectionSklad() {

        return collectionSklad;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (order == null) {
            order = new Order();
        }
        this.order = order;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public void setStageDialogOrder(Stage stageDialogOrder) {
        System.out.println("сет стейдж Ордер ДО");
        this.stageDialogOrder = stageDialogOrder;
    }

    @FXML
    public void initialize() {

    }
}
