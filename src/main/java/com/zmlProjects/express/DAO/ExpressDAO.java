package com.zmlProjects.express.DAO;

import com.zmlProjects.express.bean.ExpressBean;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ExpressDAO {
    int insert(ExpressBean expressBean);//新增需要根据主键判断，如果已经有了就不添加

    ExpressBean selectByNumber(String number);

    List<ExpressBean> queryAllExpress();

    void deleteExpress(ExpressBean expressBean);
}
