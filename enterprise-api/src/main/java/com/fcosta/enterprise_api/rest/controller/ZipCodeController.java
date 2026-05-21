package com.fcosta.enterprise_api.rest.controller;

import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;
import com.fcosta.enterprise_api.application.port.out.ZipCodeGatewayPort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zip-codes")
public class ZipCodeController {

    private final ZipCodeGatewayPort zipCodeGatewayPort;

    public ZipCodeController(ZipCodeGatewayPort zipCodeGatewayPort) {
        this.zipCodeGatewayPort = zipCodeGatewayPort;
    }

    @GetMapping("/{zipCode}")
    public ZipCodeInfo findByZipCode(@PathVariable String zipCode) {
        return zipCodeGatewayPort.findByZipCode(zipCode);
    }
}