package View.PrintModels;

import lombok.Data;

import java.util.Date;
import java.util.ArrayList;

@Data
public class OffPM {
    private int offId;
    private ArrayList<MiniProductPM> products;
    private String seller;
    private Date startTime;
    private Date endTime;
    private int offPercentage;
    private String status;

    public OffPM(int offId, ArrayList<MiniProductPM> products, String seller, Date startTime, Date endTime, int offPercentage, String status) {
        this.offId = offId;
        this.products = products;
        this.seller = seller;
        this.startTime = startTime;
        this.endTime = endTime;
        this.offPercentage = offPercentage;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.valueOf(offId);
    }
}
