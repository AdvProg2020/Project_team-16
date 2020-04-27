package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
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

    public DiscountCode getDiscountByCode(String code) {
        for (DiscountCode discountCode : discountCodes) {
            if (code.equals(discountCode.getCode()))
                return discountCode;
        }
        return null;
    }

    public boolean isDiscountAvailable(String code) {
        DiscountCode discountCode = getDiscountByCode(code);
        Date date = new Date();
        Date startDate = discountCode.getStartTime();
        Date endDate = discountCode.getEndTime();
        return !date.before(startDate) && !date.after(endDate);
    }

    public void removeDiscount(String code) {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCodes.remove(discountCode);
    }

    public void editDiscountStartingDate(String code, Date newStartingDate) {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setStartTime(newStartingDate);
    }

    public void editDiscountEndingDate(String code, Date newEndingDate) {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setEndTime(newEndingDate);
    }

    public void editDiscountOffPercentage(String code, int newPercentage) {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setOffPercentage(newPercentage);
    }

    public void editDiscountMaxDiscount(String code, long newMaxDiscount) {
        DiscountCode discountCode = getDiscountByCode(code);
        discountCode.setMaxDiscount(newMaxDiscount);
    }

    public void addUserToDiscountCodeUsers(String code, User newUser, int timesToUse) {
        DiscountCode discountCode = getDiscountByCode(code);
        // TODO : check if user exists
        discountCode.getUsers().put(newUser, timesToUse);
    }

    public void removeUserFromDiscountCodeUsers(String code, User user) {
        DiscountCode discountCode = getDiscountByCode(code);
        // TODO : check if user exists
        discountCode.getUsers().remove(user);
    }

    public List<DiscountCode> showAllDiscountCodes() {
        return getDiscountCodes();
    }

    public DiscountCode showDiscountCode(String code) {
        return getDiscountByCode(code);
    }

    public void createDiscountCode(Date startTime, Date endTime, int offPercentage, long maxDiscount) {
        // TODO : check if startTime is before endTime
        // TODO : check if offPercentage is smaller than 100
        DiscountCode discountCode = new DiscountCode(startTime, endTime, offPercentage, maxDiscount);
        discountCodes.add(discountCode);
    }
}
