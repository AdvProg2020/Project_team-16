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

    public static void showAllRequests(){

    }

    protected static Matcher getMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
