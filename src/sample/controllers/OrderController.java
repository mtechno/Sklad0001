package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.db.Connect;
import sample.impls.CollectionSklad;
import sample.objects.Order;
import sample.objects.OrderedProduct;
import sample.objects.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OrderController {
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

    Parent parentAddOrder;
    private FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/OrderedProduct.fxml"));
    private Order order;
    private CollectionSklad collectionSklad;
    private Stage stageDialogOrder;
    private Stage stageDialogOrderedProduct;
    private boolean isEdit = false;
    private OrderedProductController orderedProductController;

    //=====================INIT WINDOW===============================================
    public void setListeners() {//слушатель при каждом показе окна
        stageDialogOrder.setOnShowing(new EventHandler<WindowEvent>() { //показали окно
            @Override
            public void handle(WindowEvent windowEvent) {

            }
        });
    }

    public void initOnShown() throws IOException { //инициализация при каждом показе окошка
        initializeTableAllProducts();
        initializeTableSelectedProducts();
        initListeners();
        System.out.println(parentAddOrder + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (parentAddOrder == null) {
            initLoaderAddAmount();
        }


    }

    //=====================INIT LOADERS===============================================
    private void initLoaderAddAmount() throws IOException {
        parentAddOrder = (Parent) fxmlLoader.load();
        orderedProductController = fxmlLoader.getController();
        orderedProductController.setCollectionSklad(collectionSklad);
    }

    //=====================INIT TABLES===============================================
    private void initializeTableSelectedProducts() {
        tableSelectedProductsAddOrder.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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

    //=====================INIT LISTENERS===============================================
    private void initListeners() {
        tableSelectedProductsAddOrder.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) { //слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount() == 2 && tableSelectedProductsAddOrder.getSelectionModel().getSelectedItem() != null) {
                    orderedProductController.setOrderedProduct(tableSelectedProductsAddOrder.getSelectionModel().getSelectedItem());
                    showDlgOrderedProduct();
                }
            }
        });
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
                        Connect.deleteDB(orderedProduct, order.getOrderNumber());
                    }
                }
                break;
            case "butClear"://кнопка Очистить все

                if (tableSelectedProductsAddOrder.getItems() != null) {
                    Connect.deleteDB(order.getOrderNumber()); //удалили все товары для данного заказа в БД
                    order.clearOrderedProductList();
                    System.out.println("Нажата кнопка Clear. Очищен список продуктов у заказа.");
                }
                break;
            case "butSelect"://кнопка Перенос выделенного в таблицу справа
                //TODO перенести сюда запись заказанных продуктов в БД, потому что при редактировании заказа не
                // выходит добавлять новый продукт, учитывается только смена количества существующих в заказе
                if (tableAllProductsAddOrder.getItems().size() > 0 && tableAllProductsAddOrder.getSelectionModel().getSelectedItems().size() > 0) {
                    //бежим по списку выделенных продуктов из таблицы слева
                    //добавляем к заказу список объектов orderedProduct
                    for (Product product : tableAllProductsAddOrder.getSelectionModel().getSelectedItems()) {
                        order.addOrderedProductToList(new OrderedProduct(1, product, 0));
                    }
                    //отображаем список в таблице справа
                    tableSelectedProductsAddOrder.setItems(order.getOrderedProductList());

//                    Connect.writeDBOrderedProductsList(order); //пишем в БД список продуктов у заказа
//                    for (OrderedProduct orderedProduct : order.getOrderedProductList()) {
//                        //берем из БД айдишники и кидаем их в объекты
//                        orderedProduct.setId(Connect.readLastIdDbOrderedProducts(order));
//                    }
                    System.out.println("Нажата кнопка Select. У заказа список продуктов=" + order.getOrderedProductList());
                }
                break;
        }
    }

    public void actionClose(ActionEvent actionEvent) {//НАЖАТА ОТМЕНА
        Node source = (Node) actionEvent.getSource(); //узнаем нажатый компонент
        Stage stage = (Stage) source.getScene().getWindow(); //у него узнаем сцену, у сцены - окно
        collectionSklad.deleteProductSelectedListForOrder();//удалили из списка выбранные элементы.
        stage.hide(); //прячем окно
    }

    public void actionSave(ActionEvent actionEvent) { //нажата ОК
        if (tableSelectedProductsAddOrder.getItems().size() == 0) {
            System.out.println("Нужно добавить товары к заказу!");
        } else {
            if (!isEdit) {
                Date date = new Date();
                Format format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                order.setDate(format.format(date));
            }
            actionClose(actionEvent);
        }
    }

    //===============================================================================
    public void showDlgOrderedProduct() {
        if (stageDialogOrderedProduct == null) {
            stageDialogOrderedProduct = new Stage();
            stageDialogOrderedProduct.setTitle("Изменение количества товара");
            stageDialogOrderedProduct.setResizable(false);
            stageDialogOrderedProduct.setScene(new Scene(parentAddOrder));
            stageDialogOrderedProduct.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialogOrderedProduct.initOwner(stageDialogOrder);//указали владельца окна
            System.out.println("иниц диалога доб количества");
        }
        stageDialogOrderedProduct.showAndWait(); //ждем закрытия диалога
    }

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
