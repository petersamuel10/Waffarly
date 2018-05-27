package com.example.jesus.waffarly;

/**
 * Created by jesus on 5/11/2018.
 */

public class RecyclerModel {

    private String image;
    private String name;
    private String summary;
    private String description;
    private String address;
    private String longitude;
    private String latitude;

    public RecyclerModel()
    {

    }
    public RecyclerModel(String image, String name, String summary, String description, String address, String longitude, String latitude) {
        this.image = image;
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
