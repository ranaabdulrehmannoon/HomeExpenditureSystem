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
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `expense_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `category` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `expense_date` date NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`expense_id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
INSERT INTO `expenses` VALUES (1,1,'Education','College',10000.00,'Ahmad Fee','2025-05-06','2025-05-09 18:34:49'),(2,1,'Transport','Bus',2000.00,'School Bus','2025-05-09','2025-05-09 18:49:48'),(3,1,'Entertainment','Movie',3000.00,'Cinema','2025-05-09','2025-05-09 18:50:01'),(4,1,'Insurance','Vehicle',3000.00,'vehicle','2025-05-20','2025-05-09 18:54:18'),(5,1,'Transport','Taxi',400.00,'Office','2025-05-10','2025-05-10 17:59:13'),(102,1,'Food','debit',150.50,'Groceries at supermarket','2024-01-05','2025-05-14 16:01:19'),(103,1,'Transport','debit',45.00,'Bus pass renewal','2024-01-10','2025-05-14 16:01:19'),(104,1,'Rent','debit',1200.00,'January apartment rent','2024-01-01','2025-05-14 16:01:19'),(105,1,'Utilities','debit',320.75,'Electricity and water bill','2024-01-15','2025-05-14 16:01:19'),(106,1,'Salary','credit',3000.00,'Monthly salary','2024-01-31','2025-05-14 16:01:19'),(107,1,'Food','debit',160.00,'Dining out with friends','2024-02-03','2025-05-14 16:01:19'),(108,1,'Health','debit',95.00,'Pharmacy purchase','2024-02-07','2025-05-14 16:01:19'),(109,1,'Transport','debit',60.00,'Ride-share charges','2024-02-12','2025-05-14 16:01:19'),(110,1,'Entertainment','debit',120.00,'Concert ticket','2024-02-16','2025-05-14 16:01:19'),(111,1,'Gift','debit',250.00,'Birthday present for friend','2024-02-20','2025-05-14 16:01:19'),(112,1,'Salary','credit',3000.00,'February salary','2024-02-29','2025-05-14 16:01:19'),(113,1,'Groceries','debit',140.00,'Weekly grocery shopping','2024-03-04','2025-05-14 16:01:19'),(114,1,'Subscription','debit',15.99,'Streaming service','2024-03-08','2025-05-14 16:01:19'),(115,1,'Rent','debit',1200.00,'March rent','2024-03-01','2025-05-14 16:01:19'),(116,1,'Utilities','debit',310.25,'Internet and electricity','2024-03-14','2025-05-14 16:01:19'),(117,1,'Salary','credit',3000.00,'March salary','2024-03-31','2025-05-14 16:01:19'),(118,1,'Travel','debit',400.00,'Weekend trip expenses','2024-04-05','2025-05-14 16:01:19'),(119,1,'Food','debit',170.50,'Dinner with colleagues','2024-04-08','2025-05-14 16:01:19'),(120,1,'Transport','debit',55.00,'Train ticket','2024-04-11','2025-05-14 16:01:19'),(121,1,'Health','debit',125.00,'Doctor visit','2024-04-15','2025-05-14 16:01:19'),(122,1,'Salary','credit',3100.00,'April salary','2024-04-30','2025-05-14 16:01:19'),(123,1,'Entertainment','debit',95.00,'Theater tickets','2024-05-03','2025-05-14 16:01:19'),(124,1,'Groceries','debit',135.75,'Weekly shopping','2024-05-06','2025-05-14 16:01:19'),(125,1,'Subscription','debit',15.99,'Monthly app subscription','2024-05-09','2025-05-14 16:01:19'),(126,1,'Education','College',1000.00,'Tution fee','2025-05-16','2025-05-16 07:20:43'),(127,1,'Insurance','Vehicle',2500.00,'Corolla','2025-05-16','2025-05-16 11:15:52');
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 20:16:42
