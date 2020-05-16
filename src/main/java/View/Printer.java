package View;

import View.PrintModels.*;

import java.util.List;

public class Printer {
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


        public void productPrinterShort(MiniProductPM product){}
        public void productPrintFull(FullProductPM product){}
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
        public void printComments(List<CommentPM> comments){}
}
