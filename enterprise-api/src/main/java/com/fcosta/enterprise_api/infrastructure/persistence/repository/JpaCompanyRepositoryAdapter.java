package com.fcosta.enterprise_api.infrastructure.persistence.repository;

import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Company;
import com.fcosta.enterprise_api.infrastructure.persistence.entity.CompanyEntity;
import com.fcosta.enterprise_api.infrastructure.persistence.mapper.CompanyPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaCompanyRepositoryAdapter implements CompanyRepositoryPort {

    private final SpringDataCompanyRepository repository;

    public JpaCompanyRepositoryAdapter(SpringDataCompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Company save(Company company) {
        CompanyEntity entity = repository.findById(company.getId())
                .map(existingEntity -> {
                    existingEntity.setCnpj(company.getCnpj());
                    existingEntity.setFantasyName(company.getFantasyName());
                    existingEntity.setZipCode(company.getZipCode());
                    existingEntity.setCity(company.getCity());
                    existingEntity.setState(company.getState());

                    return existingEntity;
                })
                .orElseGet(() -> CompanyPersistenceMapper.toEntity(company));

        CompanyEntity savedEntity = repository.save(entity);

        return CompanyPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return repository.findById(id)
                .map(CompanyPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Company> findByCnpj(String cnpj) {
        return repository.findByCnpj(normalize(cnpj))
                .map(CompanyPersistenceMapper::toDomain);
    }

    @Override
    public List<Company> findAll() {
        return repository.findAll()
                .stream()
                .map(CompanyPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return repository.existsByCnpj(normalize(cnpj));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    private String normalize(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
