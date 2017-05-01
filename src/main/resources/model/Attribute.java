package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by roland on 01/05/2017.
 */
public class Attribute {
    private String name;
    private String type;
    private Object value;

    public Attribute () {

    }

    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Attribute (String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) throws Exception {
        if (type.compareTo("int") != 0
                && type.compareTo("longtext") != 0
                && type.compareTo("varchar") != 0) {
            throw new Exception("Type must be 'longtext', 'varchar' or 'int'.");
        } else {
            this.type = type;
        }
    }

    public String getSQLColumnConstructor () {
        switch (this.type) {
            case "int":
                return " INT(6) ";
            case "longtext":
                return " LONGTEXT ";
            case "varchar":
                return " VARCHAR(255) ";
            default:
                return "";
        }
    }

    public void setValueFromResultSet(ResultSet result) throws SQLException {
        if (this.type.compareTo("int") == 0) {
            this.value = result.getInt(this.name);
        } else {
            this.value = result.getString(this.name);
        }
    }
}
