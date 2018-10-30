package fr.wildcodeschool.hackathon;

public class LoginModel {

    private String email;
    private String password;
    private String photo;


    public LoginModel(String email,String password, String photo) {
        this.email = email;
        this.photo = photo;
    }

    public LoginModel() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
