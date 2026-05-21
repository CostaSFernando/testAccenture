package com.fcosta.enterprise_api.infrastructure.persistence.mapper;

import com.fcosta.enterprise_api.domain.model.DocumentType;
import com.fcosta.enterprise_api.domain.model.Supplier;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.DocumentTypeEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.SupplierEntity;

public class SupplierPersistenceMapper {

    private SupplierPersistenceMapper() {
    }

    public static Supplier toDomain(SupplierEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Supplier(
                entity.getId(),
                entity.getDocument(),
                DocumentType.valueOf(entity.getDocumentType().name()),
                entity.getName(),
                entity.getEmail(),
                entity.getZipCode(),
                entity.getRg(),
                entity.getBirthDate(),
                entity.getCity(),
                entity.getState()
        );
    }

    public static SupplierEntity toEntity(Supplier domain) {
        if (domain == null) {
            return null;
        }

        return SupplierEntity.builder()
                .id(domain.getId())
                .document(domain.getDocument())
                .documentType(DocumentTypeEntity.valueOf(domain.getDocumentType().name()))
                .name(domain.getName())
                .email(domain.getEmail())
                .zipCode(domain.getZipCode())
                .rg(domain.getRg())
                .birthDate(domain.getBirthDate())
                .city(domain.getCity())
                .state(domain.getState())
                .build();
    }
}
