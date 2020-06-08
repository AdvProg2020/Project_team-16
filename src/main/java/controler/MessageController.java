package controler;

import ModelPackage.System.MessageManager;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Message;
import ModelPackage.Users.User;
import View.PrintModels.MessagePM;

import java.util.ArrayList;
import java.util.List;

public class MessageController extends Controller {
    public List<MessagePM> getMessagesForUser(String username) throws UserNotAvailableException {
        User user = accountManager.getUserByUsername(username);
        List<Message> list = MessageManager.getInstance().getAllMesagesOfThisUser(user);
        List<MessagePM> toReturn = new ArrayList<>();
        for (Message message : list) {
            toReturn.add(new MessagePM(message.getId(),message.getSubject(),message.getMessage(),message.getIsRead(),message.getDate()));
        }
        return toReturn;
    }

    public void openMessage(int id){
        MessageManager.getInstance().setMessageAsRead(id);
    }
}
