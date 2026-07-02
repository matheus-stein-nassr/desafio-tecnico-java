CREATE TABLE events (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(100)  NOT NULL,
    description     VARCHAR(1000),
    event_date_time TIMESTAMP     NOT NULL,
    location        VARCHAR(200)  NOT NULL,
    deleted         BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);