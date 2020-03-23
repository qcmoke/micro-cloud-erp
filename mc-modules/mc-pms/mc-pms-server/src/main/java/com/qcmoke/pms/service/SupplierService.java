package com.qcmoke.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.pms.entity.Supplier;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface SupplierService extends IService<Supplier> {
    void saveOrUpdate(Supplier supplier, List<Long> materialIds);

    void remove(List<Long> idList);
}
