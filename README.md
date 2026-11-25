# WHAT'S LEFT
### Aplikasi Pengelolaan Makanan Surplus untuk Mengurangi Food Waste di Jatinangor

## 1. Deskripsi Aplikasi

**WHAT'S LEFT** merupakan aplikasi desktop yang dirancang untuk membantu mengurangi permasalahan *food waste* atau makanan yang terbuang, khususnya pada bisnis makanan di Jatinangor.

Aplikasi ini menghubungkan bisnis makanan yang memiliki produk surplus dengan konsumen yang ingin mendapatkan makanan berkualitas dengan harga lebih terjangkau.

Selain itu, aplikasi ini membantu bisnis dalam mencatat jumlah produksi dan penjualan agar dapat mengetahui jumlah makanan yang tersisa maupun terbuang.

**Tujuan:**
- Mengurangi jumlah makanan yang terbuang (*food waste*).
- Membantu bisnis mengelola dan memantau makanan surplus.
- Memberikan kesempatan kepada konsumen untuk mendapatkan makanan dengan harga lebih terjangkau.
- Membantu bisnis mencatat jumlah produksi, penjualan, dan makanan terbuang.
- Mengoptimalkan pengelolaan stok makanan melalui pencatatan yang terstruktur.

## 2. Pengguna dan Hak Akses

Aplikasi WHAT'S LEFT memiliki dua jenis pengguna, yaitu Business dan Customer.

| Peran | Deskripsi | Hak Akses |
|---|---|---|
| Business | Pemilik atau pengelola bisnis makanan | Mengelola data makanan, mencatat produksi, penjualan, makanan terbuang, membuat penawaran surplus, melihat reservasi, dan memperbarui status reservasi. |
| Customer | Konsumen yang ingin membeli makanan surplus | Melihat katalog makanan surplus, melihat informasi makanan, melakukan reservasi, melihat kode dan status reservasi, serta membatalkan reservasi yang belum diselesaikan. |

---

## 3. Fungsionalitas

### 3.1 Business

Business memiliki beberapa fitur utama berikut:
1. Mengelola data makanan, seperti nama makanan, kategori, deskripsi, dan harga normal.
2. Mencatat jumlah makanan yang diproduksi, terjual secara reguler, dan terbuang.
3. Menghitung jumlah makanan surplus berdasarkan data produksi.
4. Membuat penawaran makanan surplus dengan menentukan jumlah makanan, persentase diskon, harga setelah diskon, dan waktu pengambilan.
5. Melihat daftar reservasi yang dilakukan oleh Customer.
6. Memperbarui status reservasi setelah pesanan diambil.
7. Melihat ringkasan data produksi dan penjualan.


### 3.2 Customer

Customer memiliki beberapa fitur utama berikut:
1. Melihat daftar makanan surplus yang tersedia.
2. Melihat informasi makanan, harga normal, harga diskon, jumlah tersedia, dan waktu pengambilan.
3. Melakukan reservasi makanan sesuai jumlah yang diinginkan.
4. Melihat daftar reservasi yang telah dilakukan.
5. Melihat kode dan status reservasi.
6. Membatalkan reservasi selama pesanan belum diselesaikan.


## 4. Alur Penggunaan Aplikasi

### 4.0 Login dan Register

1. Aplikasi selalu dimulai dari halaman **Login**.
2. Pengguna yang belum punya akun memilih **Daftar**, lalu mendaftar sebagai **Customer** (nama, nomor telepon) atau **Business** (nama bisnis, alamat, nomor telepon).
3. Password disimpan dalam bentuk hash (PBKDF2), bukan teks asli.
4. Setelah login, sistem mengarahkan pengguna ke halaman sesuai perannya. Business hanya melihat data bisnisnya sendiri, dan Customer melihat riwayat reservasi akunnya sendiri.

### 4.1 Alur Business

