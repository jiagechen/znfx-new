package cn.njust.label.main.service;


import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.entity.Track;
import cn.njust.label.main.entity.Track_point_items;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TrackService {
    @Autowired
    @Qualifier("TemplateCentreTra")
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有对象
     *
     * @return
     */
    public List<Track> findAllTrack(){
        List<Track> all = mongoTemplate.findAll(Track.class);
        return all;
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
        List<Track> tracks = mongoTemplate.find(query, Track.class);
        long total = mongoTemplate.count(new Query(),Track.class);//获取总数
        MongoPage<Track> data = new MongoPage<>(currentPage,pageSize,total,tracks);
        return data;
    }

    /**
     * 创建对象
     *
     * @param track
     * @return
     */
    public Track saveTrack(Track track){
        Track save = mongoTemplate.save(track);
        return save;
    }

    /**
     * 更新对象的plane_id
     *
     * @param track
     */
    public void updateTrack(Track track){
        Query query = new Query(Criteria.where("_id").is(track.get_id()));
        Update update = new Update().set("plane_id", track.getPlane_id()).set("track_point_items", track.getTrack_point_items());
        mongoTemplate.updateFirst(query,update,Track.class);
    }

    /**
     * 删除航线
     *
     * @param id
     */
    public void deleteTrack(String id){
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Track.class);
    }

    /**
     * 往航迹中添加航迹点
     *
     * @param id 航线标识
     * @param track_point_item 添加的航迹点
     * @return 0 ：添加失败 1： 添加成功
     */
    public int addTrackPoint(String id, Track_point_items track_point_item){
        try{
            track_point_item.setItemid(1);//
            Query query = Query.query(Criteria.where("_id").is(id));
            Update update = new Update();
            update.addToSet("track_point_items",track_point_item);
            mongoTemplate.upsert(query,update,Track.class);
        }catch (Exception e){
            return 0;//添加失败
        }
        return 1;//添加成功
    }

    /**
     * 更改航线中的某一航迹点
     *
     * @param id 航线标识
     * @param track_point_item 待更改的航迹点
     * @return 0 ：添加失败 1： 添加成功
     */
    public int updateTrackPoint(String id,Track_point_items track_point_item){
        try{
            Query query = Query.query(Criteria.where("_id").is(id)
                    .and("track_point_items.itemid").is(track_point_item.getItemid()));
            System.out.println("test:############################");
            System.out.println(query.getQueryObject());
            Update update = new Update();
            update.set("track_point_items.$.longitude",track_point_item.getLongitude());
            update.set("track_point_items.$.latitude",track_point_item.getLatitude());
            update.set("track_point_items.$.heigt",track_point_item.getHeigt());
            update.set("track_point_items.$.heading",track_point_item.getHeading());
            update.set("track_point_items.$.speed",track_point_item.getSpeed());
            update.set("track_point_items.$.time_stamp",track_point_item.getTime_stamp());
            mongoTemplate.updateFirst(query,update,Track.class);
        }catch (Exception e){
            return 0;//更新失败
        }
        return 1;//更新成功
    }

    /**
     * 删除航迹点
     *
     * @param id 航线标识
     * @param itemid 航迹点标识
     * @return 0 ：添加失败 1： 添加成功
     */
    public int deleteTrackPoint(String id,Double itemid){
        try{
            Update update = new Update();
            Document document = new Document("itemid",itemid);
            update.pull("track_point_items",document);
            Query query = new Query(Criteria.where("_id").is(id));
            mongoTemplate.updateFirst(query,update,Track.class);
        }catch (Exception e){
            return 0;
        }
        /*try{ // 只能将数组中的文档设为null，无法直接删除
            Query query = Query.query(Criteria.where("_id").is(id)
                            .and("track_point_items.itemid").is(itemid));
            Update update = new Update();
            update.unset("track_point_items.$");
            //Document doc = new Document();
            //update.pull("itemid",itemid);
            mongoTemplate.updateFirst(query,update,Track.class);
        }catch (Exception e){
            return 0;//删除失败
        }*/
        return 1;//删除成功
    }
    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    public  XSSFWorkbook exportShiptrack(){
        List<Track> list = mongoTemplate.findAll(Track.class);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"民航航迹存储表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"_id","plane_id","track_point_items"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            Track track = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(track.get_id());
            cell = row.createCell(1);
            cell.setCellValue(track.getPlane_id());
            cell = row.createCell(2);
            List<Track_point_items> track_point_items = track.getTrack_point_items();
            //track_point_items可能为空，不一定有toString()方法
            cell.setCellValue(track_point_items != null ? track_point_items.toString():null);
        }
        return xssfWorkbook;
    }

    /**
     * 预处理数据的函数
     *
     * @param start
     * @param cur
     * @param end
     * @param str
     */
    public void Print_ProgressBar(int start, int cur, int end, String str){
        if(start == cur){
            int progress = (int)((double)(cur-start+1)/(end-start+1)*100);
            System.out.print(str + progress + "%");
        }
        else{
            int progress_cur = (int)((double)(cur-start+1)/(end-start+1)*100);
            int progress_pre = (int)((double)(cur-start)/(end-start+1)*100);
            if(progress_pre >= 10){
                System.out.print("\b\b\b");
                System.out.print(progress_cur + "%");
            }
            else{
                System.out.print("\b\b");
                System.out.print(progress_cur + "%");
            }
        }
        if(end == cur){
            System.out.println();
        }
    }
    /**
     * 对数据进行预处理
     * @param items
     * @param dataType  dataType = 0  ：代表txt文件   dataType = 1  ：代表csv文件
     * @return ：返回值一直为1，作为数据处理完毕的标志
     */
    public int process(List<String> items,int dataType){
        System.out.println("data processing...");
        if(dataType == 0){//txt文件类型

            // 用HashMap暂存各个航班的item，只取需要的字段
            HashMap<String, ArrayList<ArrayList<String>>> flights = new HashMap<String, ArrayList<ArrayList<String>>>();
            for(int r = 0; r < items.size(); ++r){
                String cur_item = items.get(r);
                String[] each_col = cur_item.trim().split("\\s+");
                String cur_ssr_track_num = each_col[1];

                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(each_col[2]);  // longitude
                concise_item.add(each_col[3]);  // latitude
                concise_item.add(each_col[4]);  // height
                concise_item.add(each_col[0]);  // time_stamp
                if(flights.containsKey(cur_ssr_track_num)){
                    flights.get(cur_ssr_track_num).add(concise_item);
                }
                else{
                    ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
                    temp.add(concise_item);
                    flights.put(cur_ssr_track_num, temp);
                }
            }

            // 导入数据库
            int count = 0;
            for(String ssr_track_num: flights.keySet()){
                ArrayList<ArrayList<String>> cur_flight = flights.get(ssr_track_num);
                Track track = new Track();
                ArrayList<Track_point_items> track_point_items = new ArrayList<>();
                track.setPlane_id(ssr_track_num);

                for(int j = 0; j < cur_flight.size(); ++j ){
                    Track_point_items point = new Track_point_items();
                    point.setItemid(j + 1);
                    point.setLongitude(cur_flight.get(j).get(0));
                    point.setLatitude(cur_flight.get(j).get(1));
                    point.setHeigt(cur_flight.get(j).get(2));
                    point.setHeading(null);
                    point.setSpeed(null);
                    point.setTime_stamp(cur_flight.get(j).get(3));
                    track_point_items.add(point);
                }
                track.setTrack_point_items(track_point_items);
                mongoTemplate.save(track);

            }
        }
        // ----------处理csv文件----------
        else if(dataType == 1){

            // 用HashMap暂存各个航班的item，只取需要的字段
            HashMap<String, ArrayList<ArrayList<String>>> flights = new HashMap<String, ArrayList<ArrayList<String>>>();
            for(int r = 0; r < items.size(); ++r){
                String cur_item = items.get(r);
                String[] each_col = cur_item.trim().split(",");
                String cur_ssr_track_num = each_col[2];

                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(each_col[4]);  // longitude
                concise_item.add(each_col[3]);  // latitude
                concise_item.add(each_col[6]);  // height
                concise_item.add(each_col[1]);  // time_stamp
                if(flights.containsKey(cur_ssr_track_num)){
                    flights.get(cur_ssr_track_num).add(concise_item);
                }
                else{
                    ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
                    temp.add(concise_item);
                    flights.put(cur_ssr_track_num, temp);
                }
            }

            // 导入数据库
            int count = 0;
            for(String ssr_track_num: flights.keySet()){
                ArrayList<ArrayList<String>> cur_flight = flights.get(ssr_track_num);
                Track track = new Track();
                ArrayList<Track_point_items> track_point_items = new ArrayList<>();
                track.setPlane_id(ssr_track_num);
                for(int j = 0; j < cur_flight.size(); ++j ){
                    Track_point_items point = new Track_point_items();
                    point.setItemid(j + 1);
                    point.setLongitude(cur_flight.get(j).get(0));
                    point.setLatitude(cur_flight.get(j).get(1));
                    point.setHeigt(cur_flight.get(j).get(2));
                    point.setHeading(null);
                    point.setSpeed(null);
                    point.setTime_stamp(cur_flight.get(j).get(3));
                    track_point_items.add(point);
                }
                track.setTrack_point_items(track_point_items);
                mongoTemplate.save(track);
            }
        }
        // 其他类型文件
        else{
            System.out.println("文件类型不符合要求，请更正！");
        }
        System.out.println("-------------processing ending---------------");
        return 1;
    }

    /**
     * 专门处理船的数据，目前仅支持csv格式数据
     * @param items
     * @return
     */
    public int processShipData(List<String> items){
        // 用HashMap暂存各个航班的item，只取需要的字段
        HashMap<String, ArrayList<ArrayList<String>>> ships = new HashMap<String, ArrayList<ArrayList<String>>>();
        for(int r = 1; r < items.size(); ++r){
            String cur_item = items.get(r);
            String[] each_col = cur_item.trim().split(",");
            String cur_MMSI = each_col[0];
            ArrayList<String> concise_item = new ArrayList<>();
            concise_item.add(each_col[3]);  // longitude
            concise_item.add(each_col[2]);  // latitude
            concise_item.add(each_col[6]);  // heading
            concise_item.add(each_col[4]);  // speed
            concise_item.add(each_col[1]);  // time_stamp
            if(ships.containsKey(cur_MMSI)){
                ships.get(cur_MMSI).add(concise_item);
            }
            else{
                ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
                temp.add(concise_item);
                ships.put(cur_MMSI, temp);
            }
        }
        // 导入数据库
        for(String mmsi: ships.keySet()){
            ArrayList<ArrayList<String>> cur_ship = ships.get(mmsi);
            Track track = new Track();
            track.setPlane_id(mmsi);
            List<Track_point_items> track_point_items = new ArrayList<>();
            for(int j = 0; j < cur_ship.size(); ++j ){
                Track_point_items point = new Track_point_items();
                point.setItemid(j + 1);
                point.setLongitude(cur_ship.get(j).get(0));
                point.setLatitude(cur_ship.get(j).get(1));
                point.setHeading(cur_ship.get(j).get(2));
                point.setSpeed(cur_ship.get(j).get(3));
                point.setTime_stamp(cur_ship.get(j).get(4));
                /*
                 Document track_point_item = new Document();
                 track_point_item.append("itemid", j+1);
                 track_point_item.append("longitude", cur_ship.get(j).get(0));
                 track_point_item.append("latitude", cur_ship.get(j).get(1));
                 track_point_item.append("heading", cur_ship.get(j).get(2));
                 track_point_item.append("speed", cur_ship.get(j).get(3));
                 track_point_item.append("time_stamp", cur_ship.get(j).get(4));
                */
                track_point_items.add(point);
            }
            track.setTrack_point_items(track_point_items);
            mongoTemplate.save(track);
        }
        return 1;
    }
}
