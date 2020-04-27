package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.System.exeption.discount.EndingTimeIsBeforeStartingTimeException;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.discount.NotValidPercentageException;
import ModelPackage.System.exeption.discount.StartingDateIsAfterEndingDate;
import ModelPackage.Users.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DiscountManager {
    private List<DiscountCode> discountCodes;

    private static DiscountManager discountManager = new DiscountManager();

    public static DiscountManager getInstance() {
        return discountManager;
    }

    private DiscountManager() {
        this.discountCodes = new ArrayList<>();
    }

    public DiscountCode getDiscountByCode(String code) throws NoSuchADiscountCodeException {
        for (DiscountCode discountCode : discountCodes) {
            if (code.equals(discountCode.getCode()))
                return discountCode;
        }
        throw new NoSuchADiscountCodeException(code);
    }

    public boolean isDiscountAvailable(String code) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        Date date = new Date();
        Date startDate = discountCode.getStartTime();
        Date endDate = discountCode.getEndTime();
        return !date.before(startDate) && !date.after(endDate);
    }

    public void removeDiscount(String code) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCodes.remove(discountCode);
    }

    public void editDiscountStartingDate(String code, Date newStartingDate)
            throws NoSuchADiscountCodeException, StartingDateIsAfterEndingDate {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfNewStartingDateIsBeforeEndingDate(discountCode, newStartingDate);
        discountCode.setStartTime(newStartingDate);
    }

    private void checkIfNewStartingDateIsBeforeEndingDate(DiscountCode discountCode, Date newStartingDate)
            throws StartingDateIsAfterEndingDate {
        if (newStartingDate.after(discountCode.getEndTime()))
            throw new StartingDateIsAfterEndingDate();
    }

    public void editDiscountEndingDate(String code, Date newEndingDate)
            throws NoSuchADiscountCodeException, EndingTimeIsBeforeStartingTimeException {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfEndingDateIsBeforeStartingDate(discountCode, newEndingDate);
        discountCode.setEndTime(newEndingDate);
    }

    private void checkIfEndingDateIsBeforeStartingDate(DiscountCode discountCode, Date newEndingDate)
            throws EndingTimeIsBeforeStartingTimeException {
        if (newEndingDate.before(discountCode.getStartTime()))
            throw new EndingTimeIsBeforeStartingTimeException();
    }

    public void editDiscountOffPercentage(String code, int newPercentage)
            throws NoSuchADiscountCodeException, NotValidPercentageException {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfPercentageIsValid(newPercentage);
        discountCode.setOffPercentage(newPercentage);
    }

    private void checkIfPercentageIsValid(int newPercentage) throws NotValidPercentageException {
        if (newPercentage > 100 || newPercentage < 0)
            throw new NotValidPercentageException(newPercentage);
    }

    public void editDiscountMaxDiscount(String code, long newMaxDiscount) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setMaxDiscount(newMaxDiscount);
    }

    public void addUserToDiscountCodeUsers(String code, User newUser, int timesToUse) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        // TODO : check if user exists
        discountCode.getUsers().put(newUser, timesToUse);
    }

    public void removeUserFromDiscountCodeUsers(String code, User user) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        // TODO : check if user exists
        discountCode.getUsers().remove(user);
    }

    public List<DiscountCode> showAllDiscountCodes() {
        return getDiscountCodes();
    }

    public DiscountCode showDiscountCode(String code) throws NoSuchADiscountCodeException {
        return getDiscountByCode(code);
    }

    public void createDiscountCode(Date startTime, Date endTime, int offPercentage, long maxDiscount) {
        // TODO : check if startTime is before endTime
        // TODO : check if offPercentage is smaller than 100
        DiscountCode discountCode = new DiscountCode(startTime, endTime, offPercentage, maxDiscount);
        discountCodes.add(discountCode);
    }
}
