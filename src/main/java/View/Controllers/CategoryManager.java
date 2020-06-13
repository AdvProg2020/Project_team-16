package View.Controllers;

import ModelPackage.System.editPackage.CategoryEditAttribute;
import ModelPackage.System.exeption.category.NoSuchACategoryException;
import ModelPackage.System.exeption.category.RepeatedFeatureException;
import ModelPackage.System.exeption.category.RepeatedNameInParentCategoryException;
import View.Main;
import View.PrintModels.CategoryPM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controler.ManagerController;
import controler.SellerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public Label categoryName;
    public JFXButton editName;
    public Label productCount;
    public ListView<String> featureList;
    public TextField newFeature;
    public Button newFeatureButt;
    public Button removeFeatureButt;
    public JFXTextField crName;
    public ComboBox<CategoryPM> crParent;
    public ListView<String> featureListCr;
    public TextField featureInput;
    public Button addFeatureCr;
    public JFXButton createCategory;
    public ListView<CategoryPM> categories;

    private static ManagerController managerController = ManagerController.getInstance();
    private static SellerController sellerController = SellerController.getInstance();

    @FXML
    public void initialize() {
        loadList();
        listeners();
        bindings();
        buttonInitialize();
    }

    private void buttonInitialize() {
        back.setOnAction(event -> {
        });
        minimize.setOnAction(event -> Main.minimize());
        close.setOnAction(event -> Main.close());
        editName.setOnAction(event -> sendEditNameRequest());
        addFeatureCr.setOnAction(event -> addFeatureToCreationList());
        newFeatureButt.setOnAction(event -> sendNewFeatureRequest());
        removeFeatureButt.setOnAction(event -> sendRemoveFeatureRequest());
        createCategory.setOnAction(event -> {
            if (checkValidValues()) {
                sendCategoryCreationRequest();
            }
        });
    }

    private void sendCategoryCreationRequest() {
        String name = crName.getText();
        int parentId = crParent.getSelectionModel().getSelectedItem().getId();
        List<String> features = new ArrayList<>(featureListCr.getItems());
        try {
            managerController.addCategory(name, parentId, features);
            reset();
        } catch (RepeatedNameInParentCategoryException | NoSuchACategoryException e) {
            e.printStackTrace();
        }
    }

    private boolean checkValidValues() {
        if (crName.getText().isEmpty()) {
            crName.setPromptText("Name Is Required");
            crName.setFocusColor(Paint.valueOf("#c0392b"));
            crName.requestFocus();
            return false;
        }
        return true;
    }

    private void addFeatureToCreationList() {
        String feature = featureInput.getText();
        featureListCr.getItems().add(feature);
        featureInput.setText("");
    }

    private void sendRemoveFeatureRequest() {
        String featureToRemove = featureList.getSelectionModel().getSelectedItem();
        int id = categories.getSelectionModel().getSelectedItem().getId();
        CategoryEditAttribute attribute = new CategoryEditAttribute();
        attribute.setRemoveFeature(featureToRemove);
        try {
            managerController.editCategory(id, attribute);
            featureList.getItems().removeAll(featureToRemove);
        } catch (RepeatedNameInParentCategoryException | NoSuchACategoryException | RepeatedFeatureException e) {
            new OopsAlert().show("Fuck");
        }
    }

    private void sendNewFeatureRequest() {
        String newFeatureTitle = newFeature.getText();
        CategoryEditAttribute attribute = new CategoryEditAttribute();
        attribute.setAddFeature(newFeatureTitle);
        int id = categories.getSelectionModel().getSelectedItem().getId();
        try {
            managerController.editCategory(id, attribute);
            featureList.getItems().add(newFeatureTitle);
        } catch (RepeatedNameInParentCategoryException | NoSuchACategoryException | RepeatedFeatureException e) {
            e.printStackTrace();
        }
    }

    private void sendEditNameRequest() {
        // TODO: 6/13/2020
        String newName = "";//todo : from dialog
        CategoryEditAttribute attribute = new CategoryEditAttribute();
        attribute.setName(newName);
        int id = categories.getSelectionModel().getSelectedItem().getId();
        try {
            managerController.editCategory(id, attribute);
            categoryName.setText(newName);
        } catch (RepeatedNameInParentCategoryException | NoSuchACategoryException | RepeatedFeatureException e) {
            e.printStackTrace();
        }
    }

    private void bindings() {
        removeFeatureButt.disableProperty().bind(Bindings.isEmpty(featureList.getSelectionModel().getSelectedItems()));
        newFeatureButt.disableProperty().bind(Bindings.isEmpty(newFeature.textProperty()));
        addFeatureCr.disableProperty().bind(Bindings.isEmpty(featureInput.textProperty()));
        editName.disableProperty().bind(Bindings.isEmpty(categories.getSelectionModel().getSelectedItems()));
        createCategory.disableProperty().bind(crParent.valueProperty().isNull().or(Bindings.isEmpty(featureListCr.getItems())));
    }

    private void listeners() {
        categories.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                categoryName.setText(newValue.getName());
                fillFeatureTable(newValue.getId());
            }
        });
        featureInput.setOnAction(event -> addFeatureToCreationList());
    }

    private void fillFeatureTable(int id) {
        try {
            ArrayList<String> list = loadFeature();//sellerController.getSpecialFeaturesOfCat(id);
            ObservableList<String> data = FXCollections.observableArrayList(list);
            featureList.setItems(data);
            if (false) throw new NoSuchACategoryException("");
        } catch (NoSuchACategoryException e) {
            e.printStackTrace();
        }
    }

    private void loadList() {
        ArrayList<CategoryPM> categoryPMS = load(); //managerController.getAllCategories();
        ObservableList<CategoryPM> data = FXCollections.observableArrayList(categoryPMS);
        categoryPMS.add(0, new CategoryPM("---", 0, 0));
        ObservableList<CategoryPM> data1 = FXCollections.observableArrayList(categoryPMS);

        crParent.setItems(data1);
        categories.setItems(data);
    }

    private ArrayList<CategoryPM> load() {
        CategoryPM categoryPM1 = new CategoryPM("Human", 1, 0);
        CategoryPM categoryPM2 = new CategoryPM("Ass", 2, 1);
        CategoryPM categoryPM3 = new CategoryPM("Arm", 3, 1);
        CategoryPM categoryPM4 = new CategoryPM("Butt", 4, 1);
        CategoryPM categoryPM5 = new CategoryPM("Animal", 5, 0);
        ArrayList<CategoryPM> arrayList = new ArrayList<>();
        arrayList.add(categoryPM1);
        arrayList.add(categoryPM2);
        arrayList.add(categoryPM3);
        arrayList.add(categoryPM4);
        arrayList.add(categoryPM5);
        return arrayList;
    }

    private ArrayList<String> loadFeature() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("color");
        arrayList.add("cascade");
        arrayList.add("dimention");
        arrayList.add("parameter");
        return arrayList;
    }

    private void reset() {
        categories.getItems().clear();
        featureList.getItems().clear();
        featureListCr.getItems().clear();
        categoryName.setText("");
        loadList();
    }
}
