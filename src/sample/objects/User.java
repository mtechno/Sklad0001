package sample.objects;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class User {
    private SimpleStringProperty fio = new SimpleStringProperty("");
    private SimpleStringProperty password = new SimpleStringProperty("");

    //=========================================================================================
    public User(String fio) {
        this.fio = new SimpleStringProperty(fio);
    }

    //==================================fio===================================================
    public String getFio() {
        return fio.get();
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public SimpleStringProperty fioProperty() {
        return fio;
    }

    //==================================password===================================================
    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    //=========================================================================================
    @Override
    public String toString() {
        return "User{" +
                "fio='" + fio + '\'' +
                '}';
    }
}
