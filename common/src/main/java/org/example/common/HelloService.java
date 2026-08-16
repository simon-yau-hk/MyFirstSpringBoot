package org.example.common;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String greet() {
        return "Hello from shared common module!";
    }

    public String greet(String name) {
        return "Hello, " + name + "! (from common)";
    }
}
