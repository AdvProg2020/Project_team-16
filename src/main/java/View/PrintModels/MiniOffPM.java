package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data
public class MiniOffPM {
    private int offId;
    private String seller;
    private Date startTime;
    private Date endTime;
    private int offPercentage;

    public MiniOffPM(int offId, String seller, Date startTime, Date endTime, int offPercentage) {
        this.offId = offId;
        this.seller = seller;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
    }
}
