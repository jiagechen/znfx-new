package cn.njust.label.main.entity;


import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class MatchTrackPointItems {
    private String heading;

    private String height;

    private String itemid;

    private String latitude;

    private String longitude;

    private String speed;

    private String timeStamp;

    public String toString() {
        return "PlanID{" +
                "heading='" + heading + '\'' +
                ", height='" + height + '\'' +
                ", itemid='" + itemid + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
