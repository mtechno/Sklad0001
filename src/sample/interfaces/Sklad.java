package sample.interfaces;

import sample.objects.*;

import java.sql.SQLException;

/**
 * Created by Note-001 on 15.12.2016.
 */
public interface Sklad {
    void create(Product product) throws SQLException;
    void update(Product product) throws SQLException;
    void delete(Product product);

    void create(User user);
    void update(User user);
    void delete(User user);

    void create(Supplier supplier) throws SQLException;
    void update(Supplier supplier);
    void delete(Supplier supplier) throws SQLException;

    void create(Order order) throws SQLException;
    void update(OrderedProduct orderedProduct);
    void delete(Order order) throws SQLException;

}
