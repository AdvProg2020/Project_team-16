package View.Controllers;

import ModelPackage.System.editPackage.DiscountCodeEditAttributes;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.discount.*;
import View.Main;
import View.PrintModels.DisCodeManagerPM;
import View.PrintModels.UserIntegerPM;
import com.jfoenix.controls.*;
import controler.ManagerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DiscountManager extends BackAbleController {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public JFXButton refresh;

    public JFXTextField crCode;
    public JFXSlider crPercentage;
    public JFXTextField crMaximum;
    public DatePicker crStartDate;
    public DatePicker crEndDate;
    public TextField crStartH;
    public TextField crStartM;
    public TextField crStartS;
    public TextField crEndH;
    public TextField crEndM;
    public TextField crEndS;
    public JFXButton crButton;

    public VBox editBox;
    public JFXSlider edPercent;
    public JFXTextField edMaximum;
    public DatePicker edStartDate;
    public DatePicker edEndDate;
    public TextField edStartH;
    public TextField edStartM;
    public TextField edStartS;
    public TextField edEndH;
    public TextField edEndM;
    public TextField edEndS;
    public JFXButton edConfirm;
    public JFXButton edReset;

    public TableView<UserIntegerPM> userTable;
    public TableColumn<UserIntegerPM,String> userColumn;
    public TableColumn<UserIntegerPM,Integer> amountColumn;
    public JFXButton removeUser;

    public JFXTextField addUsername;
    public JFXTextField addUserQuantity;
    public JFXButton addUser;

    public VBox sysAddBox;
    public JFXTextField sysQuantity;
    public JFXButton sysAdd;
    public JFXButton delete;

    public ListView<DisCodeManagerPM> codes;

    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");
    private static final ManagerController managerController = ManagerController.getInstance();

    @FXML
    public void initialize(){
        loadList();
        listeners();
        binds();
        resetFields();
        upBarInitialize();
        codeCreateBoxInitialize();
        userTableInitialize();
        addUserInitialize();
        editCode();
        // TODO: 6/13/2020
        //systematicAdditionInitialize();
    }

    private void editCode() {
        edReset.setOnAction(e-> reloadEditBox());
        edConfirm.setOnAction(e->{
            if (checkEditBoxValidity()){
                sendEditRequest();
                reset();
            }
        });
    }

    private void sendEditRequest() {
        DiscountCodeEditAttributes attributes = new DiscountCodeEditAttributes();
        if (edStartDate.getValue() != null) {
            Date start = createDate(edStartDate,edStartH,edStartM,edStartS);
            attributes.setStart(start);
        }
        if (edEndDate.getValue() != null){
            Date end = createDate(edEndDate,edEndH,edEndM,edEndS);
            attributes.setEnd(end);
        }
        int percent = (int)edPercent.getValue();
        attributes.setOffPercent(percent);
        if (!edMaximum.getText().isEmpty()){
            int maximum = Integer.parseInt(edMaximum.getText());
            attributes.setMaxDiscount(maximum);
        }
        String code = codes.getSelectionModel().getSelectedItem().getDiscountCode();
        try {
            managerController.editDiscountCode(code,attributes);
        } catch (NegativeMaxDiscountException |
                NotValidPercentageException |
                StartingDateIsAfterEndingDate |
                NoSuchADiscountCodeException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
            e.printStackTrace();
        }
    }

    private Date createDate(DatePicker datePicker, TextField h, TextField m, TextField s) {
        int startH = Integer.parseInt(h.getText());
        int startM = Integer.parseInt(m.getText());
        int startS = Integer.parseInt(s.getText());
        Date start = convertToDateViaInstant(datePicker.getValue());
        start.setHours(startH);
        start.setMinutes(startM);
        start.setSeconds(startS);
        return start;
    }

    private boolean checkEditBoxValidity() {
        if (crPercentage.getValue() == 0){
            new OopsAlert().show("Percentage Cannot be Zero!!");
            return false;
        } else if (!edMaximum.getText().matches("\\d{0,9}")){
            errorField(crMaximum,"Maximum Should Be Numerical");
            return false;
        } else if (hasHalfFullDates()){
            new OopsAlert().show("Invalid Date!!");
            return false;
        } else return true;
    }

    private boolean hasHalfFullDates() {
        // TODO: 6/13/2020
        return false;
    }

    private void reloadEditBox() {
        DisCodeManagerPM pm = codes.getSelectionModel().getSelectedItem();
        loadCodeInfo(pm);
    }

    private void resetFields() {
        resetSettingForFields(crCode,"Code");
        resetSettingForFields(crMaximum,"Maximum Of Discount amount");
        resetSettingForFields(edMaximum,"Maximum Of Discount amount");
        resetSettingForFields(addUsername,"Username");
        resetSettingForFields(addUserQuantity,"Quantity");
        resetSettingForFields(sysQuantity,"Quantity");
    }

    private void addUserInitialize() {
        addUser.setOnAction(e -> sendAddUserRequest());
    }

    private void sendAddUserRequest() {
        if (addValuesAreValid()) {
            String user = addUsername.getText();
            String code = codes.getSelectionModel().getSelectedItem().getDiscountCode();
            int amount = Integer.parseInt(addUserQuantity.getText());
            try {
                managerController.addUserToDiscountCode(code, user, amount);
            } catch (UserNotAvailableException | UserExistedInDiscountCodeException | NoSuchADiscountCodeException ex) {
                Notification.show("Error", ex.getMessage(), back.getScene().getWindow(), true);
                ex.printStackTrace();
            }
        }
    }

    private boolean addValuesAreValid() {
        if (addUsername.getText().isEmpty()){
            errorField(addUsername,"Username*");
            return false;
        } else if (addUserQuantity.getText().isEmpty()){
            errorField(addUserQuantity,"Amount Required");
            return false;
        } else if (!addUserQuantity.getText().matches("\\d{0,9}")){
            errorField(addUserQuantity,"Numerical!!");
            return false;
        } else return true;
    }

    private void userTableInitialize() {
        removeUser.setOnAction(e-> sendRemoveUserRequest());
    }

    private void sendRemoveUserRequest() {
        String userId = userTable.getSelectionModel().getSelectedItem().getUsername();
        String code = codes.getSelectionModel().getSelectedItem().getDiscountCode();
        try {
            managerController.removeUserFromDiscountCodeUsers(code,userId);
            reset();
        } catch (UserNotExistedInDiscountCodeException | NoSuchADiscountCodeException | UserNotAvailableException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
        }
    }

    private void codeCreateBoxInitialize() {
        crButton.setOnAction(e->{
            if (checkCreateBoxValidity()){
                sendCreateRequest();
            }
        });
    }

    private void sendCreateRequest() {
        String[] data = {crCode.getText(),
                Integer.toString((int)crPercentage.getValue()),
                crMaximum.getText()};
        int startH = Integer.parseInt(crStartH.getText());
        int startM = Integer.parseInt(crStartM.getText());
        int startS = Integer.parseInt(crStartS.getText());
        int endH = Integer.parseInt(crEndH.getText());
        int endM = Integer.parseInt(crEndM.getText());
        int endS = Integer.parseInt(crEndS.getText());
        Date start = convertToDateViaInstant(crStartDate.getValue());
        Date end = convertToDateViaInstant(crEndDate.getValue());
        start.setHours(startH);
        start.setMinutes(startM);
        start.setSeconds(startS);
        end.setHours(endH);
        end.setMinutes(endM);
        end.setSeconds(endS);
        try {
            managerController.createDiscount(data,start,end);
            reset();
        } catch (NotValidPercentageException | AlreadyExistCodeException | StartingDateIsAfterEndingDate e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
            e.printStackTrace();
        }
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    private boolean checkCreateBoxValidity() {
        if (crCode.getText().isEmpty()){
            errorField(crCode,"Code Is Required");
            return false;
        }else if (!crCode.getText().matches("\\w+")){
            errorField(crCode,"Code Can Contain letters & Numbers");
            return false;
        } else if (crPercentage.getValue() == 0){
            new OopsAlert().show("Percentage Cannot Be Zero!!");
            return false;
        } else if (isCrStartDateInvalid()){
            new OopsAlert().show("Wring Date Format");
            return false;
        } else if (crMaximum.getText().isEmpty()){
            errorField(crMaximum,"Maximum Can't Be Empty");
            return false;
        }
        else if (!crMaximum.getText().matches("\\d{0,9}")){
            errorField(crMaximum,"Maximum Should Be Numerical");
            return false;
        } else return true;
    }

    private boolean isCrStartDateInvalid() {
        if (crStartDate.getValue() == null ||
                crEndDate.getValue() == null){
            return true;
        }
        LocalDate startDate = crStartDate.getValue();
        LocalDate endDate = crEndDate.getValue();
        if (startDate.isAfter(endDate)){
            return true;
        } else if (startDate.isEqual(endDate)){
            try{
                int startH = Integer.parseInt(crStartH.getText());
                int startM = Integer.parseInt(crStartM.getText());
                int startS = Integer.parseInt(crStartS.getText());
                int endH = Integer.parseInt(crEndH.getText());
                int endM = Integer.parseInt(crEndM.getText());
                int endS = Integer.parseInt(crEndS.getText());
                Date start = new Date(2020, Calendar.APRIL,12,startH,startM,startS);
                Date end = new Date(2020,Calendar.APRIL,12,endH,endM,endS);
                return start.after(end);
            }catch (Exception e){
                return false;
            }
        } else return false;
    }

    private void errorField(JFXTextField field,String prompt){
        field.setPromptText(prompt);
        field.setFocusColor(redColor);
        field.requestFocus();
    }

    private void resetSettingForFields(JFXTextField field,String prompt){
        field.textProperty().addListener(e->{
            field.setFocusColor(blueColor);
            field.setPromptText(prompt);
        });
    }

    private void upBarInitialize() {
        back.setOnAction(e-> { try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException ignore) {}
        });
        minimize.setOnAction(e -> ((Stage) close.getScene().getWindow()).setIconified(true));
        refresh.setOnAction(e -> reset());
        close.setOnAction(e -> ((Stage) close.getScene().getWindow()).close());
    }

    private void binds() {
        editBox.disableProperty().bind(Bindings.isEmpty(codes.getSelectionModel().getSelectedItems()));
        removeUser.disableProperty().bind(Bindings.isEmpty(userTable.getSelectionModel().getSelectedItems()));
    }

    private void listeners() {
        codes.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> {
            if (newValue != null) {
                loadCodeInfo(newValue);
                loadTableData(newValue);
            }
        });
    }

    private void loadCodeInfo(DisCodeManagerPM pm) {
        edPercent.setValue(pm.getOffPercentage());
        edMaximum.setText("");
        edMaximum.setPromptText("Maximum : " + pm.getMaxOfPriceDiscounted());
        Date startDate = pm.getStartTime();
        Date endDate = pm.getEndTime();
        setDatesToFields(edStartDate,startDate,0);
        edStartDate.setValue(null);
        setDatesToFields(edEndDate,endDate,1);
        edEndDate.setValue(null);
    }

    private void setDatesToFields(DatePicker datePicker, Date date,int mode) {
        datePicker.setPromptText(date.toString());
        switch (mode){
            case 0:
                edStartH.setPromptText(String.format("%02d",date.getHours()));
                edStartM.setPromptText(String.format("%02d",date.getMinutes()));
                edStartS.setPromptText(String.format("%02d",date.getSeconds()));
                break;
            case 1:
                edEndH.setPromptText(String.format("%02d",date.getHours()));
                edEndM.setPromptText(String.format("%02d",date.getMinutes()));
                edEndS.setPromptText(String.format("%02d",date.getSeconds()));
                break;
        }
    }

    private void loadTableData(DisCodeManagerPM pm) {
        ObservableList<UserIntegerPM> data = FXCollections.observableArrayList(pm.getUsers());
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("integer"));
        userTable.setItems(data);
    }

    private void loadList() {
        ArrayList<DisCodeManagerPM> list = /*load();*/managerController.viewDiscountCodes();
        ObservableList<DisCodeManagerPM> data = FXCollections.observableArrayList(list);
        codes.setItems(data);
    }

    private void reset(){
        userTable.setItems(FXCollections.observableArrayList());
        loadList();
    }
}
