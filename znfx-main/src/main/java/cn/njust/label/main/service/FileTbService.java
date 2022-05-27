package cn.njust.label.main.service;

import cn.njust.label.main.dto.FileTb;
import cn.njust.label.main.mapper.FileTbMapper;
import cn.njust.label.main.utils.FileConstance;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class FileTbService {
    @org.springframework.beans.factory.annotation.Autowired(required=true)
    private FileTbMapper fileTbMapper;

    public void saveFile(FileTb fileTb) {
        fileTbMapper.insert(fileTb);
    }

    public void UpdateFile(FileTb fileTb) {
        fileTbMapper.UpdateFile(fileTb);
    }

    public boolean isNotExist(String key){
        Integer id = fileTbMapper.isExist(key);
        if (ObjectUtils.isEmpty(id)) {
            return true;
        }
        return false;
    }
    public FileTb selectLatestIndex(String key) {
        FileTb fileTb = fileTbMapper.selectLatestIndex(key);
        if (ObjectUtils.isEmpty(fileTb)) {
            fileTb = FileTb.builder().fKey(key).fIndex(-1).fName("").build();
        }else {
            fileTb.setFName(FileConstance.ACCESS_PATH+fileTb.getFName());
        }
        return fileTb;
    }
    public void deleteAll(){
        fileTbMapper.deleteAll();
    }
    public void delByFkey(String fkey){
        fileTbMapper.delByFkey(fkey);
    }
}
