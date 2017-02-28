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
import sample.objects.Product;
import sample.objects.Supplier;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
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

    String fioUser = "";

    private CollectionSklad collectionSklad = new CollectionSklad();
    private Stage stageMain;

    private Parent parentDialog;
    private FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/diaAddProd.fxml")); //обязательно иниц контроллер диалога тут!
    private ControllerForAddProd controllerDialog;
    private Stage stageDialog;

    private Parent parentDialogSup;
    private FXMLLoader fxmlLoaderSup = new FXMLLoader(getClass().getResource("../fxml/diaAddSup.fxml")); //обязательно иниц контроллер диалога тут!
    private ControllerForAddSup controllerDialogSup;
    private Stage stageDialogSup;

    private Parent parentDialogOrder;
    private FXMLLoader fxmlLoaderOrder = new FXMLLoader(getClass().getResource("../fxml/diaAddOrder.fxml"));
    private ControllerForAddOrder controllerForAddOrder;
    private Stage stageDialogOrder;

    @FXML
    public void initialize() throws IOException, SQLException {//ИНИЦ КОНТРОЛЛЕРА
        initializeTableProd(); //иниц таблицы продуктов
        initializeTableSup();//иниц таблицы поставщиков
        initializeTableOrder();//иниц таблицы заказов
        initListeners();//иниц слушателей
        collectionSklad.fillInitFromDb();//заполнение коллекции тестовыми данными
        initLoaderProd();//Инициализация диалогового окна продуктов
        initLoaderSup();//Инициализация диалогового окна поставщиков
        initLoaderOrder();//Иниц ДО Заказа

    }

    private void initializeTableOrder() {
        tableOrders.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        idColumnO.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderNumber"));
        dateColumnO.setCellValueFactory(new PropertyValueFactory<Order, Date>("date"));
        tableOrders.setItems(collectionSklad.getOrdersArrayList());
    }

    private void initializeTableSup() {
        tableSuppliers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); //можно выбрать только 1 строку

        idColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        nameColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, String>("company"));
        addressColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
        telColumnS.setCellValueFactory(new PropertyValueFactory<Supplier, String>("phone"));

        tableSuppliers.setItems(collectionSklad.getSuppliersArrayList()); //заполнили таблицу данными из коллекции


    }

    private void initLoaderProd() throws IOException {
        parentDialog = (Parent) fxmlLoader.load();
        controllerDialog =  fxmlLoader.getController();
        controllerDialog.setCollectionSklad(collectionSklad);
    }

    private void initLoaderSup() throws IOException {
        parentDialogSup = (Parent) fxmlLoaderSup.load();
        controllerDialogSup =  fxmlLoaderSup.getController();
        controllerDialogSup.setCollectionSklad(collectionSklad);
    }

    private void initLoaderOrder() throws IOException {
        parentDialogOrder = (Parent) fxmlLoaderOrder.load();
        controllerForAddOrder =  fxmlLoaderOrder.getController();
        controllerForAddOrder.setCollectionSklad(collectionSklad);

    }

    private void updatelabelCountProducts(){
        labelCountProducts.setText("Количество записей товаров: "+ collectionSklad.getProductArrayList().size());
    }

    public void setMainStage(Stage stage){
        this.stageMain = stage;
    }

    public void clickBtn(javafx.event.ActionEvent actionEvent) throws SQLException { //СОЗДАЕМ ДИАЛОГОВОЕ ОКНО ПРОДУКТОВ
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)){//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        Product selectedProduct = tableProducts.getSelectionModel().getSelectedItem();//запомнили выделенную строку

        switch (clickedButton.getId()){
            case "butAddProd" ://кнопка Добавить
                controllerDialog.setProduct(null);//передаем в контроллер диалОкна новый объект продукта
                showDlgProd();
                if (controllerDialog.getProduct()!=null){//если не заполнили одно из полей
                    collectionSklad.create(controllerDialog.getProduct());//получаем объект и добавляем в коллекцию
                }
                break;
            case "butUpProd" ://кнопка Изменить
                if (collectionSklad.getProductArrayList().size()==0){ //когда нет записей, нельзя ничего изменить
                    return;
                }
                controllerDialog.setProduct(selectedProduct); //запомнили выделенную строку таблицы
                showDlgProd(); //создание диалогового окна
                break;
            case "butDelProd" ://кнопка Удалить
                Connect.deleteDB(selectedProduct);
                collectionSklad.delete(selectedProduct);
                break;
        }

    }

    public void clickBtnSup(ActionEvent actionEvent) throws SQLException { //СОЗДАЕМ ОКНО ПОСТАВЩИКОВ
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)){//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        Supplier selectedSupplier = tableSuppliers.getSelectionModel().getSelectedItem();//запомнили выделенную строку

        switch (clickedButton.getId()){
            case "butAddSup" ://кнопка Добавить
                controllerDialogSup.setSupplier(null);//передаем в контроллер диалОкна новый объект поставщика
                showDlgSup();
                if (controllerDialogSup.getSupplier()!=null){//если не заполнили одно из полей
                    collectionSklad.create(controllerDialogSup.getSupplier());//получаем объект и добавляем в коллекцию
                }
                break;
            case "butUpSup" ://кнопка Изменить
                if (collectionSklad.getSuppliersArrayList().size()==0){ //когда нет записей, нельзя ничего изменить
                    return;
                }
                controllerDialogSup.setSupplier(selectedSupplier); //запомнили выделенную строку таблицы
                showDlgSup(); //создание диалогового окна
//                tableProducts.setItems(collectionSklad.getProductArrayList());
//                System.out.println(collectionSklad.getProductArrayList());
                break;
            case "butDelSup" ://кнопка Удалить
                Connect.deleteDB(selectedSupplier);
                collectionSklad.delete(selectedSupplier);
                tableProducts.setItems(collectionSklad.getProductArrayList());
                break;
        }


    }

    public void clickBtnOrder(ActionEvent actionEvent) throws SQLException { //Показываем ОКНО Заказов
        Object source = actionEvent.getSource();
        if (!(source instanceof Button)){//если нажата не кнопка, то выходим из метода
            return;
        }
        Button clickedButton = (Button) source;
        Order selectedOrder = tableOrders.getSelectionModel().getSelectedItem();//запомнили выделенную строку
        switch (clickedButton.getId()){
            case "butAddOrder" ://кнопка Добавить
                controllerForAddOrder.setOrder(new Order());//передаем в контроллер диалОкна новый объект Заказ
                controllerForAddOrder.setEdit(false);//передаем, что НЕ в режиме редактирования
                showDlgOrder();
                if (controllerForAddOrder.getOrder()!=null){//если заполнили все поля
                    collectionSklad.create(controllerForAddOrder.getOrder());//получаем объект и добавляем в коллекцию
                }
                break;
            case "butUpOrder" ://кнопка Изменить
                if (collectionSklad.getOrdersArrayList().size()==0){ //когда нет записей, нельзя ничего изменить
                    return;
                }
                controllerForAddOrder.setOrder(selectedOrder); //запомнили/передали выделенную строку таблицы в контроллер
                controllerForAddOrder.setEdit(true);//передаем, что В режиме редактирования
                showDlgOrder(); //создание диалогового окна
                break;
            case "butDelOrder" ://кнопка Удалить
                Connect.deleteDB(selectedOrder);
                collectionSklad.delete(selectedOrder);
                break;
        }


    }

