package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.Users.Customer;
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
            throws NoSuchADiscountCodeException, StartingDateIsAfterEndingDate {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfStartingDateIsBeforeEndingDate(discountCode.getStartTime(), newEndingDate);
        discountCode.setEndTime(newEndingDate);
    }

    private void checkIfStartingDateIsBeforeEndingDate(Date startDate, Date newEndingDate)
            throws StartingDateIsAfterEndingDate {
        if (newEndingDate.before(startDate))
            throw new StartingDateIsAfterEndingDate();
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

    public void editDiscountMaxDiscount(String code, long newMaxDiscount)
            throws NoSuchADiscountCodeException, NegativeMaxDiscountException {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfMaxDiscountIsPositive(newMaxDiscount);
        discountCode.setMaxDiscount(newMaxDiscount);
    }

    private void checkIfMaxDiscountIsPositive(long newMaxDiscount)
            throws NegativeMaxDiscountException {
        if (newMaxDiscount < 0)
            throw new NegativeMaxDiscountException(newMaxDiscount);
    }

    public void addUserToDiscountCodeUsers(String code, User newUser, int timesToUse)
            throws NoSuchADiscountCodeException, UserExistedInDiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfUserExists(newUser, discountCode);
        discountCode.getUsers().put(newUser, timesToUse);
    }

    private void checkIfUserExists(User user, DiscountCode discountCode)
            throws UserExistedInDiscountCodeException {
        for (User user1 : discountCode.getUsers().keySet()) {
            if (user.equals(user1))
                throw new UserExistedInDiscountCodeException(user.getUsername());
        }
    }

    public void removeUserFromDiscountCodeUsers(String code, User user)
            throws NoSuchADiscountCodeException, UserNotExistedInDiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfUserDoesNotExistInDiscount(user, discountCode);
        discountCode.getUsers().remove(user);
    }

    private void checkIfUserDoesNotExistInDiscount(User user, DiscountCode discountCode)
            throws UserNotExistedInDiscountCodeException {
        if (!discountCode.getUsers().keySet().contains(user) || discountCode.getUsers().get(user) == 0)
            throw new UserNotExistedInDiscountCodeException(user.getUsername());
    }

    public List<DiscountCode> showAllDiscountCodes() {
        return getDiscountCodes();
    }

    public DiscountCode showDiscountCode(String code) throws NoSuchADiscountCodeException {
        return getDiscountByCode(code);
    }

    public void createDiscountCode(Date startTime, Date endTime, int offPercentage, long maxDiscount)
            throws NotValidPercentageException, StartingDateIsAfterEndingDate {
        checkIfStartingDateIsBeforeEndingDate(startTime, endTime);
        checkIfPercentageIsValid(offPercentage);
        DiscountCode discountCode = new DiscountCode(startTime, endTime, offPercentage, maxDiscount);
        discountCodes.add(discountCode);
    }

    public void useADiscount(User user, String code)
            throws NoSuchADiscountCodeException, UserNotExistedInDiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        checkIfUserDoesNotExistInDiscount(user, discountCode);
        int old = discountCode.getUsers().get(user);
        discountCode.getUsers().put(user, old-1);

        Customer customer = (Customer) user;
        customer.getDiscountCodes().replace(discountCode, old-1);
    }
}
