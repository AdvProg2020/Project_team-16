package View;

import ModelPackage.System.editPackage.*;
import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.NoSuchAProductInCategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryExeption;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.clcsmanager.YouAreNotASellerException;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.off.ThisOffDoesNotBelongssToYouException;
import ModelPackage.System.exeption.product.AlreadyASeller;
import ModelPackage.System.exeption.product.EditorIsNotSellerException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import View.PrintModels.*;
import View.exceptions.InvalidCharacter;
import View.exceptions.OutOfRangeInputException;
import controler.AccountController;
import controler.CustomerController;
import controler.ManagerController;
import controler.SellerController;
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
    private static SellerController sellerController = SellerController.getInstance();
    private static CustomerController customerController = CustomerController.getInstance();
    private static Data data = Data.getInstance();
    private static Scan scan = Scan.getInstance();

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
        info[6] = scan.getLine();
        if (info[6].isEmpty())info[6] = createCompany();
        while (true){
            Printer.printMessage("Enter Your Balance(It must br a long number) :");
            info[7] = scan.getLine();
            if (!info[7].matches("^-?\\d{1,19}$")) Printer.printMessage("invalid balance! \n");
            else break;
        }
        accountController.createAccount(info,"seller");
        data.setRole("seller");
        data.setUsername(username);
        data.getLastMenu().execute();
    }

    // TODO: 18/05/2020 company exists Exception should be added
    private static String createCompany(){
        String[] info = new String[3];
        Printer.printMessage("Enter name of company : ");
        info[0] = scan.getLine();
        Printer.printMessage("Enter phone number of company : ");
        info[1] = scan.getLine();
        Printer.printMessage("Enter category of company : ");
        info[2] = scan.getLine();
        return Integer.toString(accountController.createCompany(info));
    }

    private static void createCustomerAccount(String username){
        String[] info = new String[7];
        getGeneralInformation(info);
        info[0] = username;
        Printer.printMessage("Enter Your Balance(It must br a long number) :");
        info[6] = scan.getLong();
        accountController.createAccount(info,"customer");
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
               String password = scan.getPassword("Enter Your Password : ");
                try {
                    String role = accountController.login(username,password);
                    data.setUsername(username);
                    data.setRole(role);
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
            info[0] = scan.getFormalLines(64);
        } catch (InvalidCharacter invalidCharacter) {
            Printer.printMessage("Your code only can have A-Z a-z 1-9 _ \n" +
                    "going back to menu ...");
        } catch (OutOfRangeInputException e) {
            Printer.printMessage("Your code only can have 64 characters \n" +
                    "going back to menu ..."); }
        Printer.printMessage("Enter start date in format of \"dd-MM-yyyy HH:mm:ss\" : ");
        info[1] = scan.getADate();
        Printer.printMessage("Enter end date in format of \"dd-MM-yyyy HH:mm:ss\" : ");
        info[2] = scan.getADate();
        Printer.printMessage("Enter Percentage : ");
        info[3] = scan.getPercentage();
        Printer.printMessage("Enter Maximum of discount : ");
        info[4] = scan.getLong();
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
                String date = scan.getNullAbleDate();
                if (!date.isEmpty()) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                    editAttributes.setStart(start);
                }
            } catch (ParseException e) { e.printStackTrace(); }

            Printer.printMessage("Enter new End date(Leave it blank if don't want to change)\n" +
                    "it must have format : \"yyyy-MM-dd HH:mm:ss\" : ");
            try {
                String date = scan.getNullAbleDate();
                if (!date.isEmpty()) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                    editAttributes.setEnd(start);
                }
            } catch (ParseException e) { e.printStackTrace(); }

            Printer.printMessage("Enter new percentage(Leave it blank if don't want to change) : ");
            String percentage = scan.getNullAblePercentage();
            if (!percentage.isEmpty()) {
                editAttributes.setOffPercent(Integer.parseInt(percentage));
            }
            Printer.printMessage("Enter new maximum of discount(Leave it blank if don't want to change) : ");
            String max = scan.getNullAbleLong();
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
        List<DiscountMiniPM> discountMiniPMS = managerController.viewDiscountCodes(data.getSorts());
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
            String time = scan.getPercentage();
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
            String parentId = scan.getInteger();
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
        return scan.getListByEnterTill("\\end");
    }

    public static void editCategory(String command) {
        Matcher matcher = getMatcher(command, "edit (\\d{1,9})");
        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            CategoryEditAttribute editAttribute = new CategoryEditAttribute();
            Printer.printMessage("Enter new Feature(Leave blank for no change) : ");
            String newFeature = scan.getLine();
            if (!newFeature.isEmpty())
                editAttribute.setAddFeature(newFeature);
            Printer.printMessage("Enter feature you want to remove(Leave blank for no change) : ");
            String remove = scan.getLine();
            if (!remove.isEmpty())
                editAttribute.setRemoveFeature(remove);
            Printer.printMessage("Enter new parent id(Leave blank for no change) : ");
            String newParentId = scan.getNullAbleInteger();
            if (!newParentId.isEmpty()){
                int parentId = Integer.parseInt(newParentId);
                editAttribute.setNewParentId(parentId);
            }
            Printer.printMessage("Enter new Name(Leave blank for no change) : ");
            String newName = scan.getLine();
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
        List<RequestPM> pms = managerController.manageRequests(data.getSorts());
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
        info[0] = scan.getLine();
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
        try {
            UserFullPM pm = accountController.viewPersonalInfo(data.getUsername());
            Printer.userPrinter(pm);
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void editPersonalInfo(String command){
        Matcher matcher = getMatcher(command,"edit (.*?)");
        if (matcher.find()){
            String field = matcher.group(1);
            String newField;
            UserEditAttributes attributes = new UserEditAttributes();
            Printer.printMessage("Enter new " + field + " : ");
             if (!field.equals("password")) {
                 newField = scan.getLine();
                 switch (field) {
                     case "first name":
                         attributes.setNewFirstName(newField);
                         break;
                     case "last name":
                         attributes.setNewLastName(newField);
                     case "phone number":
                     case "phone":
                     case "number":
                         attributes.setNewPhone(newField);
                     case "email":
                         attributes.setNewEmail(newField);
                 }
             }else {
                 String pass = scan.getPassword("Enter new Password : ");
                 attributes.setNewPassword(pass);
             }
            try {
                accountController.editPersonalInfo(data.getUsername(),attributes);
            } catch (UserNotAvailableException e) {
                Printer.printMessage(e.getMessage());
            }
        }
    }

    public static void viewCompanyInformation(){
        try {
            CompanyPM pm = sellerController.viewCompanyInfo(data.getUsername());
            Printer.printCompany(pm);
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void viewSalesHistory(){
        try {
            List<SellLogPM> pms = sellerController.viewSalesHistory(data.getUsername());
            Printer.printSaleHistory(pms);
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void addProduct(){
        String[] info = new String[7];
        data.setPublicFeatures(sellerController.getPublicFeatures());
        info[0] = data.getUsername();
        Printer.printMessage("Enter category id you want to create product in : ");
        info[3] = scan.getInteger();
        try {
            data.setSpecialFeatures(sellerController.getSpecialFeaturesOfCat(Integer.parseInt(info[3])));
        } catch (NoSuchACategoryException e) {
            Printer.printMessage(e.getMessage());
            return;
        }
        Printer.printMessage("Enter product name : ");
        info[1] = scan.getLine();
        Printer.printMessage("Enter company of product : ");
        info[2] = scan.getLine();
        Printer.printMessage("Enter description of the product, enter \\end at the end of your description : \n");
        info[4] = scan.getLinesUntil("\\end");
        Printer.printMessage("Enter the price : ");
        info[5] = scan.getInteger();
        Printer.printMessage("Enter the amount of product you have in stock : ");
        info[6] = scan.getInteger();
        String[] publicFeatureData = new String[data.getPublicFeatures().size()*2];
        String[] specialFeatureData = new String[data.getSpecialFeatures().size()*2];
        getFeatures("public",publicFeatureData,data.getPublicFeatures());
        getFeatures("special",specialFeatureData,data.getSpecialFeatures());
        try {
            sellerController.addProduct(info,publicFeatureData,specialFeatureData);
        } catch (NoSuchACategoryException | UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    private static void getFeatures(String title,String[] data,List<String> features){
        Printer.printMessage("Now enter the "+ title + " features of product : ");
        int i = 0;
        for (String feature : features) {
            Printer.printMessage(feature + " : ");
            String fea = scan.getLine();
            data[i*2] = feature;
            data[i*2+1] = fea;
            i++;
        }
    }

    public static void showCategories(){
        // TODO: 18/05/2020
    }

    public static void viewBalanceSeller(){
        try {
            long balance = sellerController.viewBalance(data.getUsername());
            Printer.printMessage("Your balance : " + balance);
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void viewAllProductSeller(){
        try {
            List<MiniProductPM> pms = sellerController.manageProducts(data.getUsername(), data.getSorts());
            if (pms.isEmpty()){
                Printer.printMessage("You have no product to show");
            }else {
                Printer.printAllProducts(pms);
            }
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void viewProduct(String command){
        Matcher matcher;
        if (command.startsWith("view")){
           matcher = getMatcher(command,"view (\\d+{1,9})");
        }else {
            matcher = getMatcher(command,"show product (\\d+{1,9})");
        }
        if (matcher.find()){
            String id = matcher.group(1);
            try {
                FullProductPM pm = sellerController.viewProduct(Integer.parseInt(id));
                Printer.productPrintFull(pm);
            } catch (NoSuchAProductException e) {
                Printer.printMessage(e.getMessage());
            }
        }
        else Printer.printInvalidCommand();
    }

    public static void editProduct(String command){
        ProductEditAttribute editAttribute = new ProductEditAttribute();
        Matcher matcher = getMatcher(command,"edit (\\d+{1,9})");
        if (matcher.find()){
            editAttribute.setSourceId(Integer.parseInt(matcher.group(1)));
            Printer.printMessage("Enter new name(Leave blank if don't want to change) : ");
            String newName = scan.getLine();
            if (!newName.isEmpty()){
                editAttribute.setName(newName);
            }
            Printer.printMessage("Enter public feature title you want to change(Leave blank if don't want to change) : \n");
            String publicField = scan.getLine();
            if (!publicField.isEmpty()){
                Printer.printMessage("Enter new Feature : ");
                String feature = scan.getLine();
                editAttribute.setPublicFeatureTitle(publicField);
                editAttribute.setPublicFeature(feature);
            }
            Printer.printMessage("Enter special feature title you want to change(Leave blank if don't want to change) : \n");
            String specialField = scan.getLine();
            if (!specialField.isEmpty()){
                Printer.printMessage("Enter new Feature : ");
                String feature = scan.getLine();
                editAttribute.setSpecialFeatureTitle(specialField);
                editAttribute.setSpecialFeature(feature);
            }
            Printer.printMessage("Enter new category id(Leave it blank if don't want to change) : ");
            String newCatId = scan.getNullAbleInteger();
            if (!newCatId.isEmpty()){
                editAttribute.setNewCategoryId(Integer.parseInt(newCatId));
            }
            try {
                sellerController.editProduct(data.getUsername(),editAttribute);
            } catch (NoSuchAProductException | EditorIsNotSellerException e) {
                Printer.printMessage(e.getMessage());
            }
        } else Printer.printInvalidCommand();
    }

    public static void deleteProduct(String command){
        Matcher matcher = getMatcher(command,"delete (\\d+{1,9})");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                sellerController.removeProduct(id,data.getUsername());
            } catch (NoSuchACategoryException | NoSuchAProductInCategoryException | NoSuchAProductException | EditorIsNotSellerException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void viewBuyersOfThisProduct(String command){
        Matcher matcher = getMatcher(command,"view buyers ()\\d+{1,9}");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                List<UserMiniPM> pms = sellerController.viewAllBuyersOfProduct(id, data.getUsername(),data.getSorts());
                if (!pms.isEmpty()){
                    Printer.usersPrinter(pms);
                }else {
                    Printer.printMessage("There is no seller to show");
                }
            } catch (NoSuchAProductException | YouAreNotASellerException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void viewOff(String command){
        Matcher matcher = getMatcher(command,"view ()\\d+{1,9}");
        if (matcher.find()){
            int id = Integer.parseInt(matcher.group(1));
            try {
                OffPM pm = sellerController.viewOff(id, data.getUsername());
                Printer.viewOff(pm);
            } catch (NoSuchAOffException | ThisOffDoesNotBelongssToYouException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void editOff(String command){
        Matcher matcher = getMatcher(command,"edit (\\d+{1,9})");
        if (matcher.find()){
            OffChangeAttributes changeAttributes = new OffChangeAttributes();
            Printer.printMessage("Enter new percentage(Leave it blank if don't want to change) : ");
            String percentage = scan.getNullAblePercentage();
            if (!percentage.isEmpty()) {
                changeAttributes.setPercentage(Integer.parseInt(percentage));
            }
            Printer.printMessage("Enter new Start date(Leave it blank if don't want to change)\n" +
                    "it must have format : \"yyyy-MM-dd HH:mm:ss\" : ");
            try {
                String date = scan.getNullAbleDate();
                if (!date.isEmpty()) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                    changeAttributes.setStart(start);
                }
            } catch (ParseException e) { e.printStackTrace(); }

            Printer.printMessage("Enter new End date(Leave it blank if don't want to change)\n" +
                    "it must have format : \"yyyy-MM-dd HH:mm:ss\" : ");
            try {
                String date = scan.getNullAbleDate();
                if (!date.isEmpty()) {
                    Date start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                    changeAttributes.setEnd(start);
                }
            } catch (ParseException e) { e.printStackTrace(); }

            Printer.printMessage("Enter a product id to be added on off(Leave it blank if don't want to change) : \n");
            String addProduct = scan.getNullAbleInteger();
            if (!addProduct.isEmpty()){
                changeAttributes.setProductIdToAdd(Integer.parseInt(addProduct));
            }
            Printer.printMessage("Enter a product id to be removes from off(Leave it blank if don't want to change) : \n");
            String removeProduvt = scan.getNullAbleInteger();
            if (!removeProduvt.isEmpty()){
                changeAttributes.setProductIdToRemove(Integer.parseInt(removeProduvt));
            }
            try {
                sellerController.editOff(data.getUsername(),changeAttributes);
                Printer.printMessage("your Off wouldn't be available till manager accept your edit ...");
            } catch (ThisOffDoesNotBelongssToYouException | NoSuchAOffException e) {
                Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void addOff(){
        String[] info = new String[3];
        Printer.printMessage("Enter percentage of your off : ");
        info[2] = scan.getPercentage();
        Printer.printMessage("Enter start date in format of \"dd-MM-yyyy HH:mm:ss\" : ");
        info[0] = scan.getADate();
        Printer.printMessage("Enter end date in format of \"dd-MM-yyyy HH:mm:ss\" : ");
        info[1] = scan.getADate();
        try {
            sellerController.addOff(info,data.getUsername());
        } catch (ParseException | InvalidTimes | UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void viewAllOffs(){
        try {
            List<MiniOffPM> pms = sellerController.viewAllOffs(data.getUsername(),data.getSorts());
            if (!pms.isEmpty()){
                Printer.viewOffs(pms);
            }else {
                Printer.printMessage("There isn't any off to show");
            }
        } catch (UserNotAvailableException e) {
            Printer.printMessage("Your not a seller");
        }
    }

    public static void deleteOff(String command){
        Matcher matcher = getMatcher(command,"remove (\\d+{1,9})");
        if (matcher.find()){
            String id = matcher.group(1);
            try {
                sellerController.deleteOff(id,data.getUsername());
            } catch (NoSuchAOffException | ThisOffDoesNotBelongssToYouException e) {
               Printer.printMessage(e.getMessage());
            }
        }else Printer.printInvalidCommand();
    }

    public static void viewBalanceCustomer(){
        try {
            long balance = customerController.viewBalance(data.getUsername());
            Printer.printMessage("Your balance is : " + balance);
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void viewDiscountCodesCustomer(){
        try {
            List<DisCodeUserPM> pms = customerController.viewDiscountCodes(data.getUsername(), data.getSorts());
            if (!pms.isEmpty()){
                /* TODO */
            }else {
                Printer.printMessage("No discount code to show.");
            }
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public static void showProductsInCart(){
        try {
            List<InCartPM> pms = customerController.showProducts(data.getUsername());
            if (!pms.isEmpty()){
                Printer.viewProductsInCart(pms);
            }else {
                Printer.printMessage("No product in cart to show");
            }
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }
    }

    public static void increaseProductInCart(String command){
        Matcher matcher = getMatcher(command,"increase (\\d+{1,9})");
        if (matcher.find()){
            try {
                customerController.changeAmount(data.getUsername(),Integer.parseInt(matcher.group(1)),+1);
            } catch (Exception e) {
                Printer.printMessage("Error : " + e.getMessage());
            }
        }else {
            Printer.printInvalidCommand();
        }
    }

    public static  void decreaseProductInCart(String command){
        Matcher matcher = getMatcher(command,"increase (\\d+{1,9})");
        if (matcher.find()){
            try {
                customerController.changeAmount(data.getUsername(),Integer.parseInt(matcher.group(1)),-1);
            } catch (Exception e) {
                Printer.printMessage("Error : " + e.getMessage());
            }
        }else {
            Printer.printInvalidCommand();
        }
    }

    public static void showTotalPrice(){
        try {
            long totalPrice = customerController.showTotalPrice(data.getUsername());
            Printer.printMessage("Total Price : " + totalPrice);
        } catch (UserNotAvailableException e) {
            Printer.printMessage(e.getMessage());
        }

    }

    public static void showOrder(String command){
        Matcher matcher = getMatcher(command,"show order (\\d+{1,9})");
        if (matcher.find()){
            try {
                OrderLogPM pm = customerController.showOrder(Integer.parseInt(matcher.group(1)));
                Printer.viewOrder(pm);
            } catch (NoSuchAProductException | NoSuchALogException e) {
                Printer.printMessage(e.getMessage());
            }
        }
        else Printer.printInvalidCommand();
    }

    public static void showOrders(){
        try {
            List<OrderMiniLogPM> pms = customerController.viewOrders(data.getUsername());
            if (!pms.isEmpty()){
                Printer.viewOrderHistory(pms);
            }else {
                Printer.printMessage("No Order To Show.");
            }
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }
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
