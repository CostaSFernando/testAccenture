package com.fcosta.enterprise_api.rest.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssociateSupplierToCompanyRequest(

        @NotNull(message = "Supplier id is required")
        UUID supplierId
) {
}
