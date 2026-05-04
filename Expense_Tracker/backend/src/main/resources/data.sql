USE expense_tracker;

INSERT INTO categories (name, description) VALUES
('Food', 'Meals, groceries, snacks and dining'),
('Travel', 'Transport, fuel, rides and tickets'),
('Shopping', 'Clothes, electronics and personal purchases'),
('Bills', 'Utilities, rent, subscriptions and recurring bills'),
('Entertainment', 'Movies, games, events and leisure'),
('Health', 'Medicines, doctor visits and wellness'),
('Education', 'Courses, books and learning materials'),
('Other', 'Uncategorized expenses')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- Demo login: demo@example.com / password123
INSERT INTO app_users (full_name, email, password_hash, monthly_income, created_at)
VALUES ('Demo User', 'demo@example.com', 'pbkdf2:200000:h/ba2sGQK7nelRoZ5hE7HQ==:ZWtm4tKvqod9uLxoNPxBAjmgwluOh3JlxWpnhQsBmRw=', 65000.00, NOW())
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

INSERT INTO expenses (title, amount, transaction_type, expense_date, note, user_id, category_id, created_at, updated_at)
SELECT 'Monthly Salary', 65000.00, 'INCOME', CURDATE() - INTERVAL 3 DAY, 'Sample income', u.id, c.id, NOW(), NOW()
FROM app_users u JOIN categories c ON c.name = 'Other'
WHERE u.email = 'demo@example.com'
  AND NOT EXISTS (SELECT 1 FROM expenses e WHERE e.user_id = u.id AND e.title = 'Monthly Salary');

INSERT INTO expenses (title, amount, transaction_type, expense_date, note, user_id, category_id, created_at, updated_at)
SELECT 'Groceries', 5200.00, 'EXPENSE', CURDATE() - INTERVAL 2 DAY, 'Weekly groceries', u.id, c.id, NOW(), NOW()
FROM app_users u JOIN categories c ON c.name = 'Food'
WHERE u.email = 'demo@example.com'
  AND NOT EXISTS (SELECT 1 FROM expenses e WHERE e.user_id = u.id AND e.title = 'Groceries');

INSERT INTO expenses (title, amount, transaction_type, expense_date, note, user_id, category_id, created_at, updated_at)
SELECT 'Metro Pass', 1800.00, 'EXPENSE', CURDATE() - INTERVAL 5 DAY, 'Travel pass', u.id, c.id, NOW(), NOW()
FROM app_users u JOIN categories c ON c.name = 'Travel'
WHERE u.email = 'demo@example.com'
  AND NOT EXISTS (SELECT 1 FROM expenses e WHERE e.user_id = u.id AND e.title = 'Metro Pass');

INSERT INTO expenses (title, amount, transaction_type, expense_date, note, user_id, category_id, created_at, updated_at)
SELECT 'Movie Weekend', 2400.00, 'EXPENSE', CURDATE() - INTERVAL 7 DAY, 'Entertainment spend', u.id, c.id, NOW(), NOW()
FROM app_users u JOIN categories c ON c.name = 'Entertainment'
WHERE u.email = 'demo@example.com'
  AND NOT EXISTS (SELECT 1 FROM expenses e WHERE e.user_id = u.id AND e.title = 'Movie Weekend');
