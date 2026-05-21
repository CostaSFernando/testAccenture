package com.fcosta.enterprise_api.infrastructure.persistence.mapper;

import com.fcosta.enterprise_api.domain.model.Company;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanyEntity;

public class CompanyPersistenceMapper {

    private CompanyPersistenceMapper() {
    }

    public static Company toDomain(CompanyEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Company(
                entity.getId(),
                entity.getCnpj(),
                entity.getFantasyName(),
                entity.getZipCode(),
                entity.getCity(),
                entity.getState()
        );
    }

    public static CompanyEntity toEntity(Company domain) {
        if (domain == null) {
            return null;
        }

        return CompanyEntity.builder()
                .id(domain.getId())
                .cnpj(domain.getCnpj())
                .fantasyName(domain.getFantasyName())
                .zipCode(domain.getZipCode())
                .city(domain.getCity())
                .state(domain.getState())
                .build();
    }
}
