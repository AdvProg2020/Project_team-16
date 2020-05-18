package View;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Users.Cart;
import View.PrintModels.*;
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

    public static void viewBalance(long balance){
        System.out.println(lineSmall);
        System.out.printf("|%s : %s|\n", StringUtils.center("Balance",14), StringUtils.center(Long.toString(balance),13));
        System.out.println(lineSmall);
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

    public static void viewUserDiscounts(DisCodeUserPM discountCode){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Code",15),
                StringUtils.center("Start Date",15),
                StringUtils.center("End Date",15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(discountCode.getDiscountCode(),15),
                StringUtils.center(discountCode.getStartTime().toString(),15),
                StringUtils.center(discountCode.getEndTime().toString(),15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Off Percentage",15),
                StringUtils.center("Max Discount Price",15),
                StringUtils.center("Count",15)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(Integer.toString(discountCode.getOffPercentage()),15),
                StringUtils.center(Long.toString(discountCode.getMaxOfPriceDiscounted()),15),
                StringUtils.center(Integer.toString(discountCode.getCount()),15)
        );
        System.out.println(lineMedium);
    }

    public static void comparePrinter(FullProductPM[] products){
        System.out.println(lineMedium);
        System.out.printf("          |%s|%s|\n",
                StringUtils.center(String.format("%s(%d)", products[0].getProduct().getName(), products[0].getProduct().getId()),18),
                StringUtils.center(String.format("%s(%d)", products[1].getProduct().getName(), products[1].getProduct().getId()),18)
        );
        System.out.println(lineMedium);

        System.out.printf("%s|%s|%s|\n",
                StringUtils.center("Category:",10),
                StringUtils.center(products[0].getProduct().getCategoryName(),18),
                StringUtils.center(products[1].getProduct().getCategoryName(),18)
        );
        System.out.println(lineMedium);

        System.out.printf("%s|%s|%s|\n",
                StringUtils.center("Brand:",10),
                StringUtils.center(products[0].getProduct().getBrand(),18),
                StringUtils.center(products[1].getProduct().getBrand(),18)
        );
        System.out.println(lineMedium);

        System.out.printf("%s|%s|%s|\n",
                StringUtils.center("Score:",10),
                StringUtils.center(Double.toString(products[0].getProduct().getScore()),18),
                StringUtils.center(Double.toString(products[1].getProduct().getScore()),18)
        );
        System.out.println(lineMedium);

        System.out.printf("          |%s|\n",
                StringUtils.center("Features",37)
        );
        System.out.println(lineMedium);


    }

    public static void printAllCategories(List<CategoryPM> categories){}

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
