package ModelPackage.System;

import ModelPackage.Off.Off;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.Off.OffStatus;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.off.InvalidTimes;
import ModelPackage.System.exeption.off.NoSuchAOffException;
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

    public void editStartTimeOfOff(User editor, int id, Date newDate)
            throws NoSuchAOffException {
        findOffById(id);
        OffChangeAttributes edit = new OffChangeAttributes();
        edit.setStart(newDate);
        edit.setTarget(id);
        DBManager.save(edit);
        String strRequest = String.format("%s requested to change start time of off (%d) to %s",editor.getUsername(),id,newDate);
        Request request = new Request(editor.getUsername(),RequestType.EDIT_OFF,strRequest,edit);
        RequestManager.getInstance().addRequest(request);
    }

    public void editEndTimeOfOff(User editor, int id, Date newDate)
            throws NoSuchAOffException {
        findOffById(id);
        OffChangeAttributes edit = new OffChangeAttributes();
        edit.setEnd(newDate);
        edit.setTarget(id);
        DBManager.save(edit);
        String strRequest = String.format("%s requested to change end time of off (%d) to %s",editor.getUsername(),id,newDate);
        Request request = new Request(editor.getUsername(),RequestType.EDIT_OFF,strRequest,edit);
        RequestManager.getInstance().addRequest(request);
    }

    public void editPercentageOfOff(User editor, int id, int percentage)
            throws NoSuchAOffException {
        findOffById(id);
        OffChangeAttributes edit = new OffChangeAttributes();
        edit.setPercentage(percentage);
        edit.setTarget(id);
        DBManager.save(edit);
        String strRequest = String.format("%s requested to change percentage of off (%d) to %d",editor.getUsername(),id,percentage);
        Request request = new Request(editor.getUsername(),RequestType.EDIT_OFF,strRequest,edit);
        RequestManager.getInstance().addRequest(request);
    }

    public void addProductToOff(User editor, int id, int newProduct)
            throws NoSuchAOffException, NoSuchAProductException {
        findOffById(id);
        ProductManager.getInstance().findProductById(id);
        OffChangeAttributes edit = new OffChangeAttributes();
        edit.setProductIdToAdd(newProduct);
        edit.setTarget(id);
        DBManager.save(edit);
        String strRequest = String.format("%s requested to add product (%d) to off (%d)",editor.getUsername(),newProduct,id);
        Request request = new Request(editor.getUsername(),RequestType.EDIT_OFF,strRequest,edit);
        RequestManager.getInstance().addRequest(request);
    }

    public void deleteProductFromOff(User editor, int id, int toDelete)
            throws NoSuchAOffException, NoSuchAProductException {
        findOffById(id);
        ProductManager.getInstance().findProductById(id);
        OffChangeAttributes edit = new OffChangeAttributes();
        edit.setTarget(id);
        edit.setProductIdToRemove(toDelete);
        DBManager.save(edit);
        String strRequest = String.format("%s requested to add product (%d) to off (%d)",editor.getUsername(),toDelete,id);
        Request request = new Request(editor.getUsername(),RequestType.EDIT_OFF,strRequest,edit);
        RequestManager.getInstance().addRequest(request);
    }

    public void deleteOff(int id) throws NoSuchAOffException {
        Off off = findOffById(id);
        DBManager.delete(off);
    }
}
