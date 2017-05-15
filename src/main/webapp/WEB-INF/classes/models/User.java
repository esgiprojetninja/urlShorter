package models;

import java.io.Serializable;
import java.sql.SQLException;

public class User extends EntityManager implements Serializable {
    public User() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        super();
        this.setTableName("users");
        this.addAttribute(new Attribute("name", "varchar"));
        this.addAttribute(new Attribute("pwd", "varchar"));
    }

    @Override
    public String toString() {
        return "User{" +
                "attributes=" + attributes +
                '}';
    }
}
