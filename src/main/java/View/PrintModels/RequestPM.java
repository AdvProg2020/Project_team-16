package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class RequestPM {
    private String requesterUserName;
    private String requestId;
    private String requestType;
    private String request;
}
