CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    join_at TIMESTAMP NOT NULL,
    is_locked BOOLEAN NOT NULL
);
