package cn.njust.label.main.dao;

import cn.njust.label.main.dto.TrackTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class TrackTargetDao {
    @Autowired
    @Qualifier("TemplateTrajectory")
    public MongoTemplate mongoTemplate;
    public List<TrackTarget> findAll(){
        return mongoTemplate.findAll(TrackTarget.class);
    }
}