1. Business login, lalu sistem menampilkan halaman pengelolaan Business.
3. Business mengelola daftar makanan yang dijual.
4. Business mencatat jumlah makanan yang diproduksi, terjual secara reguler, dan terbuang.
5. Sistem menghitung jumlah makanan surplus.
6. Business membuat penawaran untuk makanan surplus yang masih layak dikonsumsi.
7. Business menentukan jumlah makanan, diskon, harga akhir, dan waktu pengambilan.
8. Penawaran makanan surplus tersedia pada katalog Customer.
9. Business melihat reservasi yang masuk.
10. Business memperbarui status reservasi setelah pesanan diambil.

### 4.2 Alur Customer

1. Customer login pada halaman Login.
2. Sistem menampilkan katalog makanan surplus.
3. Customer memilih makanan dan melihat informasi penawaran.
4. Customer menentukan jumlah makanan yang ingin dipesan.
5. Sistem memeriksa ketersediaan stok dan menghitung total harga.
6. Nama dan nomor telepon Customer terisi otomatis dari akun, sehingga Customer tinggal memilih makanan dan jumlah.
7. Sistem menyimpan data reservasi dan mengurangi jumlah stok yang tersedia.
8. Customer memperoleh kode reservasi.
9. Customer mengambil makanan secara langsung sesuai waktu yang ditentukan.
10. Business memperbarui status reservasi menjadi **Completed** setelah makanan diambil.

**Status reservasi:** `Pending` → `Confirmed` → `Completed`, atau `Cancelled`.
- `Completed` dan `Cancelled` bersifat final (tidak dapat diubah lagi).
- Saat reservasi `Cancelled` (oleh Customer maupun Business), stok penawaran dikembalikan otomatis.
- Customer hanya dapat membatalkan reservasinya sendiri saat status masih `Pending`.
- Business hanya dapat menghapus riwayat reservasi yang berstatus `Completed` atau `Cancelled`.


## 5. Perhitungan dalam Aplikasi

### 5.1 Perhitungan Makanan Surplus

Sistem menghitung jumlah makanan surplus berdasarkan total produksi, penjualan reguler, dan makanan terbuang.

**Rumus:**

```text
Surplus = Total Produksi - Penjualan Reguler - Makanan Terbuang
```

**Contoh:**

| Keterangan | Jumlah |
|---|---:|
| Total Produksi | 50 porsi |
| Penjualan Reguler | 35 porsi |
| Makanan Terbuang | 5 porsi |
| **Makanan Surplus** | **10 porsi** |

### 5.2 Perhitungan Harga Setelah Diskon

Sistem menghitung harga makanan setelah diberikan diskon oleh Business.

**Rumus:**

```text
Harga Diskon = Harga Normal - (Harga Normal × Persentase Diskon / 100)
```

**Contoh:**

| Keterangan | Nilai |
|---|---:|
| Harga Normal | Rp20.000 |
| Diskon | 30% |
| **Harga Setelah Diskon** | **Rp14.000** |

### 5.3 Rekomendasi Jumlah Produksi

Sistem dapat memberikan perkiraan jumlah produksi berikutnya berdasarkan rata-rata penjualan reguler dalam beberapa hari terakhir.

Fitur ini bertujuan membantu Business memperkirakan kebutuhan produksi agar jumlah makanan yang terbuang dapat dikurangi.


## 6. Rancangan Frame Aplikasi

Aplikasi WHAT'S LEFT dirancang menggunakan JFrame yang saling terhubung melalui tombol navigasi. Halaman awal adalah LoginFrame (menggantikan MainFrame), dan RegisterFrame untuk pendaftaran akun.

