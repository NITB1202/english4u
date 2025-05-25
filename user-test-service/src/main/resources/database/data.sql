INSERT INTO results (id, test_id, user_id, submit_date, time_spent, score, accuracy)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', DATE '2025-05-20', 1530, 8, 80.0), -- 25 phút 30 giây
    ('22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'dddddddd-dddd-dddd-dddd-dddddddddddd', DATE '2025-05-21', 1800, 7, 70.0); -- 30 phút

INSERT INTO result_details (id, result_id, question_id, user_answer, state)
VALUES
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', '11111111-1111-1111-1111-111111111111', '2aaf5de4-b6c5-4860-8bc5-82fb376854d0', 'A', 'CORRECT'),
    ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', '11111111-1111-1111-1111-111111111111', '9cca9fac-74bb-44e1-80be-6503486b05d9', 'B', 'INCORRECT'),
    ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaa3', '22222222-2222-2222-2222-222222222222', '576233b9-6804-4c9e-9897-f59bb4e4a141', 'C', 'CORRECT'),
    ('aaaaaaa4-aaaa-aaaa-aaaa-aaaaaaaaaaa4', '22222222-2222-2222-2222-222222222222', '8e19d7a3-eb53-4b16-8d2a-65e4f968e642', 'D', 'EMPTY');
