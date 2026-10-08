CREATE TABLE reviews (
    id UUID NOT NULL,
    service_id UUID NOT NULL,
    author_id UUID NOT NULL,
    rating SMALLINT NOT NULL,
    comment VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_reviews PRIMARY KEY (id),
    CONSTRAINT ck_reviews_rating CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT ck_reviews_comment_length CHECK (comment IS NULL OR CHAR_LENGTH(comment) <= 500)
);

CREATE INDEX ix_reviews_service_created
    ON reviews (service_id, created_at, id);
