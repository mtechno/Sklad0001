package sample.other;

import sample.objects.Order;
import sample.objects.Product;
import sample.objects.Supplier;
import sample.objects.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Note-001 on 07.12.2016.
 */
 public  class BDAdapter {


//    private List<Product> productsList = new LinkedList<>();
//    private List<User> usersList = new LinkedList<>();
//    private List<Supplier> suppliersList = new LinkedList<>();
//
//    public List<String> getSuppliersCompanyList() {
//        return suppliersCompanyList;
//    }
//
//    private List<String> suppliersCompanyList = new LinkedList<>();
//
//    public List<User> getUsersList() {
//        return usersList;
//    }
//
//    public List<Supplier> getSuppliersList() {
//        return suppliersList;
//    }
//
//    public List<Order> getOrdersList() {
//        return ordersList;
//    }
//
//    private List<Order> ordersList = new LinkedList<>();
//
//    public void addUser(User user) {
//        usersList.add(user);
//
//        System.out.println("добавление нового пользователя системы.");
//    }
//
//    public void updateUser(User user) {
//        usersList.set(user.id, user);
//    }
//
//    public void deleteUser(User user) {
//        usersList.remove(user.id);
//    }
//
//
//    public void addProduct(Product product) {
//        productsList.add(product);
//        System.out.println("добавление товара " + product.getName() + " на склад");
//    }
//
//    public void updateProduct(Product product) {
//        int i = 0;
//        for (Product product1 : productsList) {
//
//            if (product1.id == product.id) {
//                productsList.set(i, product);
//                return;
//            }
//            i++;
//        }
//        //productsList.
//        System.out.println("добавление товара " + product.getName() + " на склад");
//    }
//
//    public void deleteProduct(Product product) {
//
//        int i = 0;
//        for (Product product1 : productsList) {
//
//            if (product1.id == product.id) {
//                productsList.remove(i);
//                return;
//            }
//            i++;
//        }
//    }
//
//
//    public void addOrder(Order order) {
//        ordersList.add(order);
//        System.out.println("добавление заказа");
//    }
//
//    public void updateOrder(Order order) {
//        ordersList.set(order.id, order);
//    }
//
//    public void deleteOrder(Order order) {
//        ordersList.remove(order.id);
//    }
//
//
//    public void addSupplier(Supplier supplier) {
//        suppliersList.add(supplier);
//        suppliersCompanyList.add(supplier.getCompany());
//        System.out.println("добавление поставщика");
//    }
//
//
//    public void updateSupplier(Supplier supplier) {
//        suppliersList.set(supplier.id, supplier);
//        System.out.println("изменение инфы о поставщике");
//    }
//
//    public void deleteSupplier(Supplier supplier) {
//        suppliersList.remove(supplier.id);
//        System.out.println("удаление поставщика");
//    }
//
//    public List<Product> getProductsList() {
//        return productsList;
//    }
//
//    public void setProductsList(List<Product> productsList) {
//        this.productsList = productsList;
//    }
//

}
