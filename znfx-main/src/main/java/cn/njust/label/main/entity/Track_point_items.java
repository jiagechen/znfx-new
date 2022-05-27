package cn.njust.label.main.entity;


import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

public class Track_point_items implements Serializable {
    private static final long serialVersionUID = -7285726924L;

    private Integer itemid;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
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

    public String getHeigt() {
        return heigt;
    }

    public void setHeigt(String heigt) {
        this.heigt = heigt;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public Track_point_items(Integer itemid, String longitude, String latitude, String heigt, String heading, String speed, String time_stamp) {
        this.itemid = itemid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.heigt = heigt;
        this.heading = heading;
        this.speed = speed;
        this.time_stamp = time_stamp;
    }

    public Track_point_items() {
    }

    @Override
    public String toString() { //得到xlsx格式的输出
        return "{" +
                "\"itemid\":" + itemid +
                ", \"longitude\":" + longitude +
                ", \"latitude\":" + latitude +
                ", \"height\":" + heigt +
                ", \"heading\":" + heading +
                ", \"speed\":" + speed +
                ", \"time_stamp\":" + time_stamp +
                '}';
    }
    //得到csv格式的输出
    public String csvStyle() {
        return "{" +
                "\"" + "\""+"itemid" + "\"" + "\"" + ":" + itemid + "," +
                "\"" + "\""+"longitude" + "\"" + "\"" + ":" + longitude + "," +
                "\"" + "\""+"latitude" + "\"" + "\"" + ":" + latitude + "," +
                "\"" + "\""+"height" + "\"" + "\"" + ":" + heigt + "," +
                "\"" + "\""+"heading" + "\"" + "\"" + ":" + heading + "," +
                "\"" + "\""+"speed" + "\"" + "\"" + ":" + speed + "," +
                "\"" + "\""+"time_stamp" + "\"" + "\"" + ":" + time_stamp +
                '}';
    }

    private String  longitude;
    private String  latitude;
    @Field("height")
    private String heigt;
    private String  heading;
    private String  speed;
    private String  time_stamp;




}
