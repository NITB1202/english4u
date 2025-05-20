INSERT INTO vocabulary_sets (id, created_by, created_at, name, version, word_count, updated_by, updated_at, is_deleted)
VALUES
    ('11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', NOW(), 'Basic Vocabulary',1, 2, '22222222-2222-2222-2222-222222222222', NOW(), FALSE),
    ('22222222-2222-2222-2222-222222222222', '33333333-3333-3333-3333-333333333333', NOW(), 'Advanced Vocabulary', 1,2, '44444444-4444-4444-4444-444444444444', NOW(), FALSE);

INSERT INTO vocabulary_words (id, set_id, position, word, pronun, ex, trans, image_url)
VALUES
    ('aaa11111-aaaa-1111-aaaa-111111111111', '11111111-1111-1111-1111-111111111111', 1, 'apple', 'ˈæpəl', 'An apple a day keeps the doctor away', 'Quả táo', 'http://example.com/apple.jpg'),
    ('bbb11111-bbbb-1111-bbbb-111111111111', '11111111-1111-1111-1111-111111111111', 2, 'banana', 'bəˈnænə', 'Bananas are rich in potassium', 'Chuối', 'http://example.com/banana.jpg'),
    ('ccc11111-cccc-1111-cccc-111111111111', '22222222-2222-2222-2222-222222222222', 1, 'antique', 'ænˈtiːk', 'This antique vase is very valuable', 'Đồ cổ', 'http://example.com/antique.jpg'),
    ('ddd11111-dddd-1111-dddd-111111111111', '22222222-2222-2222-2222-222222222222', 2, 'beautiful', 'ˈbjuːtɪfəl', 'The beautiful landscape amazed me', 'Đẹp', 'http://example.com/beautiful.jpg');
