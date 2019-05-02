package com.chpengzh.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

/**
 * @author chpengzh.
 * @since 2019-05-01
 */
@SpringBootApplication
public class Application {

    @RestController
    public static class DemoController {

        @GetMapping("/")
        public String sayHello(String name) {
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            Stream.of(stack)
                .forEach(System.out::println);
            return String.format("Hello, %s", name);
        }

        @GetMapping("/abort")
        public String doAbort() {
            throw new IllegalStateException("啊,我死了!!!");
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
