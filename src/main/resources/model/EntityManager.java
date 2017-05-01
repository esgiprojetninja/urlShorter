package model;
/**
 * Created by roland on 28/04/2017.
 */

import java.sql.*;
import java.util.ArrayList;

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
            this.conn = DriverManager.getConnection("jdbc:mysql://192.168.10.10/url_shorter?" + "user=shortenme&password=secret");
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

    public void find(int id) throws SQLException {
        this.createStatement();
        String queryString = String.format("SELECT * from " + this.tableName + " WHERE id = '%d'", id);
        ResultSet sqlQuery = this.stmt.executeQuery(queryString);
        if (sqlQuery.next()) {
            for (Attribute attribute : this.attributes) {
                attribute.setValueFromResultSet(sqlQuery);
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
                for (Attribute attribute: this.attributes) {
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

    public void save() throws SQLException {
        this.createStatement();
        StringBuilder saveQuery = new StringBuilder("INSERT INTO" + this.tableName + " (");
        Integer attributeListLength = this.attributes.size();
        int i = 0;
        for (Attribute attribute : this.attributes) {
            saveQuery.append(attribute.getName());
            if (i < attributeListLength) {
                saveQuery.append(", ");
            } else {
                saveQuery.append(")");
            }
        }
        saveQuery.append(" VALUES (");
        for (Attribute attribute : this.attributes) {
            saveQuery.append(attribute.getValue());
            if (i < attributeListLength) {
                saveQuery.append(", ");
            } else {
                saveQuery.append(");");
            }
        }
        try {
            this.stmt.executeUpdate(saveQuery.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeStatement();
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
