-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 23, 2026 at 03:26 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `store_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`id`, `user_id`, `product_id`, `quantity`, `added_at`) VALUES
(1, 2, 1, 2, '2026-06-22 16:28:16'),
(2, 2, 3, 1, '2026-06-22 16:28:16');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `full_name` varchar(150) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  `total_price` double NOT NULL DEFAULT 0,
  `status` varchar(20) NOT NULL DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `full_name`, `phone`, `address`, `total_price`, `status`, `created_at`) VALUES
(1, 4, 'mmdok', '09137759803', 'oshey', 18000, 'pending', '2026-06-22 20:28:25'),
(2, 4, 'mmdok', '09137759803', 'oshey', 25000, 'pending', '2026-06-22 22:42:28'),
(3, 4, 'mmdok', '09137759803', 'oshey', 257000, 'pending', '2026-06-22 22:44:44'),
(4, 4, 'mmdok', '09137759803', 'oshey', 107000, 'pending', '2026-06-23 10:24:36');

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(150) NOT NULL,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `product_id`, `product_name`, `price`, `quantity`) VALUES
(1, 1, 4, 'نوشابه', 18000, 1),
(2, 2, 7, 'چیپس', 25000, 1),
(3, 3, 3, 'سیب قرمز', 48000, 4),
(4, 3, 6, 'تن ماهی', 65000, 1),
(5, 4, 8, 'رب گوجه', 42000, 1),
(6, 4, 56, 'mmd', 65000, 1);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `category` varchar(60) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `price`, `image_url`, `category`, `created_at`) VALUES
(1, 'نان بربری', 15000, 'http://localhost/Foodino/upload/products/Bread1.png', 'Bread', '2026-06-22 16:28:16'),
(2, 'شیر کم‌چرب', 32000, 'http://localhost/Foodino/upload/products/Dairy1.png', 'Dairy', '2026-06-22 16:28:16'),
(3, 'سیب قرمز', 48000, 'http://localhost/Foodino/upload/products/Fruits1.png', 'Fruits', '2026-06-22 16:28:16'),
(4, 'نوشابه', 18000, 'http://localhost/Foodino/upload/products/Drinks1.png', 'Drinks', '2026-06-22 16:28:16'),
(5, 'مرغ تازه', 135000, 'http://localhost/Foodino/upload/products/Protein1.png', 'Protein', '2026-06-22 16:28:16'),
(6, 'تن ماهی', 65000, 'http://localhost/Foodino/upload/products/CannedFoods1.png', 'Canned', '2026-06-22 16:28:16'),
(7, 'چیپس', 25000, 'http://localhost/Foodino/upload/products/Snacks1.png', 'Snacks', '2026-06-22 16:28:16'),
(8, 'رب گوجه', 42000, 'http://localhost/Foodino/upload/products/Condiments1.png', 'Condiments', '2026-06-22 16:28:16'),
(56, 'mmd', 65000, '', NULL, '2026-06-23 08:59:53');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','customer') NOT NULL DEFAULT 'customer',
  `full_name` varchar(150) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_persian_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `full_name`, `phone`, `address`, `created_at`) VALUES
(1, 'fari', '123', 'admin', 'فریبا مدیر', '09120000000', 'تهران', '2026-06-22 16:28:16'),
(2, 'qazal', '123', 'customer', 'غزل خریدار', '09121111111', 'تهران، خیابان آزادی', '2026-06-22 16:28:16'),
(3, 'a', 'a', 'customer', 'کاربر تستی', '09122222222', 'اصفهان', '2026-06-22 16:28:16'),
(4, 'mmdoo', '$2y$10$GD7eTipmqSOT6myaIzQXqexyTMn3wGpLUHPEgSAbJ03F9HjqbuB92', 'customer', 'mmdok', '09137759803', 'oshey', '2026-06-22 20:26:52'),
(5, 'mmeok', '$2y$10$BmwWar676/LfTUGKbVKq7eEdn/iPidmbqitpk84gV4umykhS4g./O', 'admin', '', NULL, NULL, '2026-06-22 20:28:51'),
(6, 'mmdok', '$2y$10$Ea4sp.q4ZOXHN3C/cznlbuzsHq7QkpWiM6KIUgAqrkU9pcaPL/Nwe', 'admin', '', NULL, NULL, '2026-06-22 20:29:24'),
(7, 'test', '$2y$10$mybWm3T2DPIeC3PBk4s4Z.N.00aikJMADUUcpaht6xLoOsunUlslm', 'customer', '', NULL, NULL, '2026-06-23 11:00:41');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_user_product` (`user_id`,`product_id`),
  ADD KEY `idx_cart_user` (`user_id`),
  ADD KEY `idx_cart_product` (`product_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_orders_user` (`user_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_items_order` (`order_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
