package com.fcosta.enterprise_api.application.usecase.company;

import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Company;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCompanyByIdUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;

    public GetCompanyByIdUseCase(CompanyRepositoryPort companyRepositoryPort) {
        this.companyRepositoryPort = companyRepositoryPort;
    }

    public Company execute(UUID id) {
        return companyRepositoryPort.findById(id)
                .orElseThrow(() -> new ApplicationException("Company not found"));
    }
}
