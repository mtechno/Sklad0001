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
    public void start(Stage primaryStage) throws Exception {
        Connect.conn();//подключение к БД
        //подгружаем ресурсы JavaFX, инициализируем Controller
        FXMLLoader fxmlLoaderMain = new FXMLLoader(getClass().getResource("../fxml/sample.fxml"));
        Parent root = (Parent) fxmlLoaderMain.load();
        Controller mainController = fxmlLoaderMain.getController();
        mainController.setMainStage(primaryStage); //передаем primaryStage в главный контроллер программы
        //иниц Сцены
        primaryStage.setTitle("Система СКЛАД");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //=======================================================================================================
    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
