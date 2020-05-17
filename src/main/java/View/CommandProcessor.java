package View;

import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.discount.AlreadyExistCodeException;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.discount.StartingDateIsAfterEndingDate;
import ModelPackage.Users.Manager;
import View.PrintModels.DisCodeManagerPM;
import View.exceptions.InvalidCharacter;
import View.exceptions.OutOfRangeInputException;
import controler.AccountController;
import controler.ManagerController;
import controler.exceptions.ManagerExist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {
    private static AccountController accountController = AccountController.getInstance();
    private static ManagerController managerController = ManagerController.getInstance();

    public static void createAccount(String command){
        Matcher matcher = getMatcher(command,"create account ([M,m]anager|[S,s]eller|[C,c]ustomer) (\\S+)");
        if (matcher.find()){
            String type = matcher.group(0);
            String username = matcher.group(1);
            if (!isUsernameValid(username)){
                Printer.printMessage("Username format isn't valid. It must contain only alphabets numbers and underscore");
                return;
            }
            try { accountController.usernameInitialCheck(username);
            }catch (UserNotAvailableException e) {
                Printer.printMessage("This Username isn't available");return; }
            switch (type){
                case "Manager": case "manager" : createManagerAccount(username);
                case "Customer" : case "customer" : createCustomerAccount(username);
                case "Seller" : case "seller" : createSellerAccount(username);
            }
        }else
            Printer.printMessage("Invalid command pattern! Please see \"help\" for more.");
    }

    private static void getGeneralInformation(String[] info){
        Scan scan = Scan.getInstance();
        info[1] = scan.getPassword("Enter your Password");
        Printer.printMessage("Enter first name : ");
        info[2] = scan.getLine();
        Printer.printMessage("Enter Last name : ");
        info[3] = scan.getLine();
        while (true){
            Printer.printMessage("Enter email : ");
            info[4] = scan.getLine();
            if (info[4].matches(("\\S+@\\S+\\.(org|net|ir|com|uk|site)")))break;
            else Printer.printMessage("Invalid email format!\n");
        }
        Printer.printMessage("Enter phone number : ");
        info[5] = scan.getLine();
    }

    private static void createSellerAccount(String username){
        String[] info = new String[8];
        getGeneralInformation(info);
        info[0] = username;
        Printer.printMessage("Enter Company Id(Leave it blank to create one) : ");
        info[6] = Scan.getInstance().getLine();
        if (info[6].isEmpty())info[6] = createCompany();
        while (true){
            Printer.printMessage("Enter Your Balance(It must br a long number) :");
            info[7] = Scan.getInstance().getLine();
            if (!info[7].matches("^-?\\d{1,19}$")) Printer.printMessage("invalid balance! \n");
            else break;
        }
        accountController.createAccount(info,"seller");
        Data data = Data.getInstance();
        data.setRole("seller");
        data.setUsername(username);
        data.getLastMenu().execute();
    }

    // TODO: 18/05/2020 company exists Exception should be added
    private static String createCompany(){
        String[] info = new String[3];
        Printer.printMessage("Enter name of company : ");
        info[0] = Scan.getInstance().getLine();
        Printer.printMessage("Enter phone number of company : ");
        info[1] = Scan.getInstance().getLine();
        Printer.printMessage("Enter category of company : ");
        info[2] = Scan.getInstance().getLine();
        return Integer.toString(accountController.createCompany(info));
    }

    private static void createCustomerAccount(String username){
        String[] info = new String[7];
        getGeneralInformation(info);
        info[0] = username;
        Printer.printMessage("Enter Your Balance(It must br a long number) :");
        info[6] = Scan.getInstance().getLong();
        accountController.createAccount(info,"customer");
        Data data = Data.getInstance();
        data.setRole("customer");
        data.setUsername(username);
        data.getLastMenu().execute();
    }

    private static void createManagerAccount(String username){
        String[] info = new String[6];
        getGeneralInformation(info);
        info[0] = username;
        try {
            accountController.createFirstManager(info);
        } catch (ManagerExist managerExist) {
            Printer.printMessage("There is already a manager. You can't create any manager");
        }
        Data data = Data.getInstance();
        data.setRole("manger");
        data.setUsername(username);
        data.getLastMenu().execute();
    }

    private static boolean isUsernameValid(String username){
        return username.matches("\\w+");
    }

    public static void login(String command){
        Matcher matcher = getMatcher(command,"login (\\S+)");
        if (matcher.find()){
            String username = matcher.group(1);
            if (isUsernameValid(username)){
               String password = Scan.getInstance().getPassword("Enter Your Password : ");
                try {
                    String role = accountController.login(username,password);
                    Data.getInstance().setUsername(username);
                    Data.getInstance().setRole(role);
                } catch (NotVerifiedSeller notVerifiedSeller) {
                    Printer.printMessage("your Account Isn't Verified Yet");
                } catch (UserNotAvailableException e) {
                    Printer.printMessage("This username isn't valid");
                }
            }else {
                Printer.printMessage("Username format isn't valid. It must contain only alphabets numbers and underscore");
            }
        }
        else {
            Printer.printMessage("Invalid command pattern! Please see \"help\" for more.");
        }
    }

    public static void createDiscountCode(){
        String[] info = new String[5];
        Printer.printMessage("Enter a Code for your Discount : ");
        try {
            info[0] = Scan.getInstance().getFormalLines(64);
        } catch (InvalidCharacter invalidCharacter) {
            Printer.printMessage("Your code only can have A-Z a-z 1-9 _ \n" +
                    "going back to menu ...");
        } catch (OutOfRangeInputException e) {
            Printer.printMessage("Your code only can have 64 characters \n" +
                    "going back to menu ..."); }
        Printer.printMessage("Enter start date in format of \"dd-MM-yyyy HH:mm:ss\" : ");
        info[1] = Scan.getInstance().getADate();
        Printer.printMessage("Enter end date in format of \"dd-MM-yyyy HH:mm:ss\" : ");
        info[2] = Scan.getInstance().getADate();
        Printer.printMessage("Enter Percentage : ");
        info[3] = Scan.getInstance().getPercentage();
        Printer.printMessage("Enter Maximum of discount : ");
        info[4] = Scan.getInstance().getLong();
        try {
            managerController.createDiscount(info);
        } catch (StartingDateIsAfterEndingDate startingDateIsAfterEndingDate) {
            Printer.printMessage(startingDateIsAfterEndingDate.getMessage());
        } catch (AlreadyExistCodeException e) {
            Printer.printMessage("This code Already Exists. Going to menu...");
        }catch (Exception e){
            Printer.printMessage("A Problem happened. Try again later ...");
        }
    }

    public static void viewDiscountCode(String command){
        Matcher matcher = getMatcher(command,"view discount code (.*?)");
        if (matcher.find()){
            String code = matcher.group(1);
            try {
                DisCodeManagerPM pm = managerController.viewDiscountCode(code);
            } catch (NoSuchADiscountCodeException e) {
               Printer.printMessage(e.getMessage());
            }
        }else
            Printer.printMessage("Invalid command pattern! Please see \"help\" for more.");
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

    public static void viewOff(String command){

    }


    public static void editOff(String command){

    }


    public static void addOff(){

    }

    public static void viewAllOffs(){

    }

    public static void viewBalanceCustomer(){

    }

    public static void viewDiscountCodesCustomer(){

    }

    public static void showProductsInCart(){

    }


    public static void increaseProductInCart(String command){

    }

    public static  void decreaseProductInCart(String command){

    }

    public static void showTotalPrice(){

    }

    public static void showOrder(String comand){

    }


    public static void showOrders(){

    }

    public static void rateOrder(String command){

    }

    public static void showCart(){

    }
    
    public static void showProductsInProductMenu(){

    }

    public static void updateFilters(){

    }
    
    public static void showAvailableFilters(){

    }

    public static void filter(String command){

    }

    public static void currentFilters(){

    }

    public static void disableAFilter(String command){

    }
    
    public static void showAvailableSorts(){

    }
    
    public static void sort(String command){

    }

    public static void currentSort(){

    }

    public static void disableSort(){

    }
    
    public static void attributes(){

    }
    
    public static void compare(String command){

    }
    
    public static void showAllComments(){

    }
    
    public static void addAComment(){

    }
    
    public static void addToCart(){

    }

    public static void selectSeller(String command){

    }

    public static void briefInfo(){

    }

    public static void showAllOffs(){

    }

    private static Matcher getMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
