package cn.njust.label.main.service.impl;

import cn.njust.label.main.entity.ImportDataIndex;
import cn.njust.label.main.service.LoadFileService;
import cn.njust.label.main.utils.PathTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;

import static cn.njust.label.main.utils.ReadFileUtil.*;

@Service
@Slf4j
public class LoadFileServiceImpl implements LoadFileService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadFileServiceImpl.class);

    @Autowired
    @Qualifier("TemplateTrajectory")
    MongoTemplate mongoTemplate;

    public static LoadFileServiceImpl service;

    @PostConstruct
    public void init(){
        service = this;
        service.mongoTemplate = mongoTemplate;
    }

    public static String loadSingleCivilShipFile(File file, ImportDataIndex dataIndex) throws Exception {
        // 其他类型文件
        if(!file.getName().endsWith(".csv") && !file.getName().endsWith(".txt")){
            LOGGER.info("文件类型不符合要求，请更正！");
            return "load finished, loaded 0 items";
        }
        loadCivilShipFileByNio(file, "civil_ship_trajectory", service.mongoTemplate,  dataIndex);
        return "load success";
    }

    /**
     * @description: 加载单个民航轨迹文件
     * @param file:文件路径
     * @param dataIndex :数据索引点
     * @return java.lang.String: 导入状态
     * @date: 2022/5/3
     */
    public static String loadSingleCivilAviationFile(File file, ImportDataIndex dataIndex) throws Exception {
        // 其他类型文件
        if(!file.getName().endsWith(".csv") && !file.getName().endsWith(".txt")){
            LOGGER.info("文件类型不符合要求，请更正！");
            return "load finished, loaded 0 items";
        }
        /*
         * 导入加载数据到MongoDB
         * */
        loadCivilAviationFileByNio(file, "civil_aviation_trajectory", service.mongoTemplate, dataIndex);
        return "load success";

    }

    /**
     * 导入民船轨迹文件
     * @param filePath :文件路径
     * @param dataIndex :数据项索引对象，用于获取指定数据所在位置
     * */
    @Override
    public String loadCivilShipFile(String filePath, ImportDataIndex dataIndex) throws Exception {
        File file = new File(filePath);
        String fileType = PathTypeUtil.pathType(file);
        if(fileType.equals("file")){
            LOGGER.info(loadSingleCivilShipFile(file, dataIndex));
        }
        else if(fileType.equals("dir")){
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                LOGGER.info("文件列表为+" + Arrays.toString(files));
            }else{
                LOGGER.info("文件夹为空");
                return null;
            }
            for(File singleFile: files){
                LOGGER.info(loadSingleCivilShipFile(singleFile, dataIndex));
            }
        }
        LOGGER.info("导入完成。");
        return null;
    }

    /**
    * 导入民航轨迹文件
     * @param filePath :文件路径
     * @param dataIndex :数据项索引对象，用于获取指定数据所在位置
    * */
    @Override
    public String loadCivilAviationFile(String filePath, ImportDataIndex dataIndex) throws Exception {
        File file = new File(filePath);
        String fileType = PathTypeUtil.pathType(file);
        if(fileType.equals("file")){
            LOGGER.info(loadSingleCivilAviationFile(file, dataIndex));
        }
        else if(fileType.equals("dir")){
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                LOGGER.info("文件列表为+" + Arrays.toString(files));
            }else{
                LOGGER.info("文件夹为空");
                return null;
            }
            for(File singleFile: files){
                LOGGER.info(loadSingleCivilAviationFile(singleFile, dataIndex));
            }
        }
        LOGGER.info("导入完成。");
        return null;
    }

    public static void main(String[] args) {


    }
}
