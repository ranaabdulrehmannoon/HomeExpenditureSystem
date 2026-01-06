-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: db_project
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `expensetypes`
--

DROP TABLE IF EXISTS `expensetypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expensetypes` (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `type_name` varchar(50) NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`type_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `expensetypes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `expensecategories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expensetypes`
--

LOCK TABLES `expensetypes` WRITE;
/*!40000 ALTER TABLE `expensetypes` DISABLE KEYS */;
INSERT INTO `expensetypes` VALUES (1,1,'Health',1),(2,1,'Vehicle',1),(3,1,'Life',1),(4,2,'Electricity',1),(5,2,'Gas',1),(6,2,'Water',1),(7,2,'Internet',1),(8,3,'School',1),(9,3,'College',1),(10,3,'University',1),(11,4,'Consultation',1),(12,4,'Surgery',1),(13,4,'Medication',1),(14,5,'Bus',1),(15,5,'Taxi',1),(16,5,'Fuel',1),(17,5,'Train',1),(18,6,'Formal',1),(19,6,'Casual',1),(20,6,'Seasonal',1),(21,7,'Movie',1),(22,7,'Games',1),(23,7,'Outdoor Activity',1),(24,8,'Gifts',1),(25,8,'Donations',1),(26,8,'Unexpected',1),(27,9,'Home',1),(28,9,'Office',1),(29,9,'Shop',1);
/*!40000 ALTER TABLE `expensetypes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 20:16:41
