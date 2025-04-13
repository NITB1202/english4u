CREATE TABLE user_saved_sets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    set_id UUID NOT NULL,
    learned_words INTEGER,
    last_access TIMESTAMP NOT NULL,
    CONSTRAINT user_saved_set_unique UNIQUE (user_id, set_id)
);

CREATE TABLE user_cached_sets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    set_id UUID NOT NULL,
    learned_words INTEGER,
    last_access TIMESTAMP NOT NULL,
    CONSTRAINT user_set_unique UNIQUE (user_id, set_id)
);
