package com.fcosta.enterprise_api.infrastructure.persistence.repository;

import com.fcosta.enterprise_api.infrastructure.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSupplierRepository extends JpaRepository<SupplierEntity, UUID>, JpaSpecificationExecutor<SupplierEntity> {

    Optional<SupplierEntity> findByDocument(String document);

    boolean existsByDocument(String document);
}
