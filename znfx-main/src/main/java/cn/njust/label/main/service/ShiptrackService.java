package cn.njust.label.main.service;


import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.entity.Shiptrack;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiptrackService {
    //Mysl 改为 Mongodb
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 分页查询
     *
     * @param currentPage 第几页
     * @param pageSize 每页多少数据
     * @return
     */
    public MongoPage selectPage(int currentPage, int pageSize, String search){
        Query query = new Query();
        //限制从哪条数据开始
        query.skip((currentPage - 1)*pageSize);
        //限制取出多少条数据
        query.limit(pageSize);
        List<Shiptrack> tracks = mongoTemplate.find(query,Shiptrack.class);
        long total = mongoTemplate.count(new Query(),Shiptrack.class);//获取总数
        MongoPage<Shiptrack> data = new MongoPage<>(currentPage,pageSize,total,tracks);
        return data;
    }

    public Shiptrack insert(Shiptrack shiptrack){
        return mongoTemplate.save(shiptrack);
    }

    public void update(Shiptrack shiptrack){
        Query query = new Query(Criteria.where("_id").is(shiptrack.get_id()));
        Update update = new Update().set("fragmentId", shiptrack.getFragmentId()).set("shipId", shiptrack.getShipId()).set("fragmentNum",shiptrack.getFragmentNum()).set("nowFragment",shiptrack.getNowFragment()).set("fmId",shiptrack.getFmId()).set("dataPacket",shiptrack.getDataPacket()).set("checkData",shiptrack.getCheckData());
        mongoTemplate.updateFirst(query,update,Shiptrack.class);
    }
    public void deleteById(String id){
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Shiptrack.class);
    }

    public List<Shiptrack> findAll(){
        return mongoTemplate.findAll(Shiptrack.class);
    }

    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    public XSSFWorkbook exportShiptrack(){
        List<Shiptrack> list = mongoTemplate.findAll(Shiptrack.class);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"民船航迹目标存储表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"_id","Fragment_ID","Ship_ID","Fragment_num","Now_fragment","FM_ID","Data_packet","checkData"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            Shiptrack track = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(track.get_id());
            cell = row.createCell(1);
            cell.setCellValue(track.getFmId());
            cell = row.createCell(2);
            cell.setCellValue(track.getShipId());
            cell = row.createCell(3);
            cell.setCellValue(track.getFragmentNum());
            cell = row.createCell(4);
            cell.setCellValue(track.getNowFragment());
            cell = row.createCell(5);
            cell.setCellValue(track.getFmId());
            cell = row.createCell(6);
            cell.setCellValue(track.getDataPacket());
            cell = row.createCell(7);
            cell.setCellValue(track.getCheckData());
        }
        return xssfWorkbook;
    }

    /*@Autowired
    private ShiptrackMapper shiptrackMapper;*/

    //根据数据包标志进行模糊查询
    /*public Page<Shiptrack> selectPage(Integer pageNum, Integer pageSize, String search) {
        return shiptrackMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Shiptrack>lambdaQuery().like(Shiptrack::getShipId,search));
    }*/


    /*public int insert(Shiptrack shiptrack){
        return shiptrackMapper.insert(shiptrack);
    }

    public int update(Shiptrack shiptrack) {
        return shiptrackMapper.updateById(shiptrack);
    }

    public int deleteById(Long id) {
        int res = shiptrackMapper.deleteById(id);
        return res;
    }*/

    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    /*public  XSSFWorkbook exportShiptrack(){
        List<Shiptrack> list = shiptrackMapper.selectList(null);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"民船航迹存储表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"Fragment_ID","Ship_ID","Fragment_num","Now_fragment","FM_ID","Data_packet","checkData"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            Shiptrack shiptrack = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(shiptrack.getFragmentId());
            cell = row.createCell(1);
            cell.setCellValue(shiptrack.getShipId());
            cell = row.createCell(2);
            cell.setCellValue(shiptrack.getFragmentNum());
            cell = row.createCell(3);
            cell.setCellValue(shiptrack.getNowFragment());
            cell = row.createCell(4);
            cell.setCellValue(shiptrack.getFmId());
            cell = row.createCell(5);
            cell.setCellValue(shiptrack.getDataPacket());
            cell = row.createCell(6);
            cell.setCellValue(shiptrack.getCheckData());
        }
        return xssfWorkbook;
    }
    public List<Shiptrack> findAll(){ //查询所有
        return shiptrackMapper.selectList(null);
    }*/
}
