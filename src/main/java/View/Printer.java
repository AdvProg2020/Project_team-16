package View;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Users.Cart;
import View.PrintModels.*;
import dnl.utils.text.table.TextTable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Printer {
    private static final String lineSmall= new String(new char[32]).replace('\0', '-');
    private static final String lineMedium = new String(new char[48]).replace('\0', '-');
    private static final String lineLarge = new String(new char[80]).replace('\0', '-');

    public static void printInvalidCommand(){
        System.out.println("Invalid command pattern! Please see \"help\" for more.");
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void MenuPrinter(String name){
        System.out.print(name);
        System.out.println("+----------------------------------------+");
    }

    public static void printMessage(String message){
        System.out.print(message);
    }

    public static void productPrinterShort(MiniProductPM product){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Name",12),
                StringUtils.center("ID", 12),
                StringUtils.center("Category",12)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n\n",
                StringUtils.center(product.getName(), 20),
                StringUtils.center(Integer.toString(product.getId()), 8),
                StringUtils.center(product.getCategoryName(), 16)
        );

        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Seller", 16),
                StringUtils.center("Price", 16),
                StringUtils.center("Stock", 16)
        );
        System.out.println(lineMedium);

        int iterator = 0;
        for (SellerIntegerMap seller : product.getSellers()) {
            int price = product.getPrices().get(iterator++).getInteger();
            System.out.printf("|%s|%s|%s|\n",
                    StringUtils.center(seller.getSeller().getUsername(), 16),
                    StringUtils.center(Integer.toString(price), 16),
                    StringUtils.center(Integer.toString(seller.getInteger()), 16)
            );
        }
        System.out.println(lineMedium);

        System.out.printf("\n|%s|%s|%s|\n",
                StringUtils.center("Brand",20),
                StringUtils.center("Score", 8),
                StringUtils.center("About",16)
        );

        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(product.getBrand(), 20),
                StringUtils.center(Double.toString(product.getScore()), 8),
                StringUtils.center(product.getDescription(), 16)
        );
    }

    public static void productPrintFull(FullProductPM product){
        productPrinterShort(product.getProduct());

        System.out.println(lineMedium);
        System.out.printf("|%s|\n", StringUtils.center("Features",48));
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("Special",24),
                StringUtils.center("Public",24)
        );
        System.out.println(lineMedium);
        for (String special : product.getFeatures().keySet()) {
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(special,24),
                    StringUtils.center(product.getFeatures().get(special),24)
            );
            System.out.println(lineMedium);
        }
    }

    public static void usersPrinter(List<UserMiniPM> users){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("Username",24),
                StringUtils.center("Role",24)
        );
        System.out.println(lineMedium);

        for (UserMiniPM user : users) {
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(user.getUsername(),24),
                    StringUtils.center(user.getRole(),24)
            );
            System.out.println(lineMedium);
        }
    }

    public static void userPrinter(UserFullPM user){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Username",15),
                StringUtils.center("First Name",15),
                StringUtils.center("Last Name",15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(user.getUsername(),15),
                StringUtils.center(user.getFirstName(),15),
                StringUtils.center(user.getLastName(),15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Email",15),
                StringUtils.center("Phone Number",15),
                StringUtils.center("Role",15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(user.getEmail(),15),
                StringUtils.center(user.getPhoneNumber(),15),
                StringUtils.center(user.getRole(),15)
        );
        System.out.println(lineMedium);

    }

    public static void printAllProducts(List<MiniProductPM> products){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Name",12),
                StringUtils.center("ID", 12),
                StringUtils.center("Category",12)
        );
        System.out.println(lineMedium);

        for (MiniProductPM product : products) {
            System.out.printf("|%s|%s|%s|\n",
                    StringUtils.center(product.getName(), 20),
                    StringUtils.center(Integer.toString(product.getId()), 8),
                    StringUtils.center(product.getCategoryName(), 16)
            );
        }
        System.out.println(lineMedium);

    }

    public static void printAllDiscountCodes(List<DiscountMiniPM> discountCodes){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("Code",24),
                StringUtils.center("OffPercentage",24)
        );
        System.out.println(lineMedium);
        for (DiscountMiniPM discountCode : discountCodes) {
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(discountCode.getDiscountCode(),24),
                    StringUtils.center(Integer.toString(discountCode.getOffPercentage()),24)
            );
            System.out.println(lineMedium);
        }
    }

    public static void printDiscountManager(DisCodeManagerPM discountCode){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("Code",16),
                StringUtils.center("Start Date",16),
                StringUtils.center("End Date",16),
                StringUtils.center("Off Percentage",16),
                StringUtils.center("Max Discount Price",16)
        );
        System.out.println(lineLarge);

        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center(discountCode.getDiscountCode(),16),
                StringUtils.center(discountCode.getStartTime().toString(),16),
                StringUtils.center(discountCode.getEndTime().toString(),16),
                StringUtils.center(Integer.toString(discountCode.getOffPercentage()),16),
                StringUtils.center(Long.toString(discountCode.getMaxOfPriceDiscounted()),16)
        );
        System.out.println(lineLarge);

        System.out.println(lineSmall);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("User", 16),
                StringUtils.center("Count", 16)
        );
        System.out.println(lineSmall);

        for (UserIntegerMap user : discountCode.getUsersHavingDiscountCodeWithCount()) {
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(user.getUser().getUsername(), 16),
                    StringUtils.center(Integer.toString(user.getInteger()), 16)
            );
            System.out.println(lineSmall);
        }
    }

    public static void printAllRequests(List<RequestPM> requests){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Username",16),
                StringUtils.center("ID",16),
                StringUtils.center("Type",16)
        );
        System.out.println(lineMedium);

        for (RequestPM request : requests) {
            System.out.printf("|%s|%s|%s|\n",
                    StringUtils.center(request.getRequesterUserName(),16),
                    StringUtils.center(Integer.toString(request.getRequestId()),16),
                    StringUtils.center(request.getRequestType(),16)
            );
        }
        System.out.println(lineMedium);
    }

    public static void printDetailedRequest(RequestPM request){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Username",16),
                StringUtils.center("ID",16),
                StringUtils.center("Type",16)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n\n",
                StringUtils.center(request.getRequesterUserName(),16),
                StringUtils.center(Integer.toString(request.getRequestId()),16),
                StringUtils.center(request.getRequestType(),16)
        );

        System.out.println(lineMedium);
        System.out.printf("|%s|\n",
                StringUtils.center("Request",48)
        );
        System.out.println(lineMedium);

        System.out.printf("%s\n", request.getRequest());
        System.out.println(lineMedium);
    }

    public static void printSaleHistory(List<SellLogPM> sellLogs){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("Product",15),
                StringUtils.center("Price",15),
                StringUtils.center("Discount",10),
                StringUtils.center("Buyer",15),
                StringUtils.center("Delivery Status",20)
        );
        System.out.println(lineLarge);

        for (SellLogPM sellLog : sellLogs) {
            System.out.printf("|%s|%s|%s|%s|%s|\n",
                    StringUtils.center(Integer.toString(sellLog.getProductId()),15),
                    StringUtils.center(Integer.toString(sellLog.getMoneyGotten()),15),
                    StringUtils.center(Integer.toString(sellLog.getDiscount()),10),
                    StringUtils.center(sellLog.getBuyer(),15),
                    StringUtils.center(sellLog.getDeliveryStatus().toString(),20)
            );
        }
        System.out.println(lineLarge);
    }

    public static void printCompany(CompanyPM company){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Name",15),
                StringUtils.center("Phone Number",15),
                StringUtils.center("Group",15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(company.getName(),15),
                StringUtils.center(company.getPhone(),15),
                StringUtils.center(company.getGroup(),15)
        );
        System.out.println(lineMedium);

    }

    public static void viewOff(OffPM off){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("ID",15),
                StringUtils.center("Seller",15),
                StringUtils.center("Start Date",15),
                StringUtils.center("End Date",15),
                StringUtils.center("Off Percentage",15)
        );
        System.out.println(lineLarge);

        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center(Integer.toString(off.getOffId()),15),
                StringUtils.center(off.getSeller(),15),
                StringUtils.center("Start Date",15),
                StringUtils.center("End Date",15),
                StringUtils.center("Off Percentage",15)
        );
        System.out.println(lineLarge);
        System.out.printf("|%s|\n", StringUtils.center("Products",78));
        System.out.println(lineLarge);

        int count = 0;
        for (Integer productId : off.getProductIds()) {
            if (count == 5){
                System.out.println(lineLarge);
                count = 0;
            }
            System.out.printf("|%s|", StringUtils.center(Integer.toString(productId),14));
            count++;
        }
        System.out.println(lineLarge);
    }

    public static void viewOffs(List<MiniOffPM> offs){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("ID",15),
                StringUtils.center("Seller",15),
                StringUtils.center("Start Date",15),
                StringUtils.center("End Date",15),
                StringUtils.center("Off Percentage",15)
        );
        System.out.println(lineLarge);

        for (MiniOffPM off : offs) {
            System.out.printf("|%s|%s|%s|%s|%s|\n",
                    StringUtils.center(Integer.toString(off.getOffId()),15),
                    StringUtils.center(off.getSeller(),15),
                    StringUtils.center("Start Date",15),
                    StringUtils.center("End Date",15),
                    StringUtils.center("Off Percentage",15)
            );
        }
        System.out.println(lineLarge);
    }

    public static void viewCart(CartPM cartPM){
        System.out.println(lineLarge);
        if (cartPM.getPurchases().isEmpty()){
            System.out.printf("|%s|\n", StringUtils.center("Your Cart is Currently Empty!",46));
        } else {
            String print = String.format("There Are %d Good in Your Cart.\nTotal Price is %o",
                    cartPM.getPurchases().size(), cartPM.getTotalPrice());
            System.out.printf("|%s|\n", StringUtils.center(print,46));
        }
        System.out.println(lineLarge);
    }

    public static void viewCart(Cart cart){
        System.out.println(lineLarge);
        if (cart.getSubCarts().isEmpty()){
            System.out.printf("|%s|", StringUtils.center("Your Cart is Currently Empty!",46));
        } else {
            String print = String.format("There Are %d Good in Your Cart.\nTotal Price is %o",
                    cart.getSubCarts().size(), cart.getTotalPrice());
            System.out.printf("|%s|", StringUtils.center(print,46));
        }
        System.out.println(lineLarge);
    }

    public static void viewProductsInCart(List<InCartPM> cart){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("Product",15),
                StringUtils.center("Seller",15),
                StringUtils.center("Price",15),
                StringUtils.center("Amount",15),
                StringUtils.center("Off Price",15)
        );
        System.out.println(lineLarge);

        for (InCartPM inCartPM : cart) {
            System.out.printf("|%s|%s|%s|%s|%s|\n",
                    StringUtils.center(inCartPM.getProduct().getName(),15),
                    StringUtils.center(inCartPM.getSellerId(),15),
                    StringUtils.center(Integer.toString(inCartPM.getPrice()),15),
                    StringUtils.center(Integer.toString(inCartPM.getAmount()),15),
                    StringUtils.center(Integer.toString(inCartPM.getOffPrice()),15)
            );
        }
        System.out.println(lineLarge);
    }

    public static void viewOrderHistory(List<OrderMiniLogPM> orders){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Date",15),
                StringUtils.center("ID",15),
                StringUtils.center("Price",15)
        );
        System.out.println(lineMedium);

        for (OrderMiniLogPM order : orders) {
            System.out.printf("|%s|%s|%s|\n",
                    StringUtils.center(order.getDate().toString(),15),
                    StringUtils.center(Integer.toString(order.getOrderId()),15),
                    StringUtils.center(Long.toString(order.getPaid()),15)
            );
        }
        System.out.println(lineMedium);
    }

    public static void viewOrder(OrderLogPM order){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("Date",15),
                StringUtils.center("Delivery Status",15),
                StringUtils.center("Price",15),
                StringUtils.center("Paid",15),
                StringUtils.center("Discount",15)
        );
        System.out.println(lineLarge);

        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center(order.getDate().toString(),15),
                StringUtils.center(order.getDeliveryStatus(),15),
                StringUtils.center(Long.toString(order.getPrice()),15),
                StringUtils.center(Long.toString(order.getPaid()),15),
                StringUtils.center(Integer.toString(order.getDiscount()),15)
        );
        System.out.println(lineLarge);

        System.out.printf("|%s|\n", StringUtils.center("Products",78));

        printAllProducts(order.getProductPMs());
    }

    public static void viewUserDiscounts(List<DisCodeUserPM> discountCodes){
        String[] columns = {"Code","Amount","Start Date","End Date","percentage","max"};
        Object[][] data = new Object[discountCodes.size()][6];
        int i = 0;
        for (DisCodeUserPM code : discountCodes) {
            data[i][0] = code.getDiscountCode();
            data[i][1] = code.getCount();
            data[i][2] = code.getStartTime();
            data[i][3] = code.getEndTime();
            data[i][4] = code.getOffPercentage();
            data[i][5] = code.getMaxOfPriceDiscounted();
        }
        TextTable textTable = new TextTable(columns,data);
        textTable.printTable();
    }

    public static void comparePrinter(FullProductPM[] products){
        String[] columnNames = {"feature",products[0].getProduct().getName(),products[1].getProduct().getName()};
        int size = products[0].getFeatures().size() + 3;
        Object[][] data = new Object[size][3];
        data[0][0] = "Category";
        data[0][1] = data[0][2] = products[0].getProduct().getCategoryName();
        data[1][0] = "Brand";
        data[1][1] = products[0].getProduct().getBrand();
        data[1][2] = products[1].getProduct().getBrand();
        data[2][0] = "Score";
        data[2][1] = products[0].getProduct().getScore();
        data[2][2] = products[1].getProduct().getScore();
        int i=3;
        for (String feature : products[0].getFeatures().keySet()) {
            data[i][0] = feature;
            data[i][1] = products[0].getFeatures().get(feature);
            data[i][2] = products[1].getFeatures().get(feature);
            i++;
        }
        TextTable textTable = new TextTable(columnNames,data);
        textTable.printTable();
    }

    public static void printAllCategories(List<CategoryPM> categories){
        for (CategoryPM category : categories) {
            if (category.getIndent() == 0) System.out.println(lineMedium);
            for (int i = 0; i < category.getIndent(); i++) System.out.print("  ");
            System.out.println(category.getName() + " " + category.getId());
        }
    }

    public static void printComments(List<CommentPM> comments){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("Name",24),
                StringUtils.center("Title",24)
        );
        for (CommentPM comment : comments) {
            System.out.println(lineMedium);
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(comment.getUserName(),24),
                    StringUtils.center(comment.getTitle(),24)
            );
            System.out.println(lineMedium);
            System.out.println(comment.getComment());
            System.out.println(lineMedium);
        }
    }
}
