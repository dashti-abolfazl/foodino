# 🛒 Foodino — Grocery Shopping App

An Android grocery shopping app with an **Android (Kotlin)** client and a **PHP + MySQL**
backend. Users can register/login, browse and search products, add items to a cart, edit their
profile, and place orders. An admin can add/edit/delete products.

---

## ✨ Features

- Register & login with two roles: **admin** and **customer** (passwords stored as `bcrypt` hashes)
- Product grid with live search
- **Per-user shopping cart** (increase/decrease quantity, remove items)
- **User profile** (name, phone, address) — editable
- **Checkout form** with delivery info (pre-filled from the profile)
- **Shared hamburger menu** on every screen for easy navigation; the menu is **role-aware**
  (admin sees product/shop management, customer sees shop/cart)
- Admin panel for product management + image upload

---

## 🧱 Architecture

```
foodino-main/
├── app/                      ← Android app (Kotlin)
│   └── src/main/java/com/example/
│       ├── LoginScreen / SignupScreen      ← login & register
│       ├── ShopScreen                       ← shop + search + cart
│       ├── OrderScreen                      ← cart + checkout form
│       ├── CustomerProfile                  ← user profile
│       ├── AdminProductManagement           ← admin panel
│       ├── ApiService / RetrofitClient      ← network layer (Retrofit)
│       ├── DrawerHelper                     ← shared role-aware drawer menu
│       └── ... (models and adapters)
│
├── backend/Foodino-backend/  ← PHP backend
│   ├── config.php            ← DB connection + shared helpers
│   ├── login.php / register.php
│   ├── get_products.php / search_product.php
│   ├── insert_product.php / update_product.php / delete_product.php
│   ├── get_profile.php / update_profile.php
│   ├── add_to_cart.php / get_cart.php / update_cart.php / remove_from_cart.php
│   ├── place_order.php / get_orders.php
│   └── store_db.sql          ← full database script
│
├── unit-test/                ← JVM unit test (login function)
└── e2e-test/                 ← Espresso E2E test (login flow)
```

**Screen flow:** Splash → Welcome → LS → Login ⇄ Signup → (admin role → Admin panel) /
(customer role → Shop → Cart → Place order)

---

## ⚙️ Requirements

- **Android Studio** (Giraffe or newer) + Android SDK 34
- **XAMPP** (or any Apache + MySQL/MariaDB stack with PHP 8)
- An Android device/emulator with **minSdk 24**

---

## 🚀 Setup

### 1) Backend & database
1. Install XAMPP and start **Apache** and **MySQL**.
2. Copy the `backend/Foodino-backend` folder into your server root and name it `Foodino`:
   ```
   C:\xampp\htdocs\Foodino\
   ```
   (so the files are reachable at `http://localhost/Foodino/login.php`, etc.)
3. Open phpMyAdmin: `http://localhost/phpmyadmin`
4. Open the **SQL** tab and run the whole content of `store_db.sql` (or the query below).
   This creates the `store_db` database, all tables and sample data.

> 💡 Product image URLs are automatically rewritten to the request host (e.g. `10.0.2.2`),
> so they load correctly on the emulator.

### 2) Android app
1. Open the project in Android Studio and run **Gradle Sync**.
2. The server URL is configured in `RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2/Foodino/"
   ```
   - `10.0.2.2` means "the host machine's localhost" from inside the **emulator**.
   - When running on a **real phone**, change it to your machine's local IP,
     e.g. `http://192.168.1.10/Foodino/`.
3. Press **Run** (green button) to install the app on the emulator/phone.

### 3) Test accounts
| Username | Password | Role |
|----------|----------|------|
| `fari`  | `123` | admin |
| `qazal` | `123` | customer |
| `a`     | `a`   | customer |

---

## 🗄️ Full database query (from scratch)

Run this once in phpMyAdmin (SQL tab) — it creates the whole structure and sample data
(same as `backend/Foodino-backend/store_db.sql`):

```sql
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `store_db`
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_persian_ci;
USE `store_db`;

DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
    `id`         INT(11)      NOT NULL AUTO_INCREMENT,
    `username`   VARCHAR(100) NOT NULL,
    `password`   VARCHAR(255) NOT NULL,
    `role`       ENUM('admin','customer') NOT NULL DEFAULT 'customer',
    `full_name`  VARCHAR(150) DEFAULT NULL,
    `phone`      VARCHAR(20)  DEFAULT NULL,
    `address`    VARCHAR(255) DEFAULT NULL,
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

CREATE TABLE `products` (
    `id`         INT(11)      NOT NULL AUTO_INCREMENT,
    `name`       VARCHAR(150) DEFAULT NULL,
    `price`      DOUBLE       DEFAULT NULL,
    `image_url`  VARCHAR(255) DEFAULT NULL,
    `category`   VARCHAR(60)  DEFAULT NULL,
    `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

