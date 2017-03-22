package sample.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.*;
import sample.db.Connect;
import sample.impls.CollectionSklad;
import sample.objects.Order;
import sample.objects.OrderedProduct;
import sample.objects.Product;
import sample.objects.Supplier;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

public class MainController {
    @FXML
    Button but1;
    @FXML
    javafx.scene.control.TextField logTextField;
    @FXML
    Label labelWelcome;
    @FXML
    MenuItem menuAllProducts;
    @FXML
    TableView<Product> tableProducts;
    @FXML
    TableColumn<Product, Integer> idColumn;
    @FXML
    TableColumn<Product, String> nameColumn;
    @FXML
    TableColumn<Product, String> amountColumn;
    @FXML
    TableColumn<Product, Supplier> supplierColumn;

    @FXML
    TableColumn<Product, String> storageColumn;

    @FXML
    TableView<Supplier> tableSuppliers;
    @FXML
    TableColumn<Supplier, Integer> idColumnS;
    @FXML
    TableColumn<Supplier, String> nameColumnS;
    @FXML
    TableColumn<Supplier, String> addressColumnS;
    @FXML
    TableColumn<Supplier, String> telColumnS;


    @FXML
    TableView<Order> tableOrders;
    @FXML
    TableColumn<Order, Integer> idColumnO;
    @FXML
    TableColumn<Order, Date> dateColumnO;

    @FXML
    Button butAddProd;
    @FXML
    Button butUpProd;
    @FXML
    Button butDelProd;
    @FXML
    Label labelCountProducts;
    @FXML
    Label labelCountSuppliers;
    @FXML
    Label labelCountOrders;
    @FXML
    TabPane tabPane;
    @FXML
    Tab tabProducts;



    String userName;


    private CollectionSklad collectionSklad = new CollectionSklad();
    private Stage stageMain;

    private Parent parentDialog;
    private FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/Product.fxml")); //обязательно иниц контроллер диалога тут!
    private ProductController productController;
    private Stage stageDialog;

    private Parent parentDialogSup;
    private FXMLLoader fxmlLoaderSup = new FXMLLoader(getClass().getResource("../fxml/Supplier.fxml")); //обязательно иниц контроллер диалога тут!
    private SupplierController supplierController;
    private Stage stageDialogSup;

    private Parent parentDialogOrder;
    private FXMLLoader fxmlLoaderOrder = new FXMLLoader(getClass().getResource("../fxml/Order.fxml"));
    private OrderController orderController;
    private Stage stageDialogOrder;

    @FXML
    public void initialize() throws IOException, SQLException {//ИНИЦ КОНТРОЛЛЕРА
        initializeTableProd(); //иниц таблицы продуктов
        initializeTableSup();//иниц таблицы поставщиков
        initializeTableOrder();//иниц таблицы заказов
        initListeners();//иниц слушателей
        collectionSklad.fillInitFromDb();//заполнение коллекции данными из БД
        initLoaderProd();//Инициализация диалогового окна продуктов
        initLoaderSup();//Инициализация диалогового окна поставщиков
        initLoaderOrder();//Иниц ДО Заказа
    }

    public void initOnShow(){
        labelWelcome.setText("Здравствуйте, " + userName);
    }

