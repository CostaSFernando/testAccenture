package com.fcosta.enterprise_api.application.port.out;

import com.fcosta.enterprise_api.domain.model.Supplier;

import java.util.List;
import java.util.UUID;

public interface CompanySupplierRepositoryPort {

    void associate(UUID companyId, UUID supplierId);

    void removeAssociation(UUID companyId, UUID supplierId);

    boolean existsAssociation(UUID companyId, UUID supplierId);

    List<Supplier> findSuppliersByCompanyId(UUID companyId);
}
