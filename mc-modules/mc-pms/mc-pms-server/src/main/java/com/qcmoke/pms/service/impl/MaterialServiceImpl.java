package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.mapper.MaterialMapper;
import com.qcmoke.pms.mapper.SupplierMaterialMapper;
import com.qcmoke.pms.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    private SupplierMaterialMapper supplierMaterialMapper;

    @Override
    public List<Material> getAllBySupplierId(Long supplierId) {
        return supplierMaterialMapper.getMaterialListBySupplierId(supplierId);
    }
}
