package cn.njust.label.main.service;


import cn.njust.label.main.entity.Shipcentraltrack;
import cn.njust.label.main.mapper.ShipcentraltrackMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipcentraltrackService {
    @Autowired
    private ShipcentraltrackMapper shipcentraltrackMapper;
    //根据起点维度进行模糊查询
    public Page<Shipcentraltrack> selectPage(Integer pageNum, Integer pageSize, String search) {
        return shipcentraltrackMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Shipcentraltrack>lambdaQuery().like(Shipcentraltrack::getInitiaLatitude,search));
    }


    public int insert(Shipcentraltrack shipcentraltrack){
        return shipcentraltrackMapper.insert(shipcentraltrack);
    }

    public int update(Shipcentraltrack shipcentraltrack) {
        return shipcentraltrackMapper.updateById(shipcentraltrack);
    }

    public int deleteById(Integer id) {
        int res = shipcentraltrackMapper.deleteById(id);
        return res;
    }
    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    public XSSFWorkbook exportShiptrack(){
        List<Shipcentraltrack> list = shipcentraltrackMapper.selectList(null);//查询所有
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"民船中心航线表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"route_id","initia_latitude","initia_longitude","target_latitude","target_longitude","width","mongo_id"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            Shipcentraltrack track = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(track.getRouteID());
            cell = row.createCell(1);
            cell.setCellValue(track.getInitiaLatitude());
            cell = row.createCell(2);
            cell.setCellValue(track.getInitiaLongitude());
            cell = row.createCell(3);
            cell.setCellValue(track.getTargetLatitude());
            cell = row.createCell(4);
            cell.setCellValue(track.getTargetLongitude());
            cell = row.createCell(5);
            cell.setCellValue(track.getWidth());
            cell = row.createCell(6);
            cell.setCellValue(track.getMongoid());
        }
        return xssfWorkbook;
    }
    public List<Shipcentraltrack> findAll(){//查询所有
        return shipcentraltrackMapper.selectList(null);
    }
}
