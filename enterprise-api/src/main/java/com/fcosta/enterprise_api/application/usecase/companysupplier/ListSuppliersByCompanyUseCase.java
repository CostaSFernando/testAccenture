package com.fcosta.enterprise_api.application.usecase.companysupplier;

import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.CompanySupplierRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ListSuppliersByCompanyUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;
    private final CompanySupplierRepositoryPort companySupplierRepositoryPort;

    public ListSuppliersByCompanyUseCase(
            CompanyRepositoryPort companyRepositoryPort,
            CompanySupplierRepositoryPort companySupplierRepositoryPort
    ) {
        this.companyRepositoryPort = companyRepositoryPort;
        this.companySupplierRepositoryPort = companySupplierRepositoryPort;
    }

    @Transactional(readOnly = true)
    public List<Supplier> execute(UUID companyId) {
        if (!companyRepositoryPort.existsById(companyId)) {
            throw new ApplicationException("Company not found");
        }

        return companySupplierRepositoryPort.findSuppliersByCompanyId(companyId);
    }
}
