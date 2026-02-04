-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: inventory_db
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `id` int NOT NULL AUTO_INCREMENT,
  `action` varchar(20) NOT NULL,
  `item_id` int DEFAULT NULL,
  `product_name` varchar(120) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES (1,'ADD',NULL,'Nature Spring','Added (90 pcs) | Status: IN STOCK','2026-02-03 08:22:58'),(2,'UPDATE',NULL,'Marlboro Black','Qty: 6 → 5 | Status: LOW STOCK','2026-02-03 08:23:48'),(3,'UPDATE',NULL,'Nature Spring','Qty: 90 → 10 | Status: IN STOCK','2026-02-03 08:29:06'),(4,'UPDATE',NULL,'Nature Spring','Qty: 10 → 9 | Status: IN STOCK','2026-02-03 08:29:14'),(5,'UPDATE',NULL,'Nature Spring','Qty: 9 → 5 | Status: LOW STOCK','2026-02-03 08:29:23'),(6,'DELETE',NULL,'Nature Spring','Removed product.','2026-02-03 08:32:49'),(7,'DELETE',NULL,'Marlboro Black','Removed product.','2026-02-03 08:32:52'),(8,'ADD',NULL,'Sheal','Added (9 pcs) | Status: IN STOCK','2026-02-03 08:33:12'),(9,'UPDATE',NULL,'Sheal','Qty: 9 → 5 | Status: LOW STOCK','2026-02-03 08:33:21'),(10,'UPDATE',NULL,'Sheal','Qty: 5 → 9 | Status: IN STOCK','2026-02-03 08:33:34'),(11,'UPDATE',NULL,'Sheal','Qty: 9 → 9 | Status: IN STOCK','2026-02-03 08:36:08'),(12,'ADD',NULL,'Sheal','Added (10 pcs) | Status: IN STOCK','2026-02-03 08:36:14'),(13,'UPDATE',NULL,'Sheal','Qty: 10 → 8 | Status: IN STOCK','2026-02-03 08:36:25'),(14,'DELETE',NULL,'Sheal','Removed product.','2026-02-03 08:36:45'),(15,'UPDATE',NULL,'Sheal','Qty: 9 → 8 | Status: IN STOCK','2026-02-04 13:52:58'),(16,'UPDATE',NULL,'Sheal','Qty: 8 → 7 | Status: LOW STOCK','2026-02-04 13:54:22'),(17,'UPDATE',NULL,'Sheal','Qty: 7 → 9 | Status: LOW STOCK','2026-02-04 13:54:32'),(18,'UPDATE',NULL,'Sheal','Qty: 9 → 10 | Status: LOW STOCK','2026-02-04 13:54:39'),(19,'UPDATE',NULL,'Sheal','Qty: 10 → 11 | Status: IN STOCK','2026-02-04 13:54:47'),(20,'UPDATE',NULL,'Sheal','Qty: 11 → 11 | Status: IN STOCK','2026-02-04 14:11:58'),(21,'UPDATE',NULL,'Sheal','Qty: 11 → 11 | Status: IN STOCK','2026-02-04 14:12:43'),(22,'UPDATE',NULL,'Sheal','Qty: 11 → 11 | Status: IN STOCK','2026-02-04 14:23:18'),(23,'UPDATE',NULL,'Malonggay Pandesal','Qty: 97 → 97 | Status: IN STOCK','2026-02-04 14:24:42'),(24,'UPDATE',NULL,'Malonggay Pandesal','Qty: 97 → 97 | Status: IN STOCK','2026-02-04 14:25:28'),(25,'UPDATE',NULL,'Malonggay Pandesal','Qty: 97 → 9 | Status: LOW STOCK','2026-02-04 14:25:40'),(26,'ADD',NULL,'Malonggay Pandesal','Added (9 pcs) | Status: LOW STOCK','2026-02-04 14:25:46'),(27,'ADD',NULL,'Kopiko pink','Added (100 pcs) | Status: IN STOCK','2026-02-04 14:27:52'),(28,'DELETE',NULL,'Malonggay Pandesal','Removed product.','2026-02-04 14:28:00'),(29,'ADD',NULL,'Cheng','Added (100 pcs) | Status: IN STOCK','2026-02-04 15:57:53'),(30,'DELETE',NULL,'Cheng','Removed product.','2026-02-04 15:57:57');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  `category` varchar(80) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_images`
--

DROP TABLE IF EXISTS `product_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_images` (
  `product_id` int NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  CONSTRAINT `fk_product_images` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_images`
--

LOCK TABLES `product_images` WRITE;
/*!40000 ALTER TABLE `product_images` DISABLE KEYS */;
INSERT INTO `product_images` VALUES (2,'C:\\Users\\Sean Badaguas\\IdeaProjects\\PIS-OOP-M2\\product_images\\e745e8d4-a18b-41f9-96e8-01d7704010be.jpg'),(11,'C:\\Users\\Sean Badaguas\\IdeaProjects\\PIS-OOP-M2\\product_images\\8ff4146e-a6b6-4128-9e64-f7d4e2aa2a9c.png');
/*!40000 ALTER TABLE `product_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(120) NOT NULL,
  `category` varchar(80) NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (2,'Malonggay Pandesal','brid',9,5.00,'2026-01-31 15:58:57'),(5,'Mountain Dew Mismo','Beverage',100,20.00,'2026-02-02 14:39:16'),(9,'Marlboro Black','Cigarette',87,10.00,'2026-02-02 14:52:15'),(11,'Sheal','Black market',11,10.00,'2026-02-03 08:33:12'),(14,'Kopiko pink','kape',100,12.00,'2026-02-04 14:27:52');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'Staff',
  `reset_code` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','Admin123#','Admin',NULL),(2,'Sheal','Sheal0313@','Staff','1m5wadVimFWB'),(3,'ching','Ching123!','Staff','NLLFEiM06vJ5'),(4,'brent','brent123!','Staff','DwLMt9koTl7A'),(5,'Sirdonpogi','don1234!','Staff','LTp6MTN7eeBC'),(6,'jazzel','Pasmado123!','Staff','0g9tU3p5I80x');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-05  1:36:11
