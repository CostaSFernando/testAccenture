package com.fcosta.enterprise_api.infrastructure.persistence.repository;

import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.DocumentTypeEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.SupplierEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.mapper.SupplierPersistenceMapper;
import com.fcosta.enterprise_api.infrastructure.persistence.specification.SupplierSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaSupplierRepositoryAdapter implements SupplierRepositoryPort {

    private final SpringDataSupplierRepository repository;

    public JpaSupplierRepositoryAdapter(SpringDataSupplierRepository repository) {
        this.repository = repository;
    }

    @Override
    public Supplier save(Supplier supplier) {
        SupplierEntity entity = repository.findById(supplier.getId())
                .map(existingEntity -> {
                    existingEntity.setDocument(supplier.getDocument());
                    existingEntity.setDocumentType(DocumentTypeEntity.valueOf(supplier.getDocumentType().name()));
                    existingEntity.setName(supplier.getName());
                    existingEntity.setEmail(supplier.getEmail());
                    existingEntity.setZipCode(supplier.getZipCode());
                    existingEntity.setRg(supplier.getRg());
                    existingEntity.setBirthDate(supplier.getBirthDate());
                    existingEntity.setCity(supplier.getCity());
                    existingEntity.setState(supplier.getState());

                    return existingEntity;
                })
                .orElseGet(() -> SupplierPersistenceMapper.toEntity(supplier));

        SupplierEntity savedEntity = repository.save(entity);

        return SupplierPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Supplier> findById(UUID id) {
        return repository.findById(id)
                .map(SupplierPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Supplier> findByDocument(String document) {
        return repository.findByDocument(normalize(document))
                .map(SupplierPersistenceMapper::toDomain);
    }

    @Override
    public List<Supplier> findAll(String name, String document) {
        Specification<SupplierEntity> specification = Specification
            .where(SupplierSpecification.nameContains(name))
            .and(SupplierSpecification.documentContains(document));

        return repository.findAll(specification)
                .stream()
                .map(SupplierPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByDocument(String document) {
        return repository.existsByDocument(normalize(document));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    private String normalize(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