| No. | Nama Frame | Fungsi |
|---|---|---|
| 1 | LoginFrame | Halaman awal untuk login. Terdapat tombol menuju RegisterFrame. |
| 1a | RegisterFrame | Pendaftaran akun Customer atau Business. |
| 2 | FoodFrame | Mengelola data makanan untuk Business dan menampilkan katalog makanan untuk Customer. |
| 3 | ProductionFrame | Mencatat data produksi, penjualan reguler, makanan terbuang, dan menghitung surplus. |
| 4 | SurplusFrame | Mengelola penawaran makanan surplus, diskon, jumlah tersedia, dan waktu pengambilan. |
| 5 | ReservationFrame | Membuat reservasi bagi Customer serta melihat dan memperbarui status reservasi bagi Business. |

Setiap frame dapat diakses melalui tombol navigasi setelah login. Fitur yang ditampilkan akan disesuaikan dengan bagian Business atau Customer yang dipilih pengguna.


## 7. Rancangan Database

Database yang digunakan adalah **MySQL**. Database dirancang untuk menyimpan informasi bisnis, makanan, produksi, penawaran surplus, dan reservasi.

### 7.1 Daftar Tabel

| No. | Nama Tabel | Fungsi |
|---|---|---|
| 1 | businesses | Menyimpan informasi bisnis makanan. |
| 2 | food_items | Menyimpan data makanan yang dikelola oleh Business. |
| 3 | production_records | Menyimpan catatan produksi, penjualan reguler, dan makanan terbuang. |
| 4 | surplus_listings | Menyimpan informasi penawaran makanan surplus. |
| 5 | reservations | Menyimpan data reservasi yang dilakukan Customer. |
| 6 | users | Menyimpan akun login (username, hash password, peran, dan bisnis untuk akun Business). |

### 7.2 Relasi Antar Tabel

Relasi antar tabel dalam database WHAT'S LEFT adalah sebagai berikut:

1. **Businesses dan Food_Items** memiliki relasi One-to-Many karena satu bisnis dapat memiliki banyak jenis makanan.
2. **Food_Items dan Production_Records** memiliki relasi One-to-Many karena satu jenis makanan dapat memiliki banyak catatan produksi.
3. **Production_Records dan Surplus_Listings** memiliki relasi One-to-One dalam rancangan awal karena satu catatan produksi menghasilkan maksimal satu penawaran surplus.
4. **Surplus_Listings dan Reservations** memiliki relasi One-to-Many karena satu penawaran makanan dapat dipesan oleh beberapa Customer.
5. **Businesses dan Users** memiliki relasi One-to-Many karena satu bisnis dapat memiliki akun login (akun Customer tidak terkait bisnis mana pun).

Skrip pembuatan database lengkap ada pada file `schema.sql`.


## 8. Rancangan Class

Rancangan class dibuat untuk menerapkan konsep Object-Oriented Programming (OOP) dalam pengembangan aplikasi.

Class dibagi menjadi tiga bagian, yaitu **Model, Repository, dan Frame**.

### 8.1 Class Model

Class model digunakan untuk merepresentasikan objek dan data utama dalam aplikasi.

**A. Business**

Atribut:
- `id`
- `businessName`
- `address`
- `phone`
- `createdAt`

Method:
- `updateProfile()`
- `getBusinessInfo()`

**B. FoodItem**

Atribut:
- `id`
- `businessId`
- `foodName`
- `category`
- `description`
- `regularPrice`
- `createdAt`

Method:
- `calculateDiscountedPrice()`
- `getFoodInfo()`

**C. ProductionRecord**

Atribut:
- `id`
- `foodId`
- `productionDate`
- `totalProduced`
- `regularSold`
- `wasted`
- `createdAt`

Method:
- `calculateSurplus()`
- `getProductionSummary()`
- `calculateFoodAge()`

**D. SurplusListing**

Atribut:
- `id`
- `productionId`
- `discountPercentage`
- `discountedPrice`
- `initialQuantity`
- `availableQuantity`
- `pickupStart`
- `pickupEnd`
- `status`
- `createdAt`

Method:
- `calculateDiscountedPrice()`
- `checkAvailability()`
- `updateAvailableQuantity()`

