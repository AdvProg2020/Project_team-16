package View.PrintModels;

import lombok.Data;

@Data
public class CommentPM {
    private String userName;
    private String title;
    private String comment;

    public CommentPM(String userName, String title, String comment) {
        this.userName = userName;
        this.title = title;
        this.comment = comment;
    }
}
