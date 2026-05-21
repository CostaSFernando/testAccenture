package com.fcosta.enterprise_api.rest.request;

import com.fcosta.enterprise_api.domain.model.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateSupplierRequest(

        @NotBlank(message = "Document is required")
        @Size(min = 11, max = 18, message = "Document must have a valid size")
        String document,

        @NotNull(message = "Document type is required")
        DocumentType documentType,

        @NotBlank(message = "Supplier name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Zip code is required")
        @Size(min = 8, max = 9, message = "Zip code must have a valid size")
        String zipCode,

        String rg,

        LocalDate birthDate
) {
}
