package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
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

    public void editDiscountStartingDate(String code, Date newStartingDate) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setStartTime(newStartingDate);
    }

    public void editDiscountEndingDate(String code, Date newEndingDate) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setEndTime(newEndingDate);
    }

    public void editDiscountOffPercentage(String code, int newPercentage) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setOffPercentage(newPercentage);
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
