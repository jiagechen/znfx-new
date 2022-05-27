package cn.njust.label.main.utils;

import cn.njust.label.main.common.MongoPage;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Slf4j
public class MongoDBUtil {
    public static MongoPage findPart(int currentPage, int pageSize, MongoTemplate mongoTemplate, Class<?> entityClass){
        Query query = new Query();
        //限制从哪条数据开始
        query.skip((currentPage - 1)*pageSize);
        //限制取出多少条数据
        query.limit(pageSize);
        List<?> tracks = mongoTemplate.find(query, entityClass);
//        log.info(tracks.toString());
        long total = mongoTemplate.count(new Query(), entityClass);//获取总数
        log.info(total + "条数据");
//        log.info(entityClass.getName());
        MongoPage<?> data = new MongoPage<>(currentPage,pageSize,total,tracks);
        return data;
    }
}
