package com.credit.userms.controller;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@Endpoint(id="custom-actuator")
public class CustomActuatorEndpoints {

    @ReadOperation
    public String readOperation(){
        return "My custom actuator endpoint";
    }

    @WriteOperation
    public String writeOperation(@RequestBody String name){
        String cName=name;
        return "Got your name:"+cName;
    }
}
