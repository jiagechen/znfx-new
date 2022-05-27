package cn.njust.label.main.service;


import cn.njust.label.main.entity.Matchtrack;
import cn.njust.label.main.mapper.MatchtrackMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchtrackService {
    @Autowired
    private MatchtrackMapper matchtrackMapper;
    //根据起点维度进行模糊查询
    public Page<Matchtrack> selectPage(Integer pageNum, Integer pageSize, String search) {
        return matchtrackMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Matchtrack>lambdaQuery().like(Matchtrack::getInitiaLatitude,search));
    }


    public int insert(Matchtrack matchtrack){
        return matchtrackMapper.insert(matchtrack);
    }

    public int update(Matchtrack matchtrack) {
        return matchtrackMapper.updateById(matchtrack);
    }

    public int deleteById(String id) {
        int res = matchtrackMapper.deleteById(id);
        return res;
    }
    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    public XSSFWorkbook exportShiptrack(){
        List<Matchtrack> list = matchtrackMapper.selectList(null);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"航线匹配表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"routeID","Initia_latitude","Initia_longitude","Angel"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            Matchtrack matchtrack = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(matchtrack.getRouteID());
            cell = row.createCell(1);
            cell.setCellValue(matchtrack.getInitiaLatitude());
            cell = row.createCell(2);
            cell.setCellValue(matchtrack.getInitiaLongitude());
            cell = row.createCell(3);
            cell.setCellValue(matchtrack.getAngel());
        }
        return xssfWorkbook;
    }
    public List<Matchtrack> findAll(){//查询所有
        return matchtrackMapper.selectList(null);
    }
}
