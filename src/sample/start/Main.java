package sample.start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.LoginController;
import sample.controllers.MainController;
import sample.db.Connect;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Connect.conn();//подключение к БД
        //подгружаем ресурсы JavaFX,

        FXMLLoader fxmlLoaderLogin = new FXMLLoader(getClass().getResource("../fxml/Login.fxml"));
        Parent root = (Parent) fxmlLoaderLogin.load();
        LoginController loginController = fxmlLoaderLogin.getController();
        loginController.setPrimaryStage(primaryStage);
        //иниц Сцены
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, 400, 199));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //=======================================================================================================
    public static void main(String[] args) throws IOException {
        launch(args);
        //TODO провести нормальную обработку исключений
    }
}
