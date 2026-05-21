package com.fcosta.enterprise_api.rest.response;

import com.fcosta.enterprise_api.domain.model.DocumentType;

import java.time.LocalDate;
import java.util.UUID;

public record SupplierResponse(
        UUID id,
        String document,
        DocumentType documentType,
        String name,
        String email,
        String zipCode,
        String rg,
        LocalDate birthDate,
        String city,
        String state
) {
}
