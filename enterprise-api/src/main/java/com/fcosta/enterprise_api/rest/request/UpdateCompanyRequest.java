package com.fcosta.enterprise_api.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(

        @NotBlank(message = "Fantasy name is required")
        String fantasyName,

        @NotBlank(message = "Zip code is required")
        @Size(min = 8, max = 9, message = "Zip code must have a valid size")
        String zipCode
) {
}
