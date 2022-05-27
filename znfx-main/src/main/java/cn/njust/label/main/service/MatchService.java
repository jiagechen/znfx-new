package cn.njust.label.main.service;

import cn.njust.label.main.dto.Rtra;
import cn.njust.label.main.entity.ENPoint;

import java.util.List;

public interface MatchService {
    //得到所有的匹配航线数据库中的数据:就是中心航线
    public List<Rtra> getAllCentralRoute();

    public Rtra getCentralRouteById(String lineId);

    public double[][] Match(List<ENPoint> unmatchedLines,List<Rtra> centralLines);
}
