package cn.njust.label.main.service;

import cn.njust.label.main.entity.ImportDataIndex;

/**
 */
public interface LoadFileService {
    /*导入ssr text类型文件*/
    String loadCivilAviationFile(String filePath, ImportDataIndex dataIndex) throws Exception;
    String loadCivilShipFile(String filePath, ImportDataIndex dataIndex) throws Exception;
}
