package com.fcosta.enterprise_api.domain.model;

import com.fcosta.enterprise_api.domain.exception.DomainException;

import java.util.UUID;

public class Company {

    private final UUID id;
    private final String cnpj;
    private String fantasyName;
    private String zipCode;
    private String city;
    private String state;

    public Company(
            UUID id,
            String cnpj,
            String fantasyName,
            String zipCode,
            String city,
            String state
    ) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new DomainException("CNPJ is required");
        }

        if (fantasyName == null || fantasyName.isBlank()) {
            throw new DomainException("Fantasy name is required");
        }

        if (zipCode == null || zipCode.isBlank()) {
            throw new DomainException("Zip code is required");
        }

        this.id = id == null ? UUID.randomUUID() : id;
        this.cnpj = normalizeOnlyNumbers(cnpj);
        this.fantasyName = fantasyName.trim();
        this.zipCode = normalizeOnlyNumbers(zipCode);
        this.city = normalizeText(city);
        this.state = normalizeState(state);
    }

    public void update(
            String fantasyName,
            String zipCode,
            String city,
            String state
    ) {
        if (fantasyName == null || fantasyName.isBlank()) {
            throw new DomainException("Fantasy name is required");
        }

        if (zipCode == null || zipCode.isBlank()) {
            throw new DomainException("Zip code is required");
        }

        this.fantasyName = fantasyName.trim();
        this.zipCode = normalizeOnlyNumbers(zipCode);
        this.city = normalizeText(city);
        this.state = normalizeState(state);
    }

    public boolean isFromParana() {
        return "PR".equalsIgnoreCase(this.state);
    }

    private String normalizeOnlyNumbers(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }

    private String normalizeText(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeState(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }

    public UUID getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
