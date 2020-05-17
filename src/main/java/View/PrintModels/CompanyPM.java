package View.PrintModels;

import lombok.Data;

@Data public class CompanyPM {
    private String name;
    private String phone;
    private String group;

    public CompanyPM(String name, String phone, String group) {
        this.name = name;
        this.phone = phone;
        this.group = group;
    }


}
