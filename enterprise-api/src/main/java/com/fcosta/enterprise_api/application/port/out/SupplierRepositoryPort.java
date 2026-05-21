package com.fcosta.enterprise_api.application.port.out;

import com.fcosta.enterprise_api.domain.model.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupplierRepositoryPort {

    Supplier save(Supplier supplier);

    Optional<Supplier> findById(UUID id);

    Optional<Supplier> findByDocument(String document);

    List<Supplier> findAll(String name, String document);

    boolean existsByDocument(String document);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
