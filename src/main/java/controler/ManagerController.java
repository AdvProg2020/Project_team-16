package controler;

import ModelPackage.System.AccountManager;
import ModelPackage.System.DiscountManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.discount.NotValidPercentageException;
import ModelPackage.System.exeption.discount.StartingDateIsAfterEndingDate;
import ModelPackage.Users.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManagerController {
    private AccountManager accountManager = AccountManager.getInstance();

    public List<User> manageUsers () {
        return DBManager.loadAllData(User.class);
    }

    public void createDiscount(String[] data) throws ParseException,
            NotValidPercentageException, StartingDateIsAfterEndingDate {
        Date startTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[0]);
        Date endTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(data[1]);
        int offPercentage = Integer.parseInt(data[2]);
        long maxDiscount = Long.parseLong(data[3]);
        DiscountManager.getInstance().createDiscountCode(startTime, endTime, offPercentage, maxDiscount);
    }
}
