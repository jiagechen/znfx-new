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
@TableName("matchtrack")
public class Matchtrack implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "routeID", type = IdType.ASSIGN_UUID)
    private String routeID;

    @TableField("Initia_latitude")
    private Float initiaLatitude;

    @TableField("Initia_longitude")
    private Float initiaLongitude;

    @TableField("Angel")
    private Float Angel;


    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
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

    public Float getAngel() {
        return Angel;
    }

    public void setAngel(Float Angel) {
        this.Angel = Angel;
    }

    @Override
    public String toString() {
        return "Matchtrack{" +
        "routeID=" + routeID +
        ", initiaLatitude=" + initiaLatitude +
        ", initiaLongitude=" + initiaLongitude +
        ", Angel=" + Angel +
        "}";
    }
}
