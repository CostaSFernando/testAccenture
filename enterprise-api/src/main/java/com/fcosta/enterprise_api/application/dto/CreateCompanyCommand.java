package com.fcosta.enterprise_api.application.dto;

public record CreateCompanyCommand(
        String cnpj,
        String fantasyName,
        String zipCode
) {
}
