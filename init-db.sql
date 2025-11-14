-- Initialize database schema
CREATE TABLE IF NOT EXISTS deals (
    id BIGSERIAL PRIMARY KEY,
    deal_unique_id VARCHAR(255) UNIQUE NOT NULL,
    from_currency_iso_code VARCHAR(3) NOT NULL,
    to_currency_iso_code VARCHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP NOT NULL,
    deal_amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for faster lookups
CREATE INDEX IF NOT EXISTS idx_deal_unique_id ON deals(deal_unique_id);
CREATE INDEX IF NOT EXISTS idx_deal_timestamp ON deals(deal_timestamp);
CREATE INDEX IF NOT EXISTS idx_from_currency ON deals(from_currency_iso_code);
CREATE INDEX IF NOT EXISTS idx_to_currency ON deals(to_currency_iso_code);
