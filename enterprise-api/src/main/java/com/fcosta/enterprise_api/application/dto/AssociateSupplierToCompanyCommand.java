package com.fcosta.enterprise_api.application.dto;

import java.util.List;
import java.util.UUID;

public record AssociateSupplierToCompanyCommand(
        UUID companyId,
        List<UUID> supplierIds
) {
}
