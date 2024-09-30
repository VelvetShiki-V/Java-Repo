-- MySQL dump 10.13  Distrib 8.0.39, for Linux (x86_64)
--
-- Host: localhost    Database: cloud_model
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE IF NOT EXISTS `cloud_model` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cloud_model`;
--
-- Table structure for table `model`
--

DROP TABLE IF EXISTS `model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `model` (
  `mid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '元器件name',
  `owner` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者',
  `labels` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签信息',
  `properties` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '元器件属性',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `children` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '逻辑外键指针',
  `stock` int DEFAULT NULL COMMENT '库存',
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='元器件数据库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model`
--

LOCK TABLES `model` WRITE;
/*!40000 ALTER TABLE `model` DISABLE KEYS */;
INSERT INTO `model` VALUES ('1834157168606515201','ppppppppp','hk416','zzz','nice','2024-09-12 17:08:41','2024-09-12 17:08:41','xiao 416',30),('81501947948957712','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('81501956538892308','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('81501956538892309','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('81855058685198337','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84837926357172225','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84837934947106818','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84837956421943300','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84837965011877893','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84837965011877894','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84837969306845191','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84840103905591304','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84840103905591305','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84840116790493194','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84840142560296971','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84840151150231564','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10),('84845919291310093','Conductant','shiki','nice','3 orm',NULL,'2024-09-01 06:46:23','123123',10);
/*!40000 ALTER TABLE `model` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-18 14:31:06
