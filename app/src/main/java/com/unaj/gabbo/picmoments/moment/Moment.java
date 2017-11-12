package com.unaj.gabbo.picmoments.moment;

import java.util.Date;

/**
 * Created by Gabbo on 26/10/2017.
 */

public class Moment {

    private String id;
    private byte[] imageAsBytes;
    private String location;
    private String description;
    private String formatDate;
    private int userId;


    public Moment(String id, byte[] imageAsBytes, String location, String description, String date, int userId) {
        this.id = id;
        this.imageAsBytes = imageAsBytes;
        this.location = location;
        this.description = description;
        this.formatDate = date;
        this.userId = userId;

    }

    public int getUserId (){
        return this.userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getId (){
        return id;
    }

    public void setId (String id){
        this.id = id;
    }

    public byte[] getImageAsBytes() {
        return imageAsBytes;
    }

    public void setImageAsBytes(byte[] imageAsBytes) {
        this.imageAsBytes = imageAsBytes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }
}
