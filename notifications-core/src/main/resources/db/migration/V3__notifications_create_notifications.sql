CREATE TABLE notifications (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id BIGINT       NOT NULL,
    recipient     VARCHAR(255) NOT NULL,
    channel       VARCHAR(50)  NOT NULL,
    message       VARCHAR(1000) NOT NULL,
    sent_at       TIMESTAMP    NOT NULL
);