package View;

import View.exceptions.InvalidCharacter;
import View.exceptions.OutOfRangeInputException;

import java.util.Scanner;

public class Scan {
    private static Scan scan = new Scan();
    private Scanner scanner;

    public static Scan getInstance() {
        return scan;
    }

    private Scan(){
        scanner = new Scanner(System.in);
    }

    public String getLine(){
        return scanner.nextLine();
    }

    String getPassword(String message){
        boolean contini = true;
        String password = "";
        while (contini) {
            Printer.printMessage(message);
            password = scanner.nextLine();
            int strength = calculatePasswordStrength(password);
            if (strength == 0) {
                Printer.printMessage("\npassword must be more than 8 character");
                continue;
            } else if (strength < 4) Printer.printMessage("\npassword is weak");
            else if (strength < 6) Printer.printMessage("\npassword is good");
            else if (strength < 8) Printer.printMessage("\npassword is strong");
            else if (strength == 10) Printer.printMessage("\npassword is INSANE! hey buddy it's not a FBI account :)");
            Printer.printMessage("\nDo you want to use a new one ?");
            String todo = scanner.nextLine();
            switch (todo){
                case "yes" : case "y" : case "Yes" :  break;
                case "no" : case "n" : case "NO" : case "No" : contini = false;
            }
        }
        return password;
    }

    String getADate(){
        while (true){
            String date = scanner.nextLine();
            if (date.matches("\\b\\d{4}-\\d{2}-\\d{2} \\d{1,2}:\\d{2}:\\d{2}\\b"))return date;
            else Printer.printMessage("invalid date Enter a valid one : ");
        }
    }

    String getNullAbleDate(){
        while (true){
            String date = scanner.nextLine();
            if (date.matches("\\b\\d{4}-\\d{2}-\\d{2} \\d{1,2}:\\d{2}:\\d{2}\\b"))return date;
            else if (date.isEmpty())return date;
            else Printer.printMessage("invalid date Enter a valid one : ");
        }
    }

    String getPercentage(){
         while (true){
             String input = scanner.nextLine();
             if (input.matches("100|[0-9][0-9]"))return input;
             else Printer.printMessage("Enter a valid Percentage : ");
         }
    }

    String getNullAblePercentage(){
        while (true){
            String input = scanner.nextLine();
            if (input.matches("(100|[0-9][0-9])?"))return input;
            else Printer.printMessage("Enter a valid Percentage : ");
        }
    }

    String getLong(){
        while (true){
            String input = scanner.nextLine();
            if (input.matches("^\\d{1,19}$"))return input;
            else Printer.printMessage("Enter a valid number : ");
        }
    }

    String getNullAbleLong(){
        while (true){
            String input = scanner.nextLine();
            if (input.matches("(^\\d{1,19}$)?"))return input;
            else Printer.printMessage("Enter a valid number : ");
        }
    }

    String getInteger(){
        while (true){
            String input = scanner.nextLine();
            if (input.matches("^\\d{1,9}$")) return input;
            else Printer.printMessage("Enter a valid number");
        }
    }

    public String getLinesUntil(String end){
        StringBuilder stringBuilder = new StringBuilder();
        String line ;
        while (!(line = scanner.nextLine()).equals(end)){
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public String getFormalLines(int limit)
            throws InvalidCharacter, OutOfRangeInputException {
        String line = scanner.nextLine();
        if (line.matches("")){
            throw new InvalidCharacter();
        }
        else if (line.length() > limit) {
            throw new OutOfRangeInputException(limit);
        }
        return line;
    }

    private static int calculatePasswordStrength(String password){
        int iPasswordScore = 0;

        if( password.length() < 8 )
            return 0;
        else if( password.length() >= 10 )
            iPasswordScore += 2;
        else
            iPasswordScore += 1;

        if( password.matches("(?=.*[0-9]).*") )
            iPasswordScore += 2;
        if( password.matches("(?=.*[a-z]).*") )
            iPasswordScore += 2;
        if( password.matches("(?=.*[A-Z]).*") )
            iPasswordScore += 2;
        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
            iPasswordScore += 2;

        return iPasswordScore;
    }


}