**E. Reservation**

Atribut:
- `id`
- `listingId`
- `customerName`
- `customerPhone`
- `reservationCode`
- `quantity`
- `totalPrice`
- `reservationDate`
- `status`

Method:
- `calculateTotalPrice()`
- `createReservation()`
- `updateStatus()`

**F. User**

Atribut:
- `id`
- `username`
- `passwordHash`
- `role`
- `businessId`
- `fullName`
- `phone`
- `createdAt`

Method:
- `isBusiness()`
- `isCustomer()`

### 8.2 Class Repository

Class repository bertugas mengelola komunikasi antara aplikasi dengan database MySQL.

| No. | Nama Class | Fungsi |
|---|---|---|
| 1 | DatabaseUtil | Mengatur koneksi antara aplikasi Java dengan database MySQL. |
| 2 | BusinessRepository | Mengelola data bisnis. |
| 3 | FoodRepository | Mengelola data makanan. |
| 4 | ProductionRepository | Mengelola catatan produksi dan penjualan. |
| 5 | SurplusRepository | Mengelola penawaran makanan surplus. |
| 6 | ReservationRepository | Mengelola data reservasi Customer, perubahan status, pembatalan, dan penghapusan riwayat. |
| 7 | UserRepository | Mengelola akun: login dan register. |
| 8 | PasswordUtil | Hash dan verifikasi password (PBKDF2). |

### 8.3 Class Frame

Class frame bertugas menangani tampilan antarmuka pengguna (GUI) menggunakan Java Swing.

| No. | Nama Class | Fungsi |
|---|---|---|
| 1 | LoginFrame | Menampilkan halaman login. |
| 1a | RegisterFrame | Menampilkan form pendaftaran akun. |
| 2 | FoodFrame | Menampilkan serta mengelola data makanan. |
| 3 | ProductionFrame | Menampilkan form pencatatan produksi. |
| 4 | SurplusFrame | Menampilkan dan mengelola penawaran makanan surplus. |
| 5 | ReservationFrame | Menampilkan dan mengelola reservasi. |


## 9. Anggota Kelompok

Praktikum Pemrograman Berorientasi Objek (PBO).

| No. | Nama Anggota | NPM |
|---|---|---|
| 1 | Mochammad Mufid Mukhlis | 140810250020 |
| 2 | Azahwa Ezarmei Narita | 140810250044 |
| 3 | Muhammad Razaqa Fasya Aditya | 140810250074 |

## 10. Cara Menjalankan

1. Nyalakan **MySQL** (Laragon → Start All).
2. Jalankan `schema.sql` di HeidiSQL/phpMyAdmin untuk membuat database `whats_left_pbo` dan seluruh tabel.
   - Aplikasi ini **tidak memakai data dummy**. Seluruh data dibuat langsung lewat aplikasi.
   - Jika database lama sudah pernah diisi data dummy, jalankan `cleanup_dummy.sql` satu kali (baca petunjuk di dalam file sebelum menjalankan).
3. Buka proyek di NetBeans, lalu **Run Project**. Aplikasi dimulai dari halaman Login.
4. Pilih **Daftar**, lalu buat akun **Business** dan akun **Customer**.

### Alur pengujian

1. Login sebagai **Business** → *Makanan*: tambah makanan → *Produksi*: catat produksi (surplus dihitung otomatis) → *Surplus*: buat penawaran (diskon, jumlah, waktu pengambilan).
2. Logout, login sebagai **Customer** → katalog menampilkan penawaran dari semua Business → pilih makanan dan jumlah → **Reservasi** → catat kode reservasi.
3. Logout, login kembali sebagai **Business** pemilik makanan tersebut → *Reservasi*: reservasi masuk dan statusnya dapat diperbarui.

Catatan: Business hanya melihat reservasi untuk makanan miliknya sendiri. Reservasi untuk makanan bisnis lain tidak akan muncul di akunnya.
