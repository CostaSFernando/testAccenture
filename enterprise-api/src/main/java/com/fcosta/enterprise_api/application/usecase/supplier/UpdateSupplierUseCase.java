package com.fcosta.enterprise_api.application.usecase.supplier;

import com.fcosta.enterprise_api.application.dto.UpdateSupplierCommand;
import com.fcosta.enterprise_api.application.dto.ZipCodeInfo;
import com.fcosta.enterprise_api.application.exception.ApplicationException;
import com.fcosta.enterprise_api.application.port.out.SupplierRepositoryPort;
import com.fcosta.enterprise_api.application.port.out.ZipCodeGatewayPort;
import com.fcosta.enterprise_api.domain.model.Supplier;
import org.springframework.stereotype.Service;

@Service
public class UpdateSupplierUseCase {

    private final SupplierRepositoryPort supplierRepositoryPort;
    private final ZipCodeGatewayPort zipCodeGatewayPort;

    public UpdateSupplierUseCase(
            SupplierRepositoryPort supplierRepositoryPort,
            ZipCodeGatewayPort zipCodeGatewayPort
    ) {
        this.supplierRepositoryPort = supplierRepositoryPort;
        this.zipCodeGatewayPort = zipCodeGatewayPort;
    }

    public Supplier execute(UpdateSupplierCommand command) {
        Supplier supplier = supplierRepositoryPort.findById(command.id())
                .orElseThrow(() -> new ApplicationException("Supplier not found"));

        ZipCodeInfo zipCodeInfo = zipCodeGatewayPort.findByZipCode(command.zipCode());

        supplier.update(
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
}
