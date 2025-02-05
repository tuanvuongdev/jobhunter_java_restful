package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() {
        return "Hello World (Hỏi Dân IT )";
    }

    @GetMapping("/123123")
    public String getHelloWorld123String() {
        return "Hello World (Hỏi Dân IT)";
    }
}
