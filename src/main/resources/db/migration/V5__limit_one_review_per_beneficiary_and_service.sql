WITH ranked_reviews AS (
    SELECT id,
           ROW_NUMBER() OVER (
               PARTITION BY service_id, author_id
               ORDER BY created_at ASC, id ASC
           ) AS duplicate_position
    FROM reviews
)
DELETE FROM reviews
WHERE id IN (
    SELECT id
    FROM ranked_reviews
    WHERE duplicate_position > 1
);

ALTER TABLE reviews
    ADD CONSTRAINT uk_reviews_service_author UNIQUE (service_id, author_id);
