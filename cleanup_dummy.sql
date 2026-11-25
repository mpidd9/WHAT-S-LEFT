-- =========================================================
-- BERSIHKAN DATA DUMMY (jalankan SEKALI pada database lama)
-- Hanya perlu jika sebelumnya seed_dummy.sql sudah dijalankan.
-- Database baru yang hanya memakai schema.sql TIDAK perlu file ini.
--
-- PENTING: cek dulu bahwa id 1-5 memang bisnis dummy:
--   SELECT id, business_name FROM businesses;
-- Dummy: Roti Pagi Jatinangor, Dapur Nusa, Kedai Sejahtera,
--        Ayam Bakar Lestari, Pasta Corner.
-- Jika id 1-5 sudah berisi data nyata, JANGAN jalankan file ini.
-- =========================================================
USE whats_left_pbo;

DELETE r FROM reservations r
  JOIN surplus_listings s ON r.listing_id = s.id
  JOIN production_records p ON s.production_id = p.id
  JOIN food_items f ON p.food_id = f.id
  WHERE f.business_id BETWEEN 1 AND 5;

DELETE s FROM surplus_listings s
  JOIN production_records p ON s.production_id = p.id
  JOIN food_items f ON p.food_id = f.id
  WHERE f.business_id BETWEEN 1 AND 5;

DELETE p FROM production_records p
  JOIN food_items f ON p.food_id = f.id
  WHERE f.business_id BETWEEN 1 AND 5;

DELETE FROM food_items WHERE business_id BETWEEN 1 AND 5;
DELETE FROM users WHERE business_id BETWEEN 1 AND 5;   -- akun roti_pagi
DELETE FROM businesses WHERE id BETWEEN 1 AND 5;
DELETE FROM users WHERE username = 'andi' AND role = 'Customer';  -- customer dummy
