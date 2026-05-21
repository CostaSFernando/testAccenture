CREATE TABLE companies (
    id UUID PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL,
    fantasy_name VARCHAR(255) NOT NULL,
    zip_code VARCHAR(8) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_companies_cnpj UNIQUE (cnpj)
);

CREATE TABLE suppliers (
    id UUID PRIMARY KEY,
    document VARCHAR(14) NOT NULL,
    document_type VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    zip_code VARCHAR(8) NOT NULL,
    rg VARCHAR(20),
    birth_date DATE,
    city VARCHAR(100),
    state VARCHAR(2),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_suppliers_document UNIQUE (document),
    CONSTRAINT chk_suppliers_document_type CHECK (document_type IN ('CPF', 'CNPJ')),
    CONSTRAINT chk_suppliers_individual_required_fields CHECK (
        document_type = 'CNPJ'
        OR (
            document_type = 'CPF'
            AND rg IS NOT NULL
            AND birth_date IS NOT NULL
        )
    )
);

CREATE TABLE company_suppliers (
    company_id UUID NOT NULL,
    supplier_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT pk_company_suppliers PRIMARY KEY (company_id, supplier_id),

    CONSTRAINT fk_company_suppliers_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_company_suppliers_supplier
        FOREIGN KEY (supplier_id)
        REFERENCES suppliers(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_companies_cnpj
    ON companies(cnpj);

CREATE INDEX idx_suppliers_document
    ON suppliers(document);

CREATE INDEX idx_suppliers_name
    ON suppliers(name);

CREATE INDEX idx_suppliers_name_lower
    ON suppliers(LOWER(name));

CREATE INDEX idx_company_suppliers_company_id
    ON company_suppliers(company_id);

CREATE INDEX idx_company_suppliers_supplier_id
    ON company_suppliers(supplier_id);
    