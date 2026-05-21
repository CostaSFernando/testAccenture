package com.fcosta.enterprise_api.application.dto;

import java.util.UUID;

public record AssociateSupplierToCompanyCommand(
        UUID companyId,
        UUID supplierId
) {
}