//Переделать
    private void initListeners(){ //иниц СЛУШАТЕЛЕЙ с самого начала создания объекта контроллера
        collectionSklad.getProductArrayList().addListener(new ListChangeListener<Product>() {//реализация с анонимным классом
            @Override
            public void onChanged(Change<? extends Product> change) {
                Controller.this.updatelabelCountProducts(); //слушатель на изменения коллекции продуктов
                if (controllerDialog!=null){ //обновляем коллекцию для контроллера окна продуктов
                    controllerDialog.setCollectionSklad(collectionSklad);
                    System.out.println(collectionSklad.getProductArrayList()+"listener!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    tableProducts.setItems(collectionSklad.getProductArrayList());
                }
                if (controllerDialogSup!=null){ //обновляем коллекцию для контроллера окна поставщиков
                    controllerDialogSup.setCollectionSklad(collectionSklad);
                }
                if (controllerForAddOrder!=null){//обновляем коллекцию для контроллера окна добавления заказа
                    controllerForAddOrder.setCollectionSklad(collectionSklad);
                }

            }
        });

        tableProducts.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) { //слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount()==2&&collectionSklad.getProductArrayList().size()>0&&tableProducts.getSelectionModel().getSelectedItem()!=null){
                    controllerDialog.setProduct(tableProducts.getSelectionModel().getSelectedItem());
                    showDlgProd();
                }
            }
        });

        tableSuppliers.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {//слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount()==2&&collectionSklad.getSuppliersArrayList().size()>0&&tableSuppliers.getSelectionModel().getSelectedItem()!=null){
                    controllerDialogSup.setSupplier(tableSuppliers.getSelectionModel().getSelectedItem());
                    showDlgSup();
                }
            }
        });
        tableOrders.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() { //если щелкнули мышкой на таблице
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) { //слушатель двойного нажатия на строку таблицы, вызывает диалог редактирования
                if (mouseEvent.getClickCount()==2&&collectionSklad.getOrdersArrayList().size()>0&&tableOrders.getSelectionModel().getSelectedItem()!=null){
                    controllerForAddOrder.setOrder(tableOrders.getSelectionModel().getSelectedItem());
                    controllerForAddOrder.setEdit(true);//передаем, что В режиме редактирования
                    showDlgOrder();
                }
            }
        });


    }

    private void showDlgProd(){ //Метод либо создает, либо показывает созданное ранее диалоговое окно
        if (stageDialog==null){
            stageDialog = new Stage();
            stageDialog.setTitle("Изменение записи Товара");
            stageDialog.setResizable(false);
            stageDialog.setScene(new Scene(parentDialog, 600, 400));
            stageDialog.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialog.initOwner(stageMain);

        }

        stageDialog.showAndWait(); //ждем закрытия диалога

    }

    private void showDlgSup(){ //Метод либо создает, либо показывает созданное ранее диалоговое окно
        if (stageDialogSup==null){
            stageDialogSup = new Stage();
            stageDialogSup.setTitle("Изменение записи Поставщика");
            stageDialogSup.setResizable(false);
            stageDialogSup.setScene(new Scene(parentDialogSup, 600, 200));
            stageDialogSup.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialogSup.initOwner(stageMain);
        }
        stageDialogSup.showAndWait(); //ждем закрытия диалога

    }

    private void showDlgOrder(){ //Метод либо создает, либо показывает созданное ранее диалоговое окно
        if (stageDialogOrder==null){
            stageDialogOrder = new Stage();
            stageDialogOrder.setTitle("Заказы");
            stageDialogOrder.setResizable(false);
            stageDialogOrder.setScene(new Scene(parentDialogOrder));
            stageDialogOrder.initModality(Modality.WINDOW_MODAL); //модальность окна
            stageDialogOrder.initOwner(stageMain);//указали владельца окна
            controllerForAddOrder.setStageDialogOrder(stageDialogOrder);
            controllerForAddOrder.setListeners();
        }
        controllerForAddOrder.initOnShown();
        stageDialogOrder.showAndWait(); //ждем закрытия диалога

    }

    public void clickLogIn() { //НАЖАЛИ КНОПКУ LOG IN
        fioUser = logTextField.getText();
        logTextField.setVisible(false);
        but1.setVisible(false);
        labelWelcome.setText("Здравствуйте, " + fioUser);
    }

    public void menuAllProductsSelect() { //НАЖАЛИ МЕНЮ ВСЕ ТОВАРЫ
        System.out.println(collectionSklad.getProductArrayList().toString());
        //initializeTable();

    }

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
}
