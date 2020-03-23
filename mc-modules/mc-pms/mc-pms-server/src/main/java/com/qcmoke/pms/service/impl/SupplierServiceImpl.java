package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.pms.entity.Supplier;
import com.qcmoke.pms.entity.SupplierMaterial;
import com.qcmoke.pms.mapper.SupplierMapper;
import com.qcmoke.pms.service.SupplierMaterialService;
import com.qcmoke.pms.service.SupplierService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Autowired
    private SupplierMaterialService supplierMaterialService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void remove(List<Long> idList) {
        boolean flag = this.removeByIds(idList);
        if (!flag) {
            throw new GlobalCommonException("删除供应商异常！");
        }
        int count = supplierMaterialService.count(new LambdaQueryWrapper<SupplierMaterial>().in(SupplierMaterial::getSupplierId, idList));
        if (count > 0) {
            flag = supplierMaterialService.remove(new LambdaQueryWrapper<SupplierMaterial>().in(SupplierMaterial::getSupplierId, idList));
            if (!flag) {
                throw new GlobalCommonException("删除供应商原有的物料记录异常！");
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdate(Supplier supplier, List<Long> materialIds) {
        Long supplierId = supplier.getSupplierId();
        boolean isUpdate = supplierId != null;
        if (isUpdate) {
            supplier.setModifyTime(new Date());
        } else {
            supplier.setCreateTime(new Date());
        }
        boolean flag = this.saveOrUpdate(supplier);
        if (!flag) {
            throw new GlobalCommonException("保持supplier异常！");
        }
        if (CollectionUtils.isNotEmpty(materialIds)) {
            int count = supplierMaterialService.count(new LambdaQueryWrapper<SupplierMaterial>().eq(SupplierMaterial::getSupplierId, supplierId));
            if (count > 0) {
                flag = supplierMaterialService.remove(new LambdaQueryWrapper<SupplierMaterial>().eq(SupplierMaterial::getSupplierId, supplierId));
                if (!flag) {
                    throw new GlobalCommonException("删除供应商原有的物料记录异常！");
                }
            }
            List<SupplierMaterial> supplierMaterialList = new ArrayList<>();
            materialIds.forEach(materialId -> {
                supplierMaterialList.add(
                        new SupplierMaterial()
                                .setSupplierId(supplierId)
                                .setMaterialId(materialId));
            });

            flag = supplierMaterialService.saveBatch(supplierMaterialList);
            if (!flag) {
                throw new GlobalCommonException("保持供应商的物料记录异常！");
            }
        }
    }
}
