INSERT INTO users (
    name,
    email,
    role,
    user_type
)
SELECT
    'EventHub Administrator',
    'admin@eventhub.local',
    'ADMIN',
    '0'
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE email = 'admin@eventhub.local'
);