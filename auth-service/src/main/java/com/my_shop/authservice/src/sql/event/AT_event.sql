SET GLOBAL event_scheduler = ON;

CREATE EVENT IF NOT EXISTS delete_expired_access_tokens
ON SCHEDULE EVERY 5 MINUTE
DO
DELETE FROM access_token WHERE expired_at < NOW();