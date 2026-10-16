CREATE DATABASE IF NOT EXISTS student_db CHARACTER SET utf8mb4;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    student_id  VARCHAR(20)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    program     VARCHAR(100) NOT NULL,
    status      ENUM('ACTIVE','ARCHIVED') NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    archived_at TIMESTAMP    NULL DEFAULT NULL
);

-- Sample data (optional)
INSERT IGNORE INTO students (student_id, name, program) VALUES
 ('2026-0001', 'Juan Dela Cruz', 'BSIT'),
 ('2026-0002', 'Maria Santos',   'BSCS');
