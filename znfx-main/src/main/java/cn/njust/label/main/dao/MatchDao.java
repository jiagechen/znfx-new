package cn.njust.label.main.dao;

import cn.njust.label.main.entity.ENPoint;
import cn.njust.label.main.dto.Rtra;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class MatchDao {

    public MongoTemplate mongoTemplate;
    public List<Rtra> getALLCentralRoute(){
        return (List<Rtra>) mongoTemplate.findAll(Rtra.class);
    }

    public List<ENPoint> getAllRawData(){
        return mongoTemplate.findAll(ENPoint.class);
    }

    public Rtra getCentralRouteById(String lineID){return mongoTemplate.findById(lineID,Rtra.class);}
}
