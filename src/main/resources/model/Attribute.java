package model;

/**
 * Created by roland on 01/05/2017.
 */
public class Attribute {
    private String name;
    private String type;
    private Object value;

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

    public void setType(String type) {
        this.type = type;
    }

    public String getSQLColumnConstructor () {
        switch (this.type) {
            case "int":
                return " INT(6) ";
                break;
            case "longtext":
                return " LONGTEXT ";
                break;
            case "varchar":
                return " VARCHAR(255) ";
                break;
            default:
                return "";
        }
    }
}
