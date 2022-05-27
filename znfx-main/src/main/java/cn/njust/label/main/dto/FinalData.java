package cn.njust.label.main.dto;

import cn.njust.label.main.entity.MatchTrackPointItems;

public class FinalData {
    private MatchTrackPointItems startPoint;
    private MatchTrackPointItems desPoint;

    public MatchTrackPointItems getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(MatchTrackPointItems startPoint) {
        this.startPoint = startPoint;
    }

    public MatchTrackPointItems getDesPoint() {
        return desPoint;
    }

    public void setDesPoint(MatchTrackPointItems desPoint) {
        this.desPoint = desPoint;
    }
}
