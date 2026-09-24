CREATE TABLE enrollments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    course_id   BIGINT       NOT NULL,
    enrolled_at TIMESTAMP    NOT NULL
);

CREATE INDEX idx_enrollments_course_id ON enrollments (course_id);