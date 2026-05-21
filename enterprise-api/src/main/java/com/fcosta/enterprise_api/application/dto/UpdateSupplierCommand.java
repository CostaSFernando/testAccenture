package com.fcosta.enterprise_api.application.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateSupplierCommand(
        UUID id,
        String name,
        String email,
        String zipCode,
        String rg,
        LocalDate birthDate
) {
}
