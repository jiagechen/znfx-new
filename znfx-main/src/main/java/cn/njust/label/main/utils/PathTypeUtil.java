package cn.njust.label.main.utils;

import java.io.File;

/*
* 判断路径类型
* */
public class PathTypeUtil {
    /**
    * @Param: file:路径
    * */
    public static String pathType(File file){
        if (file.isFile()){
            return "file";
        }else if (file.isDirectory()){
            return "dir";
        }
        return "unknown";
    }
}
