package cn.njust.label.main.controller;

//上传文件

import cn.njust.label.main.common.Result;
import cn.njust.label.main.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/import")
public class importFileController {

    @Autowired
    private TrackService trackService;

    //定义日期格式
    SimpleDateFormat sdf=new SimpleDateFormat("/YYYY/MM/DD/");

    /**
     * 处理txt格式数据（飞机数据集）
     * @param chunk
     * @param request
     * @return
     */
    @PostMapping("/txt")
    public Result<?> getTxtData(MultipartFile chunk, HttpServletRequest request){
        System.out.println("导入飞机数据集 txt");

        HttpSession session = request.getSession();
        String partMess = (String)session.getAttribute("partMess");
        if(partMess == null || partMess == ""){
            partMess = "";
        }
        try {
            InputStream inputStream = chunk.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> items = new ArrayList<>();
            String lineTxt;
            int cnt = 0;
            System.out.println("----------------------------------");
            String tmp = "";
            while((lineTxt=bufferedReader.readLine())!=null){
                if(cnt == 0){
                    if(lineTxt.contains("ssr_time_detection")) continue;//第一次访问，是标题，掠过
                    //System.out.println(lineTxt);
                    int len = lineTxt.trim().split("\\s+").length;
                    if(len >= 20 & partMess == ""){
                        //System.out.println(lineTxt);
                        //处理一些操作
                        //System.out.println("index 0 : " + len);
                        items.add(lineTxt);
                    }else{
                        tmp = lineTxt;
                    }
                }else{
                    //System.out.println(lineTxt);
                    //处理一些操作
                    items.add(lineTxt);
                }
                cnt++;
            }
            //查看本次最后一行数据是否缺失
            if(items.get(items.size() - 1).trim().split("\\s+").length >= 20){ //没有缺失
                session.setAttribute("partMess","");
            }else{ //缺失了
                String cur = items.get(items.size() - 1);
                items.remove(items.size() - 1);//去除缺失数据
                session.setAttribute("partMess",cur);//加入session，供下次获取缺失的数据
            }
            if(tmp == ""){//本次开头没有缺失，即次数据没有缺失，不用拼接

            }else{//本次开头数据缺失了，即上次数据缺失了 --- 拼接
                partMess = partMess + tmp;
                items.add(partMess);//加入items
            }
            System.out.println(items.get(0));
            System.out.println(items.get(items.size() - 1));
            trackService.process(items,0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> map =  new HashMap<>();
        map.put("code",0);
        return Result.success(map);
    }

    /**
     * 处理csv格式数据(飞机数据集)
     * @param chunk
     * @param request
     * @return
     */
    @PostMapping("/csv")
    public Result<?> getCsvData(MultipartFile chunk, HttpServletRequest request){

        System.out.println("导入飞机数据集 csv");

        HttpSession session = request.getSession();
        String partMess = (String)session.getAttribute("partMess");
        if(partMess == null || partMess == ""){
            partMess = "";
        }
        try {
            InputStream inputStream = chunk.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> items = new ArrayList<>();
            String lineTxt;
            int cnt = 0;
            System.out.println("----------------------------------");
            String tmp = "";

            while((lineTxt=bufferedReader.readLine())!=null){
                if(cnt == 0){
                    if(lineTxt.contains("id")) continue;//第一次访问，有标题，不加入
                    //System.out.println(lineTxt);
                    boolean complate = false; //判断第一行读取是否完整
                    int num = 0;
                    for(int i = 0 ; i < lineTxt.length() ; i++){
                        if(lineTxt.charAt(i) == '"'){
                            num++;
                        }
                        if(num >= 2){//有两个双引号，即正常
                            complate = true;
                            break;
                        }
                    }
                    if(complate && partMess == ""){
                        //System.out.println(lineTxt);
                        //处理一些操作
                        //System.out.println("index 0 : " + len);
                        items.add(lineTxt);
                    }else{
                        tmp = lineTxt;
                    }
                }else{
                    //System.out.println(lineTxt);
                    //处理一些操作
                    items.add(lineTxt);
                }
                cnt++;
            }
            //System.out.println(items.get(items.size() - 1));
            //System.out.println("partMsg");
            //System.out.println(partMess);
            //System.out.println();
            //查看本次最后一行数据是否缺失
            boolean complate = false; //判断第一行读取是否完整
            String laststr = items.get(items.size() - 1);
            int num = 0;
            for(int i = 0 ; i < laststr.length() ; i++){
                if(laststr.charAt(i) == '"'){
                    num++;
                }
                if(num >= 2){
                    complate = true;
                    break;
                }
            }
            if(complate){ //没有缺失
                session.setAttribute("partMess","");
            }else{ //缺失了
                String cur = items.get(items.size() - 1);
                items.remove(items.size() - 1);//去除缺失数据
                session.setAttribute("partMess",cur);//加入session，供下次获取缺失的数据
            }
            if(tmp == ""){//本次开头没有缺失，即次数据没有缺失，不用拼接

            }else{//本次开头数据缺失了，即上次数据缺失了 --- 拼接
                partMess = partMess + tmp;
                items.add(partMess);//加入items
            }
            System.out.println(items.get(0));
            System.out.println(items.get(items.size() - 1));
            trackService.process(items,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> map =  new HashMap<>();
        map.put("code",0);
        return Result.success(map);
    }

    /**
     * 专门用来处理船的数据，仅支持csv格式（船的数据集）
     * @param chunk
     * @param request
     * @return
     */
    @PostMapping("/shipcsv")
    public Result<?> getShipCsvData(MultipartFile chunk, HttpServletRequest request){
        System.out.println("导入船迹数据集 csv");
        HttpSession session = request.getSession();
        String partMess = (String)session.getAttribute("partMess");
        if(partMess == null || partMess == ""){
            partMess = "";
        }
        try {
            InputStream inputStream = chunk.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> items = new ArrayList<>();
            String lineTxt;
            int cnt = 0;
            System.out.println("----------------------------------");
            String tmp = "";

            while((lineTxt=bufferedReader.readLine())!=null){
                if(cnt == 0){
                    if(lineTxt.contains("MMSI")) continue;//第一次访问，有标题，不加入
                    //System.out.println(lineTxt);
                    boolean complate = false; //判断第一行读取是否完整
                    int num = 0;
                    for(int i = 0 ; i < lineTxt.length() ; i++){
                        if(lineTxt.charAt(i) == ','){
                            num++;
                        }
                        if(num >= 16){//有16个逗号，即正常
                            complate = true;
                            break;
                        }
                    }
                    if(complate && partMess == ""){
                        //System.out.println(lineTxt);
                        //处理一些操作
                        //System.out.println("index 0 : " + len);
                        items.add(lineTxt);
                    }else{
                        tmp = lineTxt;
                    }
                }else{
                    //System.out.println(lineTxt);
                    //处理一些操作
                    items.add(lineTxt);
                }
                cnt++;
            }
            //System.out.println(items.get(items.size() - 1));
            //System.out.println("partMsg");
            //System.out.println(partMess);
            //System.out.println();
            //查看本次最后一行数据是否缺失
            boolean complate = false; //判断第一行读取是否完整
            String laststr = items.get(items.size() - 1);
            int num = 0;
            for(int i = 0 ; i < laststr.length() ; i++){
                if(laststr.charAt(i) == ','){
                    num++;
                }
                if(num >= 16){
                    complate = true;
                    break;
                }
            }
            if(complate){ //没有缺失
                session.setAttribute("partMess","");
            }else{ //缺失了
                String cur = items.get(items.size() - 1);
                items.remove(items.size() - 1);//去除缺失数据
                session.setAttribute("partMess",cur);//加入session，供下次获取缺失的数据
            }
            if(tmp == ""){//本次开头没有缺失，即次数据没有缺失，不用拼接

            }else{//本次开头数据缺失了，即上次数据缺失了 --- 拼接
                partMess = partMess + tmp;
                items.add(partMess);//加入items
            }
            System.out.println(items.get(0));
            System.out.println(items.get(items.size() - 1));
            trackService.processShipData(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String,Integer> map =  new HashMap<>();
        map.put("code",0);
        return Result.success(map);
    }


    @GetMapping("/merge")
    public Result<?> merge(MultipartFile file, HttpServletRequest req){

        HttpSession session = req.getSession();
        session.removeAttribute("partMess");//上传文件结束，清楚改关键字段
        System.out.println("enter merge");
        HashMap<String,Integer> map =  new HashMap<>();
        map.put("code",0);
        return Result.success(map);
    }


    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest req){

        /*
        try {
            System.out.println("enter upload method");
            int dataType = -1;
            if(file.getOriginalFilename().endsWith(".txt")){
                dataType = 0;
            }else if(file.getOriginalFilename().endsWith(".csv")){
                dataType = 1;
            }
            System.out.println("file name : " + file.getOriginalFilename());
            if(dataType == -1){
                return "文件格式错误，请输入txt/csv格式数据";
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            List<String> items = new ArrayList<>();
            String lineTxt;
            while((lineTxt=bufferedReader.readLine())!=null){
                items.add(lineTxt);
                //System.out.println(lineTxt);
            }
            trackService.process(items,dataType); //0:代表txt文件 1：代表csv文件
            return "数据处理完成";
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        return " ";
        /*
        //test
        //System.out.println("enter upload position");
        //return "上传成功";
        //return Result.success();
        //获取上传的日期
        String date=sdf.format(new Date());
        //当前项目下的保存路径
        String realPath=req.getServletContext().getRealPath("/img")+date;
        //判断保存路径的文件夹是否已创建
        File folder=new File(realPath);
        if (!folder.exists())
            folder.mkdirs();
        //获取上传的文件名
        String oldName=file.getOriginalFilename();
        //保存的文件名，使用UUID类生成通用唯一识别码，避免文件名重复
        String newName= UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."));

        //构建数据处理类
        DataProcession procession = null;

        //保存文件到项目的保存路径中
        try {
            file.transferTo(new File(folder,newName));
            //得到完整的保存路径
            String url= req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+"/img"+date+newName;
            String dirName = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+"/img"+date;
            //数据处理
            //trackService.process(url,dirName);

            //return Result.success();
            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }
        //return Result.fail();
        return "error";
         */
        //return "testing";

    }


    //@RequestMapping(value = "/importData",method = RequestMethod.POST)
    //@ApiOperation(value = "处理上传文件", notes = "处理上传文件", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<?> importFile(@RequestParam("file") MultipartFile multipartFile)throws Exception{
        /*File file = null;
        file = File.createTempFile("tmp", null);
        file.deleteOnExit();
        if(multipartFile.getOriginalFilename().endsWith(".txt")){
            BufferedReader reader = null;
            String temp = null;
            try {
                multipartFile.transferTo(file); //MultipartFile转File
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));//解决服务器上乱码
                reader.readLine();
                while ((temp = reader.readLine()) != null) {
                    YdywHourEntity ydywHourEntity = new YdywHourEntity();
                    String[] s = temp.split(",");
                    ydywHourEntity.setYh2g(s[0]);
                    ydywHourEntity.setYh4g(s[1]);
                    ydywHourService.save(ydywHourEntity);
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(multipartFile.getOriginalFilename().endsWith(".csv")){
            System.out.println(multipartFile.getOriginalFilename());
            BufferedReader reader = null;
            String temp = null;
            try {
                multipartFile.transferTo(file); //MultipartFile转File
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));//解决服务器上乱码
                reader.readLine();
                while ((temp = reader.readLine()) != null) {
                    YdywHourEntity ydywHourEntity = new YdywHourEntity();
                    String[] s = temp.split(",");
                    ydywHourEntity.setYh2g(s[0]);
                    ydywHourEntity.setYh4g(s[1]);
                    ydywHourEntity.setYhVolte(s[2]);
                    ydywHourService.save(ydywHourEntity);
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(multipartFile.getOriginalFilename().endsWith(".xlsx")){
            System.out.println(multipartFile.getInputStream());
            List<YdywHourTemplate> ydywHourEntityList = ExcelUtils.importData(multipartFile, YdywHourTemplate.class);
            System.out.println(ydywHourEntityList.toString());
            if (ydywHourEntityList == null || ydywHourEntityList.size() == 0) {
                throw new EIPException("导入失败,导入数据错误");
            }
            for(YdywHourTemplate ydywHourTemplate:ydywHourEntityList){
                try{
                    YdywHourEntity ydywHourEntity = new YdywHourEntity();
                    BeanUtils.copyProperties(ydywHourEntity, ydywHourTemplate);
                    ydywHourEntity.setCjsj((new Date()));
                    ydywHourService.save(ydywHourEntity);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }*/
        System.out.print("success");
        return Result.success();
    }
}
