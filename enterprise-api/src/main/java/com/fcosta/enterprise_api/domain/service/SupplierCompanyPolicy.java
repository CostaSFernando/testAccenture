package com.fcosta.enterprise_api.domain.service;

import com.fcosta.enterprise_api.domain.exception.DomainException;
import com.fcosta.enterprise_api.domain.model.Company;
import com.fcosta.enterprise_api.domain.model.Supplier;

public class SupplierCompanyPolicy {

    public void validateAssociation(Company company, Supplier supplier) {
        if (company == null) {
            throw new DomainException("Company is required");
        }

        if (supplier == null) {
            throw new DomainException("Supplier is required");
        }

        if (company.isFromParana()
                && supplier.isIndividual()
                && supplier.isUnderAge()) {
            throw new DomainException(
                    "Individual underage suppliers cannot be associated with companies from Paraná"
            );
        }
    }
}
