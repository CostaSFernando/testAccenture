package com.fcosta.enterprise_api.application.dto;

import com.fcosta.enterprise_api.domain.model.DocumentType;

import java.time.LocalDate;

public record CreateSupplierCommand(
        String document,
        DocumentType documentType,
        String name,
        String email,
        String zipCode,
        String rg,
        LocalDate birthDate
) {
}
