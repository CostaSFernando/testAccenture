package com.fcosta.enterprise_api.application.usecase.supplier;

import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteSupplierUseCase {

    private final SupplierRepositoryPort supplierRepositoryPort;

    public DeleteSupplierUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    public void execute(UUID id) {
        if (!supplierRepositoryPort.existsById(id)) {
            throw new ApplicationException("Supplier not found");
        }

        supplierRepositoryPort.deleteById(id);
    }
}
