package com.fcosta.enterprise_api.rest.mapper;

import com.fcosta.enterprise_api.application.dto.CreateCompanyCommand;
import com.fcosta.enterprise_api.application.dto.UpdateCompanyCommand;
import com.fcosta.enterprise_api.domain.model.Company;
import com.fcosta.enterprise_api.rest.request.CreateCompanyRequest;
import com.fcosta.enterprise_api.rest.request.UpdateCompanyRequest;
import com.fcosta.enterprise_api.rest.response.CompanyResponse;

import java.util.UUID;

public class CompanyRestMapper {

    private CompanyRestMapper() {
    }

    public static CreateCompanyCommand toCommand(CreateCompanyRequest request) {
        return new CreateCompanyCommand(
                request.cnpj(),
                request.fantasyName(),
                request.zipCode()
        );
    }

    public static UpdateCompanyCommand toCommand(UUID id, UpdateCompanyRequest request) {
        return new UpdateCompanyCommand(
                id,
                request.fantasyName(),
                request.zipCode()
        );
    }

    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getCnpj(),
                company.getFantasyName(),
                company.getZipCode(),
                company.getCity(),
                company.getState()
        );
    }
}
