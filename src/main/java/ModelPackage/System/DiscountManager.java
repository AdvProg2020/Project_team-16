package ModelPackage.System;

import ModelPackage.Maps.DiscountcodeIntegerMap;
import ModelPackage.Maps.SellerIntegerMap;
import ModelPackage.Maps.UserIntegerMap;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.discount.*;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.Users.Customer;
import ModelPackage.Users.Seller;
import ModelPackage.Users.SubCart;
import ModelPackage.Users.User;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

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
        removeDiscount(discountCode);
    }

    void removeDiscount(DiscountCode discountCode) {
        deleteMapsForUsers(discountCode);
        DBManager.delete(discountCode);
    }

    private void deleteMapsForUsers(DiscountCode discountCode) {
        discountCode.getUsers().forEach(userIntegerMap -> deleteMapForUser(userIntegerMap, discountCode));
    }

    private void deleteMapForUser(UserIntegerMap map, DiscountCode code) {
        code.getUsers().remove(map);
        DBManager.save(code);
        DBManager.delete(map);
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
            checkIfStartingDateIsBeforeEndingDate(newStart, discountCode.getEndTime());
            discountCode.setStartTime(newStart);
        }
        if (newEnd != null){
            checkIfStartingDateIsBeforeEndingDate(discountCode.getStartTime(), newEnd);
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

    public void addUserToDiscountCodeUsers(String code, Customer newUser, int timesToUse)
            throws NoSuchADiscountCodeException, UserExistedInDiscountCodeException {
        DiscountCode discountCode = getDiscountByCode(code);
        if (checkIfUserExists(newUser, discountCode))throw new UserExistedInDiscountCodeException(newUser.getUsername());
        UserIntegerMap map = new UserIntegerMap();
        map.setDiscountCode(discountCode);
        map.setInteger(timesToUse);
        map.setUser(newUser);
        discountCode.getUsers().add(map);

        DiscountcodeIntegerMap discountcodeIntegerMap = new DiscountcodeIntegerMap();
        discountcodeIntegerMap.setDiscountCode(discountCode);
        discountcodeIntegerMap.setInteger(timesToUse);
        newUser.getDiscountCodes().add(discountcodeIntegerMap);

        DBManager.save(newUser);
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
        DiscountcodeIntegerMap mapInUser = findDiscountMap(user,code);
        Customer customer = DBManager.load(Customer.class,user.getUsername());
        customer.getDiscountCodes().remove(mapInUser);
        DBManager.save(customer);
        DBManager.save(discountCode);
    }

    public void createDiscountCode(String code,Date startTime, Date endTime, int offPercentage, long maxDiscount)
            throws NotValidPercentageException, StartingDateIsAfterEndingDate, AlreadyExistCodeException {
        checkIfStartingDateIsBeforeEndingDate(startTime, endTime);
        checkIfPercentageIsValid(offPercentage);
        DiscountCode discountCodeDF = DBManager.load(DiscountCode.class,code);
        if (discountCodeDF != null) {
            throw new AlreadyExistCodeException();
        }
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
        Customer customer = DBManager.load(Customer.class,user.getUsername());
        for (DiscountcodeIntegerMap map : customer.getDiscountCodes()) {
            if (map.getDiscountCode().getCode().equals(code)) return map;
        }
        return null;
    }

    public void sysTo(String code, int amount, int numberOfUsers) {
        Random random = new Random();
        List<Customer> customers = DBManager.loadAllData(Customer.class);
        List<Customer> randomList = new ArrayList<>();
        if (numberOfUsers < customers.size())
            for (int i = 0; i < numberOfUsers; i++) {
                int index = random.nextInt(customers.size());
                randomList.add(customers.get(index));
                customers.remove(index);
            }
        else {
            randomList.addAll(customers);
        }
        randomList.forEach(customer -> {
            try {
                discountManager.addUserToDiscountCodeUsers(code, customer, amount);
            } catch (NoSuchADiscountCodeException | UserExistedInDiscountCodeException ignore) {
            }
        });
    }

    public void sysMore(String code, int amount, int i) {
        List<Customer> customers = getCustomersWithMorePurchase(i);
        DiscountManager discountManager = DiscountManager.getInstance();
        customers.forEach(customer -> {
            try {
                discountManager.addUserToDiscountCodeUsers(code, customer, amount);
            } catch (NoSuchADiscountCodeException | UserExistedInDiscountCodeException ignore) {
            }
        });
    }

    private List<Customer> getCustomersWithMorePurchase(int i) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);
        criteriaQuery.select(root);
        Predicate[] predicates = {
                criteriaBuilder.greaterThan(root.get("allPurchase"), i),
        };
        criteriaQuery.where(
                predicates
        );
        Query<Customer> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
