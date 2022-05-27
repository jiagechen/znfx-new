package cn.njust.label.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lian
 * @since 2022-02-17
 */
@TableName("civil_ship_center_route")
public class Shipcentraltrack implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "route_id", type = IdType.AUTO)//主键为String类型，设置为type = IdType.ASSIGN_UUID，系统自动设置主键
    private Integer routeID;

    @TableField("initia_latitude")
    private Float initiaLatitude;

    @TableField("initia_longitude")
    private Float initiaLongitude;

    @TableField("target_latitude")
    private Float targetLatitude;

    @TableField("target_longitude")
    private Float targetLongitude;

    @TableField("width")
    private Integer Width;

    @TableField("mongo_id")
    private String mongoid;

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public Float getInitiaLatitude() {
        return initiaLatitude;
    }

    public void setInitiaLatitude(Float initiaLatitude) {
        this.initiaLatitude = initiaLatitude;
    }

    public Float getInitiaLongitude() {
        return initiaLongitude;
    }

    public void setInitiaLongitude(Float initiaLongitude) {
        this.initiaLongitude = initiaLongitude;
    }

    public Float getTargetLatitude() {
        return targetLatitude;
    }

    public void setTargetLatitude(Float targetLatitude) {
        this.targetLatitude = targetLatitude;
    }

    public Float getTargetLongitude() {
        return targetLongitude;
    }

    public void setTargetLongitude(Float targetLongitude) {
        this.targetLongitude = targetLongitude;
    }

    public Integer getWidth() {
        return Width;
    }

    public void setWidth(Integer width) {
        Width = width;
    }

    public String getMongoid() {
        return mongoid;
    }

    public void setMongoid(String mongoid) {
        this.mongoid = mongoid;
    }

    public Shipcentraltrack() {
    }

    public Shipcentraltrack(Integer routeID, Float initiaLatitude, Float initiaLongitude, Float targetLatitude, Float targetLongitude, Integer width, String mongoid) {
        this.routeID = routeID;
        this.initiaLatitude = initiaLatitude;
        this.initiaLongitude = initiaLongitude;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        Width = width;
        this.mongoid = mongoid;
    }

    @Override
    public String toString() {
        return "civil_ship_center_route{" +
                "route_id=" + routeID +
                ", initia_latitude=" + initiaLatitude +
                ", initia_longitude=" + initiaLongitude +
                ", target_latitude=" + targetLatitude +
                ", target_longitude=" + targetLongitude +
                ", width=" + Width +
                ", mongo_id=" + mongoid +
                "}";
    }
}
