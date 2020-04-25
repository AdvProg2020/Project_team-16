package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import lombok.Data;

import java.util.ArrayList;
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
}
