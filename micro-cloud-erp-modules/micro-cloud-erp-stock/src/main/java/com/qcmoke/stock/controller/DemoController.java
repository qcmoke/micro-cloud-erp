package com.qcmoke.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/stock")
@RestController
public class DemoController {

    @GetMapping("/demo")
    public Object demo() {
        return "demo";
    }

}
