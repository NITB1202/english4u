CREATE TABLE vocabulary_sets (
    id UUID PRIMARY KEY,
    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL,
    version INT NOT NULL,
    word_count INT NOT NULL,
    updated_by UUID NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN NOT NULL
);

CREATE TABLE vocabulary_words (
    id UUID PRIMARY KEY,
    set_id UUID NOT NULL,
    position INT NOT NULL,
    word VARCHAR(255) NOT NULL,
    pronunciation VARCHAR(255) NOT NULL,
    ex TEXT NOT NULL,
    trans TEXT NOT NULL,
    image_url VARCHAR(255),
    CONSTRAINT fk_set FOREIGN KEY (set_id) REFERENCES vocabulary_sets(id) ON DELETE CASCADE
);

