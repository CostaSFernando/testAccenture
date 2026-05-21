package com.fcosta.enterprise_api.application.usecase.companysupplier;

import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanySupplierRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveSupplierFromCompanyUseCase {

    private final CompanySupplierRepositoryPort companySupplierRepositoryPort;

    public RemoveSupplierFromCompanyUseCase(
            CompanySupplierRepositoryPort companySupplierRepositoryPort
    ) {
        this.companySupplierRepositoryPort = companySupplierRepositoryPort;
    }

    @Transactional
    public void execute(UUID companyId, UUID supplierId) {
        if (!companySupplierRepositoryPort.existsAssociation(companyId, supplierId)) {
            throw new ApplicationException("Association between company and supplier not found");
        }

        companySupplierRepositoryPort.removeAssociation(companyId, supplierId);
    }
}
