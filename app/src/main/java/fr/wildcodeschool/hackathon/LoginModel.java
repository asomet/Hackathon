package fr.wildcodeschool.hackathon;

public class LoginModel {

    private String email;
    private String photo;
    private String pseudo;

    public LoginModel(String email, String photo, String pseudo) {
        this.email = email;
        this.photo = photo;
        this.pseudo = pseudo;
    }

    public LoginModel() {
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
}
