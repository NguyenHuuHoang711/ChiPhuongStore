CREATE TABLE access_token (
                              id VARCHAR(255) PRIMARY KEY,
                              token VARCHAR(255) NOT NULL UNIQUE,
                              user_id VARCHAR(255) NOT NULL,
                              created_at DATETIME,
                              expired_at DATETIME,
                              revoked BOOLEAN,
                              CONSTRAINT fk_access_token_user FOREIGN KEY (user_id)
                                  REFERENCES user(id)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE
);