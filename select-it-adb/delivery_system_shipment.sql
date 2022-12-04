-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: delivery_system
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `shipment`
--

DROP TABLE IF EXISTS `shipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shipment` (
  `shipment_id` bigint NOT NULL AUTO_INCREMENT,
  `emp_id` bigint DEFAULT NULL,
  `client_id` bigint DEFAULT NULL,
  `payment_type_id` bigint DEFAULT NULL,
  `shipment_size_id` bigint DEFAULT NULL,
  `package_point_id` bigint DEFAULT NULL,
  `products_price` decimal(10,0) DEFAULT NULL,
  `delivery_cost` decimal(10,0) DEFAULT NULL,
  `final_price` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`shipment_id`),
  KEY `payment_type_id` (`payment_type_id`),
  KEY `package_point_id` (`package_point_id`),
  KEY `shipment_size_id` (`shipment_size_id`),
  KEY `emp_id` (`emp_id`),
  KEY `client_id` (`client_id`),
  CONSTRAINT `shipment_ibfk_1` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_type` (`payment_type_id`),
  CONSTRAINT `shipment_ibfk_2` FOREIGN KEY (`package_point_id`) REFERENCES `package_point` (`package_point_id`),
  CONSTRAINT `shipment_ibfk_3` FOREIGN KEY (`shipment_size_id`) REFERENCES `shipment_size` (`shipment_size_id`),
  CONSTRAINT `shipment_ibfk_4` FOREIGN KEY (`emp_id`) REFERENCES `employees` (`emp_id`),
  CONSTRAINT `shipment_ibfk_5` FOREIGN KEY (`client_id`) REFERENCES `client` (`client_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shipment`
--

LOCK TABLES `shipment` WRITE;
/*!40000 ALTER TABLE `shipment` DISABLE KEYS */;
INSERT INTO `shipment` VALUES (3,8,4,1,1,1,5829,1000,6829),(4,8,4,1,1,3,5829,1000,6829),(5,8,5,2,2,2,5829,1000,6829),(6,8,6,4,3,1,12314,1000,13314),(7,9,4,3,1,3,2355,1000,3355),(8,9,5,2,2,2,5232,1000,6232),(9,9,6,4,3,1,2342,1000,3342),(10,10,4,3,1,3,6346,1000,7346),(11,10,5,1,2,2,75634,1000,76634),(12,10,6,4,3,1,5342,1000,6342),(13,11,4,2,1,3,3229,1000,4229),(14,11,5,3,2,2,1229,1000,2229),(15,11,6,4,3,1,54329,1000,55329);
/*!40000 ALTER TABLE `shipment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-04 23:02:07
