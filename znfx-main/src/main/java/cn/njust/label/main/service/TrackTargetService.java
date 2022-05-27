package cn.njust.label.main.service;


import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.dto.TrackTarget;
import cn.njust.label.main.dto.TrackTargetShip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackTargetService {
    @Autowired
    @Qualifier("TemplateTrajectory")
    private MongoTemplate mongoTemplate;
    public List<TrackTarget> findAll(){
        return mongoTemplate.findAll(TrackTarget.class);
    }

    /**
     * 获取民航轨迹数据存储表（保存于数据库znfx_trajectory中集合civil_aviation_trajectory中）
     * @param currentPage
     * @param pageSize
     * @return
     */
    public MongoPage findPart(int currentPage, int pageSize){
        Query query = new Query();
        //限制从哪条数据开始
        query.skip((currentPage - 1)*pageSize);
        //限制取出多少条数据
        query.limit(pageSize);
        List<TrackTarget> tracks = mongoTemplate.find(query, TrackTarget.class);
        long total = mongoTemplate.count(new Query(),TrackTarget.class);//获取总数
        MongoPage<TrackTarget> data = new MongoPage<>(currentPage,pageSize,total,tracks);
        return data;
    }

    /**
     *  获取页面民船轨迹目标数据（保存于数据库znfx_trajectory中集合civil_ship_trajectory中）
     * @param currentPage
     * @param pageSize
     * @return
     */
    public MongoPage findPartShip(int currentPage, int pageSize){
        Query query = new Query();
        //限制从哪条数据开始
        query.skip((currentPage - 1)*pageSize);
        //限制取出多少条数据
        query.limit(pageSize);
        List<TrackTargetShip> tracks = mongoTemplate.find(query, TrackTargetShip.class);
        long total = mongoTemplate.count(new Query(),TrackTargetShip.class);//获取总数
        MongoPage<TrackTargetShip> data = new MongoPage<>(currentPage,pageSize,total,tracks);
        return data;
    }



    /**
     * 导出文件，格式为xlsx   --- 未验证
     *
     * @return 文件的字节流
     */
    /*public XSSFWorkbook exportShiptrack(){
        List<TrackTarget> list = mongoTemplate.findAll(TrackTarget.class);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"民航轨迹数据存储表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"_id","targetId","handle","trackPointItems"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            TrackTarget track = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(track.getId());
            cell = row.createCell(1);
            cell.setCellValue(track.getTargetId());
            cell = row.createCell(2);
            cell.setCellValue(track.getHandle());
            cell = row.createCell(3);
            List<Document> trackPointItems = track.getTrackPointItems();
            //track_point_items可能为空，不一定有toString()方法
            cell.setCellValue(trackPointItems != null ? trackPointItems.toString():null);
        }
        return xssfWorkbook;
    }*/
}
