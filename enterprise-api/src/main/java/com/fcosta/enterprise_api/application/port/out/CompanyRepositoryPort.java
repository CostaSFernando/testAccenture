package com.fcosta.enterprise_api.application.port.out;

import com.fcosta.enterprise_api.domain.model.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepositoryPort {

    Company save(Company company);

    Optional<Company> findById(UUID id);

    Optional<Company> findByCnpj(String cnpj);

    List<Company> findAll();

    boolean existsByCnpj(String cnpj);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}