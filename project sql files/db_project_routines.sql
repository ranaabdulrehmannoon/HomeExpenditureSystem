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
-- Temporary view structure for view `view_active_expense_categories_and_types`
--

DROP TABLE IF EXISTS `view_active_expense_categories_and_types`;
/*!50001 DROP VIEW IF EXISTS `view_active_expense_categories_and_types`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_active_expense_categories_and_types` AS SELECT 
 1 AS `category_id`,
 1 AS `category_name`,
 1 AS `type_id`,
 1 AS `type_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_user_detailed_expenses`
--

DROP TABLE IF EXISTS `view_user_detailed_expenses`;
/*!50001 DROP VIEW IF EXISTS `view_user_detailed_expenses`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_user_detailed_expenses` AS SELECT 
 1 AS `expense_id`,
 1 AS `user_id`,
 1 AS `Name`,
 1 AS `category`,
 1 AS `type`,
 1 AS `amount`,
 1 AS `description`,
 1 AS `expense_date`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_user_budget_summary`
--

DROP TABLE IF EXISTS `view_user_budget_summary`;
/*!50001 DROP VIEW IF EXISTS `view_user_budget_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_user_budget_summary` AS SELECT 
 1 AS `UserID`,
 1 AS `Name`,
 1 AS `total_budget`,
 1 AS `total_expenses`,
 1 AS `total_savings`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_monthly_expenses_by_category`
--

DROP TABLE IF EXISTS `view_monthly_expenses_by_category`;
/*!50001 DROP VIEW IF EXISTS `view_monthly_expenses_by_category`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_monthly_expenses_by_category` AS SELECT 
 1 AS `user_id`,
 1 AS `Name`,
 1 AS `category`,
 1 AS `month`,
 1 AS `year`,
 1 AS `total_spent`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_savings_summary_by_month`
--

DROP TABLE IF EXISTS `view_savings_summary_by_month`;
/*!50001 DROP VIEW IF EXISTS `view_savings_summary_by_month`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_savings_summary_by_month` AS SELECT 
 1 AS `user_id`,
 1 AS `Name`,
 1 AS `month`,
 1 AS `year`,
 1 AS `total_savings`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_active_expense_categories_and_types`
--

/*!50001 DROP VIEW IF EXISTS `view_active_expense_categories_and_types`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_active_expense_categories_and_types` AS select `c`.`category_id` AS `category_id`,`c`.`category_name` AS `category_name`,`t`.`type_id` AS `type_id`,`t`.`type_name` AS `type_name` from (`expensecategories` `c` join `expensetypes` `t` on((`c`.`category_id` = `t`.`category_id`))) where ((`c`.`is_active` = 1) and (`t`.`is_active` = 1)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_user_detailed_expenses`
--

/*!50001 DROP VIEW IF EXISTS `view_user_detailed_expenses`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_user_detailed_expenses` AS select `e`.`expense_id` AS `expense_id`,`e`.`user_id` AS `user_id`,`u`.`Name` AS `Name`,`e`.`category` AS `category`,`e`.`type` AS `type`,`e`.`amount` AS `amount`,`e`.`description` AS `description`,`e`.`expense_date` AS `expense_date` from (`expenses` `e` join `user` `u` on((`e`.`user_id` = `u`.`UserID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_user_budget_summary`
--

/*!50001 DROP VIEW IF EXISTS `view_user_budget_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_user_budget_summary` AS select `u`.`UserID` AS `UserID`,`u`.`Name` AS `Name`,ifnull(sum(`b`.`total_amount`),0) AS `total_budget`,ifnull((select sum(`e`.`amount`) from `expenses` `e` where (`e`.`user_id` = `u`.`UserID`)),0) AS `total_expenses`,ifnull((select sum(`s`.`amount`) from `savings` `s` where (`s`.`user_id` = `u`.`UserID`)),0) AS `total_savings` from (`user` `u` left join `budget` `b` on((`u`.`UserID` = `b`.`user_id`))) group by `u`.`UserID` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_monthly_expenses_by_category`
--

/*!50001 DROP VIEW IF EXISTS `view_monthly_expenses_by_category`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_monthly_expenses_by_category` AS select `e`.`user_id` AS `user_id`,`u`.`Name` AS `Name`,`e`.`category` AS `category`,month(`e`.`expense_date`) AS `month`,year(`e`.`expense_date`) AS `year`,sum(`e`.`amount`) AS `total_spent` from (`expenses` `e` join `user` `u` on((`e`.`user_id` = `u`.`UserID`))) group by `e`.`user_id`,`e`.`category`,month(`e`.`expense_date`),year(`e`.`expense_date`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_savings_summary_by_month`
--

/*!50001 DROP VIEW IF EXISTS `view_savings_summary_by_month`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_savings_summary_by_month` AS select `s`.`user_id` AS `user_id`,`u`.`Name` AS `Name`,month(`s`.`saving_date`) AS `month`,year(`s`.`saving_date`) AS `year`,sum(`s`.`amount`) AS `total_savings` from (`savings` `s` join `user` `u` on((`s`.`user_id` = `u`.`UserID`))) group by `s`.`user_id`,month(`s`.`saving_date`),year(`s`.`saving_date`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-18 20:16:43
