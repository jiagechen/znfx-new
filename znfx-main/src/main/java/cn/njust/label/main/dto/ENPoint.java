package cn.njust.label.main.dto;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
@Service
@Data
public class ENPoint implements Comparable<ENPoint>{
    public int id;//点ID
    public double pe;//经度
    public double pn;//维度
    public double angle;//方位角:将经线至北向南方向作为基准方向，基准方向规定为。区间起点到终点方向作为该段区间航行方向，基准方向与航行方向的夹角定义区间夹角，
    public int ifturn;
    public ENPoint(){}//空构造函数
    public String toString(){
        //DecimalFormat df = new DecimalFormat("0.000000");
        return this.id+"#"+this.pn+","+this.pe+","+this.angle+","+this.ifturn;
    }
    public String getTestString(){
        DecimalFormat df = new DecimalFormat("0.000000");
        return df.format(this.pn)+","+df.format(this.pe);
    }
    public String getResultString(){
        DecimalFormat df = new DecimalFormat("0.000000");
        return this.id+"#"+df.format(this.pn)+","+df.format(this.pe);
    }
    @Override
    public int compareTo(ENPoint other) {
        return Integer.compare(this.id, other.id);
    }
}