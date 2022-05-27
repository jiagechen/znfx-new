package cn.njust.label.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "ClusterProperty")
public class ClusterProperty {
    @Id
    private String _id;
    private Double avgSpeed;
    private Double avgHeight;
    private HashMap<String,String> targetTimes;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public Double getAvgHeight() {
        return avgHeight;
    }

    public void setAvgHeight(Double avgHeight) {
        this.avgHeight = avgHeight;
    }

    public HashMap<String, String> getTargetTimes() {
        return targetTimes;
    }

    public void setTargetTimes(HashMap<String, String> targetTimes) {
        this.targetTimes = targetTimes;
    }

    public ClusterProperty(String _id, Double avgSpeed, Double avgHeight, HashMap<String, String> targetTimes) {
        this._id = _id;
        this.avgSpeed = avgSpeed;
        this.avgHeight = avgHeight;
        this.targetTimes = targetTimes;
    }

    public ClusterProperty() {
    }
}
