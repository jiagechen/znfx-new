package cn.njust.label.main.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//@Document(collection = "rds_2021-03-16_15-22-55.dat_stmsADSB航迹")
@Document(collection = "track")
public class Track {
    private static final long serialVersionUID = -7412369913295735975L;
    @Id
    private String _id;
    private String plane_id;
    //@Field("Track_point_items") //起别名
    private List<Track_point_items> track_point_items;

    public String getPlane_id() {
        return plane_id;
    }

    public void setPlane_id(String plane_id) {
        this.plane_id = plane_id;
    }

    @Override
    public String toString() {
        return "Track{" +
                "_id='" + _id + '\'' +
                ", plane_id=" + plane_id +
                ", track_point_items=" + track_point_items +
                '}';
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }



    public List<Track_point_items> getTrack_point_items() {
        return track_point_items;
    }

    public void setTrack_point_items(List<Track_point_items> track_point_items) {
        this.track_point_items = track_point_items;
    }
}
