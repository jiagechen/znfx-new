package cn.njust.label.main.service;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import cn.njust.label.main.dto.ENPoint;

public class ExtractionService {
    /**
     *函数功能：从源文件中读出所以记录中的经纬度坐标，并存入到ArrayList数组中，并将其返回
     * @param fGPS：源数据文件
     * @return pGPSArrayInit：返回保存所有点坐标的ArrayList数组
     */

    public static ArrayList<ENPoint> getENPointFromFile(File fGPS)throws Exception{
        ArrayList<ENPoint> pGPSArray = new ArrayList<>();
        if(fGPS.exists()&&fGPS.isFile()){
            InputStreamReader read = new InputStreamReader(new FileInputStream(fGPS));
            BufferedReader bReader = new BufferedReader(read);
            String str;
            String[] strGPS;
            int i = 0;
            while((str = bReader.readLine())!=null){
                strGPS = str.split("\t");
                ENPoint p = new ENPoint();
                p.id = i;
                i++;
                p.pe = Double.parseDouble(strGPS[2]);
                p.pn = Double.parseDouble(strGPS[3]);
                pGPSArray.add(p);
            }
            bReader.close();
        }
        return pGPSArray;
    }

    /**
     * 函数功能：将过滤后的点的经纬度坐标、平均距离误差、压缩率写到结果文件中
     * @param outGPSFile：结果文件
     * @param pGPSPointFilter：过滤后的点
     * @param mDerror：平均距离误差
     * @param cRate：压缩率
     */
    public static void writeFilterPointToFile(File outGPSFile,ArrayList<ENPoint> pGPSPointFilter,
                                              double mDerror,double cRate)throws Exception{
        Iterator<ENPoint> iFilter = pGPSPointFilter.iterator();
        RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
        while(iFilter.hasNext()){
            ENPoint p = iFilter.next();
            String sFilter = p.getResultString()+"\n";
            byte[] bFilter = sFilter.getBytes();
            rFilter.write(bFilter);
        }
        String strmc = "#"+ pGPSPointFilter.size() +","+
                mDerror +","+ cRate +"%"+"#"+"\n";
        byte[] bmc = strmc.getBytes();
        rFilter.write(bmc);

        rFilter.close();
    }
    /**
     * 函数功能：将转换后的原始经纬度数据点存到文件中
     * @param outGPSFile:文件对象
     * @param pGPSPointFilter:点数组
     */
    public static void writeInitPointToFile(File outGPSFile,ArrayList<ENPoint> pGPSPointFilter)throws Exception{
        Iterator<ENPoint> iFilter = pGPSPointFilter.iterator();
        RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
        while(iFilter.hasNext()){
            ENPoint p = iFilter.next();
            String sFilter = p.toString()+"\n";
            byte[] bFilter = sFilter.getBytes();
            rFilter.write(bFilter);
        }
        rFilter.close();
    }
    /**
     * 函数功能：将数组中的经纬度点坐标数据写入测试文件中，用于可视化测试
     * @param outGPSFile：文件对象
     * @param pGPSPointFilter：点数组
     */
    public static void writeTestPointToFile(File outGPSFile,ArrayList<ENPoint> pGPSPointFilter)throws Exception{
        Iterator<ENPoint> iFilter = pGPSPointFilter.iterator();
        RandomAccessFile rFilter = new RandomAccessFile(outGPSFile,"rw");
        while(iFilter.hasNext()){
            ENPoint p = iFilter.next();
            String sFilter = p.getTestString()+"\n";
            byte[] bFilter = sFilter.getBytes();
            rFilter.write(bFilter);
        }
        rFilter.close();
    }



    /**
     * 函数功能：使用三角形面积（使用海伦公式求得）相等方法计算点pX到点pA和pB所确定的直线的距离
     * @param pA：起始点
     * @param pB：结束点
     * @param pX：第三个点
     * @return distance：点pX到pA和pB所在直线的距离
     */
    public static double distToSegment(ENPoint pA,ENPoint pB,ENPoint pX){
        double a = Math.abs(geoDist(pA, pB));
        double b = Math.abs(geoDist(pA, pX));
        double c = Math.abs(geoDist(pB, pX));
        double p = (a+b+c)/2.0;
        double s = Math.sqrt(Math.abs(p*(p-a)*(p-b)*(p-c)));
        return s*2.0/a;
    }

