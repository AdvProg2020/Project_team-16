package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder
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
