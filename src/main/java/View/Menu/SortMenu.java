package View.Menu;

import ModelPackage.System.SortType;
import View.CommandProcessor;
import View.Data;
import View.Printer;
import View.SortPackage;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class SortMenu extends Menu {
    public SortMenu(Menu parent) {
        super("Sort Menu",parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("show available sorts")){
            showAvailableSorts();
        }else if (command.equalsIgnoreCase("current sort")){
            SortPackage sortPackage = Data.getInstance().getSorts();
            Printer.printMessage("Current Sort : " + (sortPackage.isAscending()? "Ascending ":"Descending ") + sortPackage.getSortType());
        }else if (command.equalsIgnoreCase("disable sort")){
            disableSort();
        }else if (command.startsWith("sort")){
            sort(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }

    @Override
    void additionalPrints() {

    }

    private void showAvailableSorts(){
        Printer.printMessage("1.name\n" +
                "view\n" +
                "price\n" +
                "bought amount\n" +
                "time\n" +
                "score\n" +
                "And Ascending and Descending are available for all");
    }

    private void disableSort(){
        Data.getInstance().getSorts().reset();
    }

    private void sort(String command){
        command = command.replaceFirst("sort ","");
        SortPackage sortPackage = Data.getInstance().getSorts();
        switch (command){
            case "name" : sortPackage.setSortType(SortType.NAME);break;
            case "view" : sortPackage.setSortType(SortType.VIEW);break;
            case "price" : sortPackage.setSortType(SortType.LESS_PRICE);break;
            case "bought amount" : sortPackage.setSortType(SortType.BOUGHT_AMOUNT);break;
            case "time" : sortPackage.setSortType(SortType.TIME);break;
            case "score" : sortPackage.setSortType(SortType.SCORE);break;
            case "Ascending" : case "ascending" : sortPackage.setAscending(true);break;
            case "Descending" : case "descending" : sortPackage.setAscending(false);break;
        }
    }
}
