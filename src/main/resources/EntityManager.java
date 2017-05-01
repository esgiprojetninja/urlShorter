/**
 * Created by roland on 28/04/2017.
 */

import model.Attribute;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class EntityManager {
    private Connection conn = null;
    private Statement stmt = null;
    private ArrayList<Attribute> attributes;
    private String tableName;

    public EntityManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        try {
            this.connect();
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public EntityManager(String tableName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.setTableName(tableName);
        try {
            this.connect();
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        this.conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection("jdbc:mysql://192.168.10.10/urlShorter?" + "user=shortenme&password=secret");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver non chargé !");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erreur SQL !");
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setAttributes (ArrayList<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void createTableIfNotExist () throws SQLException {
        this.createStatement();
        String strQuery = "SHOW TABLES;";
        ResultSet rsTables = this.stmt.executeQuery(strQuery);
        Boolean tableExist = false;
        try {
            while(rsTables.next()) {
                if (rsTables.getString(1).compareTo(this.tableName) == 0) {
                    tableExist = true;
                }
            }
            if (!tableExist) {
                StringBuilder createTableQuery = new StringBuilder("CREATE TABLE " + this.tableName + " (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT,");
                for (Attribute attribute: this.attributes) {
                    createTableQuery
                            .append(attribute.getName())
                            .append(" ")
                            .append(attribute.getSQLColumnConstructor());
                }
                createTableQuery.append(");");
                this.stmt.executeUpdate(createTableQuery.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeStatement();
    }

    public Url getUrl(String shorter_url) throws SQLException {
        this.createStatement();
        String userQuery = String.format("SELECT * from urls WHERE shorter_url = '%s'", shorter_url);
        ResultSet sqlUrl = this.stmt.executeQuery(userQuery);
        Url url = new Url();
        if (sqlUrl.next()) {
            url.setBase_url(sqlUrl.getString("base_url"));
            url.setId(sqlUrl.getInt("id"));
            url.setUser_id(sqlUrl.getInt("user_id"));
            url.setShorter_url((sqlUrl.getString("shorter_url")));
        }
        return url;
    }

    public ArrayList getAll() throws SQLException {
        ArrayList<String> items = new ArrayList<>();
        try {
            this.createStatement();
            String listUserQuery = "SELECT * FROM urls;";
            ResultSet urlList = this.stmt.executeQuery(listUserQuery);
            while(urlList.next()) {
                users.add(
                        "id: " + urlList.getInt("id")
                                + " shorter_url: " + urlList.getString("shorter_url")
                                + " base_url: " + urlList.getString("base_url")
                                + " user_id: " + urlList.getInt("user_id")
                                + "\n"
                );
            }
            this.closeStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void closeStatement() {
        try {
            this.stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createStatement() {
        try {
            this.stmt = this.conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
