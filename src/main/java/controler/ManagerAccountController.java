package controler;

import ModelPackage.System.DiscountManager;
import ModelPackage.System.exeption.discount.NotValidPercentageException;
import ModelPackage.System.exeption.discount.StartingDateIsAfterEndingDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerAccountController {

    public void createDiscount(String[] data) throws ParseException,
            NotValidPercentageException, StartingDateIsAfterEndingDate {
        Date startTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[0]);
        Date endTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[1]);
        int offPercentage = Integer.parseInt(data[2]);
        long maxDiscount = Long.parseLong(data[3]);
        DiscountManager.getInstance().createDiscountCode(startTime, endTime, offPercentage, maxDiscount);
    }


}
