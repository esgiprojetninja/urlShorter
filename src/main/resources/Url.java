/**
 * Created by roland on 28/04/2017.
 */
public class Url {
    private String base_url;
    private String shorter_url;
    private Integer id;
    private Integer user_id;

    @Override
    public String toString() {
        return "Url{" +
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
}
