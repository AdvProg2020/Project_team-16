package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.Users.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class SortManager {
    private List<Product> list;
    private List<User> usersList;
    private static SortManager sortManager = new SortManager();

    public static SortManager getInstance() {
        return sortManager;
    }

    private SortManager() {
        this.list = new ArrayList<>();
    }

    public List<Product> sort(List<Product> toSortList, SortType sortType) {
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
            case BOUGHT_AMOUNT:
                sortByBoughtAmount(list);
                break;
            case SCORE:
                sortByScore(list);
                break;
        }
        return list;
    }

    public List<User> sortUser(List<User> toSortList) {
        usersList = toSortList;
        usersList.sort(new Comparator<User>() {
            @Override
            public int compare(User firstUser, User secondUser) {
                return firstUser.getUsername().compareTo(secondUser.getUsername());
            }
        });
        return usersList;
    }

    private void sortByName(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return firstProduct.getName().compareTo(secondProduct.getName());
            }
        });
    }

    private void sortByView(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getView() - firstProduct.getView();
            }
        });
    }

    private void sortByBoughtAmount(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getBoughtAmount() - firstProduct.getBoughtAmount();
            }
        });
    }

    private void sortByTime(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getDateAdded().compareTo(firstProduct.getDateAdded());
            }
        });
    }

    private void sortByMorePrice(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return secondProduct.getLeastPrice() - firstProduct.getLeastPrice();
            }
        });
    }

    private void sortByLessPrice(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return firstProduct.getLeastPrice() - secondProduct.getLeastPrice();
            }
        });
    }

    private void sortByScore(List<Product> products) {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product firstProduct, Product secondProduct) {
                return Double.toString(secondProduct.getTotalScore()).
                        compareTo(Double.toString(firstProduct.getLeastPrice()));
            }
        });
    }
}
