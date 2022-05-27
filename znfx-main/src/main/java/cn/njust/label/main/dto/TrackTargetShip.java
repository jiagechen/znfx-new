package cn.njust.label.main.dto;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @program:
 * @description: 导入数据对象，属性包括 id（数据id），targetId（目标标识id），trackPointItems (目标轨迹点集合), handle (是否经过航线提取）
 * @
 **/
@org.springframework.data.mongodb.core.mapping.Document(collection = "civil_ship_trajectory")
public class TrackTargetShip {
    @Id
    private String id;
    private String targetId;
    private List<Document> trackPointItems;
    private Integer handle;

    @Override
    public String toString() {
        return "TrackTarget{" +
                "id='" + id + '\'' +
                ", targetId='" + targetId + '\'' +
                ", trackPointItems=" + trackPointItems +
                ", handle=" + handle +
                '}';
    }

    public TrackTargetShip(String id, String targetId, List<Document> trackPointItems, Integer handle) {
        this.id = id;
        this.targetId = targetId;
        this.trackPointItems = trackPointItems;
        this.handle = handle;
    }

    public TrackTargetShip() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public List<Document> getTrackPointItems() {
        return trackPointItems;
    }

    public void setTrackPointItems(List<Document> trackPointItems) {
        this.trackPointItems = trackPointItems;
    }

    public Integer getHandle() {
        return handle;
    }

    public void setHandle(Integer handle) {
        this.handle = handle;
    }
}
