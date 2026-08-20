-- V__insert_sample_data.sql

-- 1. Sample users
INSERT INTO app_user (userid, username, name)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'alex.johnson', 'Alex Johnson'),
    ('22222222-2222-2222-2222-222222222222', 'sarah.lee', 'Sarah Lee'),
    ('33333333-3333-3333-3333-333333333333', 'michael.brown', 'Michael Brown'),
    ('44444444-4444-4444-4444-444444444444', 'emma.wilson', 'Emma Wilson'),
    ('55555555-5555-5555-5555-555555555555', 'daniel.smith', 'Daniel Smith');


-- 2. Sample artists
INSERT INTO artist (
    artistid,
    stagename,
    artistname,
    alias,
    version,
    updatedby,
    updatedtimestamp,
    createddate,
    sequenceno
)
VALUES
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'The Midnight Echo',
        'James Carter',
        'Jimmy',
        1,
        '11111111-1111-1111-1111-111111111111',
        now(),
        '2026-08-01',
        1
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'Neon Skies',
        'Sophia Martin',
        'Sophie',
        1,
        '22222222-2222-2222-2222-222222222222',
        now(),
        '2026-08-05',
        2
    ),
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'Silver Horizon',
        'Oliver Davis',
        'Ollie',
        2,
        '33333333-3333-3333-3333-333333333333',
        now(),
        '2026-08-10',
        3
    ),
    (
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        'Velvet Pulse',
        'Emily Taylor',
        'Em',
        1,
        '44444444-4444-4444-4444-444444444444',
        now(),
        '2026-08-12',
        4
    ),
    (
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        'Urban Frequency',
        'Daniel Wilson',
        'Danny',
        3,
        '55555555-5555-5555-5555-555555555555',
        now(),
        '2026-08-15',
        5
    );


-- 3. Sample tracks
INSERT INTO track (
    trackid,
    title,
    genre,
    length,
    artistid,
    updatedby,
    updatedtimestamp
)
VALUES
    (
        'aaaaaaaa-1111-1111-1111-aaaaaaaaaaaa',
        'Echoes in the Night',
        'Electronic',
        245,
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        '11111111-1111-1111-1111-111111111111',
        now()
    ),
    (
        'bbbbbbbb-2222-2222-2222-bbbbbbbbbbbb',
        'City Lights',
        'Synthwave',
        218,
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        '22222222-2222-2222-2222-222222222222',
        now()
    ),
    (
        'cccccccc-3333-3333-3333-cccccccccccc',
        'Golden Skies',
        'Indie',
        267,
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        '33333333-3333-3333-3333-333333333333',
        now()
    ),
    (
        'dddddddd-4444-4444-4444-dddddddddddd',
        'Velvet Dreams',
        'R&B',
        231,
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        '44444444-4444-4444-4444-444444444444',
        now()
    ),
    (
        'eeeeeeee-5555-5555-5555-eeeeeeeeeeee',
        'Downtown Rhythm',
        'Hip Hop',
        302,
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        '55555555-5555-5555-5555-555555555555',
        now()
    );