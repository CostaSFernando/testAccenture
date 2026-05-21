package com.fcosta.enterprise_api.application.dto;

public record ZipCodeInfo(
        String zipCode,
        String street,
        String neighborhood,
        String city,
        String state
) {
}
