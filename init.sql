-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.43 - MySQL Community Server - GPL
-- Server OS:                    Linux
-- HeidiSQL Version:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table springboot_db.bags
CREATE TABLE IF NOT EXISTS `bags` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `user_id` bigint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_bags_user_id` (`user_id`),
  CONSTRAINT `fk_bags_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table springboot_db.bags: ~5 rows (approximately)
INSERT INTO `bags` (`id`, `name`, `description`, `user_id`, `created_at`, `updated_at`) VALUES
	(1, 'Work Bag', 'Professional items for office', 4, '2025-09-26 12:15:01', '2025-09-26 12:15:01'),
	(2, 'Travel Bag', 'Items for business trips', 4, '2025-09-26 12:15:01', '2025-09-26 12:15:01'),
	(3, 'Gym Bag', 'Fitness and workout gear', 2, '2025-09-26 12:15:01', '2025-09-26 12:15:01'),
	(4, 'Shopping Bag', 'Daily shopping items', 2, '2025-09-26 12:15:01', '2025-09-26 12:15:01'),
	(5, 'Tech Bag', 'Electronic gadgets and accessories', 3, '2025-09-26 12:15:01', '2025-09-26 12:15:01');

-- Dumping structure for table springboot_db.bag_items
CREATE TABLE IF NOT EXISTS `bag_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT '1',
  `bag_id` bigint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_bag_items_bag_id` (`bag_id`),
  CONSTRAINT `fk_bag_items_bag` FOREIGN KEY (`bag_id`) REFERENCES `bags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table springboot_db.bag_items: ~16 rows (approximately)
INSERT INTO `bag_items` (`id`, `name`, `description`, `price`, `quantity`, `bag_id`, `created_at`, `updated_at`) VALUES
	(1, 'Laptop', 'MacBook Pro 16 inch', 2500.00, 1, 1, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(2, 'Wireless Mouse', 'Logitech MX Master 3', 99.99, 1, 1, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(3, 'Notebook', 'Moleskine ruled notebook', 25.50, 2, 1, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(4, 'Pen Set', 'Premium ballpoint pens', 45.00, 1, 1, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(5, 'Power Bank', 'Anker 20000mAh portable charger', 65.99, 1, 2, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(6, 'Travel Adapter', 'Universal travel plug adapter', 29.99, 1, 2, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(7, 'Headphones', 'Sony WH-1000XM4', 349.99, 1, 2, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(8, 'Water Bottle', 'Stainless steel water bottle', 24.99, 1, 3, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(9, 'Towel', 'Quick-dry microfiber towel', 19.99, 2, 3, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(10, 'Protein Powder', 'Whey protein supplement', 45.99, 1, 3, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(11, 'Reusable Bags', 'Eco-friendly shopping bags', 15.99, 5, 4, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(12, 'Grocery List App', 'Premium subscription', 4.99, 1, 4, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(13, 'iPhone', 'iPhone 15 Pro', 1199.00, 1, 5, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(14, 'iPad', 'iPad Air with Apple Pencil', 749.00, 1, 5, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(15, 'Cables', 'USB-C and Lightning cables', 39.99, 3, 5, '2025-09-26 12:15:18', '2025-09-26 12:15:18'),
	(16, 'Wireless Charger', 'MagSafe compatible charger', 79.99, 1, 5, '2025-09-26 12:15:18', '2025-09-26 12:15:18');

-- Dumping structure for table springboot_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_users_email` (`email`),
  KEY `idx_users_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- Dumping data for table springboot_db.users: ~3 rows (approximately)
-- Sample password for all seeded users: Password123!
INSERT INTO `users` (`id`, `name`, `email`, `password`, `created_at`) VALUES
	(2, 'Jane Smith', 'jane@example.com', '$2a$10$1l2vsoINZ0Px1ykTrsqZKOm5PO0sDJJ9P2eYQImGSitZj4xP4x1ZK', '2025-09-25 15:05:05'),
	(3, 'Bob Johnson', 'bob@example.com', '$2a$10$1l2vsoINZ0Px1ykTrsqZKOm5PO0sDJJ9P2eYQImGSitZj4xP4x1ZK', '2025-09-25 15:05:05'),
	(4, 'John Doe', 'john.doe@example.com', '$2a$10$1l2vsoINZ0Px1ykTrsqZKOm5PO0sDJJ9P2eYQImGSitZj4xP4x1ZK', '2025-09-26 12:14:26');


/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
