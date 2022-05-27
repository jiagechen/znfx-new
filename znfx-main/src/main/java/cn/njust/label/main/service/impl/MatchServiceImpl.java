//package cn.njust.label.main.service.impl;
//
//import cn.njust.label.main.dao.MatchDao;
//import cn.njust.label.main.dto.FinalData;
//import cn.njust.label.main.dto.Stripe;
//import cn.njust.label.main.entity.ENPoint;
//import cn.njust.label.main.dto.Rtra;
//import cn.njust.label.main.entity.MatchTrackPointItems;
//import cn.njust.label.main.service.MatchService;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class MatchServiceImpl implements MatchService {
//
//    public MatchDao matchDao;
////    public float thresholdR = 0.1f;//半径阈值
////    public float thresholdA = 0.1f;//角度阈值
//    public double thresholdSingleDirectionSection = 0.1d;
//    public double thresholdMultiDirectionSection = 0.1d;
//
//    @Override
//    public List<Rtra> getAllCentralRoute() {return matchDao.getALLCentralRoute();}
//
//    @Override
//    public Rtra getCentralRouteById(String lineId){return matchDao.getCentralRouteById(lineId);}
//
//    @Override
//    public double[][] Match(List<ENPoint> unmatchedLines, List<Rtra> centralLines){
//
//        double[][] percentages = new double[unmatchedLines.size()][centralLines.size()];
//        int index1 = 0;
//        for(ENPoint enPoint : unmatchedLines){
//            List<MatchTrackPointItems> unmatchedLine = enPoint.getTrackPointItems();
//            List<MatchTrackPointItems> splitLine = LineSplit(1,unmatchedLine);
////            System.out.println(splitLine.get(0).getLongitude());
////            System.out.println(splitLine.get(0).getLatitude());
////            System.out.println(splitLine.get(1).getLongitude());
////            System.out.println(splitLine.get(1).getLatitude());
////            System.out.println(splitLine.get(2).getLongitude());
////            System.out.println(splitLine.get(2).getLatitude());
//            double[] angles = angleCompute(splitLine);
//            boolean[] classification = SectionCla(angles);
////            System.out.println(Arrays.toString(angles));
////            System.out.println(Arrays.toString(classification));
//            List<FinalData> finalData = FinalData(classification,splitLine);
//
//            for (FinalData finalData1 : finalData){
//                System.out.println("---------------切分之后的航线起点终点----------------------------");
//                System.out.println(finalData1.getStartPoint().getLatitude());
//                System.out.println(finalData1.getStartPoint().getLongitude());
//                System.out.println(finalData1.getDesPoint().getLatitude());
//                System.out.println(finalData1.getDesPoint().getLongitude());
//            }
//
//            List<Stripe> stripeList1 = StripeTransfer1(finalData);
//            int index2 = 0;
//            for(Rtra centralLine : centralLines){
//                List<MatchTrackPointItems> centarlLinePoints = centralLine.getTrackPointItems();
//                List<Stripe> stripeList2 = StripeTransfer2(centarlLinePoints);
//                double percentage = Match1(stripeList1,stripeList2);
//                percentages[index1][index2] = percentage;
//                index2++;
//            }
//            index1++;
//        }
//        return percentages;
//    }
//
//    //stripe1使待匹配的航迹段,返回匹配度
//    public double Match1(List<Stripe> stripe1, List<Stripe> stripe2){
//        double[] num = new double[stripe1.size()];
//        double[] num1 = new double[stripe1.size()];
//        for (int index1=0;index1<stripe1.size();index1++){
//            double max=0;
//            int index=0;
//            for(int index2=0;index2<stripe2.size();index2++){
//                if(Percentage(stripe1.get(index1),stripe2.get(index2))>max){
//                    max = Percentage(stripe1.get(index1),stripe2.get(index2));
//                    index = index2;
//                }
//            }
//            num[index1] = max;
//            num1[index1] = index;
//            //stripe2.remove(index);
//        }
//
//        double sum = 0;
//        for(double number : num){
//            sum += number;
//        }
//        System.out.println("每段的置信度"+ Arrays.toString(num));
//        System.out.println("匹配位置"+Arrays.toString(num1));
//        return sum/num.length;
//
//    }
//
//    //返回匹配百分比
//    public double Percentage(Stripe stripe1,Stripe stripe2){
//        double start_lat1 = stripe1.getStart_lat();
//        double start_lon1 = stripe1.getStart_lon();
//        double end_lat1 = stripe1.getEnd_lat();
//        double end_lon1 = stripe1.getEnd_lon();
//        double start_lat2 = stripe2.getStart_lat();
//        double start_lon2 = stripe2.getStart_lon();
//        double end_lat2 = stripe2.getEnd_lat();
//        double end_lon2 = stripe2.getEnd_lon();
//        double A = ComputeDistance(start_lon2,start_lat2,end_lon2,end_lat2);
//        double B = ComputeDistance(start_lon1,start_lat1,start_lon2,start_lat2);
//        double C = ComputeDistance(start_lon1,start_lat1,end_lon2,end_lat2);
//        double cosB = (A*A+C*C-B*B)/(2*A*C);
//        double cosC = (A*A+B*B-C*C)/(2*A*B);
//        double sinC = Math.sqrt(1-cosC*cosC);
//        double distance1 = B*sinC;
//
//
//        double A1 = A;
//        double B1 = ComputeDistance(end_lon1,end_lat1,start_lon2,start_lat2);
//        double C1 = ComputeDistance(end_lon1,end_lat1,end_lon2,end_lat2);
//        double cosB1 = (A1*A1+C1*C1-B1*B1)/(2*A1*C1);
//        double cosC1 = (A1*A1+B1*B1-C1*C1)/(2*A1*B1);
//        double sinC1 = Math.sqrt(1-cosC1*cosC1);
//        double distance2 = B1*sinC1;
//
//        double D1 = ComputeDistance(start_lon1,start_lat1,end_lon1,end_lat1);
//        double cosD1 = (B*B+B1*B1-D1*D1)/(2*B*B1);
//
//        if((cosB<0 && cosB1<0) || (cosC<0 && cosC1<0)) {
//            return 0;}
//
////        System.out.println("--------------------------------");
////        System.out.println("中心航起点终点线经纬度：");
////        System.out.println("中心航线起点lat:"+start_lat2);
////        System.out.println("中心航线起点lon:"+start_lon2);
////        System.out.println("中心航线终点lat:"+end_lat2);
////        System.out.println("中心航线终点lon:"+end_lon2);
////        System.out.println("匹配航线起始终止点");
////        System.out.println("匹配航线起点lat:"+start_lat1);
////        System.out.println("匹配航线起点lon:"+start_lon1);
////        System.out.println("匹配航线终点lat:"+end_lat1);
////        System.out.println("匹配航线终点lon:"+end_lon1);
////        System.out.println("中心航线长度A:"+A);
////        System.out.println("匹配航线起点到中心航线起点B:"+B);
////        System.out.println("匹配航线起点到中心航线终点C:"+C);
////        System.out.println("中心航线长度A1:"+A1);
////        System.out.println("匹配航线终点到中心航线起点BB1:"+B1);
////        System.out.println("匹配航线终点到中心航线起点BC1:"+C1);
////        System.out.println("匹配航线起点到中心航线距离:"+distance1);
////        System.out.println("匹配航线终点到中心航线距离"+distance2);
////        System.out.println("cosB:"+cosB);
////        System.out.println("cosC"+cosC);
////        System.out.println("cosB1"+cosB1);
////        System.out.println("cosC1"+cosC1);
//
//        if(!(Math.toDegrees(Math.acos(cosD1))>Math.toDegrees(Math.acos(cosC)) && Math.toDegrees(Math.acos(cosD1))>Math.toDegrees(Math.acos(cosC1)))){
//            double max = 0;
//            double percent = 0;
//            max = Math.max(distance1, distance2);
//            if(max>10000) {
//                percent = (Math.abs(distance1-distance2)-(max-10000))/10000;
//                return percent;
//            }else return 1;
//        }
//
//        double percent1 = 1;
//        double percent2 = 1;
//        if(distance1>10000){
//            percent1 = 10000/distance1;
//        }
//        if(distance2>10000){
//            percent2 = 10000/distance2;
//        }
//
//
//        double percent = percent1*(distance1/(distance1+distance2))+percent2*(distance2/(distance1+distance2));
//
//        System.out.println("percent:"+percent);
//        return percent;
//
//    }
//
//
//    //航迹切分
//    //input:
//    //    splitNum:每多少个航迹点做个一段小航迹，每段小航迹只选用第一个点和最后一个点连成的直线
//    //    lineData:需要处理的大航迹
//    //output:
//    //    经过分段之后的小航迹点的List。
//    public List<MatchTrackPointItems> LineSplit(int splitNum, List<MatchTrackPointItems> lineData) {
//        if(splitNum == 1) return lineData;
//
////        List<TrackPointItems> lineData = enpoint.getTrackPointItems();
//        int listSize = lineData.size() / splitNum;
//        List<MatchTrackPointItems> splitLineList = new ArrayList<>();
//
//        if (splitNum > 1) {
//            int index;
//            splitLineList.add(lineData.get(0));
//            for (index = 0; index < listSize; index++) {
//                splitLineList.add(lineData.get(splitNum * index));
//            }
//            for (int i = (index - 1) * splitNum + 1; i < lineData.size(); i++) {
//                splitLineList.add(lineData.get(i));
//            }
//        }
//
//        return splitLineList;
//    }
//
//    //航迹方向计算  纬度：lati
//    //input
//    //    经过航迹切分之后的航迹点List
//    //output
//    //    计算航迹点List后生成的方向List，方向List元素个数为航迹点List个数-1
//    public double[] angleCompute(List<MatchTrackPointItems> splitLineList) {
//        double[] angleList = new double[splitLineList.size() - 1];
//        for (int i = 0; i < splitLineList.size() && i + 1 < splitLineList.size(); i++) {
//            double lat1 = Double.parseDouble(splitLineList.get(i).getLatitude());
//            double lon1 = Double.parseDouble(splitLineList.get(i).getLongitude());
//            double lat2 = Double.parseDouble(splitLineList.get(i + 1).getLatitude());
//            double lon2 = Double.parseDouble(splitLineList.get(i + 1).getLongitude());
//            angleList[i] = angleCompute(lat1,lon1,lat2,lon2);
//        }
//        return angleList;
//    }
//
//    //转向判断,滑动窗口，窗口大小为3
//    //input
//    //    每段小航迹的航行方向数组(double[])
//    //output
//    //    判断小航迹List是否为平飞段，是为ture否则false，boolean[]
//    public boolean[] SectionCla(double[] angleList) {
//        boolean[] sectionClarification = new boolean[angleList.length];
//        Arrays.fill(sectionClarification, true);
//        for (int i = 0; i + 2 < angleList.length; i++) {
//            double t1 = angleList[i], t2 = angleList[i + 1], t3 = angleList[i + 2];
//            if (Math.abs(t1 - t2) > thresholdMultiDirectionSection && Math.abs(t2 - t3) > thresholdMultiDirectionSection) {
//                sectionClarification[i] = false;
//                sectionClarification[i + 1] = false;
//                sectionClarification[i + 2] = false;
//                continue;
//            }
//            if (Math.abs(t2 - t3) > thresholdSingleDirectionSection) {
//                sectionClarification[i + 2] = false;
//                continue;
//            }
//            sectionClarification[i] = true;
//        }
//        return sectionClarification;
//    }
//
//    //航迹分段，根据平飞段判断，将原数剧中的非平飞段数据删除。
//    //input
//    //    原有的数据，是否为平飞段的判断boolean数组
//    //output
//    //    去除非平飞段数据后的航迹数据
//    public List<FinalData> FinalData(boolean[] sectionClarification, List<MatchTrackPointItems> splitLineList) {
//        List<FinalData> finalData = new ArrayList<>();
//        boolean flag = false;
//        int index = 0;
//        FinalData finalData1 = new FinalData();
//        for (int i = 0; i < sectionClarification.length; i++) {
//            if (sectionClarification[i] && !flag) {
////                System.out.println("FinalDataStartPoint:"+i);
//                finalData1.setStartPoint(splitLineList.get(i));
//                flag = true;
//            }
//            if (!sectionClarification[i] && flag) {
////                System.out.println("FinalDataTerminationPoint:"+i);
//                finalData1.setDesPoint(splitLineList.get(i));
//
//                FinalData finalData2 = new FinalData();
//                finalData2.setStartPoint(finalData1.getStartPoint());
//                finalData2.setDesPoint(finalData1.getDesPoint());
//                finalData.add(finalData2);
////                finalData1 = null;
//                flag = false;
//            }
//        }
//        return finalData;
//    }
//
//    //以精度为基准计算角度,x1 y1 为起点坐标,x2 y2为终点坐标
//    public double angleCompute(double lat1, double lon1, double lat2, double lon2) {
//        double latitude1 = Math.toRadians(lat1);
//        double latitude2 = Math.toRadians(lat2);
//        double longDiff = Math.toRadians(lon2 - lon1);
//        double y = Math.sin(longDiff)*Math.cos(latitude2);
//        double x = Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);
//        double Nbearing = (Math.toDegrees(Math.atan2(y, x))+360)%360;
//        return -Nbearing+180;
//    }
//
//    public List<Stripe> StripeTransfer2(List<MatchTrackPointItems> list){
//        List<Stripe> stripes = new ArrayList<>();
//        for (int i=0;i<list.size()-1;i++){
//            Stripe stripe = new Stripe();
//            stripe.setStart_lat(Double.parseDouble(list.get(i).getLatitude()));
//            stripe.setStart_lon(Double.parseDouble(list.get(i).getLongitude()));
//            stripe.setEnd_lat(Double.parseDouble(list.get(i+1).getLatitude()));
//            stripe.setEnd_lon(Double.parseDouble(list.get(i+1).getLongitude()));
//            stripes.add(stripe);
//        }
//        return stripes;
//    }
//
//    public List<Stripe> StripeTransfer1(List<FinalData> list){
//        List<Stripe> stripes = new ArrayList<>();
//        for (FinalData finalData : list){
//            Stripe stripe = new Stripe();
//            stripe.setStart_lat(Double.parseDouble(finalData.getStartPoint().getLatitude()));
//            stripe.setStart_lon(Double.parseDouble(finalData.getStartPoint().getLongitude()));
//            stripe.setEnd_lat(Double.parseDouble(finalData.getDesPoint().getLatitude()));
//            stripe.setEnd_lon(Double.parseDouble(finalData.getDesPoint().getLongitude()));
//            stripes.add(stripe);
//        }
//        return stripes;
//    }
//
//    public static double ComputeDistance(double long1, double lat1, double long2, double lat2) {
//        double a, b, R;
//        R = 6378137; // 地球半径
//        lat1 = lat1 * Math.PI / 180.0;
//        lat2 = lat2 * Math.PI / 180.0;
//        a = lat1 - lat2;
//        b = (long1 - long2) * Math.PI / 180.0;
//        double d;
//        double sa2, sb2;
//        sa2 = Math.sin(a / 2.0);
//        sb2 = Math.sin(b / 2.0);
//        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
//        return d;
//    }
//}
