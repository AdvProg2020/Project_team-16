package View.PrintModels;

import lombok.Data;

import java.util.Date;

@Data public class OrderMiniLog {
    private Date[] dates;
    private String[] orderIds;
    private int[] paid;
}
