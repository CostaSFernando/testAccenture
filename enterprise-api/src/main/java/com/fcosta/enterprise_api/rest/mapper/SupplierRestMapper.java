package com.fcosta.enterprise_api.rest.mapper;

import com.fcosta.enterprise_api.application.dto.CreateSupplierCommand;
import com.fcosta.enterprise_api.application.dto.UpdateSupplierCommand;
import com.fcosta.enterprise_api.domain.model.Supplier;
import com.fcosta.enterprise_api.rest.request.CreateSupplierRequest;
import com.fcosta.enterprise_api.rest.request.UpdateSupplierRequest;
import com.fcosta.enterprise_api.rest.response.SupplierResponse;

import java.util.UUID;

public class SupplierRestMapper {

    private SupplierRestMapper() {
    }

    public static CreateSupplierCommand toCommand(CreateSupplierRequest request) {
        return new CreateSupplierCommand(
                request.document(),
                request.documentType(),
                request.name(),
                request.email(),
                request.zipCode(),
                request.rg(),
                request.birthDate()
        );
    }

    public static UpdateSupplierCommand toCommand(UUID id, UpdateSupplierRequest request) {
        return new UpdateSupplierCommand(
                id,
                request.name(),
                request.email(),
                request.zipCode(),
                request.rg(),
                request.birthDate()
        );
    }

    public static SupplierResponse toResponse(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getDocument(),
                supplier.getDocumentType(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getZipCode(),
                supplier.getRg(),
                supplier.getBirthDate(),
                supplier.getCity(),
                supplier.getState()
        );
    }
}
