package com.qcmoke.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.wms.entity.ProductStock;
import com.qcmoke.wms.mapper.ProductStockMapper;
import com.qcmoke.wms.service.ProductStockService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Service
public class ProductStockServiceImpl extends ServiceImpl<ProductStockMapper, ProductStock> implements ProductStockService {

}
