package cn.njust.label.main.service.impl;

import cn.njust.label.main.dto.ClusterProperty;
import cn.njust.label.main.service.HandleEveryRouteClassService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @program:
 * @description:
 * @
 **/
@Component
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
public class HandleEveryRouteClassServiceImpl implements HandleEveryRouteClassService {

    @Autowired
    MongoTemplate mongoTemplate;

//    HashMap<Integer , ArrayList<Line>>
    private static HandleEveryRouteClassServiceImpl service;
    private static final double EARTH_RADIUS = 6371000; // 平均半径,单位：m；不是赤道半径。赤道为6378左右

    @PostConstruct
    public void init(){
        service = this;
        service.mongoTemplate = mongoTemplate;

    }



    /**
    * @Description: 计算平均速度、高度
    * @Param: [start, end]
    * @return: void
    * @Date: 2022/4/29
    */
    public void computeProperty(Document start, Document end, ClusterProperty clusterProperty){
        double curSpeed = clusterProperty.getAvgSpeed();
        double curHeight = clusterProperty.getAvgHeight();
        double distance = getDistance((double) start.get("latitude"),
                                        (double) start.get("longitude"),
                                        (double) end.get("latitude"),
                                        (double) end.get("longitude"));
//        double sTime = (double) start.get("time_stamp");
//        double eTime = (double) end.get("time_stamp");
        double time = 1.0;
        double avgSpeed = distance / time;
        double avgHeight = (Double.parseDouble((String) start.get("height")) + Double.parseDouble((String) end.get("height"))) / 2;
        clusterProperty.setAvgHeight(curHeight == 0.0 ? avgHeight : (curHeight + avgHeight) / 2);
        clusterProperty.setAvgHeight(curSpeed == 0.0 ? avgHeight : (curSpeed + avgSpeed) / 2);
    }

    /**
     * @Description: 获取两个经纬度之间的距离（单位米）.
     * @param sLat: 起点纬度
     * @param sLon :起点经度
     * @param eLat :终点纬度
     * @param eLon ：终点经度
    * @return double: 距离（m)
    * @date : 2022/4/29
    */
    public static double getDistance(double sLat, double sLon, double eLat, double eLon){
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        double radiansAX = Math.toRadians(sLat); // A经弧度
        double radiansAY = Math.toRadians(sLon); // A纬弧度
        double radiansBX = Math.toRadians(eLat); // B经弧度
        double radiansBY = Math.toRadians(eLon); // B纬弧度

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAY) * Math.cos(radiansBY) * Math.cos(radiansAX - radiansBX)
                + Math.sin(radiansAY) * Math.sin(radiansBY);
        // System.out.println("cos = " + cos); // 值域[-1,1]
        double acos = Math.acos(cos); // 反余弦值
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        return EARTH_RADIUS * acos; // 最终结果
    }

    /**
     * @ Description: 处理集合内一个文档
     * @ Param: [id, collection]
     * @return: void
     * @Date: 2022/4/29
     */
    public ClusterProperty handle(ObjectId id, MongoCollection<Document> collection){
        BasicDBObject queryObject = new BasicDBObject("_id",id);
        FindIterable<Document> findIterable = collection.find(queryObject);
        ClusterProperty clusterProperty = new ClusterProperty();
        for (Document doc: findIterable
        ) {
            ArrayList<Document> lines = (ArrayList<Document>) doc.get("value");
            if(lines.size() == 0) return null;
            HashMap<String, Integer> appearTimes= new HashMap<>(); //保存每个目标的出现次数
            for(Document line: lines){
                int num = (int) line.get("num");
                if(appearTimes.containsKey(String.valueOf(num)))appearTimes.put(String.valueOf(num), appearTimes.get(String.valueOf(num))+ 1);
                else appearTimes.put(String.valueOf(num), 1);
                computeProperty((Document) line.get("s"), (Document) line.get("e"), clusterProperty);
            }
            clusterProperty.setTargetTimes(appearTimes);
        }
        return clusterProperty;
    }

    @Override
    public void handleEveryRouteClass() {
        // 读取Cluster集合数据
        MongoCollection<Document> collection = service.mongoTemplate.getCollection("Cluster");

        //获取集合所有文档的_id
        FindIterable<Document> findIterable = collection.find().projection(Projections.include("_id"));
        for (Document documentId : findIterable) {
            ObjectId id = documentId.getObjectId("_id");
            ClusterProperty clusterProperty = handle(id, collection);
            clusterProperty.setId(id);
            Query query = Query.query(Criteria.where("_id").is(id));
            //存在就更新，不存在就插入
            Update update = new Update();
            update.set("avgSpeed", clusterProperty.getAvgSpeed());
            update.set("avgHeight", clusterProperty.getAvgHeight());
            update.set("targetTimes", clusterProperty.getTargetTimes());
            service.mongoTemplate.upsert(query, update, ClusterProperty.class);
//            service.mongoTemplate.insert(clusterProperty, "ClusterProperty");
//            break;
        }
    }



    public static void main(String[] args) {
        System.out.println(getDistance(39.057586669921875,84.95757393215013,38.79086251986229,85.44061279296875));
    }
}

