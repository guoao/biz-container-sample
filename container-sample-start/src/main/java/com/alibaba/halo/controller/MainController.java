package com.alibaba.halo.controller;

import com.alibaba.halo.edas.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    private EchoService echoService;

    @RequestMapping(value = "/echo/{id}", method = RequestMethod.GET)
    public String echo(@PathVariable String id) {
        return echoService.echo(id);
    }
}