    //=====================INIT TABLES===============================================
    @FXML
    private void initializeTableProd() { //ТАБЛИЦА ПРОДУКТЫ
        tableProducts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //можно выбрать только 1 строку
        idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("amount"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<Product, Supplier>("supplier"));
        storageColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("storage"));
        tableProducts.setItems(collectionSklad.getProductArrayList()); //заполнили таблицу данными из коллекции
        updatelabelCountProducts(); //указываем Надпись "Количество записей"
    }

    private void initializeTableOrder() {
        tableOrders.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        idColumnO.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderNumber"));
        dateColumnO.setCellValueFactory(new PropertyValueFactory<Order, Date>("date"));
        tableOrders.setItems(collectionSklad.getOrdersArrayList());
        updatelabelCountOrders();
    }

    private void initializeTableSup() {
        tableSuppliers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //можно выбрать только 1 строку
        idColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        nameColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, String>("company"));
        addressColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        telColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, String>("phone"));
        tableSuppliers.setItems(collectionSklad.getSuppliersArrayList()); //заполнили таблицу данными из коллекции
        updatelabelCountSuppliers();
    }

    //=====================INIT LOADERS===============================================
    private void initLoaderProd() throws IOException {
        parentDialog = (Parent) fxmlLoader.load();
        productController = fxmlLoader.getController();
        productController.setCollectionSklad(collectionSklad);
    }

    private void initLoaderSup() throws IOException {
        parentDialogSup = (Parent) fxmlLoaderSup.load();
        supplierController = fxmlLoaderSup.getController();
        supplierController.setCollectionSklad(collectionSklad);
    }

    private void initLoaderOrder() throws IOException {
        parentDialogOrder = (Parent) fxmlLoaderOrder.load();
        orderController = fxmlLoaderOrder.getController();
        orderController.setCollectionSklad(collectionSklad);
    }

    //=====================INIT LISTENERS===============================================
    private void initListeners() { //иниц СЛУШАТЕЛЕЙ с самого начала создания объекта контроллера
        collectionSklad.getProductArrayList().addListener(new ListChangeListener<Product>() {//реализация с анонимным классом
            @Override
            public void onChanged(Change<? extends Product> change) {
                MainController.this.updatelabelCountProducts(); //слушатель на изменения коллекции продуктов
                if (productController != null) { //обновляем коллекцию для контроллера окна продуктов
                    productController.setCollectionSklad(collectionSklad);
                    System.out.println(collectionSklad.getProductArrayList() + "listener!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    tableProducts.setItems(collectionSklad.getProductArrayList());
                }
                if (supplierController != null) { //обновляем коллекцию для контроллера окна поставщиков
                    supplierController.setCollectionSklad(collectionSklad);
                }
                if (orderController != null) {//обновляем коллекцию для контроллера окна добавления заказа
                    orderController.setCollectionSklad(collectionSklad);
                }

            }
        });
        collectionSklad.getSuppliersArrayList().addListener(new ListChangeListener<Product>() {//реализация с анонимным классом
            @Override
            public void onChanged(Change<? extends Product> change) {
                MainController.this.updatelabelCountSuppliers(); //слушатель на изменения коллекции продуктов
                if (supplierController != null) { //обновляем коллекцию для контроллера окна поставщиков
                    supplierController.setCollectionSklad(collectionSklad);
                }
            }
        });
        //TODO добавить слушатель на изменения списка заказов, по которому сделать обновление статистической надписи


        tableProducts.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) { //слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount() == 2 && collectionSklad.getProductArrayList().size() > 0 && tableProducts.getSelectionModel().getSelectedItem() != null) {
                    productController.setEdit(true);
                    productController.setProduct(tableProducts.getSelectionModel().getSelectedItem());
                    showDlgProd();
                }
            }
        });

        tableSuppliers.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {//слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount() == 2 && collectionSklad.getSuppliersArrayList().size() > 0 && tableSuppliers.getSelectionModel().getSelectedItem() != null) {
                    supplierController.setEdit(true);
                    supplierController.setSupplier(tableSuppliers.getSelectionModel().getSelectedItem());
                    showDlgSup();
                }
            }
        });
        tableOrders.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) { //слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount() == 2 && collectionSklad.getOrdersArrayList().size() > 0 && tableOrders.getSelectionModel().getSelectedItem() != null) {
                    Order selectedOrder = tableOrders.getSelectionModel().getSelectedItem();
                    orderController.setOrder(selectedOrder); //запомнили/передали выделенную строку таблицы в контроллер
                    orderController.setEdit(true);//передаем, что В режиме редактирования

                    try {
                        showDlgOrder();
                        //сначала удаляем все продукты у заказа, а после добавляем их снова всем скопом
                        Connect.deleteDB(selectedOrder.getOrderNumber());
                        Connect.writeDBOrderedProductsList(selectedOrder); //пишем в БД список продуктов у заказа
                        for (OrderedProduct orderedProduct : selectedOrder.getOrderedProductList()) {
                            //берем из БД айдишники и кидаем их в объекты
                            orderedProduct.setId(Connect.readLastIdDbOrderedProducts(selectedOrder));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //=====================CLICK BUTTONS===============================================
    public void clickBtn(javafx.event.ActionEvent actionEvent) throws SQLException, IOException { //СОЗДАЕМ ДИАЛОГОВОЕ ОКНО ПРОДУКТОВ
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) {//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        String tabText = selectionModel.getSelectedItem().getText();
        System.out.println(tabText);
        switch (tabText) {
            case "Товары":
                Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();//запомнили выделенную строку

                switch (clickedButton.getId()) {
                    case "butAddProd"://кнопка Добавить
                        productController.setProduct(null);//передаем в контроллер диалОкна новый объект продукта
                        productController.setEdit(false);
                        showDlgProd();
                        if (productController.getProduct() != null) {//если не заполнили одно из полей
                            collectionSklad.create(productController.getProduct());//получаем объект и добавляем в коллекцию
                        }
                        break;
                    case "butUpProd"://кнопка Изменить
                        if (collectionSklad.getProductArrayList().size() == 0) { //когда нет записей, нельзя ничего изменить
                            return;
                        }
                        productController.setEdit(true);
                        productController.setProduct(selectedProduct); //запомнили выделенную строку таблицы
                        showDlgProd(); //создание диалогового окна
                        break;
                    case "butDelProd"://кнопка Удалить
                        Connect.deleteDB(selectedProduct);
                        collectionSklad.delete(selectedProduct);
                        break;
                }
                break;
            case "Поставщики":
                clickBtnSup(actionEvent);
                break;
            case "Заказы":
                clickBtnOrder(actionEvent);
        }


    }

    public void clickBtnSup(ActionEvent actionEvent) throws SQLException { //СОЗДАЕМ ОКНО ПОСТАВЩИКОВ
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) {//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        Supplier selectedSupplier = tableSuppliers.getSelectionModel().getSelectedItem();//запомнили выделенную строку

        switch (clickedButton.getId()) {
            case "butAddProd"://кнопка Добавить
                supplierController.setEdit(false);
                supplierController.setSupplier(null);//передаем в контроллер диалОкна новый объект поставщика
                showDlgSup();
                if (supplierController.getSupplier() != null) {//если не заполнили одно из полей
                    collectionSklad.create(supplierController.getSupplier());//получаем объект и добавляем в коллекцию
                }
                break;
            case "butUpProd"://кнопка Изменить
                if (collectionSklad.getSuppliersArrayList().size() == 0) { //когда нет записей, нельзя ничего изменить
                    return;
                }
                supplierController.setEdit(true);
                supplierController.setSupplier(selectedSupplier); //запомнили выделенную строку таблицы
                showDlgSup(); //создание диалогового окна
                break;
            case "butDelProd"://кнопка Удалить
                collectionSklad.delete(selectedSupplier);
                tableProducts.setItems(collectionSklad.getProductArrayList());
                break;
        }
    }

    public void clickBtnOrder(ActionEvent actionEvent) throws SQLException, IOException { //Показываем ОКНО Заказов
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) {//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        Order selectedOrder = tableOrders.getSelectionModel().getSelectedItem();//запомнили выделенную строку
        switch (clickedButton.getId()) {
            case "butAddProd"://кнопка Добавить
                orderController.setOrder(new Order());//передаем в контроллер диалОкна новый объект Заказ
                orderController.setEdit(false);//передаем, что НЕ в режиме редактирования
                showDlgOrder();
                if (orderController.getOrder() != null) {//если заполнили все поля
                    collectionSklad.create(orderController.getOrder());//получаем объект и добавляем в коллекцию
                }
                break;
            case "butUpProd"://кнопка Изменить
                if (collectionSklad.getOrdersArrayList().size() == 0) { //когда нет записей, нельзя ничего изменить
                    return;
                }
                orderController.setOrder(selectedOrder); //запомнили/передали выделенную строку таблицы в контроллер
                orderController.setEdit(true);//передаем, что В режиме редактирования
                try {
                    showDlgOrder();
                    //сначала удаляем все продукты у заказа, а после добавляем их снова всем скопом
                    //TODO при новом добавлении изменяется id ordered_product, тк БД присваивает им новый!
                    Connect.deleteDB(selectedOrder.getOrderNumber());
                    Connect.writeDBOrderedProductsList(selectedOrder); //пишем в БД список продуктов у заказа
                    for (OrderedProduct orderedProduct : selectedOrder.getOrderedProductList()) {
                        //берем из БД айдишники и кидаем их в объекты
                        orderedProduct.setId(Connect.readLastIdDbOrderedProducts(selectedOrder));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "butDelProd"://кнопка Удалить
                collectionSklad.delete(selectedOrder);
                break;
        }
    }

    public void clickBtnRefresh(ActionEvent actionEvent) throws SQLException {
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)) {//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        collectionSklad.clearLists();
        collectionSklad.fillInitFromDb();

    }

    //=====================SHOW DIALOG WINDOW===============================================
    private void showDlgProd() { //Метод либо создает, либо показывает созданное ранее диалоговое окно
        if (stageDialog == null) {
            stageDialog = new Stage();
            stageDialog.setTitle("Изменение записи Товара");
            stageDialog.setResizable(false);
            stageDialog.setScene(new Scene(parentDialog, 600, 400));
            stageDialog.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialog.initOwner(stageMain);
        }
        stageDialog.showAndWait(); //ждем закрытия диалога
    }

    private void showDlgSup() { //Метод либо создает, либо показывает созданное ранее диалоговое окно
        if (stageDialogSup == null) {
            stageDialogSup = new Stage();
            stageDialogSup.setTitle("Изменение записи Поставщика");
            stageDialogSup.setResizable(false);
            stageDialogSup.setScene(new Scene(parentDialogSup, 600, 200));
            stageDialogSup.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialogSup.initOwner(stageMain);
        }
        stageDialogSup.showAndWait(); //ждем закрытия диалога
    }

    private void showDlgOrder() throws IOException { //Метод либо создает, либо показывает созданное ранее диалоговое окно
        if (stageDialogOrder == null) {
            stageDialogOrder = new Stage();
            stageDialogOrder.setTitle("Заказы");
            stageDialogOrder.setResizable(false);
            stageDialogOrder.setScene(new Scene(parentDialogOrder));
            stageDialogOrder.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialogOrder.initOwner(stageMain);//указали владельца окна
            orderController.setStageDialogOrder(stageDialogOrder);
            orderController.setListeners();
        }
        orderController.initOnShown();
        stageDialogOrder.showAndWait(); //ждем закрытия диалога
    }

    //TODO Либо удалить, либо реализовать пункты меню
    public void menuAllProductsSelect() { //НАЖАЛИ МЕНЮ ВСЕ ТОВАРЫ
        System.out.println(collectionSklad.getProductArrayList().toString());
        //initializeTable();
    }

    private void updatelabelCountProducts() {
        labelCountProducts.setText("Количество записей товаров: " + collectionSklad.getProductArrayList().size());
    }

    private void updatelabelCountSuppliers() {
        labelCountSuppliers.setText("Количество Поставщиков: " + collectionSklad.getSuppliersArrayList().size());
    }

    private void updatelabelCountOrders() {
        labelCountOrders.setText("Количество Заказов: " + collectionSklad.getOrdersArrayList().size());
    }

    public void setMainStage(Stage stage) {
        this.stageMain = stage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
