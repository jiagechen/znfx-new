package cn.njust.label.main.mapper;

import cn.njust.label.main.dto.FileTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface FileTbMapper extends BaseMapper<FileTb> {
    void UpdateFile(@Param("fileTb") FileTb fileTb);
    Integer isExist(@Param("key") String key);
    FileTb selectLatestIndex(@Param("key") String key);
    void deleteAll();
    void delByFkey(String fkey);
}
