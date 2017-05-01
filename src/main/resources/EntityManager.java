/**
 * Created by roland on 28/04/2017.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;

public class EntityManager {
    private Connection conn = null;
    private Statement stmt = null;

    public EntityManager() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        this.conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection("jdbc:mysql://192.168.10.10/urlShorter?" + "user=shortenme&password=secret");
            this.createTableIfNotExist();
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

    public void createTableIfNotExist (String tableName, Map entity) throws SQLException {
        this.createStatement();
        String strQuery = "SHOW TABLES;";
        ResultSet rsTables = this.stmt.executeQuery(strQuery);
        Boolean tableExist = false;
        try {
            while(rsTables.next()) {
                if (rsTables.getString(1).compareTo(tableName) == 0) {
                    tableExist = true;
                }
            }
            if (!tableExist) {
                entity.forEach(new BiConsumer() {
                    @Override
                    public void accept(Object k, Object v) {
                    }
                });
                String createTableQuery = "CREATE TABLE urls (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "user_id INT," +
                        "base_url LONGTEXT" +
                        "shorter_url VARCHAR(255)" +
                        ");";
                this.stmt.executeUpdate(createTableQuery);
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

    public ArrayList getUrlList() throws SQLException {
        ArrayList<String> users = new ArrayList<>();
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
