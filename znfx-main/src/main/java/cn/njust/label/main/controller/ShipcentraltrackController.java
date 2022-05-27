package cn.njust.label.main.controller;

import cn.njust.label.main.common.CsvExportUtil;
import cn.njust.label.main.common.Result;
import cn.njust.label.main.entity.Shipcentraltrack;
import cn.njust.label.main.service.ShipcentraltrackService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
@RequestMapping("/shipcentraltrack")
public class ShipcentraltrackController {
    @Autowired
    private ShipcentraltrackService shipcentraltrackService;
    //添加
    @PostMapping
    public Result<?> insert(@RequestBody Shipcentraltrack shipcentraltrack){
        //注意，属性fmID是float（3，5），数据格式一定要保证，否则插入出错
        //需要注意前端要控制传入后台的数据格式问题，需要在前端控制好
//        System.out.println(shiptrack);
//        System.out.println("success");
        shipcentraltrackService.insert(shipcentraltrack);
        return Result.success();
    }
    //删除
    @DeleteMapping("/{id}") //占位符
    public Result<?> delete(@PathVariable Integer id){
        shipcentraltrackService.deleteById(id);
        return Result.success();
    }
    //更新
    @PutMapping
    public Result<?> update(@RequestBody Shipcentraltrack shipcentraltrack){
        shipcentraltrackService.update(shipcentraltrack);
        return Result.success();
    }
    //查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        //根据数据包标志进行模糊查询
        Page<Shipcentraltrack> shipcentraltrackPage = shipcentraltrackService.selectPage(pageNum, pageSize, search);
//        System.out.println("success");
//        System.out.println("pageNum:"+pageNum+"   pageSize:"+pageSize+"   search:"+search);
//        System.out.println("size:"+shiptrackPage.getSize());
        return Result.success(shipcentraltrackPage);
    }
    //导出数据为xlsx格式
    @PostMapping("/export")
    public void exportExcel(HttpServletResponse response) {
        System.out.println("export data");
        //设置默认的下载文件名
        String name = "民船中心航线表.xlsx";
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
        XSSFWorkbook xssfWorkbook = shipcentraltrackService.exportShiptrack();
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
            String titles = "\"route_id\",\"initia_latitude\",\"initia_longitude\",\"target_latitude\",\"target_longitude\",\"width\",\"mongo_id\"";
            //设置每列字段
            String keys = "route_id,initia_latitude,initia_longitude,target_latitude,target_longitude,width,mongo_id";
            //构造导出数据
            List<Map<String,Object>> datas = new ArrayList<>();
            Map<String,Object> mapInfo;
            //查询需要导出的数据
            List<Shipcentraltrack> allTrack = shipcentraltrackService.findAll();
            for(Shipcentraltrack data : allTrack){
                mapInfo = new HashMap<>();
                mapInfo.put("route_id","\"" +data.getRouteID() + "\"");
                mapInfo.put("initia_latitude","\"" + data.getInitiaLatitude() + "\"" );
                mapInfo.put("initia_longitude","\"" + data.getInitiaLongitude() + "\"");
                mapInfo.put("target_latitude","\"" + data.getTargetLatitude() + "\"" );
                mapInfo.put("target_longitude","\"" + data.getTargetLongitude() + "\"");
                mapInfo.put("width","\"" + data.getWidth() + "\"" );
                mapInfo.put("mongo_id","\"" + data.getMongoid() + "\"" );
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
