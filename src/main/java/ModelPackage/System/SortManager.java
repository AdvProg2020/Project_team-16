package ModelPackage.System;

import ModelPackage.Product.Product;
import lombok.Data;

import java.util.ArrayList;

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
        switch (sortType) {
            // TODO : call needed methods
            case NAME:
                break;
            case TIME:
                break;
            case VIEW:
                break;
            case PRICE:
                break;
            case BOUGHTAMOUNT:
                break;
        }
        return list;
    }
}
