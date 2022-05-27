package cn.njust.label.main.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
@TableName("match")
@Data
public class Match {
    @Id
    private String _id;
    private String target_id;
    private List<TrackPointItems> track_point_items;
    private int handle;
    private List<MatchDetails> match_result;
}
