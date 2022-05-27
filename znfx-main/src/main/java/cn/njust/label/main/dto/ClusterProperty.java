package cn.njust.label.main.dto;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

/**
 * @program:
 * @description: 每类数据的特征属性
 * @
 **/
@Data
@Document(collection = "ClusterProperty")
public class ClusterProperty {
    HashMap<String, Integer> targetTimes;   // 保存每个出现的目标和对应次数
    double avgSpeed;  // 平均速度
    double avgHeight; // 平均高度

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Id
    ObjectId id;
    public ClusterProperty() {
        this.targetTimes = new HashMap<>();
        this.avgHeight = 0.0;
        this.avgSpeed = 0.0;
    }

    public HashMap<String, Integer> getTargetTimes() {
        return targetTimes;
    }

    public void setTargetTimes(HashMap<String, Integer> targetTimes) {
        this.targetTimes = targetTimes;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgHeight() {
        return avgHeight;
    }

    public void setAvgHeight(double avgHeight) {
        this.avgHeight = avgHeight;
    }
}
