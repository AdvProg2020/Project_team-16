package ModelPackage.System;

import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.Users.Customer;
import ModelPackage.Users.User;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
public class DiscountManager {
    private static DiscountManager discountManager = new DiscountManager();

    public static DiscountManager getInstance() {
        return discountManager;
    }

    public DiscountCode getDiscountByCode(String code) throws NoSuchADiscountCodeException {
        DiscountCode CODE = DBManager.load(DiscountCode.class,code);
        if (CODE == null)
            throw new NoSuchADiscountCodeException(code);
        return CODE;
    }

    public boolean isDiscountAvailable(String code) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        Date date = new Date();
        Date startDate = discountCode.getStartTime();
        Date endDate = discountCode.getEndTime();
        return date.after(startDate) && date.before(endDate);
    }

    public void removeDiscount(String code) throws NoSuchADiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        DBManager.delete(discountCode);
    }

    public void editDiscountCode(String code, DiscountCodeEditAttributes editAttributes)
            throws NoSuchADiscountCodeException, StartingDateIsAfterEndingDate,
            NotValidPercentageException, NegativeMaxDiscountException {
        DiscountCode discountCode = getDiscountByCode(code);

        Date newStart = editAttributes.getStart();
        Date newEnd = editAttributes.getEnd();
        int newOffPercent = editAttributes.getOffPercent();
        int newMaxDiscount = editAttributes.getMaxDiscount();

        if (newStart != null){
            checkIfNewStartingDateIsBeforeEndingDate(discountCode, newStart);
            discountCode.setStartTime(newStart);
        }
        // TODO : checkIfStartingDateIsBeforeEndingDate(discountCode.getStartTime(), newEnd);
        if (newEnd != null){
            checkIfNewStartingDateIsBeforeEndingDate(discountCode, newEnd);
            discountCode.setEndTime(newEnd);
        }
        if (newOffPercent != 0) {
            checkIfPercentageIsValid(newOffPercent);
            discountCode.setOffPercentage(newOffPercent);
        }
        if (newMaxDiscount != 0) {
            checkIfMaxDiscountIsPositive(newMaxDiscount);
            discountCode.setMaxDiscount(newMaxDiscount);
        }

        DBManager.save(discountCode);
    }

    private void checkIfNewStartingDateIsBeforeEndingDate(DiscountCode discountCode, Date newStartingDate)
            throws StartingDateIsAfterEndingDate {
        if (newStartingDate.after(discountCode.getEndTime()))
            throw new StartingDateIsAfterEndingDate();
    }

    private void checkIfStartingDateIsBeforeEndingDate(Date startDate, Date newEndingDate)
            throws StartingDateIsAfterEndingDate {
        if (newEndingDate.before(startDate))
            throw new StartingDateIsAfterEndingDate();
    }

    private void checkIfPercentageIsValid(int newPercentage) throws NotValidPercentageException {
        if (newPercentage > 100 || newPercentage < 0)
            throw new NotValidPercentageException(newPercentage);
    }

    private void checkIfMaxDiscountIsPositive(long newMaxDiscount)
            throws NegativeMaxDiscountException {
        if (newMaxDiscount < 0)
            throw new NegativeMaxDiscountException(newMaxDiscount);
    }

    public void addUserToDiscountCodeUsers(String code, User newUser, int timesToUse)
            throws NoSuchADiscountCodeException, UserExistedInDiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        if (checkIfUserExists(newUser, discountCode))throw new UserExistedInDiscountCodeException(newUser.getUsername());
        UserIntegerMap map = new UserIntegerMap();
        map.setInteger(timesToUse);
        map.setUser(newUser);
        discountCode.getUsers().add(map);
        DBManager.save(discountCode);
    }

    private boolean checkIfUserExists(User user, DiscountCode discountCode) {
        String username = user.getUsername();
        for (UserIntegerMap map : discountCode.getUsers()) {
            if (map.getUser().getUsername().equals(username))
                return true;
        }
        return false;
    }

    private UserIntegerMap findMap(User user, DiscountCode discountCode){
        String username = user.getUsername();
        for (UserIntegerMap map : discountCode.getUsers()) {
            if (map.getUser().getUsername().equals(username))
                return map;
        }
        return null;
    }

    public void removeUserFromDiscountCodeUsers(String code, User user)
            throws NoSuchADiscountCodeException, UserNotExistedInDiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        UserIntegerMap map = findMap(user,discountCode);
        if (map == null) throw new UserNotExistedInDiscountCodeException(user.getUsername());
        discountCode.getUsers().remove(map);
        DBManager.save(discountCode);
    }

    public DiscountCode showDiscountCode(String code) throws NoSuchADiscountCodeException {
        return getDiscountByCode(code);
    }

    public void createDiscountCode(String code,Date startTime, Date endTime, int offPercentage, long maxDiscount)
            throws NotValidPercentageException, StartingDateIsAfterEndingDate {
        checkIfStartingDateIsBeforeEndingDate(startTime, endTime);
        checkIfPercentageIsValid(offPercentage);
        /* TODO : Check for repeated code */
        DiscountCode discountCode = new DiscountCode(code,startTime, endTime, offPercentage, maxDiscount);
        DBManager.save(discountCode);
    }

    public void useADiscount(User user, String code)
            throws NoSuchADiscountCodeException, UserNotExistedInDiscountCodeException, NoMoreDiscount {
        DiscountCode discountCode = getDiscountByCode(code);
        UserIntegerMap map = findMap(user,discountCode);
        if (map == null) throw new UserNotExistedInDiscountCodeException(user.getUsername());
        int old = map.getInteger();
        if (old == 0){
            DBManager.delete(discountCode);
            throw new NoMoreDiscount();
        }
        else
            map.setInteger(old-1);

        DiscountcodeIntegerMap discountcodeIntegerMap = findDiscountMap(user,code);
        if (discountcodeIntegerMap == null) throw new UserNotExistedInDiscountCodeException(user.getUsername());

        discountcodeIntegerMap.setInteger(old-1);
        DBManager.save(discountcodeIntegerMap);
    }

    private DiscountcodeIntegerMap findDiscountMap(User user,String code){
        Customer customer = (Customer) user;
        for (DiscountcodeIntegerMap map : customer.getDiscountCodes()) {
            if (map.getDiscountCode().getCode().equals(code)) return map;
        }
        return null;
    }
}
