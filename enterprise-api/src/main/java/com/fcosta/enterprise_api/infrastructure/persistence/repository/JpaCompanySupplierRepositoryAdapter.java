package com.fcosta.enterprise_api.infrastructure.persistence.repository;

import com.fcosta.enterprise_api.application.port.out.CompanySupplierRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanySupplierEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanySupplierId;
import com.fcosta.enterprise_api.infrastructure.persistence.mapper.SupplierPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class JpaCompanySupplierRepositoryAdapter implements CompanySupplierRepositoryPort {

    private final SpringDataCompanyRepository companyRepository;
    private final SpringDataSupplierRepository supplierRepository;
    private final SpringDataCompanySupplierRepository companySupplierRepository;

    public JpaCompanySupplierRepositoryAdapter(
            SpringDataCompanyRepository companyRepository,
            SpringDataSupplierRepository supplierRepository,
            SpringDataCompanySupplierRepository companySupplierRepository
    ) {
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.companySupplierRepository = companySupplierRepository;
    }

    @Override
    public void associate(UUID companyId, UUID supplierId) {
        var company = companyRepository.getReferenceById(companyId);
        var supplier = supplierRepository.getReferenceById(supplierId);

        var id = new CompanySupplierId(companyId, supplierId);

        var entity = CompanySupplierEntity.builder()
                .id(id)
                .company(company)
                .supplier(supplier)
                .createdAt(LocalDateTime.now())
                .build();

        companySupplierRepository.save(entity);
    }

    @Override
    public void removeAssociation(UUID companyId, UUID supplierId) {
        companySupplierRepository.deleteByIdCompanyIdAndIdSupplierId(companyId, supplierId);
    }

    @Override
    public boolean existsAssociation(UUID companyId, UUID supplierId) {
        return companySupplierRepository.existsByIdCompanyIdAndIdSupplierId(companyId, supplierId);
    }

    @Override
    public List<Supplier> findSuppliersByCompanyId(UUID companyId) {
        return companySupplierRepository.findByIdCompanyId(companyId)
                .stream()
                .map(CompanySupplierEntity::getSupplier)
                .map(SupplierPersistenceMapper::toDomain)
                .toList();
    }
}
