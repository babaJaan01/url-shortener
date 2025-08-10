package com.shayaan.urlshortener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloWorldController {
    @GetMapping(path = "/hello")
    public String hello() {
        return "Hello World this is a test!";
    }
}
