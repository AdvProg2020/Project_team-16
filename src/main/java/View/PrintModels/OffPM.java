package View.PrintModels;

import lombok.Data;
import java.sql.Time;

@Data
public class OffPM {
    private String offId;
    private String[] productNames;
    private String seller;
    private Time startTime;
    private Time endTime;
    private int offPercentage;
}
