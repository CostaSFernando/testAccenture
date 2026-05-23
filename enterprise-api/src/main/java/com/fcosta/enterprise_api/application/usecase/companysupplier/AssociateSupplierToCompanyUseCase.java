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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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

        List<UUID> desiredSupplierIds = normalizeSupplierIds(command.supplierIds());

        List<UUID> currentSupplierIds = companySupplierRepositoryPort
                .findSupplierIdsByCompanyId(command.companyId());

        List<UUID> supplierIdsToRemove = currentSupplierIds.stream()
                .filter(currentId -> !desiredSupplierIds.contains(currentId))
                .toList();

        List<UUID> supplierIdsToAdd = desiredSupplierIds.stream()
                .filter(desiredId -> !currentSupplierIds.contains(desiredId))
                .toList();

        validateSuppliersToAdd(company, supplierIdsToAdd);

        companySupplierRepositoryPort.removeAssociations(
                command.companyId(),
                supplierIdsToRemove
        );

        companySupplierRepositoryPort.associateMany(
                command.companyId(),
                supplierIdsToAdd
        );
    }

    private List<UUID> normalizeSupplierIds(List<UUID> supplierIds) {
        if (supplierIds == null) {
            return List.of();
        }

        return new ArrayList<>(new HashSet<>(supplierIds));
    }

    private void validateSuppliersToAdd(
            Company company,
            List<UUID> supplierIdsToAdd
    ) {
        for (UUID supplierId : supplierIdsToAdd) {
            Supplier supplier = supplierRepositoryPort.findById(supplierId)
                    .orElseThrow(() -> new ApplicationException("Supplier not found: " + supplierId));

            supplierCompanyPolicy.validateAssociation(company, supplier);
        }
    }
}
