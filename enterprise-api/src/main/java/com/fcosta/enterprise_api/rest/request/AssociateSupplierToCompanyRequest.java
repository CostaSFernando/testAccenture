package com.fcosta.enterprise_api.rest.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AssociateSupplierToCompanyRequest(

        @NotEmpty(message = "Supplier ids are required")
        List<@NotNull(message = "Supplier id cannot be null") UUID> supplierIds
) {
}
