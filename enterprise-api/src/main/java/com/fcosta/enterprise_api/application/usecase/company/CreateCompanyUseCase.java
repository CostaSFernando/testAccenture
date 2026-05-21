package com.fcosta.enterprise_api.application.usecase.company;

import com.fcosta.enterprise_api.application.dto.CreateCompanyCommand;
import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;
import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.CompanyRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.ZipCodeGatewayPort;
import com.fcosta.enterprise_api.domain.model.Company;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    private final CompanyRepositoryPort companyRepositoryPort;
    private final ZipCodeGatewayPort zipCodeGatewayPort;

    public CreateCompanyUseCase(
            CompanyRepositoryPort companyRepositoryPort,
            ZipCodeGatewayPort zipCodeGatewayPort
    ) {
        this.companyRepositoryPort = companyRepositoryPort;
        this.zipCodeGatewayPort = zipCodeGatewayPort;
    }

    public Company execute(CreateCompanyCommand command) {
        String normalizedCnpj = normalize(command.cnpj());

        if (companyRepositoryPort.existsByCnpj(normalizedCnpj)) {
            throw new ApplicationException("Company already exists with this CNPJ");
        }

        ZipCodeInfo zipCodeInfo = zipCodeGatewayPort.findByZipCode(command.zipCode());

        return companyRepositoryPort.save(
                new Company(
                        null,
                        normalizedCnpj,
                        command.fantasyName(),
                        command.zipCode(),
                        zipCodeInfo.city(),
                        zipCodeInfo.state()
                )
        );
    }

    private String normalize(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
