package com.qcmoke.ums.controller;

import com.alibaba.fastjson.JSON;
import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author qcmoke
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/urlGetApi")
    public Result<Object> urlGetApi(Map<String, Object> data, String name, Integer age) {
        String paramsStr = JSON.toJSONString(data);
        String format = String.format("params=%s,name=%s,age=%s", paramsStr, name, age);
        log.info(format);
        return Result.ok(format);
    }

    @PostMapping("/jsonPostAPi")
    public Result<Object> jsonPostAPi(@RequestBody Map<String, Object> data, String name, Integer age) {
        String paramsStr = JSON.toJSONString(data);
        String format = String.format("params=%s,name=%s,age=%s", paramsStr, name, age);
        log.info(format);
        return Result.ok(format);
    }

    @PostMapping("/urlPostApi")
    public Result<Object> urlPostApi(Map<String, Object> data, String name, Integer age) {
        String paramsStr = JSON.toJSONString(data);
        String format = String.format("params=%s,name=%s,age=%s", paramsStr, name, age);
        log.info(format);
        return Result.ok(format);
    }

    @PostMapping("/formPostApi")
    public Result<Object> formPostApi(Map<String, Object> data, String name, Integer age, HttpServletRequest request) {
        System.out.println(JSON.toJSONString(request.getParameterMap()));
        String paramsStr = JSON.toJSONString(data);
        String format = String.format("params=%s,name=%s,age=%s", paramsStr, name, age);
        log.info(format);
        return Result.ok(format);
    }

    @PostMapping("/formDataPostApi")
    public Result<Object> formDataPostApi(Map<String, Object> data, String name, Integer age, List<MultipartFile> files) {
        String paramsStr = JSON.toJSONString(data);
        StringBuffer fileNames = new StringBuffer();
        files.forEach(file -> {
            fileNames.append(file.getOriginalFilename());
            fileNames.append(";");
        });
        String format = String.format("params=%s,name=%s,age=%s,fileNames=%s", paramsStr, name, age, fileNames);
        log.info(format);
        return Result.ok(format);
    }

}
