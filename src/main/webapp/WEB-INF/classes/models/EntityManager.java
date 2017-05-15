package models;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class EntityManager {
    private Connection conn = null;
    private Statement stmt = null;
    protected Map<String, Attribute> attributes = new HashMap<>();

    private String tableName;

    public EntityManager() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.conn = null;
        this.addAttribute(new Attribute("id", "int"));
    }

    public EntityManager(String tableName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.conn = null;
        this.setTableName(tableName);
        this.addAttribute(new Attribute("id", "int"));
    }

    public EntityManager(Map<String, Attribute> attributes) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.conn = null;
        this.setAttributes(attributes);
        this.addAttribute(new Attribute("id", "int"));
    }


    protected void connect() throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        if (this.conn == null) {
            try {
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

    public void createTableIfNotExist () throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        try {
            this.connect();
        } catch (ClassNotFoundException | SQLException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
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
                this.createStatement();
                this.stmt.executeUpdate(createTableQuery.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.closeStatement();
    }

    public void find(int id) throws SQLException {
        String queryString = String.format("SELECT * from " + this.tableName + " WHERE id = '%d'", id);
        try {
            this.connect();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        ResultSet sqlQuery = this.stmt.executeQuery(queryString);
        this.createStatement();
        if (sqlQuery.next()) {
            for (Map.Entry<String, Attribute> entry : this.attributes.entrySet()) {
                entry.getValue().setValueFromResultSet(sqlQuery);
            }
        }
        this.closeStatement();
    }

    public int findBy(String column, String value) throws SQLException {
        int id = 0;
        String queryString = String.format(
                "SELECT * from " + this.tableName + " WHERE " + column + " = '%s'", value
        );
        try {
            this.connect();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        this.createStatement();
        ResultSet sqlQuery = this.stmt.executeQuery(queryString);
        if (sqlQuery.next()) {
            for (Map.Entry<String, Attribute> entry : this.attributes.entrySet()) {
                entry.getValue().setValueFromResultSet(sqlQuery);
            }
            id =  sqlQuery.getInt(1);
        }
        this.closeStatement();
        return id;
    }

    public Map<String, Attribute> getAttributes() {
        return attributes;
    }

    public <T extends EntityManager> ArrayList<T> getAll(Class<T> type) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException {
        ArrayList<T> entities = new ArrayList<>();
        try {
            this.connect();
            this.createStatement();
            String listUserQuery = "SELECT * FROM " + this.tableName + " ;";
            ResultSet entitySQLList = this.stmt.executeQuery(listUserQuery);
            while(entitySQLList.next()) {
                T res = type.getConstructor().newInstance();
                for (Map.Entry<String, Attribute> entry: this.attributes.entrySet()) {
                    Attribute attribute = new Attribute(entry.getValue());
                    attribute.setValueFromResultSet(entitySQLList);
                    res.addAttribute(attribute);
                }
                entities.add(res);
            }
            this.closeStatement();
        } catch (SQLException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public int update () throws SQLException {
        try {
            this.connect();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        this.createStatement();
        StringBuilder updateQuery = new StringBuilder();
        updateQuery.append("UPDATE ").append(this.tableName).append(" SET ");
        int attributeListLength = this.attributes.size();
        int i = 1;
        for (Map.Entry<String, Attribute> entry: this.attributes.entrySet()) {
            Attribute attribute = entry.getValue();
            if (attribute.getName().compareTo("id") != 0) {
                updateQuery
                        .append(attribute.getName())
                        .append("=");
                if (attribute.getType().compareTo("varchar") == 0 ||
                        attribute.getType().compareTo("longtext") == 0) {
                    updateQuery.append("'").append(attribute.getValue()).append("'");
                } else {
                    updateQuery.append(attribute.getValue());
                }
                if (i < attributeListLength) {
                    updateQuery.append(", ");
                } else {
                    updateQuery.append(" ");
                }
            }
            i++;
        }
        updateQuery
                .append("WHERE id=")
                .append(this.getAttributes().get("id").getValue())
                .append(";");
        try {
            this.stmt.executeUpdate(updateQuery.toString(), Statement.RETURN_GENERATED_KEYS);
            this.closeStatement();
            return (int) this.getAttributes().get("id").getValue();
        } catch (SQLException e) {
            e.printStackTrace();
            this.closeStatement();
            return 0;
        }
    }

    public int save() throws SQLException {
        if (this.getAttributes().get("id").getValue() != null) {
            return this.update();
        }
        try {
            this.connect();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
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
        int id = 0;
        try {
            this.stmt.executeUpdate(saveQuery.toString(), Statement.RETURN_GENERATED_KEYS);
            ResultSet gg = stmt.getGeneratedKeys();
            if (gg.next()) {
                id =  gg.getInt(1);
                this.find(id);
            }
            this.closeStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            this.closeStatement();
        }
        return id;
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
