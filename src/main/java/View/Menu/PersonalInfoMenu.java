package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class PersonalInfoMenu extends Menu {
    public PersonalInfoMenu(Menu parent) {
        super("Personal Info Menu" , parent);
    }

    @Override
    public void execute() {
        CommandProcessor.viewPersonalInfo();
        super.execute();
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("edit")){
            CommandProcessor.editPersonalInfo(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
