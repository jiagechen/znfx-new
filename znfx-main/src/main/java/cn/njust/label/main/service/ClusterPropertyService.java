package cn.njust.label.main.service;


import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.entity.ClusterProperty;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClusterPropertyService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ClusterProperty> getAll(){
        return mongoTemplate.findAll(ClusterProperty.class);
    }

    /**
     * 分页查询
     *
     * @param currentPage 第几页
     * @param pageSize 每页多少数据
     * @return
     */
    public MongoPage findPart(int currentPage, int pageSize){
        Query query = new Query();
        //限制从哪条数据开始
        query.skip((currentPage - 1)*pageSize);
        //限制取出多少条数据
        query.limit(pageSize);
        List<ClusterProperty> tracks = mongoTemplate.find(query, ClusterProperty.class);
        long total = mongoTemplate.count(new Query(),ClusterProperty.class);//获取总数
        MongoPage<ClusterProperty> data = new MongoPage<>(currentPage,pageSize,total,tracks);
        return data;
    }

    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    public XSSFWorkbook exportShiptrack(){
        List<ClusterProperty> list = mongoTemplate.findAll(ClusterProperty.class);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"航线次数统计表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"_id","avgHeight","avgSpeed","targetTimes"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            ClusterProperty track = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(track.get_id());
            cell = row.createCell(1);
            cell.setCellValue(track.getAvgHeight());
            cell = row.createCell(2);
            cell.setCellValue(track.getAvgSpeed());
            cell = row.createCell(3);
            HashMap<String, String> targetTimes = track.getTargetTimes();
            String res = "{";
            for(Map.Entry<String,String> entry : targetTimes.entrySet()){
                res += "\"" + entry.getKey() + "\":"  + entry.getValue() + ",";
            }
            res = res.substring(0,res.length() - 1);
            res += "}";
            //List<Track_point_items> track_point_items = track.getTrack_point_items();
            //track_point_items可能为空，不一定有toString()方法
            cell.setCellValue(targetTimes != null ? res : null);
        }
        return xssfWorkbook;
    }
}
