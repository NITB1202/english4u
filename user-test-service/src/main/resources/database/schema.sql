CREATE TABLE results (
    id UUID PRIMARY KEY,
    test_id UUID NOT NULL,
    user_id UUID NOT NULL,
    submit_date DATE NOT NULL,
    time_spent BIGINT NOT NULL,
    score INTEGER NOT NULL,
    accuracy REAL NOT NULL
);

CREATE TABLE result_details (
    id UUID PRIMARY KEY,
    result_id UUID NOT NULL,
    question_id UUID NOT NULL,
    user_answer TEXT NOT NULL,
    state VARCHAR(50) NOT NULL
);
