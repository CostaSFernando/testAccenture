package com.fcosta.enterprise_api.infrastructure.persistence.repository;

import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanySupplierEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanySupplierId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataCompanySupplierRepository extends JpaRepository<CompanySupplierEntity, CompanySupplierId> {

    boolean existsByIdCompanyIdAndIdSupplierId(UUID companyId, UUID supplierId);

    void deleteByIdCompanyIdAndIdSupplierId(UUID companyId, UUID supplierId);

    void deleteByIdCompanyIdAndIdSupplierIdIn(UUID companyId, List<UUID> supplierIds);

    @Query("""
        SELECT cs
        FROM CompanySupplierEntity cs
        JOIN FETCH cs.supplier
        WHERE cs.id.companyId = :companyId
    """)
    List<CompanySupplierEntity> findByCompanyIdWithSupplier(
            @Param("companyId") UUID companyId
    );

    @Query("""
        SELECT cs.id.supplierId
        FROM CompanySupplierEntity cs
        WHERE cs.id.companyId = :companyId
    """)
    List<UUID> findSupplierIdsByCompanyId(
            @Param("companyId") UUID companyId
    );
}
