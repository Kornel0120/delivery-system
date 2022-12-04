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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (14,'Admin@Admin.com','$2a$10$JyIvhY/oQ.WF8PCG7oMOLuh1IhJ9lGXc/NXuVplYDKa62jXGUppdO'),(15,'Employee@Employee.com','$2a$10$kD2JEmiFusUJDWlwlAJ6S.H.EWV6CZBAQpr984SzFYRjjb9dU6Ubu'),(16,'User@User.com','$2a$10$koSKKZPIp5gRiP3qshywo.fthwvJEv8HYI6qcAv9lJ0f5dTRnbuw6'),(17,'Employee2@Employee.com','$2a$10$P6y5WbaY5wL8qfkbGkPTA.ibmNbkx0pRnWH9CoMV4DaEnkcGk0tJK'),(18,'Employee3@Employee.com','$2a$10$fOcawIVbzRYksWkY8H408u8JAMgy2JaXE70.ugqIzMwpwWFoV920S'),(19,'Employee4@Employee.com','$2a$10$TaPYDkVfjgngUzf4GAL9OeDI5gJh7qa4Q1FtwaATE8U3O/atptNHG'),(20,'User2@User.com','$2a$10$C1Oifdo0JmppI9GIw/GEkuobZcnsCkIlx5/cqlQ9hYGhsDEh0k03m'),(21,'User3@User.com','$2a$10$VkLcyGeQHQ/tnnby/PY.TO4WLiwNhDEa3vogTMX7WeAwx7rzOG3Mm'),(22,'Employee10@Employee.com','$2a$10$X0C7ymm/TyZwraXdJlxiK.d2EZwfTOljvPDbf9mnATkQOl.1fp6V.'),(23,'Employee11@Employee.com','$2a$10$7LeyvuC/677ywtop77.XMuleXqjpq.hON4hb1XjnoHixYDB39Skh6');
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

-- Dump completed on 2022-12-04 23:02:07
