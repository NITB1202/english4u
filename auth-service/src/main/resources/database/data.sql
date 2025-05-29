INSERT INTO accounts (id, user_id, provider, provider_id, email, hashed_password, role)
VALUES
    -- password: admin123
    (
        '2e4a1e62-2ed9-4f12-9d36-1a9b1e1d1234',
        '44444444-4444-4444-4444-444444444444',
        'LOCAL',
        NULL,
        'admin@english4u.com',
        '$2a$10$BKfmTWy1Sq4NWfdXHa.08O1dMZke1Kl72tPWCJ4FGNIjFz/CxxbDu',
        'ADMIN'
    ),
    -- password: user1234
    (
        '3e1a2b13-8fd4-40ae-81b5-1ec97a5b6789',
        '11111111-1111-1111-1111-111111111111',
        'LOCAL',
        NULL,
        'user1@english4u.com',
        '$2a$10$zpygZ72GKBRwd8PNiHcRMO5muGxZr/YfJ.V4Y2sCfai7CP76wpxHC',
        'LEARNER'
    ),
    -- password: system123
    (
        '7a93f819-daa7-4d13-bc4f-e67f6e452aaa',
        '55555555-5555-5555-5555-555555555555',
        'LOCAL',
        NULL,
        'system@english4u.com',
        '$2a$10$/j.77sEIl8aJBjOSj1G6.erGDyAE19Z2BsRcPABkhgb8GNfiwp6cO',
        'SYSTEM_ADMIN'
    ),
    (
        '228e7f22-db93-4034-98c3-bb4fb7bf0803',
        '22222222-2222-2222-2222-222222222222',
        'GOOGLE',
        1284621464831,
        'user2@english4u.com',
        '',
        'LEARNER'
    );
