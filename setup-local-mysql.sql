-- Chạy script này trong MySQL Workbench bằng tài khoản có quyền quản trị.
-- Thông tin khớp với .env.example; có thể đổi mật khẩu tại đây và trong .env.
CREATE DATABASE IF NOT EXISTS quan_ly_phong_tro
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'qlytro'@'localhost'
    IDENTIFIED BY 'local_qlytro_password';
ALTER USER 'qlytro'@'localhost'
    IDENTIFIED BY 'local_qlytro_password';
GRANT ALL PRIVILEGES ON quan_ly_phong_tro.* TO 'qlytro'@'localhost';
FLUSH PRIVILEGES;
