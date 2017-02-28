package sample.other;

import sample.objects.Product;
import sample.objects.Supplier;

import java.util.List;

/**
 * Created by Note-001 on 07.12.2016.
 */
public class PrintRequest {
//    private BDAdapter bdAdapter;
//
//    public PrintRequest(BDAdapter bdAdapter) {
//        this.bdAdapter = bdAdapter;
//    }
//
//    public List<Product> stockProducts(){
//        System.out.println("Список всех товаров в наличии");
//        return bdAdapter.getProductsList();
//    }
//
//    public List requiredProducts(){
//        System.out.println("Список требуемых товаров");
//        List<Product> list;
//        list = bdAdapter.getProductsList();
//        for(Product product: list){ //удаляем из списка товары с кол >1
//            if (product.getAmount()>1){
//                list.remove(product);
//            }
//        }
//        return list;
//    }
//
//    public List supplierProducts(Supplier supplier){
//        List<Product> list;
//        list = bdAdapter.getProductsList();
//        for(Product product: list){
//            if (!product.getSupplier().equals(supplier)){
//                list.remove(product); //оставляем в списке только те товары, что есть у данного поставщика
//            }
//        }
//        System.out.println("Товары поставщика "+supplier.getCompany());
//        return list;
//    }
}
