package controler;

import ModelPackage.System.MessageManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Message;
import ModelPackage.Users.User;
import View.PrintModels.MessagePM;

import java.util.ArrayList;
import java.util.List;

public class MessageController extends Controller {
    private static MessageController messageController = new MessageController();

    private MessageController() {}

    public static MessageController getInstance() {
        return messageController;
    }

    public ArrayList<MessagePM> getMessagesForUser(String username) throws UserNotAvailableException {
        User user = accountManager.getUserByUsername(username);
        List<Message> list = MessageManager.getInstance().getAllMesagesOfThisUser(user);
        ArrayList<MessagePM> toReturn = new ArrayList<>();
        for (Message message : list) {
            toReturn.add(new MessagePM(message.getId(),message.getSubject(),message.getMessage(),message.getIsRead(),message.getDate()));
        }
        return toReturn;
    }

    public void openMessage(int id){
        MessageManager.getInstance().setMessageAsRead(id);
    }
}
