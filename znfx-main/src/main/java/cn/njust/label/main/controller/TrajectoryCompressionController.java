package cn.njust.label.main.controller;

import cn.njust.label.common.api.CommonResult;
import cn.njust.label.main.dto.ENPoint;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static cn.njust.label.main.service.ExtractionService.*;


@CrossOrigin
@RestController
@RequestMapping("/compression")

public class TrajectoryCompressionController {


    public static void compression(String[] args)throws Exception{
        //-----------------------1、相关ArrayList数组和File对象的声明和定义-------------------------------------------------//
        ArrayList<ENPoint> pGPSArrayInit;//原纪录经纬度坐标数组
        ArrayList<ENPoint> pGPSArrayFilter = new ArrayList<>();//过滤后的经纬度坐标数组
        ArrayList<ENPoint> pGPSArrayFilterLT = new ArrayList<>(getIfturn(pGPSArrayFilter, 3, 2, 2));//过滤并排序后的经纬度坐标数组
        ArrayList<ENPoint> pGPSArrayFilterLevel = new ArrayList<>();//过滤后平飞段的经纬度坐标数组
        ArrayList<ENPoint> pGPSArrayFilterTurn = new ArrayList<>();//过滤后平飞段的经纬度坐标数组
        File fGPS = new File("second_inside.txt");//原始数据文件对象
        File oGPS = new File("f_second_inside.txt");//过滤后的结果数据文件对象
        //保持转换成度后的原始经纬度数据文件，保持格式为“ID#经纬值，纬度值”，其中经度和维度单位为度，并保留小数点后6位数字
        File fInitGPSPoint = new File("second_inside-ENPoint.txt");//保持转换后的原始经纬度坐标点的数据文件
        File fTestInitPoint = new File("second_inside-InitTestPoint.txt");//用于仿真的原始经纬度坐标点数据文件
        File fTestFilterPoint = new File("second_inside-FilterTestPoint.txt");//用于仿真的过滤后的经纬度坐标点数据文件
        //-------------------------2、获取原始点坐标并将其写入到文件中-------------------------------------------------------//
        pGPSArrayInit = getENPointFromFile(fGPS);//从原始数据文件中获取转换后的经纬度坐标点数据，存放到ArrayList数组中
        writeInitPointToFile(fInitGPSPoint, pGPSArrayInit);//将转换后的原始经纬度点数据写入文件中
        System.out.println("原始经纬度点坐标的个数:"+pGPSArrayInit.size());//输出原始经纬度点坐标的个数
        //-------------------------3、进行轨迹压缩-----------------------------------------------------------------------//
        double DMax = 30.0;//设定最大距离误差阈值
        pGPSArrayFilter.add(pGPSArrayInit.get(0));//获取第一个原始经纬度点坐标并添加到过滤后的数组中
        pGPSArrayFilter.add(pGPSArrayInit.get(pGPSArrayInit.size()-1));//获取最后一个原始经纬度点坐标并添加到过滤后的数组中
        ENPoint[] enpInit = new ENPoint[pGPSArrayInit.size()];//使用一个点数组接收所有的点坐标，用于后面的压缩
        Iterator<ENPoint> iInit = pGPSArrayInit.iterator();
        int jj=0;
        while(iInit.hasNext()){
            enpInit[jj] = iInit.next();
            jj++;
        }//将ArrayList中的点坐标拷贝到点数组中
        int start = 0;//起始下标
        int end = pGPSArrayInit.size()-1;//结束下标
        TrajCompressC(enpInit,pGPSArrayFilter,start,end,DMax);//DP压缩算法
        System.out.println("压缩后的点数:"+pGPSArrayFilter.size());//输出压缩后的点数
        //-------------------------4、对压缩后的经纬度点坐标数据按照ID从小到大排序---------------------------------------------//
        ENPoint[] enpFilter = new ENPoint[pGPSArrayFilter.size()];//使用一个点数组接收过滤后的点坐标，用于后面的排序
        Iterator<ENPoint> iF = pGPSArrayFilter.iterator();
        int i = 0;
        while(iF.hasNext()){
            enpFilter[i] = iF.next();
            i++;
        }//将ArrayList中的点坐标拷贝到点数组中
        Arrays.sort(enpFilter);//进行排序
        //将排序后的点坐标写到一个新的ArrayList数组中
        //过滤并排序后的经纬度坐标数组
        ArrayList<ENPoint> pGPSArrayFilterSort = new ArrayList<>(Arrays.asList(enpFilter));
        //-------------------------5、生成仿真测试文件--------------------------------------------------------------------//
        writeTestPointToFile(fTestInitPoint,pGPSArrayInit);//将原始经纬度数据点写入仿真文件中，格式为“经度，维度”
        writeTestPointToFile(fTestFilterPoint, pGPSArrayFilterSort);//将过滤后的经纬度数据点写入仿真文件中，格式为“经度，维度”
        //-------------------------6、求平均误差-------------------------------------------------------------------------//
        double mDError = getMeanDistError(pGPSArrayInit,pGPSArrayFilterSort);//求平均误差
        System.out.println("平均误差:"+mDError);
        //-------------------------7、求压缩率--------------------------------------------------------------------------//
        double cRate = (double)pGPSArrayFilter.size()/pGPSArrayInit.size()*100;//求压缩率
        System.out.println("压缩率:"+cRate);
        //-------------------------8、求平飞段、转向段--------------------------------------------------------------------------//
        pGPSArrayFilterLT.addAll(getIfturn(pGPSArrayInit,3,2,2));
        for (ENPoint enPoint : pGPSArrayFilterLT) {
            if (enPoint.ifturn == 0) {
                pGPSArrayFilterLevel.add(enPoint);//平飞段
            } else {
                pGPSArrayFilterTurn.add(enPoint);//转向段
            }
        }
        //-------------------------9、生成最终结果文件--------------------------------------------------------------------//
        //将最终结果写入结果文件中，包括过滤后的点的ID，点的个数、平均误差和压缩率
        writeFilterPointToFile(oGPS,pGPSArrayFilterSort,mDError,cRate);
        writeInitPointToFile(new File("pGPSArrayFilterLevel.txt"),pGPSArrayFilterLevel);
        writeInitPointToFile(new File("pGPSArrayFilterTurn.txt"),pGPSArrayFilterTurn);
        System.out.println("分段文件输出：pGPSArrayFilterLevel.txt");
        //------------------------------------------------------------------------------------------------------------//
    }

    //-------------------------------测试前端是否get到--------------------------------------------------
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    @ResponseBody
//    public CommonResult<List<String>> getTestList() {
//        List<String> ret = new ArrayList<>();
//        ret.add("test1");
//        ret.add("test2");
//        ret.add("test3");
//        ret.add("test4");
//        return CommonResult.success(ret);
//    }
}