CREATE TABLE `cart` (
    `id`         INT(11)   NOT NULL AUTO_INCREMENT,
    `user_id`    INT(11)   NOT NULL,
    `product_id` INT(11)   NOT NULL,
    `quantity`   INT(11)   NOT NULL DEFAULT 1,
    `added_at`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_user_product` (`user_id`,`product_id`),
    KEY `idx_cart_user` (`user_id`),
    KEY `idx_cart_product` (`product_id`),
    CONSTRAINT `fk_cart_user`    FOREIGN KEY (`user_id`)    REFERENCES `users`(`id`)    ON DELETE CASCADE,
    CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

CREATE TABLE `orders` (
    `id`          INT(11)      NOT NULL AUTO_INCREMENT,
    `user_id`     INT(11)      NOT NULL,
    `full_name`   VARCHAR(150) NOT NULL,
    `phone`       VARCHAR(20)  NOT NULL,
    `address`     VARCHAR(255) NOT NULL,
    `total_price` DOUBLE       NOT NULL DEFAULT 0,
    `status`      VARCHAR(20)  NOT NULL DEFAULT 'pending',
    `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_orders_user` (`user_id`),
    CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

CREATE TABLE `order_items` (
    `id`           INT(11)      NOT NULL AUTO_INCREMENT,
    `order_id`     INT(11)      NOT NULL,
    `product_id`   INT(11)      DEFAULT NULL,
    `product_name` VARCHAR(150) NOT NULL,
    `price`        DOUBLE       NOT NULL,
    `quantity`     INT(11)      NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`),
    KEY `idx_items_order` (`order_id`),
    CONSTRAINT `fk_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

INSERT INTO `users` (`id`,`username`,`password`,`role`,`full_name`,`phone`,`address`) VALUES
(1,'fari','123','admin','Fariba Admin','09120000000','Tehran'),
(2,'qazal','123','customer','Qazal Buyer','09121111111','Tehran, Azadi St'),
(3,'a','a','customer','Test User','09122222222','Isfahan');

INSERT INTO `products` (`id`,`name`,`price`,`image_url`,`category`) VALUES
(1,'Barbari Bread',15000,'http://localhost/Foodino/upload/products/Bread1.png','Bread'),
(2,'Low-fat Milk',32000,'http://localhost/Foodino/upload/products/Dairy1.png','Dairy'),
(3,'Red Apple',48000,'http://localhost/Foodino/upload/products/Fruits1.png','Fruits'),
(4,'Soda',18000,'http://localhost/Foodino/upload/products/Drinks1.png','Drinks'),
(5,'Fresh Chicken',135000,'http://localhost/Foodino/upload/products/Protein1.png','Protein'),
(6,'Canned Tuna',65000,'http://localhost/Foodino/upload/products/CannedFoods1.png','Canned'),
(7,'Chips',25000,'http://localhost/Foodino/upload/products/Snacks1.png','Snacks'),
(8,'Tomato Paste',42000,'http://localhost/Foodino/upload/products/Condiments1.png','Condiments');

INSERT INTO `cart` (`user_id`,`product_id`,`quantity`) VALUES (2,1,2),(2,3,1);
COMMIT;
```

---

## 🧪 Tests

The project has two separate test folders (in the project root):

| Folder | Type | Run |
|--------|------|-----|
| [`unit-test/`](unit-test/) | Unit test for the `LoginValidator` login function (JVM) | `./gradlew test` |
| [`e2e-test/`](e2e-test/) | E2E login test with `test/test` (emulator) | `./gradlew connectedAndroidTest` |

Each folder has its own `README` with details. Both print `✅ PASS` on success.

---

## 🔌 API list (backend)

| Method | Path | Description |
|--------|------|-------------|
| POST | `register.php` | register a user |
| POST | `login.php` | login (returns role + `user_id` + profile) |
| GET  | `get_products.php` | list products |
| GET  | `search_product.php?query=` | search products |
| POST | `insert_product.php` | add product (admin) |
| POST | `update_product.php` | edit product (admin) |
| POST | `delete_product.php` | delete product (admin) |
| GET  | `get_profile.php?user_id=` | get profile |
| POST | `update_profile.php` | update profile |
| POST | `add_to_cart.php` | add to cart |
| GET  | `get_cart.php?user_id=` | view cart |
| POST | `update_cart.php` | change item quantity |
| POST | `remove_from_cart.php` | remove item from cart |
| POST | `place_order.php` | place an order |
| GET  | `get_orders.php?user_id=` | list orders |

---

## 🧰 Tech stack

**Android:** Kotlin · Retrofit + Gson · Glide · Material Components · RecyclerView · ViewBinding
**Backend:** PHP 8 · MySQL/MariaDB · mysqli (Prepared Statements)

---

## ⚠️ Notes

- This is an educational project; traffic uses **HTTP** (`usesCleartextTraffic=true`).
- To run on a real phone, change `BASE_URL` to your machine's local IP and make sure the phone
  and computer are on the same Wi-Fi network.
