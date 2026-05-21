package com.fcosta.enterprise_api.rest.controller;

import com.fcosta.enterprise_api.application.usecase.supplier.CreateSupplierUseCase;
import com.fcosta.enterprise_api.application.usecase.supplier.DeleteSupplierUseCase;
import com.fcosta.enterprise_api.application.usecase.supplier.GetSupplierByIdUseCase;
import com.fcosta.enterprise_api.application.usecase.supplier.ListSuppliersUseCase;
import com.fcosta.enterprise_api.application.usecase.supplier.UpdateSupplierUseCase;
import com.fcosta.enterprise_api.rest.mapper.SupplierRestMapper;
import com.fcosta.enterprise_api.rest.request.CreateSupplierRequest;
import com.fcosta.enterprise_api.rest.request.UpdateSupplierRequest;
import com.fcosta.enterprise_api.rest.response.SupplierResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final CreateSupplierUseCase createSupplierUseCase;
    private final ListSuppliersUseCase listSuppliersUseCase;
    private final GetSupplierByIdUseCase getSupplierByIdUseCase;
    private final UpdateSupplierUseCase updateSupplierUseCase;
    private final DeleteSupplierUseCase deleteSupplierUseCase;

    public SupplierController(
            CreateSupplierUseCase createSupplierUseCase,
            ListSuppliersUseCase listSuppliersUseCase,
            GetSupplierByIdUseCase getSupplierByIdUseCase,
            UpdateSupplierUseCase updateSupplierUseCase,
            DeleteSupplierUseCase deleteSupplierUseCase
    ) {
        this.createSupplierUseCase = createSupplierUseCase;
        this.listSuppliersUseCase = listSuppliersUseCase;
        this.getSupplierByIdUseCase = getSupplierByIdUseCase;
        this.updateSupplierUseCase = updateSupplierUseCase;
        this.deleteSupplierUseCase = deleteSupplierUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SupplierResponse create(@Valid @RequestBody CreateSupplierRequest request) {
        var command = SupplierRestMapper.toCommand(request);
        var supplier = createSupplierUseCase.execute(command);

        return SupplierRestMapper.toResponse(supplier);
    }

    @GetMapping
    public List<SupplierResponse> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String document
    ) {
        return listSuppliersUseCase.execute(name, document)
                .stream()
                .map(SupplierRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public SupplierResponse findById(@PathVariable UUID id) {
        var supplier = getSupplierByIdUseCase.execute(id);

        return SupplierRestMapper.toResponse(supplier);
    }

    @PutMapping("/{id}")
    public SupplierResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSupplierRequest request
    ) {
        var command = SupplierRestMapper.toCommand(id, request);
        var supplier = updateSupplierUseCase.execute(command);

        return SupplierRestMapper.toResponse(supplier);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteSupplierUseCase.execute(id);
    }
}