package com.fcosta.enterprise_api.application.usecase.supplier;

import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetSupplierByIdUseCase {

    private final SupplierRepositoryPort supplierRepositoryPort;

    public GetSupplierByIdUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    public Supplier execute(UUID id) {
        return supplierRepositoryPort.findById(id)
                .orElseThrow(() -> new ApplicationException("Supplier not found"));
    }
}
