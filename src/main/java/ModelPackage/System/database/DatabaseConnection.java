package ModelPackage.System.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private String dbDriver;
    private String dbUser;
    private String dbPass;
    private String dbUrl;
    private Connection connection;

    private static DatabaseConnection databaseConnection = null;

    private DatabaseConnection(){

    }

    public Connection getConnection()
            throws IOException {
        File file = new File("src\\main\\resources\\application.properties");
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        this.dbDriver = properties.getProperty("com.settings.db_driver");
        this.dbUser = properties.getProperty("com.settings.db_user");
        this.dbPass = properties.getProperty("com.settings.db_pass");
        this.dbUrl = properties.getProperty("com.settings.db_url");

        try {
            connection = DriverManager.getConnection(this.dbUrl,this.dbUser,this.dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
            /* TODO : throw Exception */
        }

        return connection;
    }

    public static DatabaseConnection getInstance(){
        synchronized (DatabaseConnection.class){
            if (databaseConnection == null){
                databaseConnection = new DatabaseConnection();
            }
        }
        return databaseConnection;
    }
}
