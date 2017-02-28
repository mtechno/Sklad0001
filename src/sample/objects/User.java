package sample.objects;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class User{
    private SimpleStringProperty fio;


    public User(String fio) {
        this.fio = new SimpleStringProperty(fio);

    }


    public String getFio() {
        return fio.get();
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    @Override
    public String toString() {
        return "User{" +
                "fio='" + fio + '\'' +
                '}';
    }
}
