INSERT INTO tests (id, created_by, create_at, name, minutes, topic, part_count, completed_users, updated_by, update_at, is_deleted)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', NOW(), 'Test 1', 60, 'English Grammar', 2, 100, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', NOW(), false),
    ('22222222-2222-2222-2222-222222222222', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', NOW(), 'Test 2', 45, 'Vocabulary', 1, 50, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', NOW(), false);

INSERT INTO parts (id, test_id, position, content, question_count)
VALUES
    ('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 1, 'Part 1 content', 5),
    ('44444444-4444-4444-4444-444444444444', '11111111-1111-1111-1111-111111111111', 2, 'Part 2 content', 5),
    ('55555555-5555-5555-5555-555555555555', '22222222-2222-2222-2222-222222222222', 1, 'Part 1 content for Test 2', 3);

INSERT INTO questions (id, part_id, position, content, answers, correct_answer, explanation)
VALUES
    ('66666666-6666-6666-6666-666666666666', '33333333-3333-3333-3333-333333333333', 1, 'What is the past tense of "go"?', 'goes,went,goed,going', 'went', 'Past tense of go is went.'),
    ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', 2, 'Choose the correct article: ___ apple.', 'a,an,the,none', 'an', 'Use "an" before vowels.'),
    ('88888888-8888-8888-8888-888888888888', '44444444-4444-4444-4444-444444444444', 1, 'Synonym of "big"?', 'small,tiny,huge,thin', 'huge', 'Huge means very big.'),
    ('99999999-9999-9999-9999-999999999999', '55555555-5555-5555-5555-555555555555', 1, 'Opposite of "happy"?', 'sad,joyful,glad,smile', 'sad', 'Sad is the opposite of happy.');
