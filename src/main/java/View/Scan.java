package View;

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


}
