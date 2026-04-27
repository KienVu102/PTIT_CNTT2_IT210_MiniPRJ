CREATE DATABASE IF NOT EXISTS miniprj_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE miniprj_db;
CREATE TABLE IF NOT EXISTS todos (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    content  VARCHAR(500)  NOT NULL,
    due_date DATE          NOT NULL,
    status   VARCHAR(50)   NOT NULL DEFAULT 'PENDING',
    priority VARCHAR(50)   NOT NULL DEFAULT 'MEDIUM'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO todos (content, due_date, status, priority) VALUES
    ('Hoàn thành Mini Project Spring Boot',  DATE_ADD(CURDATE(), INTERVAL 3 DAY),  'IN_PROGRESS', 'HIGH'),
    ('Ôn tập Session 12 - Validation',       DATE_ADD(CURDATE(), INTERVAL 1 DAY),  'PENDING',     'MEDIUM'),
    ('Đọc tài liệu Thymeleaf th:each',       CURDATE(),                            'DONE',        'LOW');
