package com.fcosta.enterprise_api.application.port.out;

import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;

public interface ZipCodeGatewayPort {

    ZipCodeInfo findByZipCode(String zipCode);
}
