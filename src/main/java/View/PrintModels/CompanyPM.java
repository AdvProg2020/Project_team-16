package View.PrintModels;

import lombok.Data;

@Data public class CompanyPM {
    private int id;
    private String name;
    private String phone;
    private String group;

    public CompanyPM(int id, String name, String phone, String group) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.group = group;
    }
}
