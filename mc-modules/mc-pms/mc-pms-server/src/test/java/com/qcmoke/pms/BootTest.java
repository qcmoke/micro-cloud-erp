package com.qcmoke.pms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.pms.client.MaterialStockClient;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BootTest {

    @Autowired
    private PurchaseOrderMasterService purchaseOrderMasterService;


    @Test
    public void test1() {
        System.out.println(purchaseOrderMasterService.getPage(new Page<PurchaseOrderMaster>(1, 5), null));
    }
}
