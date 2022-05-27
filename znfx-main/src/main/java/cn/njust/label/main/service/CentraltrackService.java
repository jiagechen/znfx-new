package cn.njust.label.main.service;


import cn.njust.label.main.entity.Centraltrack;
import cn.njust.label.main.mapper.CentraltrackMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CentraltrackService {

    @Autowired
    private CentraltrackMapper centraltrackMapper;

    //根据起始经度模糊查询
    public Page<Centraltrack> selectPage(Integer pageNum, Integer pageSize, String search) {
        return centraltrackMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<Centraltrack>lambdaQuery().like(Centraltrack::getInitiaLatitude,search));
    }

    public int insert(Centraltrack centraltrack){

        return centraltrackMapper.insert(centraltrack);
    }

    public int update(Centraltrack centraltrack) {
        return centraltrackMapper.updateById(centraltrack);
    }

    public int deleteById(Integer id) {
        int res = centraltrackMapper.deleteById(id);
        return res;
    }
    /**
     * 导出文件，格式为xlsx
     *
     * @return 文件的字节流
     */
    public XSSFWorkbook exportShiptrack(){
        List<Centraltrack> list = centraltrackMapper.selectList(null);
        //创建工作普
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //获取样式
        XSSFCellStyle setBorder = xssfWorkbook.createCellStyle();
        setBorder.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        setBorder.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        //创建工作表
        XSSFSheet sheet = xssfWorkbook.createSheet();
        xssfWorkbook.setSheetName(0,"民航中心航线表");

        //创建表头
        XSSFRow head = sheet.createRow(0);
        String[] heads = {"routeID","initia_latitude","initia_longitude","target_latitude","target_longitude","width","height_left","height_right","MongoID"};
        for (int i = 0; i < heads.length; i++) {
            XSSFCell cell = head.createCell(i);
            cell.setCellValue(heads[i]);
        }
        for (int i = 1; i <= list.size(); i++) {
            Centraltrack centraltrack = list.get(i - 1);
            //创建行，从第二行开始，所有for循环的i从1开始取
            XSSFRow row = sheet.createRow(i);
            //创建单元格，开始填充数据
            XSSFCell cell = row.createCell(0);
            cell.setCellValue(centraltrack.getRouteID());
            cell = row.createCell(1);
            cell.setCellValue(centraltrack.getInitiaLatitude());
            cell = row.createCell(2);
            cell.setCellValue(centraltrack.getInitiaLongitude());
            cell = row.createCell(3);
            cell.setCellValue(centraltrack.getTargetLatitude());
            cell = row.createCell(4);
            cell.setCellValue(centraltrack.getTargetLongitude());
            cell = row.createCell(5);
            cell.setCellValue(centraltrack.getWidth());
            cell = row.createCell(6);
            cell.setCellValue(centraltrack.getHeightLeft());
            cell = row.createCell(7);
            cell.setCellValue(centraltrack.getHeightRight());
            cell = row.createCell(8);
            cell.setCellValue(centraltrack.getMongoID());
        }
        return xssfWorkbook;
    }
    //查询所有
    public List<Centraltrack> findAll(){
        return centraltrackMapper.selectList(null);
    }
}
