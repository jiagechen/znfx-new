package cn.njust.label.main.utils;

import cn.njust.label.main.dto.TrackTarget;
import cn.njust.label.main.entity.ImportDataIndex;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ReadFileUtil {

    /**
    *   读取文件
     * @param file :文件路径
    * */
    public static ArrayList<String> readFile(File file){
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> items = new ArrayList<>();
            // 按行读取字符串
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                items.add(str);
            }
            bufferedReader.close();
            fileReader.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * 解析ssr数据txt文件读取的信息
    * */
    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilAviationTxt(ArrayList<String> items, int[] dataIndex){
        // 用HashMap暂存各个航班的item，只取需要的字段
        HashMap<String, ArrayList<ArrayList<String>>> flights = new HashMap<>();
        if(items != null){
            for(int r = 1; r < items.size(); ++r){
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
            return flights;
        }else return null;
    }

    /**
    * @description: 用于判断是否存在该属性
    * @param index:参数位置
    * @return :是否存在
    * @date: 2022/5/3
    */
    public static boolean isExist(Integer index){
        return index != null && index >= 0;
    }
    /*
    * 解析ssr数据，从csv文件中读取的数据
    * */

    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilAviationFile(ArrayList<String> items, String fileName, ImportDataIndex dataIndex){
        if(items != null) {
            // 用HashMap暂存各个航班的item，只取需要的字段
            HashMap<String, ArrayList<ArrayList<String>>> flights = new HashMap<>();
            for (int r = 1; r < items.size(); ++r) {
                String cur_item = items.get(r);
                String regex = "";
                if (fileName.endsWith(".txt"))regex = "\\s+";
                else if(fileName.endsWith(".csv")) regex = ",";
                else return null;
                String[] each_col = cur_item.trim().split(regex);
                if(dataIndex.getTotal() != null && dataIndex.getTotal() != each_col.length) continue;
//                log.info(Arrays.toString(each_col));
                if(each_col[dataIndex.getLatitude()].equals("")||each_col[dataIndex.getLongitude()].equals("")||each_col[dataIndex.getTimeStamp()].equals(""))continue;
                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(isExist(dataIndex.getLongitude()) ? each_col[dataIndex.getLongitude()]: null);  // longitude
                concise_item.add(isExist(dataIndex.getLatitude()) ? each_col[dataIndex.getLatitude()]: null);  // latitude
                concise_item.add(isExist(dataIndex.getHeight()) ? each_col[dataIndex.getHeight()] : null); // height
                concise_item.add(isExist(dataIndex.getTimeStamp()) ? each_col[dataIndex.getTimeStamp()]: null);  // time_stamp
                concise_item.add(isExist(dataIndex.getHeading()) ? each_col[dataIndex.getHeading()]: null);  // heading
                concise_item.add(isExist(dataIndex.getSpeed()) ? each_col[dataIndex.getSpeed()]: null);  // speed
                String cur_ssr_track_num = each_col[dataIndex.getTargetId()];
                if (flights.containsKey(cur_ssr_track_num)) {
                    flights.get(cur_ssr_track_num).add(concise_item);
                } else {
                    ArrayList<ArrayList<String>> temp = new ArrayList<>();
                    temp.add(concise_item);
                    flights.put(cur_ssr_track_num, temp);
                }
            }
            return flights;
        }else return null;
    }

    public static HashMap<String, ArrayList<ArrayList<String>>> parseCivilShipFile(ArrayList<String> items, String fileName, ImportDataIndex dataIndex) {
        if(items != null) {
            // 用HashMap暂存各个航班的item，只取需要的字段
            HashMap<String, ArrayList<ArrayList<String>>> ships = new HashMap<>();
            for (int r = 1; r < items.size(); ++r) {
                String cur_item = items.get(r);
                String regex = "";
                if (fileName.endsWith(".txt"))regex = "\\s+";
                else if(fileName.endsWith(".csv")) regex = ",";
                else return null;
                String[] each_col = cur_item.trim().split(regex);
                String cur_ssr_track_num = each_col[dataIndex.getTargetId()];
                if(dataIndex.getTotal() != null && dataIndex.getTotal() != each_col.length) continue;
//                log.info(Arrays.toString(each_col));
                if(each_col[dataIndex.getLatitude()].equals("")||each_col[dataIndex.getLongitude()].equals("")||each_col[dataIndex.getTimeStamp()].equals(""))continue;
                ArrayList<String> concise_item = new ArrayList<>();
                concise_item.add(isExist(dataIndex.getLongitude()) ? each_col[dataIndex.getLongitude()]: null);  // longitude
                concise_item.add(isExist(dataIndex.getLatitude()) ? each_col[dataIndex.getLatitude()]: null);  // latitude
                concise_item.add(isExist(dataIndex.getTimeStamp()) ? each_col[dataIndex.getTimeStamp()]: null);  // time_stamp
                concise_item.add(isExist(dataIndex.getHeading()) ? each_col[dataIndex.getHeading()]: null);  // heading
                concise_item.add(isExist(dataIndex.getSpeed()) ? each_col[dataIndex.getSpeed()]: null);  // speed
                if (ships.containsKey(cur_ssr_track_num)) {
                    ships.get(cur_ssr_track_num).add(concise_item);
                } else {
                    ArrayList<ArrayList<String>> temp = new ArrayList<>();
                    temp.add(concise_item);
                    ships.put(cur_ssr_track_num, temp);
                }
            }
            return ships;
        }else return null;
    }
    /**
    * @Description: 输入导入到MongoDB(轨迹库),民航
    * @Param: [items, collectName]: [待导入项, 集合名]
    * @return: java.lang.String
    * @Date:
    */
    public static String importCivilAviationToDB(HashMap<String, ArrayList<ArrayList<String>>> items, String collectName, MongoTemplate mongoTemplate){
        try {
            for(String trackNum: items.keySet()){
                ArrayList<ArrayList<String>> curItem = items.get(trackNum);

//                Document trackStorageTable = new Document();
                ArrayList<Document> trackPointItems = new ArrayList<>();

//                trackStorageTable.append("plane_id", trackNum);
                for(int j = 0; j < curItem.size(); ++j ){
                    Document trackPointItem = new Document();
                    trackPointItem.append("itemid", j+1);
                    trackPointItem.append("latitude", curItem.get(j).get(0));
                    trackPointItem.append("longitude", curItem.get(j).get(1));
                    trackPointItem.append("height", curItem.get(j).get(2));
                    trackPointItem.append("heading", curItem.get(j).get(4));
                    trackPointItem.append("speed", curItem.get(j).get(5));
                    trackPointItem.append("time_stamp", curItem.get(j).get(3));

                    trackPointItems.add(trackPointItem);
                }
//                trackStorageTable.append("track_point_items", trackPointItems);
                if(trackPointItems.size() <= 50) continue;
                TrackTarget trackTarget = new TrackTarget(null, trackNum, trackPointItems, 0);
                mongoTemplate.insert(trackTarget, collectName);
            }
            return "import success";
        }catch (Exception e){
            e.printStackTrace();
            return "import failure";
        }
    }

    /**
     * @Description: 输入导入到MongoDB(轨迹库),民船
     * @Param: [items, collectName]: [待导入项, 集合名]
     * @return: java.lang.String
     * @Date:
     */
    public static String importCivilShipToDB(HashMap<String, ArrayList<ArrayList<String>>> items, String collectName, MongoTemplate mongoTemplate){
        try {
            for(String trackNum: items.keySet()){
                ArrayList<ArrayList<String>> curItem = items.get(trackNum);
//                Document trackStorageTable = new Document();
                ArrayList<Document> trackPointItems = new ArrayList<>();

//                trackStorageTable.append("ship_id", trackNum);
                for(int j = 0; j < curItem.size(); ++j ){
                    Document track_point_item = new Document();
                    track_point_item.append("itemid", j+1);
                    track_point_item.append("longitude", curItem.get(j).get(0));
                    track_point_item.append("latitude", curItem.get(j).get(1));
                    track_point_item.append("heading", curItem.get(j).get(3));
                    track_point_item.append("speed", curItem.get(j).get(4));
                    track_point_item.append("time_stamp", curItem.get(j).get(2));
                    trackPointItems.add(track_point_item);
                }
//                if(trackPointItems.size() <= 50) continue;
                MongoCollection<Document> collection = mongoTemplate.getCollection(collectName);
                BasicDBObject object  = new BasicDBObject("targetId", trackNum);
                Document doc = collection.find(object).first();
                if(doc != null){
                    List<Document> oldTrackPointItems = (List<Document>) doc.get("trackPointItems");
                    oldTrackPointItems.addAll(trackPointItems);
                    Query query = new Query();
                    query.addCriteria(Criteria.where("_id").is(doc.get("_id")));
                    Update update = new Update();
                    update.set("trackPointItems", oldTrackPointItems);
                    mongoTemplate.updateMulti(query, update, collectName);
                }else {
                    TrackTarget trackTarget = new TrackTarget(null, trackNum, trackPointItems, 0);
                    mongoTemplate.insert(trackTarget, collectName);
                }
            }
            return "import success";
        }catch (Exception e){
            e.printStackTrace();
            return "import failure";
        }
    }

    public static void loadCivilShipFileByNio(File file, String collectName, MongoTemplate mongoTemplate, ImportDataIndex dataIndex) throws Exception {
        int bufSize = 50000;
        FileInputStream fis = new FileInputStream(file);
        FileChannel fcin = fis.getChannel();
        ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);
        String enterStr = "\n";
        try {
            // 如果出现了换行符，将temp中的内容与换行符之前的内容拼接
            StringBuilder strBuf = new StringBuilder("");
            int lineNum = 0;
            ArrayList<String>  list; // = new ArrayList<>();
            while (fcin.read(rBuffer) != -1) {
                list = new ArrayList<>();
                int rSize = rBuffer.position();
                rBuffer.clear();
                String tempString = new String(rBuffer.array(), 0, rSize);
                if(fis.available() ==0){//最后一行，加入"\n分割符"
                    tempString+="\n";
                }
                int fromIndex = 0;
                int endIndex = 0;
                while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
                    String line = tempString.substring(fromIndex, endIndex);
                    line = strBuf + line;
                    list.add(line);
//                    System.out.println(line);
                    strBuf.delete(0, strBuf.length());
                    fromIndex = endIndex + 1;
                }
                /**
                 * 当前段数据处理，入库
                 */
                HashMap<String, ArrayList<ArrayList<String>>> ships = parseCivilShipFile(list, file.getName(), dataIndex);
                importCivilShipToDB(ships, collectName, mongoTemplate);
                if (rSize > tempString.length()) {
                    strBuf.append(tempString.substring(fromIndex));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally { //关闭文件io流
            if(fcin != null){
                fcin.close();
            }
            if(fis != null){
                fis.close();
            }
        }
    }
    public static void loadCivilAviationFileByNio(File file, String collectName, MongoTemplate mongoTemplate, ImportDataIndex dataIndex) throws Exception {
        int bufSize = 50000;
        FileInputStream fis = new FileInputStream(file);
        FileChannel fcin = fis.getChannel();
        ByteBuffer rBuffer = ByteBuffer.allocate(bufSize);
        String enterStr = "\n";
        try {
            // 如果出现了换行符，将temp中的内容与换行符之前的内容拼接
            StringBuilder strBuf = new StringBuilder("");
            int lineNum = 0;
            ArrayList<String>  list; // = new ArrayList<>();
            while (fcin.read(rBuffer) != -1) {
                list = new ArrayList<>();
                int rSize = rBuffer.position();
                rBuffer.clear();
                String tempString = new String(rBuffer.array(), 0, rSize);
                if(fis.available() ==0){//最后一行，加入"\n分割符"
                    tempString+="\n";
                }
                int fromIndex = 0;
                int endIndex = 0;
                while ((endIndex = tempString.indexOf(enterStr, fromIndex)) != -1) {
                    String line = tempString.substring(fromIndex, endIndex);
                    line = strBuf + line;
                    list.add(line);
                    strBuf.delete(0, strBuf.length());
                    fromIndex = endIndex + 1;
                }
                HashMap<String, ArrayList<ArrayList<String>>> flights = parseCivilAviationFile(list, file.getName(), dataIndex);
                importCivilShipToDB(flights, collectName, mongoTemplate);
                if (rSize > tempString.length()) {
                    strBuf.append(tempString.substring(fromIndex));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fcin != null){
                fcin.close();
            }
            if(fis != null){
                fis.close();
            }
        }
    }
}
