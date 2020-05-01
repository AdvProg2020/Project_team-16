package ModelPackage.System;

import ModelPackage.Product.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;

@Data
public class SortManager {
    private ArrayList<Product> list;
    private static SortManager sortManager = new SortManager();

    public static SortManager getInstance() {
        return sortManager;
    }

    private SortManager() {
        this.list = new ArrayList<>();
    }

    public ArrayList<Product> sort(ArrayList<Product> toSortList, SortType sortType) {
        list = toSortList;
        switch (sortType) {
            // TODO : call needed methods
            case NAME:
                sortByName(list);
                break;
            case TIME:
                break;
            case VIEW:
                sortByView(list);
                return list;
            case PRICE:
                break;
            case BOUGHTAMOUNT:
                break;
        }
        return null;
    }

    private void sortByName(ArrayList<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return firstProduct.getName().compareTo(secondProduct.getName());
            }
        });
    }

    private void sortByView(ArrayList<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getView() - firstProduct.getView();
            }
        });
    }
}
