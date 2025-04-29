CREATE TABLE tests (
    id UUID PRIMARY KEY NOT NULL,
    created_by UUID NOT NULL,
    create_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    minutes INT NOT NULL,
    topic VARCHAR(255),
    part_count INT NOT NULL,
    completed_users BIGINT NOT NULL,
    updated_by UUID NOT NULL,
    update_at TIMESTAMP NOT NULL,
    is_deleted BOOLEAN
);

CREATE TABLE parts (
    id UUID PRIMARY KEY NOT NULL,
    test_id UUID NOT NULL,
    position INT NOT NULL,
    content VARCHAR(255),
    question_count INT NOT NULL,
    CONSTRAINT fk_parts_test FOREIGN KEY (test_id) REFERENCES tests(id)
);

CREATE TABLE questions (
    id UUID PRIMARY KEY NOT NULL,
    part_id UUID NOT NULL,
    position INT NOT NULL,
    content VARCHAR(255) NOT NULL,
    answers TEXT NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    explanation TEXT,
    CONSTRAINT fk_questions_part FOREIGN KEY (part_id) REFERENCES parts(id)
);

