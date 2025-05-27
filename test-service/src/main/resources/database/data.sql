INSERT INTO tests (id, created_by, create_at, name, version, minutes, topic, part_count, completed_users, updated_by, update_at, is_deleted)
VALUES
    ('11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', NOW(), 'Test 1', 1, 60, 'English Grammar', 2, 100, '55555555-5555-5555-5555-555555555555', NOW(), false),
    ('22222222-2222-2222-2222-222222222222', '55555555-5555-5555-5555-555555555555', NOW(), 'Test 2',1, 45, 'Vocabulary', 1, 50, '44444444-4444-4444-4444-444444444444', NOW(), false);

INSERT INTO parts (id, test_id, position, content, question_count)
VALUES
    ('33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 1, 'Part 1 content', 5),
    ('44444444-4444-4444-4444-444444444444', '11111111-1111-1111-1111-111111111111', 2, 'Part 2 content', 5),
    ('55555555-5555-5555-5555-555555555555', '22222222-2222-2222-2222-222222222222', 1, 'Part 1 content for Test 2', 3);

INSERT INTO questions (id, part_id, position, content, answers, correct_answer, explanation)
VALUES
    ('66666666-6666-6666-6666-666666666666', '33333333-3333-3333-3333-333333333333', 1, 'What is the past tense of "go"?', 'goes,went,goed,going', 'B', 'Past tense of go is went.'),
    ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333', 2, 'Choose the correct article: ___ apple.', 'a,an,the,none', 'B', 'Use "an" before vowels.'),
    ('88888888-8888-8888-8888-888888888888', '44444444-4444-4444-4444-444444444444', 1, 'Synonym of "big"?', 'small,tiny,huge,thin', 'C', 'Huge means very big.'),
    ('99999999-9999-9999-9999-999999999999', '55555555-5555-5555-5555-555555555555', 1, 'Opposite of "happy"?', 'sad,joyful,glad,smile', 'A', 'Sad is the opposite of happy.');

INSERT INTO comments (id, parent_id, user_id, test_id, create_at, content)
VALUES
    ('10000000-0000-0000-0000-000000000001', NULL, '11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '2025-05-25 10:00:00', 'This test is very helpful!'),
    ('10000000-0000-0000-0000-000000000002', NULL, '22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', '2025-05-25 10:05:00', 'I found question 3 a bit tricky.'),
    ('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', '2025-05-25 10:10:00', 'Agree! The examples are great.'),
    ('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', '2025-05-25 10:15:00', 'I think it was about grammar structure.');
