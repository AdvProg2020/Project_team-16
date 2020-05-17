package View;

import ModelPackage.Maps.SellerIntegerMap;
import View.PrintModels.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Printer {
    private String line = new String(new char[48]).replace('\0', '-');

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
        System.out.println(line);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Name",20),
                StringUtils.center("ID", 8),
                StringUtils.center("Category",16)
        );

        System.out.println(line);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(product.getName(), 20),
                StringUtils.center(Integer.toString(product.getId()), 8),
                StringUtils.center(product.getCategoryName(), 16)
        );

        System.out.println(line);
        System.out.println("| Sellers : ");
        for (SellerIntegerMap seller : product.getSellers()) {
            System.out.printf("%s In Stock : %d |",
                    seller.getSeller().getUsername(),
                    seller.getInteger()
            );
        }
        System.out.println("\b\n");

        System.out.println(line);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center("Brand",20),
                StringUtils.center("Score", 8),
                StringUtils.center("About",16)
        );

        System.out.println(line);
        System.out.printf("|%s|%s|%s|\n",
                StringUtils.center(product.getBrand(), 20),
                StringUtils.center(Double.toString(product.getScore()), 8),
                StringUtils.center(product.getDescription(), 16)
        );
    }

    public void productPrintFull(FullProductPM product){
        productPrinterShort(product.getProduct());

        System.out.println(line);
        System.out.printf("|%s|\n", StringUtils.center("Features",48));
        System.out.println(line);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("Special",24),
                StringUtils.center("Public",24)
        );
        for (String special : product.getFeatures().keySet()) {
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(special,24),
                    StringUtils.center(product.getFeatures().get(special),24)
            );
            System.out.println(line);
        }
    }

    public void usersPrinter(List<UserMiniPM> users){}
    public void userPrinter(UserFullPM user){}
    public void printAllProducts(List<MiniProductPM> products){}
    public void printAllDiscountCodes(List<DiscountMiniPM> discountCode){}
    public void printDiscountManager(DisCodeManagerPM discountCode){}
    public void printAllRequests(List<RequestPM> requests){}
    public void printDetailedRequest(RequestPM request){}
    public void printAllCategories(List<CategoryPM> categories){}
    public void printCompany(CompanyPM company){}
    public void printSaleHistory(List<SellLogPM> sellLog){}
    public void viewOffs(List<OffPM> offs){}
    public void viewBalance(long balance){}
    public void viewCart(CartPM cartPM){}
    public void viewProductsInCart(InCartPM cart){}
    public void viewOrderHistory (List<OrderLogPM> orders){}
    public void viewUserDiscounts(DisCodeUserPM discountCode){}
    public void comparePrinter(FullProductPM[] products){}

    public void printComments(List<CommentPM> comments){
        System.out.println(line);
        System.out.printf("|%s|%s|\n",
                StringUtils.center("Name",24),
                StringUtils.center("Title",24)
        );
        for (CommentPM comment : comments) {
            System.out.println(line);
            System.out.printf("|%s|%s|\n",
                    StringUtils.center(comment.getUserName(),24),
                    StringUtils.center(comment.getTitle(),24)
            );
            System.out.println(line);
            System.out.println(comment.getComment());
            System.out.println(line);
        }
    }
}
