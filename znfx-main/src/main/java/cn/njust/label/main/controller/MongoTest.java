package cn.njust.label.main.controller;

import cn.njust.label.main.dto.ClusterProperty;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class MongoTest {
    @Autowired
//    @Qualifier("Template")
    private MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier("TemplateTrajectory")
    private MongoTemplate mongoTemplateTra;

    @RequestMapping("/mongodbtest")
    @ResponseBody
    public String Mongodbtest() {
        mongoTemplate.getCollection("ClusterProperty");
        Query query = new Query(Criteria.where("_id").is("626bc743a58b176133acbcbd"));
        List<ClusterProperty> test = mongoTemplate.find(query, ClusterProperty.class);
        System.out.println(test);
        return "ok";
    }

    @RequestMapping("/mongodbTraTest")
    @ResponseBody
    public FindIterable<Document> mongodbTraTest(){
        MongoCollection<Document> collection = mongoTemplateTra.getCollection("states_2022-04-25");
//        Query query = new Query(Criteria.where("_id").is("626bc743a58b176133acbcbd"));
        FindIterable<Document> test = collection.find().projection(Projections.include("_id")).limit(5);
        System.out.println(test);
        return test;
    }
}
