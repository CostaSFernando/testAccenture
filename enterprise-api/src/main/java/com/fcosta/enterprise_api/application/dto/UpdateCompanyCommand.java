package com.fcosta.enterprise_api.application.dto;

import java.util.UUID;

public record UpdateCompanyCommand(
        UUID id,
        String fantasyName,
        String zipCode
) {
}
