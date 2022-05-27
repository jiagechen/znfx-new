package cn.njust.label.main.controller;


import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.common.Result;
import cn.njust.label.main.dto.FileTb;
import cn.njust.label.main.dto.TrackTarget;
import cn.njust.label.main.dto.TrackTargetShip;
import cn.njust.label.main.entity.FilePojo;
import cn.njust.label.main.entity.ImportDataIndex;
import cn.njust.label.main.service.FileTbService;
import cn.njust.label.main.service.TrackTargetService;
import cn.njust.label.main.service.impl.LoadFileServiceImpl;
import cn.njust.label.main.utils.FileConstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/tracktarget")
public class TrackTargetController {

    @Autowired
    private TrackTargetService service;

    //查询 ---  民航轨迹数据存储表（保存于数据库znfx_trajectory中集合civil_aviation_trajectory）
    @GetMapping("/aviation")
    public Result<?> findPage1(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "5") Integer pageSize){
        //List<Track> list = trackService.findAllTrack();
        //List<Track> list = trackService.findPart(pageNum, pageSize);
        System.out.println("enter track");
        MongoPage<TrackTarget> data = service.findPart(pageNum,pageSize);
        return Result.success(data);
    }

    //查询 ---  页面民船轨迹目标数据（保存于数据库znfx_trajectory中集合civil_ship_trajectory）
    @GetMapping("/ship")
    public Result<?> findPage2(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "5") Integer pageSize){
        //List<Track> list = trackService.findAllTrack();
        //List<Track> list = trackService.findPart(pageNum, pageSize);
        System.out.println("enter ship track");
        MongoPage<TrackTargetShip> data = service.findPartShip(pageNum,pageSize);
        return Result.success(data);
    }


    @Autowired
    private FileTbService fileTbService;
    @Autowired
    private LoadFileServiceImpl loadFileService;
    //上传目标船迹数据   upload，merge，check
    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file") MultipartFile file,
                         FilePojo filePojo,ImportDataIndex dataIndex) throws Exception {
        System.out.println("enter upload ship");
        System.out.println(dataIndex);
        File fullDir = new File(FileConstance.FILE_PATH);
        if (!fullDir.exists()) {
            fullDir.mkdir();
        }
        try{
            //uid 防止文件名重复,又可以作为文件的唯一标识
            //String fileDirPath = FileConstance.FILE_PATH + UUID.randomUUID().toString() + "//"; //大文件夹下的子文件夹存放
            String fullPath = FileConstance.FILE_PATH + filePojo.getKey() + "." + filePojo.getShardIndex();
            File dest = new File(fullPath);
            if(dest.exists()){
                dest.delete();
            }
            file.transferTo(dest);
            System.out.println("文件分片 {} 保存完成" + filePojo.getShardIndex());
            //log.info("文件分片 {} 保存完成",filePojo.getShardIndex());
            //开始保存索引分片信息 bu不存在就新加 存在就修改索引分片
            FileTb fileTb = FileTb.builder()
                    .fKey(filePojo.getKey())
                    .fIndex(Math.toIntExact(filePojo.getShardIndex()))
                    .fTotal(Math.toIntExact(filePojo.getShardTotal()))
                    .fName(filePojo.getFileName())
                    .build();
            if (fileTbService.isNotExist(filePojo.getKey())) {
                fileTbService.saveFile(fileTb);
            }else {
                fileTbService.UpdateFile(fileTb);
            }
            if (filePojo.getShardIndex() == filePojo.getShardTotal()) {
                //开始合并
                merge(filePojo,dataIndex);
                return Result.success(FileConstance.ACCESS_PATH + filePojo.getFileName());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("文件加载错误");
            //如果出错，删除文件夹下与key同名的文件
            fullDir = new File(FileConstance.FILE_PATH);
            if (fullDir != null && fullDir.exists()) {
                File[] files = fullDir.listFiles();
                System.out.println("分文件数量" + files.length);
                System.out.println("分文件名");
                for (File f : files) {
                    if (!f.getName().contains(".")) {
                        continue;
                    }
                    System.out.println(f.getName());
                    String fname = f.getName().substring(0, f.getName().indexOf('.'));
                    String delname = filePojo.getKey();
                    System.out.println("要删除文件名 : " + delname);
                    if (delname != null && delname.equals(fname)) { //删除
                        System.out.println("删除分文件 : " + fname);
                        f.delete();
                    }
                }
            }
            return Result.fail();
        }

        return Result.success();
    }
    public void merge(FilePojo filePojo,ImportDataIndex dataIndex) throws Exception {
        System.out.println("enter merge");
        Long shardTotal = filePojo.getShardTotal();
        File newFile = new File(FileConstance.FILE_PATH + filePojo.getFileName());
        if (newFile.exists()) {
            newFile.delete();
        }
        FileOutputStream outputStream = new FileOutputStream(newFile, true);//文件追加写入
        FileInputStream fileInputStream = null;//分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                // 读取第i个分片
                fileInputStream = new FileInputStream(new File(FileConstance.FILE_PATH + filePojo.getKey() + "." + (i + 1))); //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);//一直追加到合并的新文件中
                }
            }
        } catch (IOException e) {
            System.out.println("分片合并异常" + e);
            //log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                System.out.println("IO流关闭");
                //log.info("IO流关闭");
                System.gc();
            } catch (Exception e) {
                System.out.println("IO流关闭" + e);
                //log.error("IO流关闭", e);
            }
            //删除所有的临时文件
        }
        System.out.println("合并分片结束");
        //文件合并完成后，进行数据处理并存入数据库中 --- newFile
        //int[] dataIndex = {0, 2, 3, -1, 2, 6, -1};//{0,2,3,-1,1,6,4};//0, 2, 3, -1, 2, 6, -1
        /** dataIndex数组中的含义
         * targetID 目标标识所在的列
         * longitude longitude所在的列
         * latitude latitude所在的列
         * height   height所在的列（民船数据没有这一列应为-1）
         * time_stamp   timestamp所在的列
         * heading  heading所在的列
         * speed    speed所在的列
         * 以上数据如果在文件中不存在，则值为-1
         */
        String filePath = FileConstance.FILE_PATH + filePojo.getFileName();
        System.out.println("filePath : " + filePath);
        System.out.println("newFile length : " + newFile.length());
        System.out.println("newFile name : " + newFile.getName());
        //LoadFileServiceImpl.loadSingleCivilShipFile(newFile,dataIndex);
        try{
            //targetId,longitude,latitude,height,timeStamp,heading,speed,total
            //ImportDataIndex dataIndex = new ImportDataIndex(0,2,3,-1,1,6,4,17);
            loadFileService.loadCivilShipFile(filePath,dataIndex);
            System.out.println("数据导入完毕");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("文件导入报错");
        }finally { //最后删除产生的临时文件，合并文件，数据库文件
            //根据文件名 = f_key 清除临时文件   以及   合并后的文件
            //最后清空FileConstance.FILE_PATH这个文件夹下所有 文件 及其 目录
            try{
                File fullDir = new File(FileConstance.FILE_PATH);
                if (fullDir != null && fullDir.exists()) {
                    File[] files = fullDir.listFiles();
                    System.out.println("分文件数量" + files.length);
                    System.out.println("分文件名");
                    for(File f : files){
                        if(!f.getName().contains(".")){
                            continue;
                        }
                        System.out.println(f.getName());
                        String fname = f.getName().substring(0,f.getName().indexOf('.'));
                        String delname = filePojo.getKey();
                        System.out.println("要删除文件名 : " + delname);
                        if(delname != null && delname.equals(fname)){ //删除
                            System.out.println("删除分文件 : " + fname);
                            f.delete();
                        }
                    }
                }
                System.out.println("临时文件清除完毕");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("临时文件删除出错");
            }
            try{
                System.out.println("合并文件名 ： " + newFile.getName());
                newFile.delete();
                System.out.println("合并文件删除完毕");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("合并文件删除报错");
            }
            try{
                //根据f_key清除mysql表
                fileTbService.delByFkey(filePojo.getKey());
                System.out.println("文件数据库临时文件删除成功");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("文件所在临时数据库删除错误");
            }
        }
    }
    //文件上传之前判断是否已经上传过 -1就是没有
    @GetMapping("/check")
    public Result check(@RequestParam String key){
        FileTb fileTb = fileTbService.selectLatestIndex(key);
        System.out.println("检查分片" + key);
        //log.info("检查分片：{}");
        return Result.success(fileTb);
    }



    //上传目标航迹数据 uploadaviation,mergeaviation,checkaviation
    @PostMapping("/uploadaviation")
    public Result uploadaviation(@RequestParam(value = "file") MultipartFile file,
                         FilePojo filePojo,ImportDataIndex dataIndex) throws Exception {
        System.out.println("enter upload aviation");
        System.out.println(dataIndex);
        File fullDir = new File(FileConstance.FILE_PATH_AVIATION);
        if (!fullDir.exists()) {
            fullDir.mkdir();
        }
        try{
            //uid 防止文件名重复,又可以作为文件的唯一标识
            //String fileDirPath = FileConstance.FILE_PATH + UUID.randomUUID().toString() + "//"; //大文件夹下的子文件夹存放
            String fullPath = FileConstance.FILE_PATH_AVIATION + filePojo.getKey() + "." + filePojo.getShardIndex();
            File dest = new File(fullPath);
            if(dest.exists()){
                dest.delete();
            }
            file.transferTo(dest);
            System.out.println("文件分片 {} 保存完成" + filePojo.getShardIndex());
            //log.info("文件分片 {} 保存完成",filePojo.getShardIndex());
            //开始保存索引分片信息 bu不存在就新加 存在就修改索引分片
            FileTb fileTb = FileTb.builder()
                    .fKey(filePojo.getKey())
                    .fIndex(Math.toIntExact(filePojo.getShardIndex()))
                    .fTotal(Math.toIntExact(filePojo.getShardTotal()))
                    .fName(filePojo.getFileName())
                    .build();
            if (fileTbService.isNotExist(filePojo.getKey())) {
                fileTbService.saveFile(fileTb);
            }else {
                fileTbService.UpdateFile(fileTb);
            }
            if (filePojo.getShardIndex() == filePojo.getShardTotal()) {
                //开始合并
                mergeaviation(filePojo,dataIndex);
                return Result.success(FileConstance.ACCESS_PATH + filePojo.getFileName());
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("文件加载错误");
            //如果出错，删除文件夹下与key同名的文件
            fullDir = new File(FileConstance.FILE_PATH_AVIATION);
            if (fullDir != null && fullDir.exists()) {
                File[] files = fullDir.listFiles();
                System.out.println("分文件数量" + files.length);
                System.out.println("分文件名");
                for (File f : files) {
                    if (!f.getName().contains(".")) {
                        continue;
                    }
                    System.out.println(f.getName());
                    String fname = f.getName().substring(0, f.getName().indexOf('.'));
                    String delname = filePojo.getKey();
                    System.out.println("要删除文件名 : " + delname);
                    if (delname != null && delname.equals(fname)) { //删除
                        System.out.println("删除分文件 : " + fname);
                        f.delete();
                    }
                }
            }
            return Result.fail();
        }
        return Result.success();
    }
    public void mergeaviation(FilePojo filePojo,ImportDataIndex dataIndex) throws Exception {
        System.out.println("enter merge");
        Long shardTotal = filePojo.getShardTotal();
        File newFile = new File(FileConstance.FILE_PATH_AVIATION + filePojo.getFileName());
        if (newFile.exists()) {
            newFile.delete();
        }
        FileOutputStream outputStream = new FileOutputStream(newFile, true);//文件追加写入
        FileInputStream fileInputStream = null;//分片文件
        byte[] byt = new byte[10 * 1024 * 1024];
        int len;
        try {
            for (int i = 0; i < shardTotal; i++) {
                // 读取第i个分片
                fileInputStream = new FileInputStream(new File(FileConstance.FILE_PATH_AVIATION + filePojo.getKey() + "." + (i + 1))); //  course\6sfSqfOwzmik4A4icMYuUe.mp4.1
                while ((len = fileInputStream.read(byt)) != -1) {
                    outputStream.write(byt, 0, len);//一直追加到合并的新文件中
                }
            }
        } catch (IOException e) {
            System.out.println("分片合并异常" + e);
            //log.error("分片合并异常", e);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                outputStream.close();
                System.out.println("IO流关闭");
                //log.info("IO流关闭");
                System.gc();
            } catch (Exception e) {
                System.out.println("IO流关闭" + e);
                //log.error("IO流关闭", e);
            }
            //删除所有的临时文件
        }
        System.out.println("合并分片结束");
        //文件合并完成后，进行数据处理并存入数据库中 --- newFile
        //int[] dataIndex = {0, 2, 3, -1, 2, 6, -1};//{0,2,3,-1,1,6,4};//0, 2, 3, -1, 2, 6, -1
        /** dataIndex数组中的含义
         * targetID 目标标识所在的列
         * longitude longitude所在的列
         * latitude latitude所在的列
         * height   height所在的列（民船数据没有这一列应为-1）
         * time_stamp   timestamp所在的列
         * heading  heading所在的列
         * speed    speed所在的列
         * 以上数据如果在文件中不存在，则值为-1
         */
        String filePath = FileConstance.FILE_PATH_AVIATION + filePojo.getFileName();
        System.out.println("filePath : " + filePath);
        System.out.println("newFile length : " + newFile.length());
        System.out.println("newFile name : " + newFile.getName());
        //LoadFileServiceImpl.loadSingleCivilShipFile(newFile,dataIndex);
        try{
            //targetId,longitude,latitude,height,timeStamp,heading,speed,total
            //ImportDataIndex dataIndex = new ImportDataIndex(1,2,3,6,0,5,4,16);
            loadFileService.loadCivilAviationFile(filePath,dataIndex);
            //loadFileService.loadCivilShipFile(filePath,dataIndex);
            System.out.println("数据导入完毕");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("文件导入报错");
        }finally { //最后删除产生的临时文件，合并文件，数据库文件
            //根据文件名 = f_key 清除临时文件   以及   合并后的文件
            //最后清空FileConstance.FILE_PATH这个文件夹下所有 文件 及其 目录
            try{
                File fullDir = new File(FileConstance.FILE_PATH_AVIATION);
                if (fullDir != null && fullDir.exists()) {
                    File[] files = fullDir.listFiles();
                    System.out.println("分文件数量" + files.length);
                    System.out.println("分文件名");
                    for(File f : files){
                        if(!f.getName().contains(".")){
                            continue;
                        }
                        System.out.println(f.getName());
                        String fname = f.getName().substring(0,f.getName().indexOf('.'));
                        String delname = filePojo.getKey();
                        System.out.println("要删除文件名 : " + delname);
                        if(delname != null && delname.equals(fname)){ //删除
                            System.out.println("删除分文件 : " + fname);
                            f.delete();
                        }
                    }
                }
                System.out.println("临时文件清除完毕");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("临时文件删除出错");
            }
            try{
                System.out.println("合并文件名 ： " + newFile.getName());
                newFile.delete();
                System.out.println("合并文件删除完毕");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("合并文件删除报错");
            }
            try{
                //根据f_key清除mysql表
                fileTbService.delByFkey(filePojo.getKey());
                System.out.println("文件数据库临时文件删除成功");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("文件所在临时数据库删除错误");
            }
        }
    }
    //文件上传之前判断是否已经上传过 -1就是没有
    @GetMapping("/checkaviation")
    public Result checkaviation(@RequestParam String key){
        FileTb fileTb = fileTbService.selectLatestIndex(key);
        System.out.println("检查分片" + key);
        //log.info("检查分片：{}");
        return Result.success(fileTb);
    }
}
