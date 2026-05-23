package com.fcosta.enterprise_api.application.port.out;

import com.fcosta.enterprise_api.domain.model.Supplier;

import java.util.List;
import java.util.UUID;

public interface CompanySupplierRepositoryPort {

    void associate(UUID companyId, UUID supplierId);

    void associateMany(UUID companyId, List<UUID> supplierIds);

    void removeAssociation(UUID companyId, UUID supplierId);

    void removeAssociations(UUID companyId, List<UUID> supplierIds);

    boolean existsAssociation(UUID companyId, UUID supplierId);

    List<Supplier> findSuppliersByCompanyId(UUID companyId);

    List<UUID> findSupplierIdsByCompanyId(UUID companyId);
}
