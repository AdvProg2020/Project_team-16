package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.Product.Category;
import ModelPackage.Product.Product;
import ModelPackage.Users.Request;
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
        usersList.sort(Comparator.comparing(User::getUsername));
        return usersList;
    }

    public void sortCategories(List<Category> list){
        for (Category category : list) {
            if (!category.getSubCategories().isEmpty()){
                sortCategories(category.getSubCategories());
            }
        }
        list.sort(Comparator.comparing(Category::getName));
    }

    public void sortRequests(List<Request> list,SortType sortType){
        switch (sortType){
            case CATEGORIZED_REQUESTS:sortRequestsCategorized(list);
            case NAME:sortRequestsByName(list);
            case TIME:default:break;
        }
    }

    private void sortRequestsCategorized(List<Request> list){
        list.sort(Comparator.comparing(Request::getRequestType));
    }

    private void sortRequestsByName(List<Request> list){
        list.sort(Comparator.comparing(Request::getUserHasRequested));
    }

    public void sortOff(List<Off> list){
        list.sort(Comparator.comparingInt(Off::getOffPercentage));
    }

    private void sortByName(List<Product> products) {
        products.sort(Comparator.comparing(Product::getName));
    }

    private void sortByView(List<Product> products) {
        products.sort((firstProduct, secondProduct) -> secondProduct.getView() - firstProduct.getView());
    }

    private void sortByBoughtAmount(List<Product> products) {
        products.sort((firstProduct, secondProduct) -> secondProduct.getBoughtAmount() - firstProduct.getBoughtAmount());
    }

    private void sortByTime(List<Product> products) {
        products.sort((firstProduct, secondProduct) -> secondProduct.getDateAdded().compareTo(firstProduct.getDateAdded()));
    }

    private void sortByMorePrice(List<Product> products) {
        products.sort((firstProduct, secondProduct) -> secondProduct.getLeastPrice() - firstProduct.getLeastPrice());
    }

    private void sortByLessPrice(List<Product> products) {
        products.sort(Comparator.comparingInt(Product::getLeastPrice));
    }

    private void sortByScore(List<Product> products) {
        products.sort((firstProduct, secondProduct) -> Double.toString(secondProduct.getTotalScore()).
                compareTo(Double.toString(firstProduct.getLeastPrice())));
    }
}
