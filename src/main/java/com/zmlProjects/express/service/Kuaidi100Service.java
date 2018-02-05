package com.zmlProjects.express.service;

import com.zmlProjects.express.bean.KuaidiBean;

public interface Kuaidi100Service {
    public KuaidiBean queryOneExpress(String tiantiancom, String expNumber);

    public boolean queryOneState(KuaidiBean kuaidiBean);
}
