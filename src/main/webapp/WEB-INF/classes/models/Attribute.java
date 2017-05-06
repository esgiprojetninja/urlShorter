package models;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Attribute {
    private String name;
    private String type;
    private Object value;

    public Attribute(String name, String type) {
        this.name = name;
        try {
            this.setType(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Attribute (String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Attribute (Attribute attribute) {
        this.name = attribute.name;
        this.value = attribute.value;
        this.type = attribute.type;
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
        System.out.println(this.type);
        switch (this.type) {
            case "int":
                return "INT(6)";
            case "longtext":
                return "LONGTEXT";
            case "varchar":
                return "VARCHAR(255)";
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
