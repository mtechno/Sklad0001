package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.db.Connect;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Note-001 on 22.03.2017.
 */
public class LoginController {
    @FXML
    TextField txtUsername;
    @FXML
    TextField txtPassword;
    @FXML
    Button butLogin;
    @FXML
    Label lblIsLogin;

    public MainController mainController;
    public Stage primaryStage;
    public String username;

    public void clickBtnLogin(ActionEvent actionEvent) throws InterruptedException, IOException {
        try {
            if (isLogin(txtUsername.getText(), txtPassword.getText())) {
                username = txtUsername.getText();
                primaryStage.hide();
                initLoaderMain();
            } else {
                lblIsLogin.setText("Некорректная пара логин/пароль!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isLogin(String userName, String password) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from users where name = ? and password = ?";
        try {
            preparedStatement = Connect.connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
    }

    public void initLoaderMain() throws IOException {
        FXMLLoader fxmlLoaderMain = new FXMLLoader(getClass().getResource("../fxml/Main.fxml"));
        Parent root = (Parent) fxmlLoaderMain.load();
        mainController = fxmlLoaderMain.getController();
        mainController.setMainStage(primaryStage); //передаем primaryStage в главный контроллер программы
        mainController.setUserName(username);
        System.out.println(username);
        mainController.initOnShow();
        //иниц Сцены
        primaryStage.setTitle("Система СКЛАД");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
