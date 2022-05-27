package cn.njust.label.main.controller;


import cn.njust.label.main.common.CsvExportUtil;
import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.common.Result;
import cn.njust.label.main.entity.ClusterProperty;
import cn.njust.label.main.service.ClusterPropertyService;
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
@RequestMapping("/clusterproperty")
public class ClusterPropertyController {

    @Autowired
    private ClusterPropertyService clusterPropertyService;

    //查询所有数据
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize){
        MongoPage<ClusterProperty> data = clusterPropertyService.findPart(pageNum, pageSize);//clusterPropertyService.getAll();
        return Result.success(data);
    }

    //导出数据为xlsx格式
    @PostMapping("/export")
    public void exportExcel(HttpServletResponse response) {
        System.out.println("export cluster data");
        //设置默认的下载文件名
        String name = "航线次数统计表.xlsx";
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
        XSSFWorkbook xssfWorkbook = clusterPropertyService.exportShiptrack();
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
    //导出数据未csv格式
    @PostMapping("/exportCsv")
    public void exportCsv(HttpServletResponse response){
        OutputStream os = null;
        try {
            //查询需要导出的数据
            //构造导出数据结构
            //设置表头
            String titles = "\"_id\",\"avgHeight\",\"avgSpeed\",\"targetTimes\"";
            //设置每列字段
            String keys = "_id,avgHeight,avgSpeed,targetTimes";
            //构造导出数据
            List<Map<String,Object>> datas = new ArrayList<>();
            Map<String,Object> mapInfo;
            //查询需要导出的数据
            List<ClusterProperty> allTrack = clusterPropertyService.getAll();
            for(ClusterProperty data : allTrack){
                mapInfo = new HashMap<>();
                mapInfo.put("_id","\"" + data.get_id() + "\"");
                mapInfo.put("avgHeight","\"" + data.getAvgHeight() + "\"" );
                mapInfo.put("avgSpeed","\"" + data.getAvgSpeed() + "\"");
                //mapInfo.put("track_point_items","\""  + data.getTrack_point_items() + "\"" );
                //文档数组的拼接
                String res = "\"{";
                HashMap<String, String> items = data.getTargetTimes();
                if(items == null){
                    mapInfo.put("targetTimes","");
                    datas.add(mapInfo);
                    continue;
                }
                for(Map.Entry<String,String> entry : items.entrySet()){
                    res += "\"\"" + entry.getKey() + "\"\":" + entry.getValue() + ",";
                }
                /*for(int i = 0 ; i < items.size() ; i++){
                    res += items.get(i).csvStyle();
                    if(i != items.size() - 1) res += ",";
                }*/
                res = res.substring(0,res.length() - 1);
                res += "}\"";
                mapInfo.put("targetTimes",res);
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
