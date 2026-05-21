package com.fcosta.enterprise_api.application.usecase.company;

import com.fcosta.enterprise_api.application.dto.UpdateCompanyCommand;
import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;
import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.ZipCodeGatewayPort;
import com.fcosta.enterprise_api.domain.model.Company;
import org.springframework.stereotype.Service;

@Service
public class UpdateCompanyUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;
    private final ZipCodeGatewayPort zipCodeGatewayPort;

    public UpdateCompanyUseCase(
            CompanyRepositoryPort companyRepositoryPort,
            ZipCodeGatewayPort zipCodeGatewayPort
    ) {
        this.companyRepositoryPort = companyRepositoryPort;
        this.zipCodeGatewayPort = zipCodeGatewayPort;
    }

    public Company execute(UpdateCompanyCommand command) {
        Company company = companyRepositoryPort.findById(command.id())
                .orElseThrow(() -> new ApplicationException("Company not found"));

        ZipCodeInfo zipCodeInfo = zipCodeGatewayPort.findByZipCode(command.zipCode());

        company.update(
                command.fantasyName(),
                command.zipCode(),
                zipCodeInfo.city(),
                zipCodeInfo.state()
        );

        return companyRepositoryPort.save(company);
    }
}
