package View;

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
}
