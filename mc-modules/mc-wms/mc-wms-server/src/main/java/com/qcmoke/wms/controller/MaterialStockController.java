package com.qcmoke.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.dto.MaterialStockDto;
import com.qcmoke.wms.entity.MaterialStock;
import com.qcmoke.wms.service.MaterialStockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Slf4j
@RestController
@RequestMapping("/materialStock")
public class MaterialStockController {

    @Autowired
    private MaterialStockService materialStockService;

//    @PostMapping
//    @Transactional(rollbackFor = Exception.class)
//    public Result<Boolean> addMaterial2(List<MaterialStockDto> materialStockDtoList) {
//        List<Long> materialIdList = materialStockDtoList.stream().map(MaterialStockDto::getMaterialId).collect(Collectors.toList());
//        List<MaterialStock> existedMaterialStockList = materialStockService.list(new LambdaQueryWrapper<MaterialStock>().in(MaterialStock::getMaterialId, materialIdList));
//        //已经存在的
//        List<Long> existedMaterialIdList = existedMaterialStockList.stream().map(MaterialStock::getMaterialId).collect(Collectors.toList());
//
//        //对已经存在的进行修改
//        materialStockDtoList.forEach(materialStockDto -> {
//            Long materialId = materialStockDto.getMaterialId();
//            if (existedMaterialIdList.contains(materialId)) {
//                existedMaterialStockList.forEach(existedMaterialStock -> {
//                    if (existedMaterialStock.getMaterialId().equals(materialId)) {
//                        existedMaterialStock.setModifyTime(new Date());
//                        existedMaterialStock.setCount(existedMaterialStock.getCount() + materialStockDto.getCount());
//                    }
//                });
//            }
//        });
//        boolean flag = materialStockService.updateBatchById(existedMaterialStockList);
//        if (!flag) {
//            return Result.error();
//        }
//        List<MaterialStock> newMaterialStocks = new ArrayList<>();
//        materialStockDtoList.forEach(materialStockDto -> {
//            if (existedMaterialIdList.contains(materialStockDto.getMaterialId())) {
//                return;
//            }
//            MaterialStock materialStockNew = new MaterialStock();
//            materialStockNew.setMaterialId(materialStockDto.getMaterialId());
//            materialStockNew.setMaterialName(materialStockDto.getMaterialName());
//            materialStockNew.setCount(materialStockDto.getCount());
//            materialStockNew.setCreateTime(new Date());
//            newMaterialStocks.add(materialStockNew);
//        });
//        flag = materialStockService.saveBatch(newMaterialStocks);
//
//        return flag ? Result.ok() : Result.error();
//    }


    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<?> addMaterial(@RequestBody List<MaterialStockDto> materialStockDtoList) {
        List<Long> materialIdList = materialStockDtoList.stream().map(MaterialStockDto::getMaterialId).collect(Collectors.toList());
        List<MaterialStock> newStockMaterialStocks = new ArrayList<>();
        List<MaterialStock> existedMaterialStockIdList = materialStockService.list(new LambdaQueryWrapper<MaterialStock>().in(MaterialStock::getMaterialId, materialIdList));
        materialStockDtoList.forEach(materialStockDto -> {
            MaterialStock materialStockNew = new MaterialStock();
            materialStockNew.setMaterialId(materialStockDto.getMaterialId());
            materialStockNew.setMaterialName(materialStockDto.getMaterialName());
            List<MaterialStock> existedMaterialStockList = existedMaterialStockIdList.stream().filter(materialStock -> materialStock.getMaterialId().equals(materialStockDto.getMaterialId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(existedMaterialStockList)) {
                MaterialStock existedMaterialStock = existedMaterialStockList.get(0);
                if (existedMaterialStock.getMaterielStockId() != null) {
                    materialStockNew.setMaterielStockId(existedMaterialStock.getMaterielStockId());
                    materialStockNew.setCount(existedMaterialStock.getCount() + materialStockDto.getCount());
                    materialStockNew.setModifyTime(new Date());
                }
            } else {
                materialStockNew.setCreateTime(new Date());
                materialStockNew.setCount(materialStockDto.getCount());
            }
            newStockMaterialStocks.add(materialStockNew);
        });
        //有id则修改，无id则新增
        boolean flag = materialStockService.saveOrUpdateBatch(newStockMaterialStocks);
        return flag ? Result.ok() : Result.error();
    }
}