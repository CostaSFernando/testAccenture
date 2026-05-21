package com.fcosta.enterprise_api.application.usecase.supplier;

import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSuppliersUseCase {

    private final SupplierRepositoryPort supplierRepositoryPort;

    public ListSuppliersUseCase(SupplierRepositoryPort supplierRepositoryPort) {
        this.supplierRepositoryPort = supplierRepositoryPort;
    }

    public List<Supplier> execute(String name, String document) {
        return supplierRepositoryPort.findAll(name, document);
    }
}
