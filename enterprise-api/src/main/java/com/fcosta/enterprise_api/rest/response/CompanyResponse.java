package com.fcosta.enterprise_api.rest.response;

import java.util.UUID;

public record CompanyResponse(
        UUID id,
        String cnpj,
        String fantasyName,
        String zipCode,
        String city,
        String state
) {
}
