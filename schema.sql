-- ============================================================
-- WHAT'S LEFT - skema database (MySQL / MariaDB di Laragon)
-- Jalankan di HeidiSQL / phpMyAdmin. Aman dijalankan berulang kali:
-- tabel yang sudah ada tidak diubah (CREATE TABLE IF NOT EXISTS).
-- ============================================================

CREATE DATABASE IF NOT EXISTS whats_left_pbo
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE whats_left_pbo;

CREATE TABLE IF NOT EXISTS businesses (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    business_name VARCHAR(100) NOT NULL,
    address       VARCHAR(255),
    phone         VARCHAR(20),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS food_items (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    business_id   INT NOT NULL,
    food_name     VARCHAR(100) NOT NULL,
    category      VARCHAR(50),
    description   TEXT,
    regular_price DECIMAL(12,2) NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (business_id) REFERENCES businesses(id)
);

CREATE TABLE IF NOT EXISTS production_records (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    food_id         INT NOT NULL,
    production_date DATETIME NOT NULL,
    total_produced  INT NOT NULL,
    regular_sold    INT NOT NULL,
    wasted          INT NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (food_id) REFERENCES food_items(id)
);

CREATE TABLE IF NOT EXISTS surplus_listings (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    production_id       INT NOT NULL,
    discount_percentage DECIMAL(5,2) NOT NULL,
    discounted_price    DECIMAL(12,2) NOT NULL,
    initial_quantity    INT NOT NULL,
    available_quantity  INT NOT NULL,
    pickup_start        DATETIME NOT NULL,
    pickup_end          DATETIME NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'Available',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (production_id) REFERENCES production_records(id)
);

-- status: Pending, Confirmed, Completed, Cancelled
CREATE TABLE IF NOT EXISTS reservations (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    listing_id       INT NOT NULL,
    customer_name    VARCHAR(100) NOT NULL,
    customer_phone   VARCHAR(20) NOT NULL,
    reservation_code VARCHAR(20) NOT NULL UNIQUE,
    quantity         INT NOT NULL,
    total_price      DECIMAL(12,2) NOT NULL,
    reservation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status           VARCHAR(20) NOT NULL DEFAULT 'Pending',
    FOREIGN KEY (listing_id) REFERENCES surplus_listings(id)
);

-- Akun login. Password disimpan sebagai hash PBKDF2 (bukan teks asli).
-- role = 'Business' -> business_id terisi; role = 'Customer' -> business_id NULL.
CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL,
    business_id   INT NULL,
    full_name     VARCHAR(100) NOT NULL,
    phone         VARCHAR(20),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (business_id) REFERENCES businesses(id)
);

-- ------------------------------------------------------------
-- MIGRASI untuk database lama (aman dijalankan pada database baru).
-- Pastikan kolom status bisa menyimpan nilai baru 'Completed'.
-- Jika sebelumnya bertipe ENUM('Pending','Confirmed','Cancelled'),
-- tanpa baris ini status Completed akan ditolak MySQL.
-- ------------------------------------------------------------
ALTER TABLE reservations
    MODIFY status VARCHAR(20) NOT NULL DEFAULT 'Pending';

-- ------------------------------------------------------------
-- OPSIONAL: data lama (makanan dsb.) dulu otomatis memakai business_id = 1.
-- Setelah mendaftar akun Business lewat aplikasi, pindahkan data lama ke
-- bisnis tersebut (ganti 2 dengan id bisnis akun baru; lihat tabel businesses):
--   UPDATE food_items SET business_id = 2 WHERE business_id = 1;
-- ------------------------------------------------------------
