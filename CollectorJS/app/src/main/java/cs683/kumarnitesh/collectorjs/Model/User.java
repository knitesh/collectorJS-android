package cs683.kumarnitesh.collectorjs.Model;

public class User {
    public String id;
    public String name;
    public String email;
    public String initial;
    public String image;
    public String loginMedium;


    public String thumb_image;

    public String getLoginMedium() {
        return loginMedium;
    }

    public void setLoginMedium(String loginMedium) {
        this.loginMedium = loginMedium;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
