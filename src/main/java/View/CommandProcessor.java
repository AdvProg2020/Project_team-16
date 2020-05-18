package View;

import ModelPackage.System.editPackage.CategoryEditAttribute;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryExeption;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.System.exeption.product.AlreadyASeller;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import View.PrintModels.DisCodeManagerPM;
import View.PrintModels.DiscountMiniPM;
import View.PrintModels.RequestPM;
import View.PrintModels.UserMiniPM;
import View.exceptions.InvalidCharacter;
import View.exceptions.OutOfRangeInputException;
import controler.AccountController;
import controler.ManagerController;
import controler.exceptions.ManagerExist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
            Printer.printInvalidCommand();
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
            Printer.printInvalidCommand();
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
                Printer.printDiscountManager(pm);
            } catch (NoSuchADiscountCodeException e) {
               Printer.printMessage(e.getMessage());
            }
        }else
            Printer.printInvalidCommand();
    }

    public static void editDiscountCode(String command){
        Matcher matcher = getMatcher(command,"edit discount code (.*?)");
        if (matcher.find()){
            DiscountCodeEditAttributes editAttributes = new DiscountCodeEditAttributes();
            String code = matcher.group(1);
            Printer.printMessage("Enter new Start date(Leave it blank if don't want to change)\n" +
                    "it must have format : \"yyyy-MM-dd HH:mm:ss\" : ");
            try {
                String date = Scan.getInstance().getNullAbleDate();
                if (!date.isEmpty()) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                    editAttributes.setStart(start);
                }
            } catch (ParseException e) { e.printStackTrace(); }

            Printer.printMessage("Enter new End date(Leave it blank if don't want to change)\n" +
                    "it must have format : \"yyyy-MM-dd HH:mm:ss\" : ");
            try {
                String date = Scan.getInstance().getNullAbleDate();
                if (!date.isEmpty()) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                    editAttributes.setEnd(start);
                }
            } catch (ParseException e) { e.printStackTrace(); }

            Printer.printMessage("Enter new percentage(Leave it blank if don't want to change) : ");
            String percentage = Scan.getInstance().getNullAblePercentage();
            if (!percentage.isEmpty()) {
                editAttributes.setOffPercent(Integer.parseInt(percentage));
            }
            Printer.printMessage("Enter new maximum of discount(Leave it blank if don't want to change) : ");
            String max = Scan.getInstance().getNullAbleLong();
            if (!max.isEmpty()){
                editAttributes.setMaxDiscount(Integer.parseInt(max));
            }
            try {
                managerController.editDiscountCode(code,editAttributes);
            }catch (NoSuchADiscountCodeException | StartingDateIsAfterEndingDate e) {
                Printer.printMessage(e.getMessage());
            } catch (Exception e){
                Printer.printMessage("Something went wrong. Try later...");
            }
        }else
            Printer.printInvalidCommand();
    }

    public static void removeDiscountCode(String command){
        Matcher matcher = getMatcher(command,"remove discount code (.*?)");
        if (matcher.find()){
            String code = matcher.group(1);
            try {
                managerController.removeDiscountCode(code);
            } catch (NoSuchADiscountCodeException e) {
                Printer.printMessage(e.getMessage());
            }
        }
        else
            Printer.printInvalidCommand();
    }

    public static void viewDiscountCodes(){
        List<DiscountMiniPM> discountMiniPMS = managerController.viewDiscountCodes();
        if (!discountMiniPMS.isEmpty()){
            Printer.printAllDiscountCodes(discountMiniPMS);
        }else {
            Printer.printMessage("There isn't discount code to show.");
        }
    }

    public static void addUserToDiscountCode(String command){
        Matcher matcher = getMatcher(command,"add user (.*?) to discount (.*?)");
        if (matcher.find()){
            String code = matcher.group(1);
            String username = matcher.group(2);
            Printer.printMessage("Set the number of usage for this user(max 100) : ");
            String time = Scan.getInstance().getPercentage();
            try {
                managerController.addUserToDiscountCode(code,username,Integer.parseInt(time));
            } catch (UserNotAvailableException | UserExistedInDiscountCodeException | NoSuchADiscountCodeException e) {
                Printer.printMessage(e.getMessage());
            }
        }else
            Printer.printInvalidCommand();
    }

    public static void removeUserFromDiscountCode(String command){
        Matcher matcher = getMatcher(command,"remove user (.*?) from discount (.*?)");
        if (matcher.find()){
            String code = matcher.group(1);
            String username = matcher.group(2);
            try {
                managerController.removeUserFromDiscountCodeUsers(code,username);
            } catch (UserNotExistedInDiscountCodeException | NoSuchADiscountCodeException | UserNotAvailableException e) {
                Printer.printMessage(e.getMessage());
            }
        }else
            Printer.printInvalidCommand();
    }

    public static void addCategory(String command){
        Matcher matcher = getMatcher(command,"add (\\S+)");
        if (matcher.find()){
            String name = matcher.group(1);
            Printer.printMessage("Enter parent ID(Enter zero for no parent) : ");
            String parentId = Scan.getInstance().getInteger();
            try {
                managerController.addCategory(name,Integer.parseInt(parentId),getInitialFeaturesForCategory());
            } catch (RepeatedNameInParentCategoryExeption | NoSuchACategoryException e) {
                Printer.printMessage(e.getMessage());
            }
        }else
            Printer.printInvalidCommand();
    }

    private static List<String> getInitialFeaturesForCategory(){
        Printer.printMessage("Enter features for this category,separate them with an Enter\n" +
                "use \\end at a new line to submit features : \n");
        return Scan.getInstance().getListByEnterTill("\\end");
    }

    public static void editCategory(String command) {
        Matcher matcher = getMatcher(command, "edit (\\d{1,9})");
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            CategoryEditAttribute editAttribute = new CategoryEditAttribute();
            Printer.printMessage("Enter new Feature(Leave blank for no change) : ");
            String newFeature = Scan.getInstance().getLine();
            if (!newFeature.isEmpty())
                editAttribute.setAddFeature(newFeature);
            Printer.printMessage("Enter feature you want to remove(Leave blank for no change) : ");
            String remove = Scan.getInstance().getLine();
            if (!remove.isEmpty())
                editAttribute.setRemoveFeature(remove);
            Printer.printMessage("Enter new parent id(Leave blank for no change) : ");
            String newParentId = Scan.getInstance().getNullAbleInteger();
            if (!newParentId.isEmpty()){
                int parentId = Integer.parseInt(newParentId);
                editAttribute.setNewParentId(parentId);
            }
            Printer.printMessage("Enter new Name(Leave blank for no change) : ");
            String newName = Scan.getInstance().getLine();
            if (!newName.isEmpty()){
                editAttribute.setName(newName);
            }
            try {
                managerController.editCategory(id,editAttribute);
            } catch (RepeatedNameInParentCategoryExeption | RepeatedFeatureException | NoSuchACategoryException e) {
                Printer.printMessage(e.getMessage());
            }
        } else Printer.printInvalidCommand();
    }

    public static void removeCategory(String command){
        Matcher matcher = getMatcher(command,"remove (\\d{1,9})");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                managerController.removeCategory(id);
            } catch (NoSuchACategoryException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void removeProduct(String command){
        Matcher matcher = getMatcher(command,"remove (\\d{1,9})");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                managerController.removeProduct(id);
            } catch (NoSuchACategoryException | NoSuchAProductException | NoSuchAProductInCategoryException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void viewAllRequests(){
        List<RequestPM> pms = managerController.manageRequests();
        Printer.printAllRequests(pms);
    }

    public static void viewRequest(String command){
        Matcher matcher = getMatcher(command,"details (\\d{1,9})");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                RequestPM requestPM = managerController.viewRequest(id);
                Printer.printDetailedRequest(requestPM);
            } catch (NoSuchARequestException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void acceptRequest(String command){
        Matcher matcher = getMatcher(command,"accept (\\d{1,9})");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                managerController.acceptRequest(id);
            } catch (NoSuchARequestException | NoSuchAProductException | AlreadyASeller e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void declineRequest(String command){
        Matcher matcher = getMatcher(command,"decline (\\d{1,9})");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                managerController.declineRequest(id);
            } catch (NoSuchARequestException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void viewAllUsers(){
        List<UserMiniPM> pms = managerController.manageUsers();
        if (pms.isEmpty()){
            Printer.printMessage("No User To Show");
        }else {
            Printer.usersPrinter(pms);
        }
    }

    public static void viewUser(String command){
        Matcher matcher = getMatcher(command,"view (\\S+)");
        if (matcher.find()){
            String username = matcher.group(1);
            try {
                Printer.userPrinter(managerController.viewUser(username));
            } catch (UserNotAvailableException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void deleteUser(String command){
        Matcher matcher = getMatcher(command,"view (\\S+)");
        if (matcher.find()){
            String username = matcher.group(1);
            try {
                managerController.deleteUser(username);
            } catch (UserNotAvailableException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void createManagerProfile(){
        String[] info = new String[6];
        Printer.printMessage("Enter a username : ");
        info[0] = Scan.getInstance().getLine();
        if (!isUsernameValid(info[0])){
            Printer.printMessage("Username format isn't valid. It must contain only alphabets, numbers and underscore");
            return; }
        try { accountController.usernameInitialCheck(info[0]);
        }catch (UserNotAvailableException e) {
            Printer.printMessage("This Username isn't available");
        }
        getGeneralInformation(info);
        managerController.createManagerProfile(info);
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
