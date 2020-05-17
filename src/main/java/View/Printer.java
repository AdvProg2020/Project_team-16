package View;

import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.UserIntegerMap;
import View.PrintModels.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Printer {
    private String lineSmall= new String(new char[32]).replace('\0', '-');
    private String lineMedium = new String(new char[48]).replace('\0', '-');
    private String lineLarge = new String(new char[80]).replace('\0', '-');

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void MenuPrinter(String name){
        System.out.print(name);
        System.out.println("+----------------------------------------+");
    }

    public static void printMessage(String message){
        System.out.println(message);
    }


    public void productPrinterShort(MiniProductPM product){
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

    public void productPrintFull(FullProductPM product){
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

    public void usersPrinter(List<UserMiniPM> users){
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

    public void userPrinter(UserFullPM user){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n|%s|%s|%s|\n",
                StringUtils.center("Username",16),
                StringUtils.center("First Name",16),
                StringUtils.center("Last Name",16),
                StringUtils.center("Email",16),
                StringUtils.center("Phone Number",16),
                StringUtils.center("Role",16)
        );
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n|%s|%s|%s|\n",
                StringUtils.center(user.getUsername(),16),
                StringUtils.center(user.getFirstName(),16),
                StringUtils.center(user.getLastName(),16),
                StringUtils.center(user.getEmail(),16),
                StringUtils.center(user.getPhoneNumber(),16),
                StringUtils.center(user.getRole(),16)
        );

    }

    public void printAllProducts(List<MiniProductPM> products){
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

    public void printAllDiscountCodes(List<DiscountMiniPM> discountCodes){
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

    public void printDiscountManager(DisCodeManagerPM discountCode){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("Code",12),
                StringUtils.center("Start Date",9),
                StringUtils.center("End Date",9),
                StringUtils.center("Off Percentage",9),
                StringUtils.center("Max Discount Price",9)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center(discountCode.getDiscountCode(),12),
                StringUtils.center(discountCode.getStartTime().toString(),9),
                StringUtils.center(discountCode.getEndTime().toString(),9),
                StringUtils.center(Integer.toString(discountCode.getOffPercentage()),9),
                StringUtils.center(Long.toString(discountCode.getMaxOfPriceDiscounted()),9)
        );
        System.out.println(lineMedium);

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

    public void printAllRequests(List<RequestPM> requests){
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

    public void printDetailedRequest(RequestPM request){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Username",16),
                StringUtils.center("ID",16),
                StringUtils.center("Type",16)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(request.getRequesterUserName(),16),
                StringUtils.center(Integer.toString(request.getRequestId()),16),
                StringUtils.center(request.getRequestType(),16)
        );
        System.out.printf("Request :\n%s\n", request.getRequest());
        System.out.println(lineMedium);
    }

    public void printAllCategories(List<CategoryPM> categories){}

    public void printSaleHistory(List<SellLogPM> sellLogs){
        System.out.println(lineLarge);
        System.out.printf("|%s|%s|%s|%s|%s|\n",
                StringUtils.center("Product",16),
                StringUtils.center("Price",16),
                StringUtils.center("Discount",10),
                StringUtils.center("Buyer",16),
                StringUtils.center("Delivery Status",22)
        );
        System.out.println(lineLarge);

        for (SellLogPM sellLog : sellLogs) {
            System.out.printf("|%s|%s|%s|%s|%s|\n",
                    StringUtils.center(Integer.toString(sellLog.getProductId()),16),
                    StringUtils.center(Integer.toString(sellLog.getMoneyGotten()),16),
                    StringUtils.center(Integer.toString(sellLog.getDiscount()),10),
                    StringUtils.center(sellLog.getBuyer(),16),
                    StringUtils.center(sellLog.getDeliveryStatus().toString(),22)
            );
        }
        System.out.println(lineLarge);
    }

    public void printCompany(CompanyPM company){
        System.out.println(lineMedium);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Name",16),
                StringUtils.center("Phone Number",16),
                StringUtils.center("Group",16)
        );
        System.out.println(lineMedium);

        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(company.getName(),16),
                StringUtils.center(company.getPhone(),16),
                StringUtils.center(company.getGroup(),16)
        );
        System.out.println(lineMedium);

    }

    public void viewOffs(List<OffPM> offs){}
    public void viewBalance(long balance){}
    public void viewCart(CartPM cartPM){}
    public void viewProductsInCart(InCartPM cart){}
    public void viewOrderHistory (List<OrderLogPM> orders){}
    public void viewUserDiscounts(DisCodeUserPM discountCode){}
    public void comparePrinter(FullProductPM[] products){}

    public void printComments(List<CommentPM> comments){
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
