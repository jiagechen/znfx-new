package cn.njust.label.main.dto;

public class Stripe {
    double start_lon;
    double start_lat;
    double end_lon;
    double end_lat;

    public double getStart_lon() {
        return start_lon;
    }

    public void setStart_lon(double start_lon) {
        this.start_lon = start_lon;
    }

    public double getStart_lat() {
        return start_lat;
    }

    public void setStart_lat(double start_lat) {
        this.start_lat = start_lat;
    }

    public double getEnd_lon() {
        return end_lon;
    }

    public void setEnd_lon(double end_lon) {
        this.end_lon = end_lon;
    }

    public double getEnd_lat() {
        return end_lat;
    }

    public void setEnd_lat(double end_lat) {
        this.end_lat = end_lat;
    }
}
