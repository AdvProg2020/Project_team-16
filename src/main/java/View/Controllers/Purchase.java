package View.Controllers;

import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.product.NoSuchAPackageException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.CartPM;
import View.PrintModels.DisCodeUserPM;
import View.PrintModels.InCartPM;
import View.PrintModels.MiniProductPM;
import View.SortPackage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controler.CustomerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Purchase extends BackAbleController {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TextField card1;
    public TextField card2;
    public TextField card3;
    public TextField card4;
    public TextField cvv2;
    public TextField pass;
    public TextField month;
    public TextField year;
    public Button purchaseButt;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label totalPrice;
    @FXML
    private ComboBox<String> country;
    @FXML
    private ComboBox<String> city;
    @FXML
    private JFXTextArea zipCode;
    @FXML
    private JFXTextArea address;
    @FXML
    private Tab postalInformation;
    @FXML
    private Tab discount;
    @FXML
    private Tab payment;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Button nextPagePostalInformation;
    @FXML
    private Button nextPageDiscount;
    @FXML
    private Button previousPageDiscount;
    @FXML
    private JFXTextField discountField;
    @FXML
    private JFXButton useDiscount;

    private static final String CARD_REGEX = "\\d{4}";
    private static final String CVV2_REGEX = "\\d{3}\\d?";
    private static final String MONTH_REGEX = "(0[1-9]|1[012])";
    private static final String YEAR_REGEX = "\\d{2}";
    private static final String CARD_PASS_REGEX = "\\d{5,64}";
    private static final Paint redColor = Paint.valueOf("#c0392b");

    private final CacheData cacheData = CacheData.getInstance();
    private final CustomerController customerController = CustomerController.getInstance();
    private List<DisCodeUserPM> discountCodes;
    private final SortPackage sortPackage = new SortPackage();
    private CartPM cartPM;
    private String selectedDisCode = "";
    private boolean usedDisCodeThisTime = false;


    @FXML
    public void initialize() {
        initializeButtons();
        bindings();
        disableTabs();
        loadComboBox();
        loadCart();
        loadDiscountCode();
        initializeLabels();
    }

    private void loadDiscountCode() {
        sortPackage.reset();
        try {
            discountCodes = customerController.viewDiscountCodes(cacheData.getUsername(), sortPackage);
        } catch (UserNotAvailableException ignore) {}
    }

    private void disableTabs() {
        /*postalInformation.setDisable(true);
        discount.setDisable(true);
        payment.setDisable(true);*/
    }

    private void loadComboBox() {
        loadComboBoxCountry();
        loadComboBoxCities();
    }

    private void loadComboBoxCountry() {
        ArrayList<String> count = new ArrayList<>();
        count.add("USA");
        count.add("Canada");
        count.add("Iran");
        count.add("UK");
        ObservableList<String> countries = FXCollections.observableArrayList(count);
        country.setItems(countries);
    }

    private void loadComboBoxCities() {
        country.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                city.getItems().clear();
                city.setDisable(true);
            } else {
                List<String> cities = getCitiesForCountry(newValue);
                city.getItems().setAll(cities);
                city.setDisable(false);
            }
        });
    }

    private List<String> getCitiesForCountry(String country) {
        ArrayList<String> cities = new ArrayList<>();
        switch (country) {
            case "USA" : {
                cities.add("New York");
                cities.add("Washington DC");
                cities.add("Las Vegas");
                cities.add("Los Angeles");
                break;
            }
            case "UK" : {
                cities.add("London");
                cities.add("Manchester");
                cities.add("New Castle");
                cities.add("Cambridge");
                break;
            }
            case "Iran" : {
                cities.add("Tehran");
                cities.add("Yazd");
                cities.add("Shiraz");
                cities.add("Mashhad");
                break;
            }
            case "Canada" : {
                cities.add("Toronto");
                cities.add("Vancouver");
                cities.add("Ottawa");
                cities.add("Montreal");
                break;
            }
        }
        return cities;
    }

    private void initializeLabels() {
        totalPrice.setText(cartPM.getTotalPrice() + "$");
    }

    private void loadCart() {
        try {
            cartPM = customerController.viewCart(cacheData.getUsername());
        } catch (UserNotAvailableException e) {
            new OopsAlert().show(e.getMessage());
        }
    }

    private void initializeButtons() {
        back.setOnAction(e -> handleBackButton());
        minimize.setOnAction(e -> minimize());
        close.setOnAction(e -> close());
        purchaseButt.setOnAction(e -> handlePurchaseButt());
        nextPagePostalInformation.setOnAction(e -> tabPane.getSelectionModel().selectNext());
        nextPageDiscount.setOnAction(e -> tabPane.getSelectionModel().selectNext());
        previousPageDiscount.setOnAction(e -> tabPane.getSelectionModel().selectPrevious());
        useDiscount.setOnAction(e -> handleUseDiscountButt());
    }

    private void bindings() {
        nextPagePostalInformation.disableProperty().bind(
                Bindings.isNull(country.getSelectionModel().selectedItemProperty())
                .or(Bindings.isNull(city.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isEmpty(address.textProperty()))
                .or(Bindings.isEmpty(zipCode.textProperty()))
        );
    }

    private void handleUseDiscountButt() {
        String disCodeId = discountField.getText();
        if (disCodeId.isEmpty())
            errorField(discountField, "Discount Code Is Required!!");
        if (!usedDisCodeThisTime && isValidDisCode(disCodeId)) {
            usedDisCodeThisTime = true;
            selectedDisCode = disCodeId;
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        try {
            totalPrice.setText(String.valueOf(customerController.getPurchaseTotalPrice(selectedDisCode, cacheData.getUsername())));
        } catch (NoSuchADiscountCodeException | UserNotAvailableException | NoSuchSellerException | NoSuchAPackageException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidDisCode(String disCodeId) {
        for (DisCodeUserPM discountCodePM : discountCodes) {
            if (discountCodePM.getDiscountCode().equals(disCodeId)) {
                return true;
            }
        }
        errorField(discountField, "Invalid Discount Code!!");
        return false;
    }

    private void handlePurchaseButt() {
        if (checkIfCountryAndCityAreSelected() && checkIfAddressAndZipCodeIsNotEmpty() &&
                checkCardsNumbers() && checkCVV2Numbers() && checkCardPass() && checkExpDateNumber()) {
            try {
                customerController.purchase(cacheData.getUsername(), makeCustomerInformation(), selectedDisCode);
                reset();
            } catch (NoSuchADiscountCodeException | NotEnoughAmountOfProductException | NoSuchAProductException | NoSuchSellerException | NoSuchAPackageException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] makeCustomerInformation() {
        String[] data = new String[4];
        data[0] = address.getText();
        data[1] = zipCode.getText();
        data[2] = card1.getText() + '-' + card2.getText() + '-'
                + card3.getText() + '-' + card4.getText();
        data[3] = pass.getText();
        return data;
    }

    private boolean checkCardPass() {
        if (pass.getText().isEmpty()) {
            new OopsAlert().show("Enter Your Card Password, please!!");
            return false;
        } else if (!pass.getText().matches(CARD_PASS_REGEX)) {
            new OopsAlert().show("Invalid Card Password!!");
            return false;
        }
        return true;
    }

    private void reset() {
        usedDisCodeThisTime = false;
        selectedDisCode = "";
    }

    private boolean checkIfAddressAndZipCodeIsNotEmpty() {
        if (address.getText().isEmpty()) {
            errorField(address, "Address Is Required!!");
            return false;
        } else if (zipCode.getText().isEmpty()) {
            errorField(zipCode, "Zip Code Is Required!!");
            return false;
        }
        return true;
    }

    private void errorField(JFXTextArea area, String message) {
        area.setFocusColor(redColor);
        area.setPromptText(message);
        area.requestFocus();
    }

    private void errorField(JFXTextField field, String message) {
        field.setFocusColor(redColor);
        field.setPromptText(message);
        field.requestFocus();
    }

    private boolean checkIfCountryAndCityAreSelected() {
        if (country.getSelectionModel().getSelectedItem() == null) {
            country.setPromptText("Select A Country!!");
            country.requestFocus();
            return false;
        } else if (city.getSelectionModel().getSelectedItem() == null) {
            city.setPromptText("Select A City!!");
            city.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkCardsNumbers() {
        if (card1.getText().isEmpty() || card2.getText().isEmpty() ||
                card3.getText().isEmpty() || card4.getText().isEmpty()) {
            new OopsAlert().show("Fill Card Number Fields, please!");
            return false;
        } else if (!card1.getText().matches(CARD_REGEX) || !card2.getText().matches(CARD_REGEX) ||
        !card3.getText().matches(CARD_REGEX) || !card4.getText().matches(CARD_REGEX)) {
            new OopsAlert().show("Invalid Card Number!");
            return false;
        }
        return true;
    }

    private boolean checkCVV2Numbers() {
        if (cvv2.getText().isEmpty()) {
            Notification.show("Error", "Fill CVV2 field, please!", back.getScene().getWindow(), true);
            new OopsAlert().show("Fill CVV2 field, please!");
            return false;
        }
        if (!cvv2.getText().matches(CVV2_REGEX)) {
            new OopsAlert().show("Invalid CVV2!");
            return false;
        }
        return true;
    }

    private boolean checkExpDateNumber() {
        if (year.getText().isEmpty() || month.getText().isEmpty()) {
            new OopsAlert().show("Fill Exp. Date, please!");
            return false;
        } else if(!year.getText().matches(YEAR_REGEX) || !month.getText().matches(MONTH_REGEX)) {
            new OopsAlert().show("Invalid Exp. Date!");
            return false;
        }
        return true;
    }

    private void close() {
        Stage window = (Stage) rootPane.getScene().getWindow();
        window.close();
    }

    private void minimize() {
        Stage window = (Stage) rootPane.getScene().getWindow();
        window.setIconified(true);
    }

    private void handleBackButton() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException ignore) {}
    }
}
