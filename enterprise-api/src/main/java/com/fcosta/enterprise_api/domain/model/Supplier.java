package com.fcosta.enterprise_api.domain.model;

import com.fcosta.enterprise_api.domain.exception.DomainException;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class Supplier {

    private final UUID id;
    private final String document;
    private final DocumentType documentType;

    private String name;
    private String email;
    private String zipCode;
    private String rg;
    private LocalDate birthDate;
    private String city;
    private String state;

    public Supplier(
            UUID id,
            String document,
            DocumentType documentType,
            String name,
            String email,
            String zipCode,
            String rg,
            LocalDate birthDate,
            String city,
            String state
    ) {
        validateRequiredFields(document, documentType, name, email, zipCode);
        validateDocumentByType(document, documentType);
        validateIndividualSupplier(documentType, rg, birthDate);

        this.id = id == null ? UUID.randomUUID() : id;
        this.document = normalizeOnlyNumbers(document);
        this.documentType = documentType;
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.zipCode = normalizeOnlyNumbers(zipCode);
        this.rg = normalizeText(rg);
        this.birthDate = birthDate;
        this.city = normalizeText(city);
        this.state = normalizeState(state);
    }

    public void update(
            String name,
            String email,
            String zipCode,
            String rg,
            LocalDate birthDate,
            String city,
            String state
    ) {
        validateUpdateFields(name, email, zipCode);
        validateIndividualSupplier(this.documentType, rg, birthDate);

        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.zipCode = normalizeOnlyNumbers(zipCode);
        this.rg = normalizeText(rg);
        this.birthDate = birthDate;
        this.city = normalizeText(city);
        this.state = normalizeState(state);
    }

    public boolean isIndividual() {
        return documentType == DocumentType.CPF;
    }

    public boolean isCompany() {
        return documentType == DocumentType.CNPJ;
    }

    public boolean isUnderAge() {
        if (birthDate == null) {
            return false;
        }

        return Period.between(birthDate, LocalDate.now()).getYears() < 18;
    }

    private void validateRequiredFields(
            String document,
            DocumentType documentType,
            String name,
            String email,
            String zipCode
    ) {
        if (document == null || document.isBlank()) {
            throw new DomainException("Document is required");
        }

        if (documentType == null) {
            throw new DomainException("Document type is required");
        }

        if (name == null || name.isBlank()) {
            throw new DomainException("Supplier name is required");
        }

        if (email == null || email.isBlank()) {
            throw new DomainException("Email is required");
        }

        if (zipCode == null || zipCode.isBlank()) {
            throw new DomainException("Zip code is required");
        }
    }

    private void validateUpdateFields(
            String name,
            String email,
            String zipCode
    ) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Supplier name is required");
        }

        if (email == null || email.isBlank()) {
            throw new DomainException("Email is required");
        }

        if (zipCode == null || zipCode.isBlank()) {
            throw new DomainException("Zip code is required");
        }
    }

    private void validateDocumentByType(
            String document,
            DocumentType documentType
    ) {
        String normalizedDocument = normalizeOnlyNumbers(document);

        if (documentType == DocumentType.CPF && normalizedDocument.length() != 11) {
            throw new DomainException("CPF must have 11 digits");
        }

        if (documentType == DocumentType.CNPJ && normalizedDocument.length() != 14) {
            throw new DomainException("CNPJ must have 14 digits");
        }
    }

    private void validateIndividualSupplier(
            DocumentType documentType,
            String rg,
            LocalDate birthDate
    ) {
        if (documentType == DocumentType.CPF) {
            if (rg == null || rg.isBlank()) {
                throw new DomainException("RG is required for individual suppliers");
            }

            if (birthDate == null) {
                throw new DomainException("Birth date is required for individual suppliers");
            }

            if (birthDate.isAfter(LocalDate.now())) {
                throw new DomainException("Birth date cannot be in the future");
            }
        }
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

    public String getDocument() {
        return document;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getRg() {
        return rg;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
