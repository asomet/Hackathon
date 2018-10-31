package fr.wildcodeschool.hackathon;

public class Item {

    private String thumbnail;
    private  String name;

    public Item(String thumbnail, String name) {
        this.thumbnail = thumbnail;
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setName(String name) {
        this.name = name;
    }

}
