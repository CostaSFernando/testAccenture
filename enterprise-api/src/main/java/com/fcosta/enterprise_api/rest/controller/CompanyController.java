package com.fcosta.enterprise_api.rest.controller;

import com.fcosta.enterprise_api.application.usecase.company.CreateCompanyUseCase;
import com.fcosta.enterprise_api.application.usecase.company.DeleteCompanyUseCase;
import com.fcosta.enterprise_api.application.usecase.company.GetCompanyByIdUseCase;
import com.fcosta.enterprise_api.application.usecase.company.ListCompaniesUseCase;
import com.fcosta.enterprise_api.application.usecase.company.UpdateCompanyUseCase;
import com.fcosta.enterprise_api.rest.mapper.CompanyRestMapper;
import com.fcosta.enterprise_api.rest.request.CreateCompanyRequest;
import com.fcosta.enterprise_api.rest.request.UpdateCompanyRequest;
import com.fcosta.enterprise_api.rest.response.CompanyResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CreateCompanyUseCase createCompanyUseCase;
    private final ListCompaniesUseCase listCompaniesUseCase;
    private final GetCompanyByIdUseCase getCompanyByIdUseCase;
    private final UpdateCompanyUseCase updateCompanyUseCase;
    private final DeleteCompanyUseCase deleteCompanyUseCase;

    public CompanyController(
            CreateCompanyUseCase createCompanyUseCase,
            ListCompaniesUseCase listCompaniesUseCase,
            GetCompanyByIdUseCase getCompanyByIdUseCase,
            UpdateCompanyUseCase updateCompanyUseCase,
            DeleteCompanyUseCase deleteCompanyUseCase
    ) {
        this.createCompanyUseCase = createCompanyUseCase;
        this.listCompaniesUseCase = listCompaniesUseCase;
        this.getCompanyByIdUseCase = getCompanyByIdUseCase;
        this.updateCompanyUseCase = updateCompanyUseCase;
        this.deleteCompanyUseCase = deleteCompanyUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@Valid @RequestBody CreateCompanyRequest request) {
        var command = CompanyRestMapper.toCommand(request);
        var company = createCompanyUseCase.execute(command);

        return CompanyRestMapper.toResponse(company);
    }

    @GetMapping
    public List<CompanyResponse> findAll() {
        return listCompaniesUseCase.execute()
                .stream()
                .map(CompanyRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CompanyResponse findById(@PathVariable UUID id) {
        var company = getCompanyByIdUseCase.execute(id);

        return CompanyRestMapper.toResponse(company);
    }

    @PutMapping("/{id}")
    public CompanyResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCompanyRequest request
    ) {
        var command = CompanyRestMapper.toCommand(id, request);
        var company = updateCompanyUseCase.execute(command);

        return CompanyRestMapper.toResponse(company);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteCompanyUseCase.execute(id);
    }
}
