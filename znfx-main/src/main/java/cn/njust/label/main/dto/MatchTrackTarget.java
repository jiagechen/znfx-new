package cn.njust.label.main.dto;

import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

/**
 * @program:
 * @description:
 * @
 **/
public class MatchTrackTarget {
    @Id
    private String id;
    private String targetId;
    private List<Document> trackPointItems;
    private Integer handle;
    private Map<String, String> matchResult;

    public MatchTrackTarget() {
    }

    @Override
    public String toString() {
        return "MatchTrackTarget{" +
                "id='" + id + '\'' +
                ", targetId='" + targetId + '\'' +
                ", trackPointItems=" + trackPointItems +
                ", handle=" + handle +
                ", matchResult=" + matchResult +
                '}';
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

    public Map<String, String> getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(Map<String, String> matchResult) {
        this.matchResult = matchResult;
    }

    public MatchTrackTarget(String id, String targetId, List<Document> trackPointItems, Integer handle, Map<String, String> matchResult) {
        this.id = id;
        this.targetId = targetId;
        this.trackPointItems = trackPointItems;
        this.handle = handle;
        this.matchResult = matchResult;
    }
}
