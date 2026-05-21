package com.fcosta.enterprise_api.application.usecase.company;

import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListCompaniesUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;

    public ListCompaniesUseCase(CompanyRepositoryPort companyRepositoryPort) {
        this.companyRepositoryPort = companyRepositoryPort;
    }

    public List<Company> execute() {
        return companyRepositoryPort.findAll();
    }
}
