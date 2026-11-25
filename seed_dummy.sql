-- =========================================================
-- DATA DUMMY WHAT'S LEFT (jalankan SEKALI, setelah schema.sql)
-- Data diambil dari whats_left_pbo.sql, dengan penyesuaian agar cocok
-- dengan kode Java:
--  * status penawaran  : 'Available' (bukan 'ACTIVE')
--  * status reservasi  : Pending / Confirmed / Completed / Cancelled
--  * waktu pengambilan : relatif terhadap NOW(), supaya penawaran tampil
--    di katalog (katalog hanya menampilkan pickup_end yang belum lewat)
-- Menjalankan file ini dua kali akan gagal (kode reservasi harus unik).
-- =========================================================
USE whats_left_pbo;

INSERT INTO businesses (business_name, address, phone) VALUES
('Roti Pagi Jatinangor', 'Jl. Raya Jatinangor No. 10', '081234567801'),
('Dapur Nusa', 'Jl. Caringin No. 15', '081234567802'),
('Kedai Sejahtera', 'Jl. Cikuda No. 8', '081234567803'),
('Ayam Bakar Lestari', 'Jl. Hegarmanah No. 21', '081234567804'),
('Pasta Corner', 'Jl. Sayang No. 5', '081234567805');

INSERT INTO food_items (business_id, food_name, category, description, regular_price) VALUES
(1, 'Roti Cokelat', 'Bakery', 'Roti isi cokelat', 12000.00),
(2, 'Nasi Ayam', 'Makanan Berat', 'Nasi dengan ayam dan sambal', 18000.00),
(3, 'Donat Gula', 'Bakery', 'Donat dengan taburan gula', 8000.00),
(4, 'Ayam Bakar', 'Makanan Berat', 'Ayam bakar dengan nasi', 25000.00),
(5, 'Spaghetti Bolognese', 'Pasta', 'Spaghetti dengan saus bolognese', 22000.00);

INSERT INTO production_records (food_id, production_date, total_produced, regular_sold, wasted) VALUES
(1, '2026-11-20 07:00:00', 30, 20, 2),
(2, '2026-11-20 09:00:00', 25, 18, 1),
(3, '2026-11-20 06:30:00', 40, 30, 3),
(4, '2026-11-20 10:00:00', 20, 12, 2),
(5, '2026-11-20 11:00:00', 18, 10, 1);

-- initial_quantity = total_produced - regular_sold - wasted
-- available_quantity sudah dikurangi jumlah reservasi dummy di bawah
INSERT INTO surplus_listings
(production_id, discount_percentage, discounted_price, initial_quantity,
 available_quantity, pickup_start, pickup_end, status) VALUES
(1, 25.00,  9000.00, 8, 6, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'Available'),
(2, 20.00, 14400.00, 6, 5, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'Available'),
(3, 30.00,  5600.00, 7, 4, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'Available'),
(4, 20.00, 20000.00, 6, 4, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'Available'),
(5, 25.00, 16500.00, 7, 6, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'Available');

INSERT INTO reservations
(listing_id, customer_name, customer_phone, reservation_code, quantity, total_price, status) VALUES
(1, 'Andi Pratama',  '081298765401', 'WL-20261120-001', 2, 18000.00, 'Pending'),
(2, 'Siti Rahma',    '081298765402', 'WL-20261120-002', 1, 14400.00, 'Confirmed'),
(3, 'Budi Santoso',  '081298765403', 'WL-20261120-003', 3, 16800.00, 'Pending'),
(4, 'Dina Amelia',   '081298765404', 'WL-20261120-004', 2, 40000.00, 'Completed'),
(5, 'Rizky Maulana', '081298765405', 'WL-20261120-005', 1, 16500.00, 'Pending');

-- AKUN DEMO (password keduanya: password123)
--  * roti_pagi : akun Business untuk "Roti Pagi Jatinangor" (business id 1)
--  * andi      : akun Customer, cocok dengan reservasi Andi Pratama
-- Akun bisnis lain (id 2-5) belum punya login; daftarkan lewat aplikasi bila perlu.
INSERT INTO users (username, password_hash, role, business_id, full_name, phone) VALUES
('roti_pagi', '120000:lBxnGdYANHZwthu4vQqMjg==:0i4YVCREdlG5k+yO174cAfntCsQasPgl9Gc3JnjDFqU=', 'Business', 1, 'Roti Pagi Jatinangor', '081234567801'),
('andi',      '120000:lBxnGdYANHZwthu4vQqMjg==:0i4YVCREdlG5k+yO174cAfntCsQasPgl9Gc3JnjDFqU=', 'Customer', NULL, 'Andi Pratama', '081298765401');
