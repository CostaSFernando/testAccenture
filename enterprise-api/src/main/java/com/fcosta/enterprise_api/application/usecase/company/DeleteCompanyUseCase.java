package com.fcosta.enterprise_api.application.usecase.company;

import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteCompanyUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;

    public DeleteCompanyUseCase(CompanyRepositoryPort companyRepositoryPort) {
        this.companyRepositoryPort = companyRepositoryPort;
    }

    public void execute(UUID id) {
        if (!companyRepositoryPort.existsById(id)) {
            throw new ApplicationException("Company not found");
        }

        companyRepositoryPort.deleteById(id);
    }
}
