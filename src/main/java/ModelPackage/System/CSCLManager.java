package ModelPackage.System;

import ModelPackage.Product.Comment;
import ModelPackage.Product.Company;
import ModelPackage.Product.Score;
import lombok.Builder;
import lombok.Data;
import sun.rmi.runtime.Log;

import java.util.ArrayList;

@Builder @Data
public class CSCLManager {
    private static CSCLManager csclManager = new CSCLManager();
    private ArrayList<Company> allCompanies;
    private ArrayList<Comment> allComments;
    private ArrayList<Score> allScores;
    private ArrayList<Log> allLogs;

    private CSCLManager() {
        this.allCompanies = new ArrayList<Company>();
        this.allLogs = new ArrayList<Log>();
        this.allScores = new ArrayList<Score>();
        this.allComments = new ArrayList<Comment>();
    }

    public static CSCLManager getInstance() {
        return csclManager;
    }
                                /*create Company*/
    public void createCompany(Company newCompany) {
        allCompanies.add(newCompany);
    }
                                /*find company by name*/
    public Company getCompanyByName(String companyName) {
        for (Company company : allCompanies) {
            if (companyName.equals(company.getName()))
                return company;
        }
        return null;
    }
}
