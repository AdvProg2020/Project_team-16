package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.Off.OffStatus;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
import ModelPackage.System.exeption.off.ThisOffDoesNotBelongssToYouException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;

import java.util.Date;

public class OffManager {
    private static OffManager offManager;

    public static OffManager getInstance() {
        return offManager;
    }

    public void createOff(Seller seller, Date[] dates,int percentage) throws InvalidTimes {
        if (!dates[0].before(dates[1])) throw new InvalidTimes();
        Off off = new Off();
        off.setSeller(seller);
        off.setOffPercentage(percentage);
        off.setOffStatus(OffStatus.CREATION);
        off.setStartTime(dates[0]);
        off.setEndTime(dates[1]);
        DBManager.save(off);
        String strRequest = String.format("%s requested to create an off with percentage %d",seller.getUsername(),percentage);
        Request request = new Request(seller.getUsername(), RequestType.CREATE_OFF,strRequest,off);
        RequestManager.getInstance().addRequest(request);
    }

    public Off findOffById(int id) throws NoSuchAOffException {
        Off off = DBManager.load(Off.class,id);
        if (off == null) throw new NoSuchAOffException(id);
        return off;
    }

    public void editOff(OffChangeAttributes changeAttributes,String editor) throws NoSuchAOffException, ThisOffDoesNotBelongssToYouException {
        Off off = findOffById(changeAttributes.getSourceId());
        checkIfThisSellerCreatedTheOff(off,editor);
        off.setOffStatus(OffStatus.EDIT);
        /* TODO : Alert Time manager to remove off */
        String strRequest = String.format("%s requested to edit an off with id %d",editor,off.getOffId());
        Request request = new Request(editor, RequestType.EDIT_OFF,strRequest,changeAttributes);
        RequestManager.getInstance().addRequest(request);
    }

    public void deleteOff(int id,String remover) throws NoSuchAOffException, ThisOffDoesNotBelongssToYouException {
        Off off = findOffById(id);
        checkIfThisSellerCreatedTheOff(off,remover);
        /* TODO : Notify Time manager to remove off */
        DBManager.delete(off);
    }

    public void checkIfThisSellerCreatedTheOff(Off off,String viwer) throws ThisOffDoesNotBelongssToYouException {
        if (!off.getSeller().getUsername().equals(viwer))throw new ThisOffDoesNotBelongssToYouException();
    }
}