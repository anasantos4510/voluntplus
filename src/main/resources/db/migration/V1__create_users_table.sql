CREATE TABLE users (
    id UUID NOT NULL,
    clerk_user_id VARCHAR(255) NOT NULL,
    person_type VARCHAR(20) NOT NULL,
    current_user_role VARCHAR(20) NOT NULL,
    email VARCHAR(320) NOT NULL,
    full_name VARCHAR(255),
    birth_date DATE,
    gender VARCHAR(32),
    organization_name VARCHAR(255),
    cnpj VARCHAR(14),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_clerk_user_id UNIQUE (clerk_user_id),
    CONSTRAINT ck_users_clerk_user_id_not_blank CHECK (BTRIM(clerk_user_id) <> ''),
    CONSTRAINT ck_users_email_not_blank CHECK (BTRIM(email) <> ''),
    CONSTRAINT ck_users_person_type CHECK (person_type IN ('INDIVIDUAL', 'ORGANIZATION')),
    CONSTRAINT ck_users_current_role CHECK (current_user_role IN ('BENEFICIARY', 'OFFERER')),
    CONSTRAINT ck_users_gender CHECK (
        gender IS NULL OR gender IN ('FEMALE', 'MALE', 'NON_BINARY', 'OTHER', 'PREFER_NOT_TO_SAY')
    ),
    CONSTRAINT ck_users_organization_role CHECK (
        person_type <> 'ORGANIZATION' OR current_user_role = 'OFFERER'
    ),
    CONSTRAINT ck_users_cnpj_format CHECK (
        cnpj IS NULL OR cnpj ~ '^[0-9]{14}$'
    ),
    CONSTRAINT ck_users_registration_data CHECK (
        (
            person_type = 'INDIVIDUAL'
            AND full_name IS NOT NULL
            AND BTRIM(full_name) <> ''
            AND birth_date IS NOT NULL
            AND gender IS NOT NULL
            AND organization_name IS NULL
            AND cnpj IS NULL
        )
        OR
        (
            person_type = 'ORGANIZATION'
            AND organization_name IS NOT NULL
            AND BTRIM(organization_name) <> ''
            AND cnpj IS NOT NULL
            AND full_name IS NULL
            AND birth_date IS NULL
            AND gender IS NULL
        )
    )
);
