package cn.njust.label.main.service;

import io.swagger.models.auth.In;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface MongoFindPageService {
    List<Object> findPage(Query query, Integer page, Integer pageSize);
}