    /**
     * 函数功能：用老师给的看不懂的方法求两个经纬度点之间的距离
     * @param pA：起始点
     * @param pB：结束点
     * @return distance：距离
     */
    public static double geoDist(ENPoint pA,ENPoint pB)
    {
        double radLat1 = Rad(pA.pn);
        double radLat2 = Rad(pB.pn);
        double delta_lon = Rad(pB.pe - pA.pe);
        double top_1 = Math.cos(radLat2) * Math.sin(delta_lon);
        double top_2 = Math.cos(radLat1) * Math.sin(radLat2) - Math.sin(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
        double top = Math.sqrt(top_1 * top_1 + top_2 * top_2);
        double bottom = Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(delta_lon);
        double delta_sigma = Math.atan2(top, bottom);
        return delta_sigma * 6378137.0;
    }
    /**
     * 函数功能：角度转弧度
     * @param d：角度
     * @return 返回的是弧度
     */
    public static double Rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 函数功能：根据最大距离限制，采用DP方法递归的对原始轨迹进行采样，得到压缩后的轨迹
     * @param enpInit：原始经纬度坐标点数组
     * @param enpArrayFilter：保持过滤后的点坐标数组
     * @param start：起始下标
     * @param end：终点下标
     * @param DMax：预先指定好的最大距离误差
     */
    public static void TrajCompressC(ENPoint[] enpInit,ArrayList<ENPoint> enpArrayFilter,
                                     int start,int end,double DMax){
        if(start < end){//递归进行的条件
            double maxDist = 0;//最大距离
            int cur_pt = 0;//当前下标
            for(int i=start+1;i<end;i++){
                double curDist = distToSegment(enpInit[start],enpInit[end],enpInit[i]);//当前点到对应线段的距离
                if(curDist > maxDist){
                    maxDist = curDist;
                    cur_pt = i;
                }//求出最大距离及最大距离对应点的下标
            }
            //若当前最大距离大于最大距离误差
            if(maxDist >= DMax){
                enpArrayFilter.add(enpInit[cur_pt]);//将当前点加入到过滤数组中
                //将原来的线段以当前点为中心拆成两段，分别进行递归处理
                TrajCompressC(enpInit,enpArrayFilter,start,cur_pt,DMax);
                TrajCompressC(enpInit,enpArrayFilter,cur_pt,end,DMax);
            }
        }
    }
    /**
     * 函数功能：求平均距离误差
     * @param pGPSArrayInit：原始数据点坐标
     * @param pGPSArrayFilterSort：过滤后的数据点坐标
     * @return ：返回平均距离
     */
    public static double getMeanDistError(
            ArrayList<ENPoint> pGPSArrayInit,ArrayList<ENPoint> pGPSArrayFilterSort){
        double sumDist = 0.0;
        for(int i=1;i<pGPSArrayFilterSort.size();i++){
            int start = pGPSArrayFilterSort.get(i-1).id;
            int end = pGPSArrayFilterSort.get(i).id;
            for(int j=start+1;j<end;j++){
                sumDist += distToSegment(
                        pGPSArrayInit.get(start),pGPSArrayInit.get(end),pGPSArrayInit.get(j));
            }
        }
        return sumDist/(pGPSArrayInit.size());
    }

    /**
     * 函数功能：求航向角（将经线至北向南方向作为基准方向。区间起点到终点方向作为该段区间航行方向，基准方向与航行方向的夹角定义区间夹角，）
     * @param lat1：起始点纬度
     * @param lon1：起始点经度
     * @param lat2：终点纬度
     * @param lon2：终点经度
     * @return ：返回航向角
     */
    public static double bearing(double lat1, double lon1, double lat2, double lon2){
        double latitude1 = Math.toRadians(lat1);
        double latitude2 = Math.toRadians(lat2);
        double longDiff = Math.toRadians(lon2 - lon1);
        double y = Math.sin(longDiff)*Math.cos(latitude2);
        double x = Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
        double Nbearing = (Math.toDegrees(Math.atan2(y, x))+360)%360;
        return -Nbearing+180;
    }

    /**
     * 函数功能：为对象补充航向角数据
     * @param pGPSArrayFilter：原始数据点坐标
     */
    //方位角 数组
    public static ArrayList<ENPoint> getDirectList(ArrayList<ENPoint> pGPSArrayFilter){
        for(int i=0;i<pGPSArrayFilter.size() - 1;i++){
            pGPSArrayFilter.get(i).angle = bearing(pGPSArrayFilter.get(i).pe, pGPSArrayFilter.get(i).pn, pGPSArrayFilter.get(i+1).pe, pGPSArrayFilter.get(i+1).pn);
        }
        return pGPSArrayFilter;
    }
    //getIfturn;
    /**
     * 函数功能：标记平飞段、转向段
     * @param window:窗口大小
     * @param a:多区间转向阈值
     * @param b:单区间转向阈值
     * @param pGPSArrayFilter:原始数据
     */
    public  static  ArrayList<ENPoint> getIfturn(ArrayList<ENPoint> pGPSArrayFilter,int window,double a,double b) {
        LinkedList<Object> l = new LinkedList<>();
        ArrayList<ENPoint> p = new ArrayList<>(getDirectList(pGPSArrayFilter));
        for (int i = 0; i < p.size(); i++) {
            l.add(p.get(i).id);
            if (l.size() == window) {
                if (Math.abs(p.get(i-2).angle-p.get(i-1).angle)>=a && Math.abs(p.get(i-1).angle-p.get(i).angle)>=a){
                    p.get(i-2).ifturn = 1;
                    p.get(i-1).ifturn = 1;
                    p.get(i).ifturn = 1;
                }else if (Math.abs(p.get(i-1).angle-p.get(i).angle)>=b){
                    p.get(i).ifturn = 1;
                }else {
                    p.get(i-2).ifturn = 0;
                }
                l.removeFirst();
            }
        }
        return p;
    }
}
