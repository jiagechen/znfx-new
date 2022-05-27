package cn.njust.label.main.controller;



import cn.njust.label.main.common.MongoPage;
import cn.njust.label.main.common.Result;
import cn.njust.label.main.dto.Match;
//import cn.njust.label.main.service.impl.MatchServiceImpl;
import cn.njust.label.main.entity.Track;
import cn.njust.label.main.utils.MongoDBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchController {
    @Autowired
    private MongoTemplate mongoTemplate;
//    private MatchServiceImpl matchServiceImpl;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "5") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        //List<Track> list = trackService.findAllTrack();
        //List<Track> list = trackService.findPart(pageNum, pageSize);
        System.out.println("enter match");
        MongoPage<Track> data = MongoDBUtil.findPart(pageNum, pageSize, mongoTemplate, Match.class);
        return Result.success(data);
    }

//    不知道前端业务数据格式，文件处理只需要传String数组和文件类型 txt/csv
    @RequestMapping("/flight")
    @ResponseBody
    public void FlightMatch(@RequestParam("file") MultipartFile file){
//        //调用文件处理，返回航迹数组
//        List<ENPoint> list =  new ArrayList<>();
//        String[] idlist = {"6270ee916eae177bde22a574",
//                "6270ee926eae177bde22a576","6270ee926eae177bde22a575",
//                "6270ee926eae177bde22a577","6270ee926eae177bde22a578",
//                "6270ee936eae177bde22a5b0","6270ee936eae177bde22a5b1",
//                "6270ee936eae177bde22a5c4","6270ee936eae177bde22a5c5",
//                "6270ee936eae177bde22a5c6","6270ee936eae177bde22a5c7",
//                "6270ee936eae177bde22a5d8","6270ee936eae177bde22a5d9",
//                "6270ee936eae177bde22a5ec","6270ee936eae177bde22a5ed\n",
//                "6270ee936eae177bde22a5ee","6270ee946eae177bde22a600",
//                "6270ee946eae177bde22a601","6270ee946eae177bde22a614"
//        };
//        List<Rtra> centralRlist = mongoTemplate.findAll(Rtra.class);
//
//        for(String id : idlist){
//            ENPoint enPoint = mongoTemplate.findById(id,ENPoint.class);
//            list.add(enPoint);
//        }
//        //如果需要先二元匹配的话
//        //
//        //二元匹配
//        //
//        //
//        //
//        double[][] percentages = matchServiceImpl.Match(list,centralRlist);
//
//        double[] resultPercentage = new double[list.size()];
//        List<String> resultIds = new ArrayList<>();
//
//        int index = 0;
//        for (double[] percentage : percentages){
//            double max = 0;
//            String routeID = "";
//            for(int i = 0;i<percentage.length;i++){
//                if(percentage[i]>max){
//                    max = percentage[i];
//                    routeID = centralRlist.get(i).getId();
//                }
//            }
//            if(routeID.equals("")) routeID="无匹配航线";
//            resultPercentage[index] = max;
//            resultIds.add(routeID);
//
//            index++;
//        }
//
//        System.out.println(Arrays.toString(resultPercentage));
//        System.out.println(resultIds);
    }
}
