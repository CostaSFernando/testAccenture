package com.fcosta.enterprise_api.infrastructure.persistence.specification;

import com.fcosta.enterprise_api.infrastructure.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification {

    private SupplierSpecification() {
    }

    public static Specification<SupplierEntity> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<SupplierEntity> documentContains(String document) {
        return (root, query, criteriaBuilder) -> {
            if (document == null || document.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String normalizedDocument = document.replaceAll("\\D", "");

            return criteriaBuilder.like(
                    root.get("document"),
                    "%" + normalizedDocument + "%"
            );
        };
    }
}