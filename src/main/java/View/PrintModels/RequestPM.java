package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class RequestPM {
    private String requesterUserName;
    private int requestId;
    private String requestType;
    private String request;

    public RequestPM(String requesterUserName, int requestId, String requestType, String request) {
        this.requesterUserName = requesterUserName;
        this.requestId = requestId;
        this.requestType = requestType;
        this.request = request;
    }
}
