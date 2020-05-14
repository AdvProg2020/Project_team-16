package View.Menu;

import View.CommandProcessor;
import View.exceptions.InvalidCommandException;
import View.exceptions.NotAnAvailableMenu;

public class ManagerRequestMenu extends Menu {
    public ManagerRequestMenu(Menu parent) {
        super("Request Menu",parent);
    }

    @Override
    public void execute() {
        CommandProcessor.viewAllRequests();
        super.execute();
    }

    @Override
    void helpPrinter() {
        /*TODO*/
    }

    @Override
    void executeValidCommand(String command) throws InvalidCommandException {
        if (command.startsWith("details")){
            CommandProcessor.viewRequest(command);
        }else if (command.startsWith("accept")){
            CommandProcessor.acceptRequest(command);
        }else if (command.startsWith("decline")){
            CommandProcessor.declineRequest(command);
        }else {
            throw new InvalidCommandException();
        }
    }

    @Override
    void goToSubMenusIfAvailable(String menuName) throws NotAnAvailableMenu {
        throw new NotAnAvailableMenu();
    }
}
