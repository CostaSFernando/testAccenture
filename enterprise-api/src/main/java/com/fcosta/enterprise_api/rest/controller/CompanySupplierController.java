package com.fcosta.enterprise_api.rest.controller;

import com.fcosta.enterprise_api.application.usecase.companysupplier.AssociateSupplierToCompanyUseCase;
import com.fcosta.enterprise_api.application.usecase.companysupplier.ListSuppliersByCompanyUseCase;
import com.fcosta.enterprise_api.application.usecase.companysupplier.RemoveSupplierFromCompanyUseCase;
import com.fcosta.enterprise_api.rest.mapper.CompanySupplierRestMapper;
import com.fcosta.enterprise_api.rest.mapper.SupplierRestMapper;
import com.fcosta.enterprise_api.rest.request.AssociateSupplierToCompanyRequest;
import com.fcosta.enterprise_api.rest.response.SupplierResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/companies/{companyId}/suppliers")
public class CompanySupplierController {

    private final AssociateSupplierToCompanyUseCase associateSupplierToCompanyUseCase;
    private final RemoveSupplierFromCompanyUseCase removeSupplierFromCompanyUseCase;
    private final ListSuppliersByCompanyUseCase listSuppliersByCompanyUseCase;

    public CompanySupplierController(
            AssociateSupplierToCompanyUseCase associateSupplierToCompanyUseCase,
            RemoveSupplierFromCompanyUseCase removeSupplierFromCompanyUseCase,
            ListSuppliersByCompanyUseCase listSuppliersByCompanyUseCase
    ) {
        this.associateSupplierToCompanyUseCase = associateSupplierToCompanyUseCase;
        this.removeSupplierFromCompanyUseCase = removeSupplierFromCompanyUseCase;
        this.listSuppliersByCompanyUseCase = listSuppliersByCompanyUseCase;
    }

    @GetMapping
    public List<SupplierResponse> findSuppliersByCompany(@PathVariable UUID companyId) {
        return listSuppliersByCompanyUseCase.execute(companyId)
                .stream()
                .map(SupplierRestMapper::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateSupplier(
            @PathVariable UUID companyId,
            @Valid @RequestBody AssociateSupplierToCompanyRequest request
    ) {
        var command = CompanySupplierRestMapper.toCommand(companyId, request);

        associateSupplierToCompanyUseCase.execute(command);
    }

    @DeleteMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSupplier(
            @PathVariable UUID companyId,
            @PathVariable UUID supplierId
    ) {
        removeSupplierFromCompanyUseCase.execute(companyId, supplierId);
    }
}
