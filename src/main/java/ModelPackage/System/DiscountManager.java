package ModelPackage.System;

import ModelPackage.Off.DiscountCode;

import java.util.ArrayList;
import java.util.List;

public class DiscountManager {
    private List<DiscountCode> discountCodes;

    private static DiscountManager discountManager = new DiscountManager();

    public static DiscountManager getInstance() {
        return discountManager;
    }

    private DiscountManager() {
        this.discountCodes = new ArrayList<>();
    }
}
