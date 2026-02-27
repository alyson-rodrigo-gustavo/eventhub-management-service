CREATE SEQUENCE IF NOT EXISTS ticket_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS ticket (
    id BIGINT PRIMARY KEY DEFAULT NEXTVAL('ticket_seq'),
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    purchased_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ticket_event
    FOREIGN KEY (event_id)
    REFERENCES event(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_ticket_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_ticket_event
    ON ticket(event_id);

CREATE INDEX IF NOT EXISTS idx_ticket_user
    ON ticket(user_id);