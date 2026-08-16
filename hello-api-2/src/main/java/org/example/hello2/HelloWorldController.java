package org.example.hello2;

import org.example.common.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    private final HelloService helloService;

    public HelloWorldController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/")
    public String root() {
        return helloService.greet() + " This is hello-api-2 on port 8082.";
    }

    @GetMapping("/hello")
    public String hello() {
        return helloService.greet("World");
    }

    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return helloService.greet(name);
    }
}
