package com.fcosta.enterprise_api.rest.mapper;

import com.fcosta.enterprise_api.application.dto.AssociateSupplierToCompanyCommand;
import com.fcosta.enterprise_api.rest.request.AssociateSupplierToCompanyRequest;

import java.util.UUID;

public class CompanySupplierRestMapper {

    private CompanySupplierRestMapper() {
    }

    public static AssociateSupplierToCompanyCommand toCommand(
            UUID companyId,
            AssociateSupplierToCompanyRequest request
    ) {
        return new AssociateSupplierToCompanyCommand(
                companyId,
                request.supplierIds()
        );
    }
}
