package cn.njust.label.main.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * mongodb config
 * 多数据库
 *  装载MongoTemplate时使用@Qualifier("Template[Database]")选择数据库
 *  例如
 *  @Qualifier("TemplateTrajectory")
 *  private MongoTemplate mongoTemplateTra;
 *  装载${data.mongodb.database.trajectory}数据库
 */
@Configuration
public class MongoDBConfig {
    @Value("${data.mongodb.url}")
    private String mongoUrl;

    @Value("${data.mongodb.database.default}")
    private String defaultDatabase;
    @Value("${data.mongodb.database.trajectory}")
    private String trajectoryDatabase;

    @Value("${data.mongodb.database.process}")
    private String processDatabase;

    @Value("${data.mongodb.database.centreTra}")
    private String centreTraDatabase;

    @Value("${data.mongodb.database.match}")
    private String matchDatabase;
    @Primary
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoUrl), defaultDatabase));
    }

    @Bean(name = "TemplateTrajectory")
    public  MongoTemplate mongoTemplateTrajectory() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoUrl), trajectoryDatabase));
    }

    @Bean(name = "TemplateProcess")//数据源2的database使用test1
    public MongoTemplate mongoTemplateProcess() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoUrl), processDatabase));
    }
    @Bean(name = "TemplateCentreTra")//数据源2的database使用test1
    public MongoTemplate mongoTemplateCentreTra() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoUrl), centreTraDatabase));
    }
    @Bean(name = "TemplateMatch")//数据源匹配数据的database
    public MongoTemplate mongoTemplateMatch() {
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MongoClients.create(mongoUrl), centreTraDatabase));
    }
    // 开启事务
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory factory){
//        return new MongoTransactionManager(factory);
//    }

}
