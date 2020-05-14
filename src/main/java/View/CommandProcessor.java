package View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {
    public static void createAccount(String command){

    }

    public static void login(String command){

    }

    public static void createDiscountCode(String command){

    }

    public static void viewDiscountCode(String command){

    }

    public static void editDiscountCode(String command){

    }

    public static void removeDiscountCode(String command){

    }

    public static void viewDiscountCodes(){

    }

    public static void addCategory(String command){

    }

    public static void editCategory(String command){

    }

    public static void removeCategory(String command){

    }

    public static void removeProduct(String command){

    }

    public static void viewAllRequests(){

    }

    public static void viewRequest(String command){

    }

    public static void acceptRequest(String command){

    }

    public static void declineRequest(String command){

    }

    public static void viewAllUsers(){

    }

    public static void viewUser(String command){

    }

    public static void deleteUser(String command){

    }

    public static void createManagerProfile(){

    }

    public static void viewPersonalInfo(){

    }

    public static void editPersonalInfo(String command){

    }

    public static void viewCompanyInformation(){

    }

    public static void viewSalesHistory(){

    }

    public static void addProduct(){

    }

    public static void showCategories(){

    }

    public static void viewBalanceSeller(){

    }

    public static void viewAllProductSeller(){

    }

    public static void viewProduct(String command){

    }

    public static void editProduct(String command){

    }

    public static void deleteProduct(String command){

    }

    public static void viewBuyersOfThisProduct(String command){

    }





    private static Matcher getMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
