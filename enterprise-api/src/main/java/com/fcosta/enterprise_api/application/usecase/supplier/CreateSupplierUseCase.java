package com.fcosta.enterprise_api.application.usecase.supplier;

import com.fcosta.enterprise_api.application.dto.CreateSupplierCommand;
import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;
import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.ZipCodeGatewayPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import org.springframework.stereotype.Service;

@Service
public class CreateSupplierUseCase {

    private final SupplierRepositoryPort supplierRepositoryPort;
    private final ZipCodeGatewayPort zipCodeGatewayPort;

    public CreateSupplierUseCase(
            SupplierRepositoryPort supplierRepositoryPort,
            ZipCodeGatewayPort zipCodeGatewayPort
    ) {
        this.supplierRepositoryPort = supplierRepositoryPort;
        this.zipCodeGatewayPort = zipCodeGatewayPort;
    }

    public Supplier execute(CreateSupplierCommand command) {
        String normalizedDocument = normalize(command.document());

        if (supplierRepositoryPort.existsByDocument(normalizedDocument)) {
            throw new ApplicationException("Supplier already exists with this document");
        }

        ZipCodeInfo zipCodeInfo = zipCodeGatewayPort.findByZipCode(command.zipCode());

        Supplier supplier = new Supplier(
                null,
                normalizedDocument,
                command.documentType(),
                command.name(),
                command.email(),
                command.zipCode(),
                command.rg(),
                command.birthDate(),
                zipCodeInfo.city(),
                zipCodeInfo.state()
        );

        return supplierRepositoryPort.save(supplier);
    }

    private String normalize(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }
}
