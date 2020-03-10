package com.qcmoke.oms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.oms.entity.Product;
import com.qcmoke.oms.mapper.ProductMapper;
import com.qcmoke.oms.service.ProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
