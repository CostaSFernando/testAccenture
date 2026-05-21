package com.fcosta.enterprise_api.infrastructure.persistence.repository;

import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanySupplierEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanySupplierId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCompanySupplierRepository extends JpaRepository<CompanySupplierEntity, CompanySupplierId> {

    boolean existsByIdCompanyIdAndIdSupplierId(UUID companyId, UUID supplierId);

    void deleteByIdCompanyIdAndIdSupplierId(UUID companyId, UUID supplierId);

    List<CompanySupplierEntity> findByIdCompanyId(UUID companyId);
}