package cn.njust.label.main.controller;


import cn.njust.label.main.common.CsvExportUtil;
import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.common.Result;
import cn.njust.label.main.entity.Track;
import cn.njust.label.main.entity.Track_point_items;
import cn.njust.label.main.service.TrackService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/track")
public class TrackController {
    @Autowired
    private TrackService trackService;
    //查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "5") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        //List<Track> list = trackService.findAllTrack();
        //List<Track> list = trackService.findPart(pageNum, pageSize);
        System.out.println("enter track");
        MongoPage<Track> data = trackService.findPart(pageNum,pageSize);
        return Result.success(data);
    }
    //删除
    @DeleteMapping("/{id}") //占位符
    public Result<?> delete(@PathVariable String id){
        trackService.deleteTrack(id);
        return Result.success();
    }
    //添加
    @PostMapping
    public Result<?> insert(@RequestBody Track track){
        track.set_id(null);//由系统自动设定id值
        track.setTrack_point_items(null);
        trackService.saveTrack(track);
        return Result.success();
    }
    //给航迹添加航迹点
    @PostMapping("/trackPoint/{trackid}")
    public Result<?> addPoint(@PathVariable String trackid, @RequestBody Track_point_items track_point_items){
//        System.out.println(trackid);
//        System.out.println(track_point_items);
        trackService.addTrackPoint(trackid,track_point_items);
        return Result.success();
    }
    //删除某条航迹的航迹点
    @DeleteMapping("/deletePoint/{trackid}/{itemid}") //占位符
    public Result<?> deletePoint(@PathVariable String trackid, @PathVariable Double itemid){
        //trackService.deleteTrackPoint(trackid,itemid);//待修改
        System.out.println("trackid:" + trackid + "   itemid:" + itemid);
        return Result.success();
    }
    //更新某条航迹的航迹点
    //添加
    @PostMapping("/updatePoint/{trackid}")
    public Result<?> updatePoint(@PathVariable String trackid, @RequestBody Track_point_items track_point_items){

        trackService.updateTrackPoint(trackid,track_point_items);
        return Result.success();
    }
    //导出数据为xlsx格式
    @PostMapping("/export")
    public void exportExcel(HttpServletResponse response) {
        System.out.println("export data");
        //设置默认的下载文件名
        String name = "民船航迹存储表.xlsx";
        try {
            //避免文件名中文乱码，将UTF8打散重组成ISO-8859-1编码方式
            name = new String (name.getBytes("UTF8"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //设置响应头的类型
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //让浏览器下载文件,name是上述默认文件下载名
        response.addHeader("Content-Disposition","attachment;filename=\"" + name + "\"");
        OutputStream outputStream=null;
        XSSFWorkbook xssfWorkbook = trackService.exportShiptrack();
        try {
            outputStream = response.getOutputStream();
            xssfWorkbook.write(outputStream);
            outputStream.flush();
            //修正 Excel在“xxx.xlsx”中发现不可读取的内容。是否恢复此工作薄的内容？如果信任此工作簿的来源，请点击"是"
            //response.setHeader("Content-Length", String.valueOf());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @PostMapping("/exportCsv")
    public void exportCsv(HttpServletResponse response){
        OutputStream os = null;
        try {
            //查询需要导出的数据
            //构造导出数据结构
            //设置表头
            String titles = "\"_id\",\"plane_id\",\"track_point_items\"";
            //设置每列字段
            String keys = "_id,plane_id,track_point_items";
            //构造导出数据
            List<Map<String,Object>> datas = new ArrayList<>();
            Map<String,Object> mapInfo;
            //查询需要导出的数据
            List<Track> allTrack = trackService.findAllTrack();
            for(Track data : allTrack){
                mapInfo = new HashMap<>();
                mapInfo.put("_id","\"" + data.get_id() + "\"");
                mapInfo.put("plane_id","\"" + data.getPlane_id() + "\"" );
                //mapInfo.put("track_point_items","\""  + data.getTrack_point_items() + "\"" );
                //文档数组的拼接
                String res = "\"[";
                //List<Track_point_items> items = data.getTrack_point_items();
                List<Track_point_items> items = data.getTrack_point_items();
                if(items == null){
                    mapInfo.put("track_point_items","");
                    datas.add(mapInfo);
                    continue;
                }
                for(int i = 0 ; i < items.size() ; i++){
                    res += items.get(i).csvStyle();
                    if(i != items.size() - 1) res += ",";
                }
                res += "]\"";
                mapInfo.put("track_point_items",res);
                datas.add(mapInfo);
            }

            //设置导出文件前缀
            String fName = "统计_";
            //文件导出
            os = response.getOutputStream();
            CsvExportUtil.responseSetProperties(fName,response);
            CsvExportUtil.doExport(datas,titles,keys,os);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
