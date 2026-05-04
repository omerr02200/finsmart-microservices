CREATE TABLE user_analysis (
    user_id BIGINT PRIMARY KEY,
    total_expense DECIMAL(19, 2),
    total_income DECIMAL(19,2),
    last_transaction_date TIMESTAMP
);