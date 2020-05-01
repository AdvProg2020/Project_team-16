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
            case NAME:
                sortByName(list);
                break;
            case TIME:
                sortByTime(list);
                break;
            case VIEW:
                sortByView(list);
                break;
            case MORE_PRICE:
                sortByMorePrice(list);
                break;
            case LESS_PRICE:
                sortByLessPrice(list);
                break;
            case BOUGHTAMOUNT:
                sortByBoughtAmount(list);
                break;
        }
        return list;
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

    private void sortByBoughtAmount(ArrayList<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getBoughtAmount() - firstProduct.getBoughtAmount();
            }
        });
    }

    private void sortByTime(ArrayList<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getDateAdded().compareTo(firstProduct.getDateAdded());
            }
        });
    }

    private void sortByMorePrice(ArrayList<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getLeastPrice() - firstProduct.getLeastPrice();
            }
        });
    }

    private void sortByLessPrice(ArrayList<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return firstProduct.getLeastPrice() - secondProduct.getLeastPrice();
            }
        });
    }
}
