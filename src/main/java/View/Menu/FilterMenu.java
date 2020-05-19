package View.Menu;

import ModelPackage.System.exeption.category.NoSuchACategoryException;
import View.Data;
import View.Printer;
import View.Scan;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;
import controler.SellerController;

public class FilterMenu extends Menu {
    public FilterMenu(Menu parent) {
        super("Filter Menu" , parent);
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.equalsIgnoreCase("show available filters")){
            showAvailableFilters();
        }else if (command.startsWith("filter")){
            filter(command);
        }else if (command.equalsIgnoreCase("current filters")){
            currentFilters();
        }else if (command.startsWith("disable a filter")){
            disableAFilter(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    private void filter(String command) {

    }

    private void currentFilters() {
        Data.getInstance().printFilters();
    }

    private void disableAFilter(String command) {
        command = command.replaceFirst("disable a filter ","");
        Data.getInstance().getFilters().disableAFilter(command);
    }

    private void showAvailableFilters() {
        Printer.printMessage("Enter category id you want filter(enter 0 for just public filter) : ");
        String catId = Scan.getInstance().getInteger();
        try {
            Data.getInstance().setSpecialFeatures(SellerController.getInstance().getSpecialFeaturesOfCat(Integer.parseInt(catId)));
        } catch (NoSuchACategoryException e) {
            Printer.printMessage(e.getMessage());
            return;
        }
        Data.getInstance().allPrintFilters();
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }

    @Override
    void additionalPrints() {
        Data.getInstance().setPublicFeatures(SellerController.getInstance().getPublicFeatures());
    }
}
