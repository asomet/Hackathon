package fr.wildcodeschool.hackathon;

public class Candy {

    //Attributs
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private int icon;
    private String image;
    private String type;

    //constructeur
    public Candy(String name, String description, double latitude, double longitude, int icon, String image, String type) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.image = image;
        this.type = type;
    }

    public Candy() {

    }
    //getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }
}
