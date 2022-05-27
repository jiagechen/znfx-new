package cn.njust.label.main.service.impl;

import cn.njust.label.main.service.MongoFindPageService;
import com.mongodb.client.FindIterable;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @program:
 * @description:
 * @
 **/
@Service
@Slf4j
public class MongoFindPageServiceImpl implements MongoFindPageService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public List<Object> findPage(Query query, Integer page, Integer pageSize) {
        long count = mongoTemplate.count(query, "states_2022-04-25");
        FindIterable<Document> documents = mongoTemplate.getCollection("states_2022-04-25").find().limit(pageSize).skip((page - 1) * pageSize);
        return Collections.singletonList(documents);
    }
}
