package com.alibaba.halo.controller;

import com.alibaba.biz.ExtensionRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContainerController {

    @RequestMapping(value = "/run/{extensionCode}", method = RequestMethod.GET)
    public String echo(@PathVariable String extensionCode) {
        Object result =  ExtensionRunner.runExtension(extensionCode);
        if(result !=null){
            return result.toString();
        }
        return null;
    }
}
