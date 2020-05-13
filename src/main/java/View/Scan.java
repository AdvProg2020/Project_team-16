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
}
