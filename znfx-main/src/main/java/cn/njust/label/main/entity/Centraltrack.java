package cn.njust.label.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("civil_aviation_center_route")//必须写
public class Centraltrack implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "routeID", type = IdType.AUTO)
    private Integer routeID;

    @TableField("initia_latitude")
    private Double initiaLatitude;

    @TableField("initia_longitude")
    private Double initiaLongitude;

    @TableField("target_latitude")
    private Double targetLatitude;

    @TableField("target_longitude")
    private Double targetLongitude;

    @TableField("width")
    private Integer width;

    @TableField("height_left")
    private Integer heightLeft;

    @TableField("height_right")
    private Integer heightRight;

    @TableField("MongoID")
    private String mongoID;

    public Centraltrack(Integer routeID, Double initiaLatitude, Double initiaLongitude, Double targetLatitude, Double targetLongitude, Integer width, Integer heightLeft, Integer heightRight, String mongoID) {
        this.routeID = routeID;
        this.initiaLatitude = initiaLatitude;
        this.initiaLongitude = initiaLongitude;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        this.width = width;
        this.heightLeft = heightLeft;
        this.heightRight = heightRight;
        this.mongoID = mongoID;
    }

    public Centraltrack() {
    }

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public Double getInitiaLatitude() {
        return initiaLatitude;
    }

    public void setInitiaLatitude(Double initiaLatitude) {
        this.initiaLatitude = initiaLatitude;
    }

    public Double getInitiaLongitude() {
        return initiaLongitude;
    }

    public void setInitiaLongitude(Double initiaLongitude) {
        this.initiaLongitude = initiaLongitude;
    }

    public Double getTargetLatitude() {
        return targetLatitude;
    }

    public void setTargetLatitude(Double targetLatitude) {
        this.targetLatitude = targetLatitude;
    }

    public Double getTargetLongitude() {
        return targetLongitude;
    }

    public void setTargetLongitude(Double targetLongitude) {
        this.targetLongitude = targetLongitude;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeightLeft() {
        return heightLeft;
    }

    public void setHeightLeft(Integer heightLeft) {
        this.heightLeft = heightLeft;
    }

    public Integer getHeightRight() {
        return heightRight;
    }

    public void setHeightRight(Integer heightRight) {
        this.heightRight = heightRight;
    }

    public String getMongoID() {
        return mongoID;
    }

    public void setMongoID(String mongoID) {
        this.mongoID = mongoID;
    }

    @Override
    public String toString() {
        return "civil_aviation_center_route{" +
                "routeID=" + routeID +
                ", initia_latitude=" + initiaLatitude +
                ", initia_longitude=" + initiaLongitude +
                ", target_latitude=" + targetLatitude +
                ", target_longitude=" + targetLongitude +
                ", width=" + width +
                ", height_left=" + heightLeft +
                ", height_right=" + heightRight +
                ", MongoID=" + mongoID +
                "}";
    }
}
