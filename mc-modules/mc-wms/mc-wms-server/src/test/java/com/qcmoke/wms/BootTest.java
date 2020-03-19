package com.qcmoke.wms;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.wms.service.StockService;
import com.qcmoke.wms.vo.StockVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BootTest {
    @Autowired
    private StockService stockService;

    @Test
    public void test1() {
        IPage<StockVo> page = stockService.getPage(new Page<>(1, 5), new StockVo());
        log.info(JSONObject.toJSONString(page));
    }
}
