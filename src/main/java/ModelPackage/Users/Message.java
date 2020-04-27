package ModelPackage.Users;

import View.Data;
import lombok.AllArgsConstructor;

@lombok.Data
@AllArgsConstructor
public class Message {
    private String subject;
    private String message;
    private Data data;
    boolean isRead;



}
