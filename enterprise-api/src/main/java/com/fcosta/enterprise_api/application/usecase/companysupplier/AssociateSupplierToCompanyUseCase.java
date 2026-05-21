package com.fcosta.enterprise_api.application.usecase.companysupplier;

import com.fcosta.enterprise_api.application.dto.AssociateSupplierToCompanyCommand;
import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.CompanySupplierRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Company;
import com.fcosta.enterprise_api.domain.model.Supplier;
import com.fcosta.enterprise_api.domain.service.SupplierCompanyPolicy;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AssociateSupplierToCompanyUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;
    private final SupplierRepositoryPort supplierRepositoryPort;
    private final CompanySupplierRepositoryPort companySupplierRepositoryPort;
    private final SupplierCompanyPolicy supplierCompanyPolicy;

    public AssociateSupplierToCompanyUseCase(
            CompanyRepositoryPort companyRepositoryPort,
            SupplierRepositoryPort supplierRepositoryPort,
            CompanySupplierRepositoryPort companySupplierRepositoryPort
    ) {
        this.companyRepositoryPort = companyRepositoryPort;
        this.supplierRepositoryPort = supplierRepositoryPort;
        this.companySupplierRepositoryPort = companySupplierRepositoryPort;
        this.supplierCompanyPolicy = new SupplierCompanyPolicy();
    }

    @Transactional
    public void execute(AssociateSupplierToCompanyCommand command) {
        Company company = companyRepositoryPort.findById(command.companyId())
                .orElseThrow(() -> new ApplicationException("Company not found"));

        Supplier supplier = supplierRepositoryPort.findById(command.supplierId())
                .orElseThrow(() -> new ApplicationException("Supplier not found"));

        if (companySupplierRepositoryPort.existsAssociation(command.companyId(), command.supplierId())) {
            throw new ApplicationException("Supplier is already associated with this company");
        }

        supplierCompanyPolicy.validateAssociation(company, supplier);

        companySupplierRepositoryPort.associate(command.companyId(), command.supplierId());
    }
}
