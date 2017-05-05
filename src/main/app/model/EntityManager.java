package model;
/**
 * Created by roland on 28/04/2017.
 */

import org.w3c.dom.Attr;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntityManager {
    private Connection conn = null;
    private Statement stmt = null;
    protected Map<String, Attribute> attributes = new HashMap<>();
    private String tableName;

    public EntityManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.addAttribute(new Attribute("id", "int"));
    }

    public EntityManager(String tableName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.setTableName(tableName);
        this.addAttribute(new Attribute("id", "int"));
    }

    protected void connect() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        this.conn = null;
        try {
            // Class.forName("com.mysql.jdbc.Driver").newInstance();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            this.conn = DriverManager.getConnection("jdbc:mysql://192.168.10.10/url_shorter?" + "user=shortenme&password=secret");
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) throws SQLException { this.tableName = tableName; }

    public void setAttributes (Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.put(attribute.getName(), attribute);
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
                int i = 1;
                int attributeListSize = this.attributes.size() - 1; // Id is hardcoded upper.
                for (Map.Entry<String, Attribute> entry: this.attributes.entrySet()) {
                    Attribute attribute = entry.getValue();
                    if(attribute.getName().compareTo("id") != 0) {
                        createTableQuery
                                .append(attribute.getName())
                                .append(" ")
                                .append(attribute.getSQLColumnConstructor());
                        if (i < attributeListSize) {
                            createTableQuery.append(", ");
                        }
                        i++;
                    }
                }
                createTableQuery.append(");");
                System.out.println(createTableQuery.toString());
                this.stmt.executeUpdate(createTableQuery.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeStatement();
    }

    public void find(int id) throws SQLException {
        this.createStatement();
        String queryString = String.format("SELECT * from " + this.tableName + " WHERE id = '%d'", id);
        ResultSet sqlQuery = this.stmt.executeQuery(queryString);
        if (sqlQuery.next()) {
            for (Map.Entry<String, Attribute> entry : this.attributes.entrySet()) {
                entry.getValue().setValueFromResultSet(sqlQuery);
            }
        }
        this.stmt.close();
    }

    public ArrayList<EntityManager> getAll() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ArrayList<EntityManager> entities = new ArrayList<>();
        try {
            this.createStatement();
            String listUserQuery = "SELECT * FROM " + this.tableName + " ;";
            ResultSet entitySQLList = this.stmt.executeQuery(listUserQuery);
            while(entitySQLList.next()) {
                EntityManager entity = new EntityManager(this.tableName);
                for (Map.Entry<String, Attribute> entry: this.attributes.entrySet()) {
                    Attribute attribute = entry.getValue();
                    attribute.setValueFromResultSet(entitySQLList);
                    entity.addAttribute(attribute);
                }
                entities.add(entity);
            }
            this.closeStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public boolean save() throws SQLException {
        this.createStatement();
        StringBuilder saveQuery = new StringBuilder("INSERT INTO " + this.tableName + " (");
        Integer attributeListLength = this.attributes.size();
        int i = 1;
        for (Map.Entry<String, Attribute> entry: this.attributes.entrySet()) {
            Attribute attribute = entry.getValue();
            if (attribute.getName().compareTo("id") != 0) {
                saveQuery.append(attribute.getName());
                if (i < attributeListLength) {
                    saveQuery.append(", ");
                } else {
                    saveQuery.append(")");
                }
            }
            i++;
        }
        saveQuery.append(" VALUES (");
        i = 1;
        for (Map.Entry<String, Attribute> entry: this.attributes.entrySet()) {
            Attribute attribute = entry.getValue();
            if (attribute.getName().compareTo("id") != 0) {
                if (attribute.getType().compareTo("varchar") == 0 ||
                        attribute.getType().compareTo("longtext") == 0) {
                    saveQuery.append("'").append(attribute.getValue()).append("'");
                } else {
                    saveQuery.append(attribute.getValue());
                }
                if (i < attributeListLength) {
                    saveQuery.append(", ");
                } else {
                    saveQuery.append(");");
                }
            }
            i++;
        }
        try {
            System.out.println(saveQuery.toString());
            this.stmt.executeUpdate(saveQuery.toString());
            this.closeStatement();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            this.closeStatement();
            return false;
        }
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
