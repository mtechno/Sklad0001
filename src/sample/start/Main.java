package sample.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.Controller;
import sample.db.Connect;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Connect.conn();
        Connect.createTable();
//        Connect.readDB();


        FXMLLoader fxmlLoaderMain = new FXMLLoader(getClass().getResource("../fxml/sample.fxml"));
        Parent root = (Parent) fxmlLoaderMain.load();
        Controller mainController = fxmlLoaderMain.getController();
        mainController.setMainStage(primaryStage); //передаем primaryStage в главный контроллер программы

        primaryStage.setTitle("Система СКЛАД");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
//        testdata();
    }


    public static void main(String[] args) throws IOException {
        launch(args);



    }

//    private void testdata(){
//        CollectionSklad collectionSklad = new CollectionSklad();
//
//        Supplier supplier1 = new Supplier("'ООО Соки и Воды'", "380000, Москва, Тверской проезд, стр. 4", 49512345);
//        Supplier supplier2 = new Supplier("'ООО ИванКо'", "380000, Москва, Тверской проезд, стр. 4/3", 49515);
//
//        Product product1 = new Product("Боржоми", 12, 1, supplier1);
//        Product product2 = new Product("Зефир Бело-Розовый", 100, 2, supplier2);
//
//        collectionSklad.create(supplier1);
//        collectionSklad.create(supplier2);
//        collectionSklad.create(product1);
//        collectionSklad.create(product2);
//
//    }
}
