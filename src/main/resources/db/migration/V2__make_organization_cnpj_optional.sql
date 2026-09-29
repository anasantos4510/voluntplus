ALTER TABLE users
    DROP CONSTRAINT ck_users_registration_data;

ALTER TABLE users
    ADD CONSTRAINT ck_users_registration_data CHECK (
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
            AND full_name IS NULL
            AND birth_date IS NULL
            AND gender IS NULL
        )
    );
