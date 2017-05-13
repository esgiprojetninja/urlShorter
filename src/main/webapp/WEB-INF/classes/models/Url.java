package models;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class Url extends EntityManager implements Serializable {
    private String base_url;
    private String shorter_url;
    private Integer id;
    private Integer user_id;

    public Url() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        super();
        this.setTableName("urls");
        this.addAttribute(new Attribute("base_url", "longtext"));
        this.addAttribute(new Attribute("shorter_url", "varchar"));
        this.addAttribute(new Attribute("user_id", "int"));
    }

    @Override
    public String toString() {
        return "models.Url{" +
                "base_url='" + base_url + '\'' +
                ", shorter_url='" + shorter_url + '\'' +
                ", id=" + id +
                ", user_id=" + user_id +
                '}';
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
        this.attributes.get("base_url").setValue(base_url);
    }

    public String getShorter_url() {
        return shorter_url;
    }

    public void setShorter_url(String shorter_url) {
        this.shorter_url = shorter_url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public int save() throws SQLException {
        id = 0;
        try {
            int id = super.save();
            if (id != 0) {
                char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                StringBuilder sb = new StringBuilder();
                sb.append(id);
                Random random = new Random();
                for (int i = 0; i < 7; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                this.getAttributes().get("shorter_url").setValue(sb.toString());
                return super.save();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
