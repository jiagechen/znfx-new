package cn.njust.label.main.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "states_2022-04-25-00.csv")
public class ENPoint {
   // private static final long serialVersionUID = -70067266044697552L;
    @Id
    private String Id;
    @Field("plane_id")
    private String planeId;
    @Field("track_point_items")
    private List<MatchTrackPointItems> trackPointItems;

 public String getId() {
  return Id;
 }

 public void setId(String id) {
  Id = id;
 }

 public String getPlaneId() {
  return planeId;
 }

 public void setPlaneId(String planeId) {
  this.planeId = planeId;
 }

 public List<MatchTrackPointItems> getTrackPointItems() {
  return trackPointItems;
 }

 public void setTrackPointItems(List<MatchTrackPointItems> trackPointItems) {
  this.trackPointItems = trackPointItems;
 }
}
