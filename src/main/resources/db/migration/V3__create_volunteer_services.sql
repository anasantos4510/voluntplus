CREATE TABLE volunteer_services (
    id UUID NOT NULL,
    owner_id UUID NOT NULL,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(32) NOT NULL,
    modality VARCHAR(16) NOT NULL,
    status VARCHAR(16) NOT NULL,
    postal_code VARCHAR(32),
    state VARCHAR(64),
    city VARCHAR(255),
    neighborhood VARCHAR(255),
    location_type SMALLINT,
    whatsapp VARCHAR(11),
    phone VARCHAR(11),
    instagram VARCHAR(255),
    website VARCHAR(2048),
    image_data_url TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ,

    CONSTRAINT pk_volunteer_services PRIMARY KEY (id),
    CONSTRAINT ck_volunteer_services_name_not_blank CHECK (BTRIM(name) <> ''),
    CONSTRAINT ck_volunteer_services_description_length CHECK (CHAR_LENGTH(BTRIM(description)) >= 30),
    CONSTRAINT ck_volunteer_services_category CHECK (
        category IN (
            'EDUCACAO', 'MUSICA', 'TECNOLOGIA', 'ESPORTE', 'ALIMENTACAO', 'DOACOES',
            'SAUDE', 'ANIMAIS', 'GERAIS', 'APOIO_COMUNITARIO', 'OUTROS'
        )
    ),
    CONSTRAINT ck_volunteer_services_modality CHECK (modality IN ('PRESENCIAL', 'ONLINE', 'HIBRIDO')),
    CONSTRAINT ck_volunteer_services_status CHECK (status IN ('ATIVO', 'INATIVO')),
    CONSTRAINT ck_volunteer_services_location_type CHECK (location_type IS NULL OR location_type IN (1, 2, 5, 6)),
    CONSTRAINT ck_volunteer_services_location CHECK (
        (
            modality = 'ONLINE'
            AND postal_code IS NULL
            AND state IS NULL
            AND city IS NULL
            AND neighborhood IS NULL
            AND location_type IS NULL
        )
        OR
        (
            modality IN ('PRESENCIAL', 'HIBRIDO')
            AND postal_code IS NOT NULL AND BTRIM(postal_code) <> ''
            AND state IS NOT NULL AND BTRIM(state) <> ''
            AND city IS NOT NULL AND BTRIM(city) <> ''
            AND neighborhood IS NOT NULL AND BTRIM(neighborhood) <> ''
            AND location_type IS NOT NULL
        )
    ),
    CONSTRAINT ck_volunteer_services_contact CHECK (
        whatsapp IS NOT NULL OR phone IS NOT NULL OR instagram IS NOT NULL OR website IS NOT NULL
    ),
    CONSTRAINT ck_volunteer_services_whatsapp CHECK (whatsapp IS NULL OR whatsapp ~ '^[0-9]{10,11}$'),
    CONSTRAINT ck_volunteer_services_phone CHECK (phone IS NULL OR phone ~ '^[0-9]{10,11}$'),
    CONSTRAINT ck_volunteer_services_instagram CHECK (instagram IS NULL OR BTRIM(instagram) <> ''),
    CONSTRAINT ck_volunteer_services_website CHECK (website IS NULL OR BTRIM(website) <> ''),
    CONSTRAINT ck_volunteer_services_dates CHECK (
        updated_at >= created_at
        AND (deleted_at IS NULL OR (deleted_at >= created_at AND deleted_at <= updated_at))
    )
);

CREATE TABLE volunteer_service_weekdays (
    service_id UUID NOT NULL,
    weekday VARCHAR(16) NOT NULL,

    CONSTRAINT pk_volunteer_service_weekdays PRIMARY KEY (service_id, weekday),
    CONSTRAINT fk_volunteer_service_weekdays_service FOREIGN KEY (service_id)
        REFERENCES volunteer_services (id) ON DELETE CASCADE,
    CONSTRAINT ck_volunteer_service_weekdays_value CHECK (
        weekday IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO')
    )
);

CREATE TABLE volunteer_service_shifts (
    service_id UUID NOT NULL,
    shift VARCHAR(16) NOT NULL,

    CONSTRAINT pk_volunteer_service_shifts PRIMARY KEY (service_id, shift),
    CONSTRAINT fk_volunteer_service_shifts_service FOREIGN KEY (service_id)
        REFERENCES volunteer_services (id) ON DELETE CASCADE,
    CONSTRAINT ck_volunteer_service_shifts_value CHECK (shift IN ('MANHA', 'TARDE', 'NOITE'))
);

CREATE INDEX ix_volunteer_services_owner_deleted
    ON volunteer_services (owner_id, deleted_at);

CREATE INDEX ix_volunteer_services_public_catalog
    ON volunteer_services (status, deleted_at, created_at DESC, id);
