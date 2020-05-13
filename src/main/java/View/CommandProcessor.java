package View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandProcessor {
    public static void createAccount(String command){

    }

    public static void login(String command){

    }







    protected static Matcher getMatcher(String input, String regex){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
