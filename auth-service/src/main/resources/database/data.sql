INSERT INTO accounts (id, user_id, provider, provider_id, email, hashed_password, role)
VALUES
    -- password: admin123
    (
        '2e4a1e62-2ed9-4f12-9d36-1a9b1e1d1234',
        'b6a8d8c1-5e14-4b7c-8c89-57e44b8d5678',
        'LOCAL',
        NULL,
        'admin@english4u.com',
        '$2a$10$Z9f2vWgq9D8ZVQps4hWzUOvRfQoL3/vbdOsjTyhzHRDj7PTDFfhmS',
        'ADMIN'
    ),
    -- password: user1234
    (
        '3e1a2b13-8fd4-40ae-81b5-1ec97a5b6789',
        '44444444-4444-4444-4444-444444444444',
        'LOCAL',
        NULL,
        'user1@english4u.com',
        '$2a$10$wK5G.k2A1JZ9ID6Q8e6QAem2wPb6sbdpq0RHyUNo2JQ32Zqtz5TW6',
        'LEARNER'
    ),
    -- password: system123
    (
        '7a93f819-daa7-4d13-bc4f-e67f6e452aaa',
        '55555555-5555-5555-5555-555555555555',
        'LOCAL',
        NULL,
        'system@english4u.com',
        '$2a$10$GdWi6.zjCCbBzGvLzBz8ROXnPTz9Lbz80RETxXoxBsdmWaUXCfuqG',
        'SYSTEM_ADMIN'
    );
