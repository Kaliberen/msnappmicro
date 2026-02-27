package org.example.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InstanceController {

    @GetMapping("/users/instance")
    public Map<String,String> instance(){
        String host = System.getenv().getOrDefault("HOSTNAME","unknown");
        return Map.of("instance",host);
    }

}
