
package fr.wildcodeschool.hackathon;

public class DataModel {

    private String latitude;
    private String longitude;
    private String name;


    public DataModel(String latitude,String longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public DataModel() {
    }

    public String getlatitude() {
        return latitude;
    }

    public String getlongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }


}
