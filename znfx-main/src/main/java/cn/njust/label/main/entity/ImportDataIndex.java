package cn.njust.label.main.entity;

/**
 * @program:
 * @description: 数据对应的index
 * @
 **/
public class ImportDataIndex {
    private Integer targetId;
    private Integer longitude;
    private Integer latitude;
    private Integer height;
    private Integer timeStamp;
    private Integer heading;
    private Integer speed;
    // 数据总列数
    private Integer total;

    public ImportDataIndex(Integer targetId, Integer longitude, Integer latitude, Integer height, Integer timeStamp, Integer heading, Integer speed, Integer total) {
        this.targetId = targetId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
        this.timeStamp = timeStamp;
        this.heading = heading;
        this.speed = speed;
        this.total = total;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ImportDataIndex() {
    }

    @Override
    public String toString() {
        return "ImportDataIndex{" +
                "targetId=" + targetId +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", height=" + height +
                ", timeStamp=" + timeStamp +
                ", heading=" + heading +
                ", speed=" + speed +
                ", total=" + total +
                '}';
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
}
