package com.qcmoke.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.pms.entity.Material;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface MaterialService extends IService<Material> {

    List<Material> getAllBySupplierId(Long supplierId);
}
