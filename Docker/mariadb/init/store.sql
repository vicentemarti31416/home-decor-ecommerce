-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: store
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `store`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `store` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `store`;

--
-- Table structure for table `authorities`
--

DROP TABLE IF EXISTS `authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority` enum('ROLE_ADMIN','ROLE_USER') NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk91upmbueyim93v469wj7b2qh` (`user_id`),
  CONSTRAINT `FKk91upmbueyim93v469wj7b2qh` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorities`
--

LOCK TABLES `authorities` WRITE;
/*!40000 ALTER TABLE `authorities` DISABLE KEYS */;
INSERT INTO `authorities` VALUES (1,'ROLE_ADMIN','4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc'),(8,'ROLE_USER','b98f3367-cbaa-45d5-8bc4-320d6b890f4c'),(9,'ROLE_USER','06bf6eb4-f11b-4b5d-86bb-fe2ad42a5c49'),(11,'ROLE_USER','eb249f31-9ce9-4541-a1cd-bdb118c45f17');
/*!40000 ALTER TABLE `authorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_participants`
--

DROP TABLE IF EXISTS `chat_participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_participants` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `participant_id` varchar(255) NOT NULL,
  `chat_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_participants`
--

LOCK TABLES `chat_participants` WRITE;
/*!40000 ALTER TABLE `chat_participants` DISABLE KEYS */;
INSERT INTO `chat_participants` VALUES (1,'74d3d066-e6d3-4ade-97a9-406ee878d5f3',1),(2,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc',1),(9,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc',5),(10,'06bf6eb4-f11b-4b5d-86bb-fe2ad42a5c49',5),(11,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc',6),(12,'c360c162-17d3-4269-8548-9cb89f84509b',6);
/*!40000 ALTER TABLE `chat_participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chats`
--

DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `chat_room_type` enum('GROUP','PRIVATE','PUBLIC') DEFAULT NULL,
  `created` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chats`
--

LOCK TABLES `chats` WRITE;
/*!40000 ALTER TABLE `chats` DISABLE KEYS */;
INSERT INTO `chats` VALUES (1,'PRIVATE','2026-10-21 09:27:35.186979'),(5,'PRIVATE','2026-10-23 14:47:30.725248'),(6,'PRIVATE','2026-10-26 11:34:35.381895');
/*!40000 ALTER TABLE `chats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_favorites`
--

DROP TABLE IF EXISTS `customer_favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_favorites` (
  `customer_id` varchar(255) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  UNIQUE KEY `UK5bkqvrk4ph54ddb2dy0vrlrhb` (`customer_id`,`product_id`),
  CONSTRAINT `FKaymemcrnqvtm40pt3pbu064p4` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_favorites`
--

LOCK TABLES `customer_favorites` WRITE;
/*!40000 ALTER TABLE `customer_favorites` DISABLE KEYS */;
INSERT INTO `customer_favorites` VALUES ('74d3d066-e6d3-4ade-97a9-406ee878d5f3',2),('74d3d066-e6d3-4ade-97a9-406ee878d5f3',3),('74d3d066-e6d3-4ade-97a9-406ee878d5f3',9),('74d3d066-e6d3-4ade-97a9-406ee878d5f3',11),('74d3d066-e6d3-4ade-97a9-406ee878d5f3',13),('74d3d066-e6d3-4ade-97a9-406ee878d5f3',17),('74d3d066-e6d3-4ade-97a9-406ee878d5f3',18),('ce381434-a3dd-464e-9bb2-34d0f7727a2c',4),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',2),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',3),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',4),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',5),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',6),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',9),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',11),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e',12);
/*!40000 ALTER TABLE `customer_favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customers` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKeuat1oase6eqv195jvb71a93s` (`user_id`),
  CONSTRAINT `FKrh1g1a20omjmn6kurd35o3eit` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES ('74d3d066-e6d3-4ade-97a9-406ee878d5f3','Calle del percebe, 13','León','alutecval@gmail.com','lópez','josé','','26532','b98f3367-cbaa-45d5-8bc4-320d6b890f4c','https://res.cloudinary.com/ddjupcyst/image/upload/v1792593855/profile-picture-5b_eatn5p.jpg'),('ce381434-a3dd-464e-9bb2-34d0f7727a2c',NULL,NULL,'vicentemartiemailsender@gmail.com',NULL,NULL,NULL,NULL,'eb249f31-9ce9-4541-a1cd-bdb118c45f17','https://res.cloudinary.com/ddjupcyst/image/upload/v1793014155/profile-picture-2b_hcrgqj.jpg'),('d70c4491-07a7-40c2-9a7d-16f3a6b0f69e','Calle del percebe, 13','Madrid','vicentemarti31416@gmail.com','martí','vicente','','28017','4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc','https://res.cloudinary.com/ddjupcyst/image/upload/v1791892905/cwsnc78ic5f5pubsh6jj.png');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `quantity` bigint(20) DEFAULT NULL,
  `unit_price` decimal(38,2) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,5,3,8.04,'01879eaf-0c94-4d1f-93f4-f734b784b865'),(6,4,1,10.54,'57da6af7-dc31-49b7-ad76-dfc220b45eab'),(11,2,1,9.43,'9dc9543a-8160-4519-90af-5479a1e69a4e'),(13,3,4,6.91,'24f2de8c-b9b2-4bbc-a350-264370ed4dda'),(14,2,2,9.43,'24f2de8c-b9b2-4bbc-a350-264370ed4dda'),(16,9,2,14.07,'24f2de8c-b9b2-4bbc-a350-264370ed4dda'),(18,2,2,9.43,'7ec06085-a21c-40b8-bdd3-0eb6a35d80ea'),(19,3,1,6.91,'40995985-c09a-49f4-9ed3-720f9eca6045'),(26,1,3,NULL,'be39e589-721d-4519-b3b1-aebd69788926'),(27,4,2,NULL,'be39e589-721d-4519-b3b1-aebd69788926'),(28,9,1,NULL,'be39e589-721d-4519-b3b1-aebd69788926'),(29,2,1,9.43,'596d696e-3a64-4620-b909-9d81639c56f9'),(30,2,1,9.43,'cc4ea216-0c60-4db1-99bd-dcc0976913f4'),(31,5,1,8.04,'8396dae0-0649-42f0-a179-817d12cf1e7c'),(32,1,1,14.69,'fe473cac-214a-4775-9326-a943b1f0f159'),(33,12,1,8.61,'13c1f715-23c9-4d20-80e0-1477f03bb9a5'),(34,11,1,6.22,'7ee197ba-08e6-4968-924e-b074a6a380c0'),(35,13,1,10.40,'9e487b97-d35f-48a7-b76c-cda118c1a3d9'),(36,18,1,46.02,'b3025dc1-4962-45a4-a351-01bc2ec65619'),(37,14,1,15.96,'99d669c1-a1a1-498f-94c9-c924bf21e9ca'),(38,17,1,19.44,'521728c3-e192-4b2e-8214-b9140a3df9c8'),(41,15,2,1867.00,'ea12c26e-f551-4ec7-b212-153436ae402a'),(42,5,1,8.04,'ea12c26e-f551-4ec7-b212-153436ae402a'),(43,1,1,14.69,'17338242-093a-4945-8359-e24af24ad118'),(44,11,1,6.22,'ffe44566-cadb-453c-9757-ccfa005985ea'),(45,4,1,10.54,'7b3464d1-64e8-49df-a40c-c0e810287564'),(46,16,1,22.22,'8ef84eaa-d872-4f45-90f6-4e6549c7b4f1'),(50,18,1,46.02,'10b1e859-def0-42a6-8310-9b5fac1f8f4c'),(51,2,3,9.43,'10b1e859-def0-42a6-8310-9b5fac1f8f4c'),(52,9,2,14.07,'10b1e859-def0-42a6-8310-9b5fac1f8f4c'),(56,2,1,9.43,'faa8c377-f866-421f-9788-ef4604bf0945'),(57,1,1,14.69,'faa8c377-f866-421f-9788-ef4604bf0945'),(58,6,1,22.22,'faa8c377-f866-421f-9788-ef4604bf0945'),(59,2,1,9.43,'115eded8-da9e-4abd-a8d1-738319a9bb43'),(60,2,1,9.43,'a52e5760-653a-4869-b05c-0f123bb5a66e'),(64,2,1,NULL,'76223834-7eb2-4c64-bc4e-142ffdcd0ac3'),(72,3,1,6.91,'32649c06-af2b-4171-866e-2413469b7865'),(73,4,1,10.54,'9437c284-dd7d-4406-a3a1-d1243abf785c'),(84,4,1,10.54,'bd612861-22ec-4304-a4b3-dbcd7133bc67'),(85,5,1,8.04,'bd612861-22ec-4304-a4b3-dbcd7133bc67'),(86,7,1,22.68,'bd612861-22ec-4304-a4b3-dbcd7133bc67'),(87,8,1,21.94,'bd612861-22ec-4304-a4b3-dbcd7133bc67'),(88,9,1,14.07,'bd612861-22ec-4304-a4b3-dbcd7133bc67'),(125,10,1,5.36,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(126,11,1,6.22,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(127,12,1,8.61,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(128,13,1,10.40,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(129,14,1,15.96,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(130,15,1,1867.00,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(131,16,1,22.22,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(132,17,1,19.44,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(133,18,2,46.02,'a587852b-c5d1-4960-a1d4-d1a4353644ef'),(135,9,5,NULL,'c26dc91c-85d0-4195-8276-d97a332c9624'),(137,3,5,6.91,'754bd4f0-aabe-4cc4-9dfb-f3d50e3706a7'),(139,4,4,10.54,'754bd4f0-aabe-4cc4-9dfb-f3d50e3706a7'),(140,2,1,9.43,'754bd4f0-aabe-4cc4-9dfb-f3d50e3706a7'),(141,2,1,9.43,'e3a2eec8-433e-4ac7-ae31-1344b1f7f099'),(142,2,1,9.43,'08fe27a5-ee19-4566-a953-2d7bef17349c'),(143,2,1,9.43,'dd186bbf-f42a-4627-a623-d4913eba15bf'),(145,2,4,9.43,'903c34db-f8fb-456b-b5f4-cf6dfff55fb1'),(146,3,1,6.91,'351bb721-69e9-4857-a72a-6b03ae38fb90'),(147,3,2,6.91,'4a470dd0-fd20-492e-a57d-d0d68ba81f50'),(148,2,1,9.43,'4a470dd0-fd20-492e-a57d-d0d68ba81f50'),(149,3,1,6.91,'19cd205f-f47b-4852-94b2-116ead176985'),(150,2,1,9.43,'5e3a42fe-7ba3-4fbe-b552-b0211b23f6ac'),(151,2,1,9.43,'1cbbfacf-12bb-4b9c-b25a-4be1d550a18e'),(152,1,1,14.69,'83eb4302-3e10-4875-86cf-97bb0b1b3e15'),(153,4,1,10.54,'75cc3210-bb0f-4ad8-ae3c-db6ff11921a5'),(154,7,1,22.68,'a7fb78e4-6a81-4801-ac20-42c6ee4f2248'),(155,11,1,6.22,'4282220e-ed5d-4dd2-9430-bae9596825a3'),(156,6,1,22.22,'2d9212a3-ffaa-4e86-98ee-e78b90b211fc'),(157,12,1,8.61,'f6e40efb-9008-4566-af8f-5523c3ec905d'),(158,12,1,8.61,'48ba9c6a-8f9c-4aca-8411-460795716a36'),(159,4,1,10.54,'48ba9c6a-8f9c-4aca-8411-460795716a36'),(160,4,1,10.54,'4cf238dd-ccf0-4e8b-887d-395a00e3e0b3'),(161,4,1,10.54,'b14d7c37-d6f5-4e1b-82d7-88fcfc9f83d3'),(162,8,1,21.94,'87b864ca-9c9a-47eb-8a0d-c6920ba510bb'),(165,3,3,6.91,'a8dd9382-107f-4190-b0af-cfaa751f8fc1'),(166,4,1,10.54,'e7f340dc-4e3a-4d31-b089-bc728a12b411'),(170,15,1,1867.00,'d74342f8-c215-426c-a6d2-d4611238407f'),(171,13,1,10.40,'d74342f8-c215-426c-a6d2-d4611238407f'),(172,17,1,19.44,'d74342f8-c215-426c-a6d2-d4611238407f'),(175,12,4,8.61,'20d291c6-8c74-4032-8700-babd18da53fa'),(185,1,5,14.69,'88fd9df5-c7a0-4d0c-8d94-3c2a069ccc1c'),(186,4,1,10.54,'83db33d3-f5cd-4081-a134-1a8231cfc259'),(187,2,1,9.43,'8234cf11-ed21-4d3e-9d63-f059000bf6f0'),(188,16,1,22.22,'7fbe8742-70e0-4f43-92b1-898cccd26ef4'),(189,7,1,22.68,'dc139a16-a7ae-4504-8590-2ffdafe0c856'),(190,5,1,8.04,'90c15b68-a5f1-4e9c-9686-a2d54085050b'),(191,1,1,14.69,'1e723f1d-e6ee-4f61-84ed-37f2c69cdef0'),(192,5,1,8.04,'60c19be3-12cb-4192-969d-ef8f0a6a5f51'),(193,5,1,8.04,'862052fe-d5b7-4740-82cd-840f17e507d4'),(194,4,1,10.54,'80f41d4d-02be-48fd-98c3-3671f2f41cac'),(195,5,1,8.04,'6cc8f78d-8c46-47cb-ab57-20ea4cdd7285'),(205,12,2,8.61,'37a1869f-2802-43e0-a62d-6b84681997a6'),(206,11,1,6.22,'37a1869f-2802-43e0-a62d-6b84681997a6'),(207,9,3,14.07,'37a1869f-2802-43e0-a62d-6b84681997a6'),(208,3,1,6.91,'60dde0bb-d7a0-4180-862e-59dab6c5bc35'),(216,12,2,8.61,'9d026769-e155-4bf3-9fc3-5232f6b40d5f'),(217,11,1,6.22,'9d026769-e155-4bf3-9fc3-5232f6b40d5f'),(218,9,3,14.07,'9d026769-e155-4bf3-9fc3-5232f6b40d5f'),(220,2,3,9.43,'9d026769-e155-4bf3-9fc3-5232f6b40d5f'),(242,9,1,14.07,'fe544b7b-2991-4a4a-aa56-3ed4c636b03d'),(243,11,1,6.22,'fe544b7b-2991-4a4a-aa56-3ed4c636b03d'),(244,12,3,8.61,'fe544b7b-2991-4a4a-aa56-3ed4c636b03d'),(245,5,1,8.04,'3393ad74-975f-421e-a232-cf9a8b591c81'),(246,5,1,8.04,'66b896e8-6f3e-417d-8cfd-c23edefcf6d4'),(247,9,1,14.07,'e74b20e7-5d2b-4d75-a0fd-816a8861f9b0'),(248,12,1,8.61,'43426603-720b-4015-bca5-baaa87905f2d'),(258,12,2,8.61,'b36ef80c-f8ba-44f7-a643-4aeb5cf99dd3'),(259,9,3,14.07,'b36ef80c-f8ba-44f7-a643-4aeb5cf99dd3'),(260,11,5,6.22,'b36ef80c-f8ba-44f7-a643-4aeb5cf99dd3'),(279,5,1,8.04,'e0ad3d1a-2781-45a8-bbb3-f05963e302a8'),(280,4,3,10.54,'e0ad3d1a-2781-45a8-bbb3-f05963e302a8'),(281,9,2,14.07,'e0ad3d1a-2781-45a8-bbb3-f05963e302a8'),(282,2,1,9.43,'5ae20661-8330-4706-972a-ed5dee7d21d5'),(293,1,1,14.69,'8e2a584f-0585-4d49-8f3c-d718226b99a2'),(294,2,1,9.43,'8e2a584f-0585-4d49-8f3c-d718226b99a2'),(295,3,1,6.91,'8e2a584f-0585-4d49-8f3c-d718226b99a2'),(296,4,1,10.54,'8e2a584f-0585-4d49-8f3c-d718226b99a2'),(297,5,1,8.04,'8e2a584f-0585-4d49-8f3c-d718226b99a2'),(301,5,1,8.04,'d84d0abf-7d59-4e09-93c7-2cd4c95af1ce'),(302,9,1,14.07,'d84d0abf-7d59-4e09-93c7-2cd4c95af1ce'),(303,7,1,22.68,'d84d0abf-7d59-4e09-93c7-2cd4c95af1ce'),(307,11,1,6.22,'cf94589d-ab83-419a-84b5-f709e0018660'),(308,15,1,1867.00,'cf94589d-ab83-419a-84b5-f709e0018660'),(309,17,1,19.44,'cf94589d-ab83-419a-84b5-f709e0018660'),(311,2,1,9.43,'8ca03efa-1c6b-4e59-8a8e-615a1f27f09d'),(312,1,1,14.69,'8ca03efa-1c6b-4e59-8a8e-615a1f27f09d'),(313,3,1,6.91,'8b660e56-ba2e-462a-b266-e2333752bbea'),(314,3,1,6.91,'325e883b-2f82-403e-b7a2-264dfe125e96'),(315,9,1,14.07,'95139366-cff2-4a43-9757-b9b27dc64eb3');
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime(6) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `message_type` enum('CHAT','JOIN','LEAVE') DEFAULT NULL,
  `chat_id` bigint(20) DEFAULT NULL,
  `participant_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
INSERT INTO `messages` VALUES (2,'2026-10-22 12:28:11.000000','hola user','CHAT',1,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc'),(3,'2026-10-22 12:51:10.223000','hola admin','CHAT',2,'06bf6eb4-f11b-4b5d-86bb-fe2ad42a5c49'),(9,'2026-10-21 11:45:32.822530','hola admin','CHAT',1,'74d3d066-e6d3-4ade-97a9-406ee878d5f3'),(12,'2026-10-22 13:01:02.608815','cómo estás?','CHAT',1,'74d3d066-e6d3-4ade-97a9-406ee878d5f3'),(14,'2026-10-23 10:04:45.631244','Buenos días','CHAT',3,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc'),(15,'2026-10-23 15:10:43.410886','Hola buenos días','CHAT',5,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc'),(17,'2026-10-26 11:37:33.155452','Hola admin','CHAT',6,'c360c162-17d3-4269-8548-9cb89f84509b'),(18,'2026-10-26 11:37:59.269839','hola sender','CHAT',6,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc'),(19,'2026-10-26 12:27:43.118017','qué tal todo?','CHAT',6,'c360c162-17d3-4269-8548-9cb89f84509b'),(24,'2026-10-27 21:15:46.902319','de maravilla','CHAT',6,'4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc');
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth2_authorization`
--

DROP TABLE IF EXISTS `oauth2_authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) NOT NULL,
  `registered_client_id` varchar(100) NOT NULL,
  `principal_name` varchar(200) NOT NULL,
  `authorization_grant_type` varchar(100) NOT NULL,
  `authorized_scopes` varchar(1000) DEFAULT NULL,
  `attributes` text DEFAULT NULL,
  `state` varchar(500) DEFAULT NULL,
  `authorization_code_value` text DEFAULT NULL,
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
  `authorization_code_metadata` text DEFAULT NULL,
  `access_token_value` text DEFAULT NULL,
  `access_token_issued_at` timestamp NULL DEFAULT NULL,
  `access_token_expires_at` timestamp NULL DEFAULT NULL,
  `access_token_metadata` text DEFAULT NULL,
  `access_token_type` varchar(100) DEFAULT NULL,
  `access_token_scopes` varchar(1000) DEFAULT NULL,
  `oidc_id_token_value` text DEFAULT NULL,
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_metadata` text DEFAULT NULL,
  `refresh_token_value` text DEFAULT NULL,
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
  `refresh_token_metadata` text DEFAULT NULL,
  `user_code_value` text DEFAULT NULL,
  `user_code_issued_at` timestamp NULL DEFAULT NULL,
  `user_code_expires_at` timestamp NULL DEFAULT NULL,
  `user_code_metadata` text DEFAULT NULL,
  `device_code_value` text DEFAULT NULL,
  `device_code_issued_at` timestamp NULL DEFAULT NULL,
  `device_code_expires_at` timestamp NULL DEFAULT NULL,
  `device_code_metadata` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth2_authorization`
--

LOCK TABLES `oauth2_authorization` WRITE;
/*!40000 ALTER TABLE `oauth2_authorization` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauth2_authorization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth2_authorization_consent`
--

DROP TABLE IF EXISTS `oauth2_authorization_consent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) NOT NULL,
  `principal_name` varchar(200) NOT NULL,
  `authorities` varchar(1000) NOT NULL,
  PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth2_authorization_consent`
--

LOCK TABLES `oauth2_authorization_consent` WRITE;
/*!40000 ALTER TABLE `oauth2_authorization_consent` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauth2_authorization_consent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauth2_registered_client`
--

DROP TABLE IF EXISTS `oauth2_registered_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauth2_registered_client` (
  `id` varchar(100) NOT NULL,
  `client_id` varchar(100) NOT NULL,
  `client_id_issued_at` timestamp NULL DEFAULT NULL,
  `client_secret` varchar(200) DEFAULT NULL,
  `client_secret_expires_at` timestamp NULL DEFAULT NULL,
  `client_name` varchar(200) NOT NULL,
  `client_authentication_methods` varchar(1000) NOT NULL,
  `authorization_grant_types` varchar(1000) NOT NULL,
  `redirect_uris` varchar(1000) DEFAULT NULL,
  `post_logout_redirect_uris` varchar(1000) DEFAULT NULL,
  `scopes` varchar(1000) NOT NULL,
  `client_settings` varchar(2000) NOT NULL,
  `token_settings` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauth2_registered_client`
--

LOCK TABLES `oauth2_registered_client` WRITE;
/*!40000 ALTER TABLE `oauth2_registered_client` DISABLE KEYS */;
INSERT INTO `oauth2_registered_client` VALUES ('b9b14387-25d0-46c8-8406-2c11deff05f0','client','2026-06-05 10:56:52','{bcrypt}$2a$14$k4M/IICUdwmeTk0/nByDqee/dZ3YRPK6KlHHqEcIKUfVZR3R8.AX6','2026-06-06 10:56:52','b9b14387-25d0-46c8-8406-2c11deff05f0','client_secret_basic','refresh_token,authorization_code','http://127.0.0.1:8090/login/oauth2/code/gateway','http://127.0.0.1:8090/logged-out','openid,profile','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}','{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\n \"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\n \"settings.token.access-token-time-to-live\":[\"java.time.Duration\",300.000000000],\n \"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\n \"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\n \"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\n \"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');
/*!40000 ALTER TABLE `oauth2_registered_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` varchar(255) NOT NULL,
  `amount` decimal(38,2) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `updated_date` datetime(6) DEFAULT NULL,
  `status` enum('CANCELLED','CART','FAILED','PAID','PENDING') DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('01879eaf-0c94-4d1f-93f4-f734b784b865',24.12,'2026-06-10 09:42:55.330608','2026-06-10 09:42:55.330608','PAID','vicentemarti31416@gmail.com'),('08fe27a5-ee19-4566-a953-2d7bef17349c',9.43,'2026-09-14 09:04:29.418435','2026-09-14 09:04:29.418435','PAID','alutecval@gmail.com'),('10b1e859-def0-42a6-8310-9b5fac1f8f4c',102.45,'2026-07-03 10:12:21.216559','2026-07-03 10:12:21.216559','PAID','vicentemarti31416@gmail.com'),('115eded8-da9e-4abd-a8d1-738319a9bb43',9.43,'2026-07-06 12:51:11.310870','2026-07-06 12:51:11.310870','PAID','vicentemarti31416@gmail.com'),('13c1f715-23c9-4d20-80e0-1477f03bb9a5',8.61,'2026-07-03 09:35:21.764754','2026-07-03 09:35:21.764754','PAID','vicentemarti31416@gmail.com'),('17338242-093a-4945-8359-e24af24ad118',14.69,'2026-07-03 09:49:52.727815','2026-07-03 09:49:52.727815','PAID','vicentemarti31416@gmail.com'),('19cd205f-f47b-4852-94b2-116ead176985',6.91,'2026-09-18 08:34:45.281878','2026-09-21 09:03:24.056908','PAID','alutecval@gmail.com'),('1cbbfacf-12bb-4b9c-b25a-4be1d550a18e',9.43,'2026-09-21 09:18:15.815801','2026-09-21 09:18:27.048856','PAID','alutecval@gmail.com'),('1e723f1d-e6ee-4f61-84ed-37f2c69cdef0',14.69,'2026-10-06 13:25:14.705920','2026-10-06 13:25:42.970187','PAID','alutecval@gmail.com'),('20d291c6-8c74-4032-8700-babd18da53fa',34.44,'2026-09-28 10:32:21.728962','2026-09-28 11:26:23.359102','PAID','alutecval@gmail.com'),('24f2de8c-b9b2-4bbc-a350-264370ed4dda',74.64,'2026-06-15 12:07:45.820524','2026-06-15 12:07:45.820524','PAID','vicentemarti31416@gmail.com'),('2d9212a3-ffaa-4e86-98ee-e78b90b211fc',22.22,'2026-09-21 09:46:11.683695','2026-09-21 09:46:19.103358','PAID','alutecval@gmail.com'),('325e883b-2f82-403e-b7a2-264dfe125e96',6.91,'2026-11-03 17:44:22.303085','2026-11-09 12:03:14.404489','PAID','vicentemarti31416@gmail.com'),('32649c06-af2b-4171-866e-2413469b7865',6.91,'2026-07-14 06:52:17.050891','2026-07-14 06:52:17.050891','PAID','alutecval@gmail.com'),('3393ad74-975f-421e-a232-cf9a8b591c81',8.04,'2026-10-16 10:53:43.455843','2026-10-16 10:53:54.533849','PAID','vicentemarti31416@gmail.com'),('351bb721-69e9-4857-a72a-6b03ae38fb90',6.91,'2026-09-14 11:04:32.290842','2026-09-14 11:04:48.590147','PAID','alutecval@gmail.com'),('37a1869f-2802-43e0-a62d-6b84681997a6',65.65,'2026-10-13 12:00:47.465230','2026-10-13 12:02:42.942710','PAID','alutecval@gmail.com'),('40995985-c09a-49f4-9ed3-720f9eca6045',6.91,'2026-06-29 09:44:56.419177','2026-06-29 09:44:56.419177','PAID','vicentemarti31416@gmail.com'),('4282220e-ed5d-4dd2-9430-bae9596825a3',6.22,'2026-09-21 09:30:13.965585','2026-09-21 09:30:23.928663','PAID','alutecval@gmail.com'),('43426603-720b-4015-bca5-baaa87905f2d',8.61,'2026-10-16 11:48:36.545569','2026-10-22 11:00:43.936327','PAID','vicentemarti31416@gmail.com'),('488d05a5-110e-4457-9ddd-c717738df1a0',10.54,'2026-06-05 12:55:54.322548','2026-06-05 12:55:54.322548','PAID','other'),('48ba9c6a-8f9c-4aca-8411-460795716a36',19.15,'2026-09-21 10:38:24.184539','2026-09-24 09:40:37.126185','PAID','alutecval@gmail.com'),('4a470dd0-fd20-492e-a57d-d0d68ba81f50',23.25,'2026-09-14 11:05:28.047660','2026-09-17 11:03:38.726044','PAID','alutecval@gmail.com'),('4cf238dd-ccf0-4e8b-887d-395a00e3e0b3',10.54,'2026-09-24 09:44:36.089728','2026-09-24 10:13:04.592602','PAID','alutecval@gmail.com'),('521728c3-e192-4b2e-8214-b9140a3df9c8',19.44,'2026-07-03 09:46:02.991732','2026-07-03 09:46:02.991732','PAID','vicentemarti31416@gmail.com'),('57da6af7-dc31-49b7-ad76-dfc220b45eab',10.54,'2026-06-15 09:33:32.167827','2026-06-15 09:33:32.167827','PAID','vicentemarti31416@gmail.com'),('596d696e-3a64-4620-b909-9d81639c56f9',9.43,'2026-07-01 09:30:44.215073','2026-07-01 09:30:44.215073','PAID','vicentemarti31416@gmail.com'),('5ae20661-8330-4706-972a-ed5dee7d21d5',9.43,'2026-11-02 13:58:13.886248','2026-11-03 11:35:47.712766','PAID','vicentemarti31416@gmail.com'),('5e3a42fe-7ba3-4fbe-b552-b0211b23f6ac',9.43,'2026-09-21 09:12:54.639607','2026-09-21 09:16:35.914188','PAID','alutecval@gmail.com'),('60c19be3-12cb-4192-969d-ef8f0a6a5f51',8.04,'2026-10-06 13:27:13.141605','2026-10-06 13:27:20.314598','PAID','alutecval@gmail.com'),('60dde0bb-d7a0-4180-862e-59dab6c5bc35',6.91,'2026-10-13 12:02:56.782923','2026-10-13 12:03:56.937913','PAID','alutecval@gmail.com'),('66b896e8-6f3e-417d-8cfd-c23edefcf6d4',8.04,'2026-10-16 10:54:01.179112','2026-10-16 11:17:29.322304','PAID','vicentemarti31416@gmail.com'),('6cc8f78d-8c46-47cb-ab57-20ea4cdd7285',8.04,'2026-10-13 10:32:49.738416','2026-10-13 11:58:27.605637','PAID','alutecval@gmail.com'),('754bd4f0-aabe-4cc4-9dfb-f3d50e3706a7',86.14,'2026-08-27 11:17:29.103294','2026-08-27 11:17:29.103294','PAID','alutecval@gmail.com'),('75cc3210-bb0f-4ad8-ae3c-db6ff11921a5',10.54,'2026-09-21 09:24:24.359645','2026-09-21 09:24:33.604516','PAID','alutecval@gmail.com'),('76223834-7eb2-4c64-bc4e-142ffdcd0ac3',9.43,'2026-07-10 09:45:11.974810','2026-07-10 09:45:11.974810','PAID','alutecval@gmail.com'),('7b3464d1-64e8-49df-a40c-c0e810287564',10.54,'2026-07-03 09:58:56.926767','2026-07-03 09:58:56.926767','PAID','vicentemarti31416@gmail.com'),('7ec06085-a21c-40b8-bdd3-0eb6a35d80ea',18.86,'2026-06-29 09:43:16.878637','2026-06-29 09:43:16.878637','PAID','vicentemarti31416@gmail.com'),('7ee197ba-08e6-4968-924e-b074a6a380c0',6.22,'2026-07-03 09:39:11.071995','2026-07-03 09:39:11.071995','PAID','vicentemarti31416@gmail.com'),('7fbe8742-70e0-4f43-92b1-898cccd26ef4',22.22,'2026-09-30 08:17:57.343250','2026-09-30 10:32:22.305232','PAID','alutecval@gmail.com'),('80f41d4d-02be-48fd-98c3-3671f2f41cac',10.54,'2026-10-08 09:45:47.745250','2026-10-09 12:01:25.853390','PAID','alutecval@gmail.com'),('8234cf11-ed21-4d3e-9d63-f059000bf6f0',9.43,'2026-09-29 11:31:42.940621','2026-09-29 11:42:08.095915','PAID','alutecval@gmail.com'),('8396dae0-0649-42f0-a179-817d12cf1e7c',8.04,'2026-07-03 08:25:05.794813','2026-07-03 08:25:05.794813','PAID','vicentemarti31416@gmail.com'),('83db33d3-f5cd-4081-a134-1a8231cfc259',10.54,'2026-09-29 11:29:51.313633','2026-09-29 11:29:58.675846','PAID','alutecval@gmail.com'),('83eb4302-3e10-4875-86cf-97bb0b1b3e15',14.69,'2026-09-21 09:21:00.102154','2026-09-21 09:21:08.709640','PAID','alutecval@gmail.com'),('862052fe-d5b7-4740-82cd-840f17e507d4',8.04,'2026-10-06 13:31:55.087291','2026-10-06 13:33:46.109061','PAID','alutecval@gmail.com'),('86e206ee-de83-420b-9300-89eb2151c2a6',0.00,'2026-10-21 08:46:10.210284','2026-10-21 08:46:10.210284','CART','alutecval@gmail.com'),('87b864ca-9c9a-47eb-8a0d-c6920ba510bb',21.94,'2026-09-24 10:47:25.789811','2026-09-24 10:47:41.855358','PAID','alutecval@gmail.com'),('88fd9df5-c7a0-4d0c-8d94-3c2a069ccc1c',73.45,'2026-09-28 11:27:10.106987','2026-09-29 11:28:38.047199','PAID','alutecval@gmail.com'),('8b660e56-ba2e-462a-b266-e2333752bbea',6.91,'2026-11-03 17:39:41.626863','2026-11-03 17:39:47.762840','PAID','vicentemarti31416@gmail.com'),('8ca03efa-1c6b-4e59-8a8e-615a1f27f09d',24.12,'2026-11-03 17:39:27.779897','2026-11-03 17:39:38.023220','PAID','vicentemarti31416@gmail.com'),('8e2a584f-0585-4d49-8f3c-d718226b99a2',49.61,'2026-11-03 11:36:03.466034','2026-11-03 11:45:16.277992','PAID','vicentemarti31416@gmail.com'),('8ef84eaa-d872-4f45-90f6-4e6549c7b4f1',22.22,'2026-07-03 10:01:41.770066','2026-07-03 10:01:41.770066','PAID','vicentemarti31416@gmail.com'),('903c34db-f8fb-456b-b5f4-cf6dfff55fb1',37.72,'2026-09-14 10:06:19.273347','2026-09-14 10:12:33.775405','PAID','alutecval@gmail.com'),('90750812-45a5-41ab-8888-a747a378aa8b',0.00,'2026-10-26 11:37:25.970285','2026-10-26 11:37:25.970285','CART','vicentemartiemailsender@gmail.com'),('90c15b68-a5f1-4e9c-9686-a2d54085050b',8.04,'2026-10-06 13:23:31.088751','2026-10-06 13:23:38.872710','PAID','alutecval@gmail.com'),('9437c284-dd7d-4406-a3a1-d1243abf785c',10.54,'2026-07-23 10:44:31.498577','2026-07-23 10:44:31.498577','PAID','alutecval@gmail.com'),('95139366-cff2-4a43-9757-b9b27dc64eb3',14.07,'2026-11-09 13:40:20.692101','2026-11-09 13:40:53.446177','PAID','vicentemarti31416@gmail.com'),('99d669c1-a1a1-498f-94c9-c924bf21e9ca',15.96,'2026-07-03 09:43:49.247837','2026-07-03 09:43:49.247837','PAID','vicentemarti31416@gmail.com'),('9d026769-e155-4bf3-9fc3-5232f6b40d5f',93.94,'2026-10-13 12:06:08.991771','2026-10-15 09:06:50.591975','PAID','alutecval@gmail.com'),('9dc9543a-8160-4519-90af-5479a1e69a4e',9.43,'2026-06-15 11:43:06.198482','2026-06-15 11:43:06.198482','PAID','vicentemarti31416@gmail.com'),('9e487b97-d35f-48a7-b76c-cda118c1a3d9',10.40,'2026-07-03 09:40:41.017512','2026-07-03 09:40:41.017512','PAID','vicentemarti31416@gmail.com'),('a52e5760-653a-4869-b05c-0f123bb5a66e',9.43,'2026-07-10 09:44:31.484226','2026-07-10 09:44:31.484226','PAID','alutecval@gmail.com'),('a587852b-c5d1-4960-a1d4-d1a4353644ef',2047.25,'2026-07-23 11:39:18.194662','2026-07-23 11:39:18.194662','PAID','alutecval@gmail.com'),('a7fb78e4-6a81-4801-ac20-42c6ee4f2248',22.68,'2026-09-21 09:26:48.919629','2026-09-21 09:26:57.963960','PAID','alutecval@gmail.com'),('a8dd9382-107f-4190-b0af-cfaa751f8fc1',20.73,'2026-09-28 08:23:30.980024','2026-09-28 08:38:18.640092','PAID','alutecval@gmail.com'),('b14d7c37-d6f5-4e1b-82d7-88fcfc9f83d3',10.54,'2026-09-24 10:41:30.537328','2026-09-24 10:42:47.871315','PAID','alutecval@gmail.com'),('b3025dc1-4962-45a4-a351-01bc2ec65619',46.02,'2026-07-03 09:41:52.547217','2026-07-03 09:41:52.547217','PAID','vicentemarti31416@gmail.com'),('b36ef80c-f8ba-44f7-a643-4aeb5cf99dd3',90.53,'2026-10-22 11:08:11.392095','2026-10-26 19:58:30.851970','PAID','vicentemarti31416@gmail.com'),('bd612861-22ec-4304-a4b3-dbcd7133bc67',77.27,'2026-07-23 11:20:14.661200','2026-07-23 11:20:14.661200','PAID','alutecval@gmail.com'),('be39e589-721d-4519-b3b1-aebd69788926',39.30,'2026-06-29 09:45:45.363862','2026-06-29 09:45:45.363862','PAID','vicentemarti31416@gmail.com'),('c26dc91c-85d0-4195-8276-d97a332c9624',42.21,'2026-07-23 13:02:15.264946','2026-07-23 13:02:15.264946','PAID','alutecval@gmail.com'),('cc4ea216-0c60-4db1-99bd-dcc0976913f4',9.43,'2026-07-02 10:20:39.286693','2026-07-02 10:20:39.286693','PAID','vicentemarti31416@gmail.com'),('cf94589d-ab83-419a-84b5-f709e0018660',1892.66,'2026-11-03 17:38:58.796227','2026-11-03 17:39:23.323414','PAID','vicentemarti31416@gmail.com'),('d74342f8-c215-426c-a6d2-d4611238407f',1896.84,'2026-09-28 09:56:49.593724','2026-09-28 10:03:55.157790','PAID','alutecval@gmail.com'),('d84d0abf-7d59-4e09-93c7-2cd4c95af1ce',44.79,'2026-11-03 11:46:10.004971','2026-11-03 17:38:50.945430','PAID','vicentemarti31416@gmail.com'),('dc139a16-a7ae-4504-8590-2ffdafe0c856',22.68,'2026-09-30 10:33:09.428282','2026-10-06 13:21:39.056245','PAID','alutecval@gmail.com'),('dd186bbf-f42a-4627-a623-d4913eba15bf',9.43,'2026-09-14 09:28:39.241773','2026-09-14 09:28:50.014937','PAID','alutecval@gmail.com'),('e0ad3d1a-2781-45a8-bbb3-f05963e302a8',67.80,'2026-10-26 20:11:22.555497','2026-10-27 21:14:06.128821','PAID','vicentemarti31416@gmail.com'),('e3a2eec8-433e-4ac7-ae31-1344b1f7f099',9.43,'2026-09-10 11:58:15.229426','2026-09-10 11:58:15.229426','PAID','alutecval@gmail.com'),('e4ccb5e6-7afb-43df-8ca5-822894ff9452',0.00,'2026-11-09 14:31:21.562902','2026-11-09 14:31:21.562902','CART','vicentemarti31416@gmail.com'),('e74b20e7-5d2b-4d75-a0fd-816a8861f9b0',14.07,'2026-10-16 11:17:31.529807','2026-10-16 11:48:35.806426','PAID','vicentemarti31416@gmail.com'),('e7f340dc-4e3a-4d31-b089-bc728a12b411',10.54,'2026-09-28 09:29:37.632931','2026-09-28 09:29:56.417876','PAID','alutecval@gmail.com'),('f6e40efb-9008-4566-af8f-5523c3ec905d',8.61,'2026-09-21 09:54:42.733163','2026-09-21 09:54:56.212155','PAID','alutecval@gmail.com'),('faa8c377-f866-421f-9788-ef4604bf0945',46.34,'2026-07-07 09:43:09.504668','2026-07-07 09:43:09.504668','PAID','alutecval@gmail.com'),('fe473cac-214a-4775-9326-a943b1f0f159',14.69,'2026-07-03 09:30:32.716057','2026-07-03 09:30:32.716057','PAID','vicentemarti31416@gmail.com'),('fe544b7b-2991-4a4a-aa56-3ed4c636b03d',46.12,'2026-07-09 09:56:49.897802','2026-10-15 12:29:11.092252','PAID','vicentemarti31416@gmail.com'),('ffe44566-cadb-453c-9757-ccfa005985ea',6.22,'2026-07-03 09:51:23.481500','2026-07-03 09:51:23.481500','PAID','vicentemarti31416@gmail.com');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participants`
--

DROP TABLE IF EXISTS `participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participants` (
  `id` varchar(255) NOT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participants`
--

LOCK TABLES `participants` WRITE;
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` VALUES ('06bf6eb4-f11b-4b5d-86bb-fe2ad42a5c49','https://res.cloudinary.com/ddjupcyst/image/upload/v1792590559/profile-picture-3b_nevil8.jpg','cuentadeprueba13102026@gmail.com'),('4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc','https://res.cloudinary.com/ddjupcyst/image/upload/v1791892905/cwsnc78ic5f5pubsh6jj.png','vicentemarti31416@gmail.com'),('74d3d066-e6d3-4ade-97a9-406ee878d5f3','https://res.cloudinary.com/ddjupcyst/image/upload/v1792593855/profile-picture-5b_eatn5p.jpg','alutecval@gmail.com'),('c360c162-17d3-4269-8548-9cb89f84509b','https://res.cloudinary.com/ddjupcyst/image/upload/v1793014155/profile-picture-2b_hcrgqj.jpg','vicentemartiemailsender@gmail.com');
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `photos`
--

DROP TABLE IF EXISTS `photos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `photos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `public_id` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `photos`
--

LOCK TABLES `photos` WRITE;
/*!40000 ALTER TABLE `photos` DISABLE KEYS */;
INSERT INTO `photos` VALUES (1,'ufxqzst1by3fvwhnfmsy','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664056/ufxqzst1by3fvwhnfmsy.avif',1),(2,'hif0dtuaovf1id1brkrh','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664058/hif0dtuaovf1id1brkrh.avif',1),(3,'ojbvhu41o4vrdnw0olaz','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664060/ojbvhu41o4vrdnw0olaz.avif',1),(4,'ynntblsunqaq2ziewa9g','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664062/ynntblsunqaq2ziewa9g.avif',1),(5,'tb2k1s2voy5hhqjk03oz','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664064/tb2k1s2voy5hhqjk03oz.avif',1),(6,'sb0dlzj89uoag2lpcpkp','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664066/sb0dlzj89uoag2lpcpkp.avif',1),(7,'swrokrybsrl6we4cqxrl','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664194/swrokrybsrl6we4cqxrl.avif',2),(8,'l7yxshbgdmrdn6jiorbu','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664199/l7yxshbgdmrdn6jiorbu.avif',2),(9,'vn5dvoezo4vg6lbumqud','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664201/vn5dvoezo4vg6lbumqud.avif',2),(10,'xrdyszdlhd58ml4qns21','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664206/xrdyszdlhd58ml4qns21.avif',2),(11,'ls9xwroqweg2j9gr55ts','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664207/ls9xwroqweg2j9gr55ts.avif',2),(12,'o1zupld36q5jepch2ygb','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664299/o1zupld36q5jepch2ygb.avif',3),(13,'ihvn6korygmoykgnysb3','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664302/ihvn6korygmoykgnysb3.avif',3),(14,'uukwrtj4lvtoqjslwytz','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664304/uukwrtj4lvtoqjslwytz.avif',3),(15,'rumv1kl38lht20cq0it6','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664306/rumv1kl38lht20cq0it6.avif',3),(16,'mtfwrk0c4pxyatlycdxr','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664309/mtfwrk0c4pxyatlycdxr.avif',3),(17,'ch2dldcqm7d4zvpgzq8k','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664311/ch2dldcqm7d4zvpgzq8k.avif',3),(18,'sp2wy09r0o7vmyjm0j06','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664397/sp2wy09r0o7vmyjm0j06.avif',4),(19,'ox7eaxtbvyalpphbvuwd','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664404/ox7eaxtbvyalpphbvuwd.avif',4),(20,'tk1su9tjlrryligoicvr','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664408/tk1su9tjlrryligoicvr.avif',4),(21,'lz6tueia9xeiyudkufao','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664410/lz6tueia9xeiyudkufao.avif',4),(22,'gjqzvrxjolpb1rebzoc6','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664411/gjqzvrxjolpb1rebzoc6.avif',4),(23,'wcrimpiga4e26vbnu0eu','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664573/wcrimpiga4e26vbnu0eu.avif',5),(24,'lwk7hmzfpauupxaggl8a','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664576/lwk7hmzfpauupxaggl8a.avif',5),(25,'xtgvqwzcgfr6hyeydpfy','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664578/xtgvqwzcgfr6hyeydpfy.avif',5),(26,'sgbgvirvjziiffytpq8x','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664583/sgbgvirvjziiffytpq8x.avif',5),(27,'nwmqt1donrurrr9qngab','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664584/nwmqt1donrurrr9qngab.avif',5),(28,'k8inomkgpgjv5dvlukls','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664587/k8inomkgpgjv5dvlukls.avif',5),(29,'buiryzgqzycsxbbjhnxm','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664677/buiryzgqzycsxbbjhnxm.avif',6),(30,'yfnmf0yopx7d3sf3uau8','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664680/yfnmf0yopx7d3sf3uau8.avif',6),(31,'ihpy42cduyhqjojcrfkw','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664681/ihpy42cduyhqjojcrfkw.avif',6),(32,'zjllfjdsh5la0yaaju5g','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664683/zjllfjdsh5la0yaaju5g.avif',6),(33,'taegqooshipmtirbpgnd','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664684/taegqooshipmtirbpgnd.avif',6),(34,'bakszywwgaq8gcst9wbi','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664805/bakszywwgaq8gcst9wbi.avif',7),(35,'wp0sjo63ookrb0i1zfzm','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664807/wp0sjo63ookrb0i1zfzm.avif',7),(36,'bmw7vylxjettjo1i3leq','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664809/bmw7vylxjettjo1i3leq.avif',7),(37,'ksx4pepho30s9y9ikyv5','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664811/ksx4pepho30s9y9ikyv5.avif',7),(38,'vy6pxq7sn65egykmiajn','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664812/vy6pxq7sn65egykmiajn.avif',7),(39,'ow56whfnt6tvaix6pfud','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664866/ow56whfnt6tvaix6pfud.avif',8),(40,'xclqvxohyblov7jn7r3k','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664867/xclqvxohyblov7jn7r3k.avif',8),(41,'bkbdgagutayor1ipomnj','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664869/bkbdgagutayor1ipomnj.avif',8),(42,'pxhsqe6rnasdx5raeo9t','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664870/pxhsqe6rnasdx5raeo9t.avif',8),(43,'fuoojlbrrfzlckij4ngm','http://res.cloudinary.com/ddjupcyst/image/upload/v1780664872/fuoojlbrrfzlckij4ngm.avif',8),(44,'lppv6rnfnwyoehzb5jvz','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665110/lppv6rnfnwyoehzb5jvz.avif',9),(45,'eoc77okrlhe7nyqvnynr','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665113/eoc77okrlhe7nyqvnynr.avif',9),(46,'z3nxhvyyegp8uh8olrza','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665115/z3nxhvyyegp8uh8olrza.avif',9),(47,'zepd45vtqgual8sidont','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665117/zepd45vtqgual8sidont.avif',9),(48,'bufnqcdrpdecrefbs4cj','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665118/bufnqcdrpdecrefbs4cj.avif',9),(49,'ad5j0ss6ddtbtoegcthx','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665119/ad5j0ss6ddtbtoegcthx.avif',9),(50,'u3lsrw74npqwbc7skkkt','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665235/u3lsrw74npqwbc7skkkt.avif',10),(51,'ify4wfkq70nev6oyx6c2','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665237/ify4wfkq70nev6oyx6c2.avif',10),(52,'ahtknsguwp0rrlzjur2c','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665239/ahtknsguwp0rrlzjur2c.avif',10),(53,'oev2h40mwywtawnda9xk','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665241/oev2h40mwywtawnda9xk.avif',10),(54,'ca3jvufin8paqjglszcb','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665248/ca3jvufin8paqjglszcb.avif',10),(55,'ozrztjma0o1g4pdv0kl7','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665250/ozrztjma0o1g4pdv0kl7.avif',10),(56,'eazthtmsjsoaypnl54vw','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665352/eazthtmsjsoaypnl54vw.avif',11),(57,'d7dr12quygh3cubszwfm','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665369/d7dr12quygh3cubszwfm.avif',11),(58,'pyh8tpdelrz5am6pahft','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665370/pyh8tpdelrz5am6pahft.avif',11),(59,'i8hjxsqw6dcm8jch7ji3','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665372/i8hjxsqw6dcm8jch7ji3.avif',11),(60,'imbqxyukao8tssj7dctu','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665374/imbqxyukao8tssj7dctu.avif',11),(61,'dyd6jpkb1ql1oshi8atn','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665375/dyd6jpkb1ql1oshi8atn.avif',11),(62,'dxhoqg2vfs5nevoqc203','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665423/dxhoqg2vfs5nevoqc203.avif',12),(63,'tt20quihkqgtyez9ew1z','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665428/tt20quihkqgtyez9ew1z.avif',12),(64,'d8zukd4xhwvkrir91wxx','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665430/d8zukd4xhwvkrir91wxx.avif',12),(65,'d1gzdlbrfucutiev17we','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665432/d1gzdlbrfucutiev17we.avif',12),(66,'ygq0jqwsgd5y8lftehlu','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665434/ygq0jqwsgd5y8lftehlu.avif',12),(67,'awm67hh83a0w8j5sf2hg','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665436/awm67hh83a0w8j5sf2hg.avif',12),(68,'fcvfnua37wwgfowsm03d','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665438/fcvfnua37wwgfowsm03d.avif',12),(69,'wrcegpvsqbuegrefmbsl','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665717/wrcegpvsqbuegrefmbsl.avif',13),(70,'baf8zz6smhudt5e0ptws','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665724/baf8zz6smhudt5e0ptws.avif',13),(71,'cywdgxaxwcjrjw6pkeyg','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665726/cywdgxaxwcjrjw6pkeyg.avif',13),(72,'b8duufdsppp1pwvajmy8','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665742/b8duufdsppp1pwvajmy8.avif',13),(73,'opvk0zititetje6zwjqi','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665769/opvk0zititetje6zwjqi.avif',13),(74,'h9vnjvqh1pmhtsdrgtjq','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665776/h9vnjvqh1pmhtsdrgtjq.avif',13),(75,'qbrv3baidwdrxd0tckkc','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665930/qbrv3baidwdrxd0tckkc.avif',14),(76,'nq9a6fsuhwpnqkp4snyp','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665933/nq9a6fsuhwpnqkp4snyp.avif',14),(77,'grg7byd5w8touf5dpbpl','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665935/grg7byd5w8touf5dpbpl.avif',14),(78,'uivyiqowq0sx9zhb21wb','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665937/uivyiqowq0sx9zhb21wb.avif',14),(79,'wgqzpumge3e9ot93y6ok','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665938/wgqzpumge3e9ot93y6ok.avif',14),(80,'kqluhrcdpuxwydiwtkxe','http://res.cloudinary.com/ddjupcyst/image/upload/v1780665940/kqluhrcdpuxwydiwtkxe.avif',14),(81,'hu4mabtutme27okpbprx','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666003/hu4mabtutme27okpbprx.avif',15),(82,'ycti5g7bkbiu3i2ylayi','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666005/ycti5g7bkbiu3i2ylayi.avif',15),(83,'p5xk4vizp0vefkrer6p7','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666007/p5xk4vizp0vefkrer6p7.avif',15),(84,'ptj1wspcsxpv1dr1ny2m','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666009/ptj1wspcsxpv1dr1ny2m.avif',15),(85,'ziwfnwbtj3gy4jeveejj','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666011/ziwfnwbtj3gy4jeveejj.avif',15),(86,'wwp4nsuophft5964tkpl','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666013/wwp4nsuophft5964tkpl.avif',15),(87,'njazghx6uky8qlpkwjys','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666169/njazghx6uky8qlpkwjys.avif',16),(88,'zjspzifhhk4dkuyf8lzi','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666172/zjspzifhhk4dkuyf8lzi.avif',16),(89,'valfnnp0rcozph6jmvb7','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666174/valfnnp0rcozph6jmvb7.avif',16),(90,'kzshevbps7jewb4hrbgl','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666178/kzshevbps7jewb4hrbgl.avif',16),(91,'vljlgo0quh1myyidksvt','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666181/vljlgo0quh1myyidksvt.avif',16),(92,'jhruusffaliyyhlhxjra','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666186/jhruusffaliyyhlhxjra.avif',16),(93,'rmlj6gxctmziqduihv3q','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666188/rmlj6gxctmziqduihv3q.avif',16),(94,'rzclvuaz8kok8f1tbx52','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666309/rzclvuaz8kok8f1tbx52.avif',17),(95,'cffla95jpp5eih3gwt1n','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666311/cffla95jpp5eih3gwt1n.avif',17),(96,'j0apms7j9xg7cdnt7vtt','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666312/j0apms7j9xg7cdnt7vtt.avif',17),(97,'hm3xi6kcvwsn2essxnun','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666313/hm3xi6kcvwsn2essxnun.avif',17),(98,'sp9hdv2d6wzyr3dc62we','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666315/sp9hdv2d6wzyr3dc62we.avif',17),(99,'svwvsgieevytkuosu0vj','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666317/svwvsgieevytkuosu0vj.avif',17),(100,'ft8qwpbpvrhxrp8qo5zi','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666515/ft8qwpbpvrhxrp8qo5zi.avif',18),(101,'wkumq7uvfb1omauudtmb','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666520/wkumq7uvfb1omauudtmb.avif',18),(102,'aqbbjvlmy2sbvzgli88t','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666521/aqbbjvlmy2sbvzgli88t.avif',18),(103,'blovqetys0v3gw6wffiq','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666525/blovqetys0v3gw6wffiq.avif',18),(104,'ae5b0ynblokilb3nlo8x','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666526/ae5b0ynblokilb3nlo8x.avif',18),(105,'aeno2jfxbra66fpnrhdd','http://res.cloudinary.com/ddjupcyst/image/upload/v1780666528/aeno2jfxbra66fpnrhdd.avif',18);
/*!40000 ALTER TABLE `photos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_statistics`
--

DROP TABLE IF EXISTS `product_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_statistics` (
  `product_id` bigint(20) NOT NULL,
  `statistics` int(11) DEFAULT NULL,
  `position` int(11) NOT NULL,
  PRIMARY KEY (`product_id`,`position`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_statistics`
--

LOCK TABLES `product_statistics` WRITE;
/*!40000 ALTER TABLE `product_statistics` DISABLE KEYS */;
INSERT INTO `product_statistics` VALUES (1,18,0),(1,2,1),(1,1,2),(1,2,3),(1,3,4),(1,10,5),(2,40,0),(2,4,1),(2,3,2),(2,5,3),(2,8,4),(2,20,5),(3,17,0),(3,3,1),(3,1,2),(3,1,3),(3,2,4),(3,10,5),(4,17,0),(4,2,1),(4,2,2),(4,1,3),(4,4,4),(4,8,5),(5,13,0),(5,1,1),(5,1,2),(5,1,3),(5,3,4),(5,7,5),(6,20,0),(6,2,1),(6,1,2),(6,1,3),(6,3,4),(6,13,5),(7,14,0),(7,0,1),(7,0,2),(7,1,3),(7,2,4),(7,11,5),(8,15,0),(8,1,1),(8,1,2),(8,1,3),(8,3,4),(8,9,5),(9,12,0),(9,1,1),(9,1,2),(9,1,3),(9,2,4),(9,7,5),(10,13,0),(10,1,1),(10,1,2),(10,1,3),(10,2,4),(10,8,5),(11,17,0),(11,2,1),(11,1,2),(11,1,3),(11,3,4),(11,10,5),(12,16,0),(12,1,1),(12,1,2),(12,1,3),(12,3,4),(12,10,5),(13,22,0),(13,1,1),(13,1,2),(13,3,3),(13,5,4),(13,12,5),(14,13,0),(14,1,1),(14,1,2),(14,1,3),(14,2,4),(14,8,5),(15,15,0),(15,2,1),(15,1,2),(15,1,3),(15,2,4),(15,9,5),(16,11,0),(16,1,1),(16,1,2),(16,1,3),(16,1,4),(16,7,5),(17,16,0),(17,1,1),(17,1,2),(17,1,3),(17,2,4),(17,11,5),(18,19,0),(18,1,1),(18,1,2),(18,1,3),(18,3,4),(18,13,5);
/*!40000 ALTER TABLE `product_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `reference` varchar(255) NOT NULL,
  `stock` bigint(20) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Decoración','2026-06-05 12:55:28.764749','Juego de 4 jarrones de estilo bohemio, jarrones para flores secas y arreglos florales, ideales para decoración del hogar, jarrones para decoración de entradas, jarrones para decoración de escritorios de oficinas, jarrones para decoración de gabinetes, regalo ideal para las fiestas','Juego de 4 jarrones de estilo bohemio',14.69,'5955ee80-a135-44b3-9e57-8384dec70dae',61,4),(2,'Decoración','2026-06-05 12:57:51.813064','Cinco Bayas Naranja-Marrón, Arreglo Floral Artificial Realista de Otoño, Ideal para Acción de Gracias, Halloween, Bodas, Decoración del Hogar y Centros de Mesa','Cinco Bayas Naranja-Marrón',9.43,'de27f6a8-eda8-4241-af6e-900abeaf300b',55,3.925),(3,'Decoración','2026-06-05 12:59:33.014924','1pc/5pcs Jarrón de Cerámica Bohemio Minimalista para Dormitorio, Sala de Estar, Oficina, Centro de Mesa para Chimenea, Decoración Rústica Vintage para el Hogar, Regalos para Diversas Fiestas','Jarrón de Cerámica Bohemio',6.91,'95c1c758-b052-4f0e-8098-9972e50e3b30',75,3.8823529411764706),(4,'Decoración','2026-06-05 13:00:59.084230','Juego de 4 jarrones diagonales de imitación de cerámica, jarrones para arreglos de flores secas, adecuados para adornos de jarrón de decoración del hogar, adornos de jarrón para vitrina de vinos y estantería, adornos de jarrón para entrada de puerta, decoración de jarrón de oficina','Jarrones diagonales de imitación',10.54,'b4f38595-7c90-4430-ae37-53759b465e13',33,3.823529411764706),(5,'Decoración','2026-06-05 13:04:11.579644','Juego de 4 Piezas de Ramas Artificiales de Eucalipto y Hojas de Eucalipto de Otoño. Decoración para casa, oficina, negocio. Decoración neutra y elegante','Ramas Artificiales de Eucalipto',8.04,'efc0982b-937a-4531-bf81-3473fbe13e43',67,4.076923076923077),(6,'Decoración','2026-06-05 13:05:47.626489','Un jarrón de mesa de tamaño pequeño con un extremo cerámico mate, perfecto para la decoración del hogar, jarrón decorativo, portavelas para incienso, en un estilo moderno minimalista, con un diseño de nudo, hecho a mano','Jarrón de mesa cerámico mate',22.22,'30bc726c-0401-42b1-8c39-1ee90ddeb341',91,4.2),(7,'Decoración','2026-06-05 13:07:56.798895','3-4 piezas Conjunto de estatuas de gato de cerámica nórdica mate - Escultura de animal moderna para decoración de hogar y oficina, perfecta para estantería, salón o entrada - Ideal regalo, no necesita electricidad','Conjunto de estatuas de gato',22.68,'f770d688-f90b-46f8-8f3b-a2f54eef07e0',63,4.714285714285714),(8,'Decoración','2026-06-05 13:09:05.820394','Jarrón de Cerámica Blanca de Jingdezhen - Jarrón Cilíndrico Hueco con Agujero Central y Gran Apertura, Jarrón de Porcelana China Vintage para Decoración Moderna del Hogar, Centro de Mesa Rústico para Sala de Estar, Mesa de Comedor, Estudio - Flor de Cerámica Inspirada en Antigüedades','Jarrón de Cerámica Blanca de Jingdezhen',21.94,'fe834634-6382-4002-97af-790bbef7ed0d',63,4.2),(9,'Decoración','2026-06-05 13:13:35.204297','Jarrón Espiral Bohemio en Forma de Donut - Jarrón Redondo de Plástico Multicolor para Decoración de Hogar y Oficina, Centro de Mesa para Flores Artificiales, Arreglos Modernos de Mesa','Jarrón Espiral Bohemio',14.07,'83b24455-d801-4286-9460-df7a2a9dfc28',30,4.083333333333333),(10,'Decoración','2026-06-05 13:15:15.526803','Escultura de velero abstracto moderno, 1 pieza - Figura náutica minimalista con diseño de vela elegante, plástico / Decoración adecuada para el hogar, oficina, estante, escritorio - Líneas fluidas y suaves para un regalo de éxito de navegación suave (cumpleaños/Navidad)','Escultura de velero abstracto',5.36,'7585b800-adf4-4bde-aac0-2a2a73047d39',47,4.153846153846154),(11,'Decoración','2026-06-05 13:17:18.264222','1 pieza Jarrón de cerámica beige vintage | Jarrón pequeño | Material de cerámica | Decoración del hogar | Estilo nórdico | Jarrón esmerilado beige','Jarrón de cerámica beige vintage',6.22,'53d9fa24-db7f-401e-b72c-599de5dff5b2',69,4.0588235294117645),(12,'Decoración','2026-06-05 13:18:22.359420','Juego de 3 jarrones de cerámica blanca, adecuados para arreglos florales de hierba de las pampas estilo bohemio campestre, decoraciones de boda, exhibición de escritorio para el hogar y la oficina, decoración de habitación, decoración de estantería - Artículos de decoración del hogar hechos a mano','Jarrones de cerámica blanca',8.61,'03646522-5eb7-4092-a539-235b7e7d7741',73,4.25),(13,'Decoración','2026-06-05 13:23:57.957346','Esculturas y decoraciones de pared de plástico abstractas, modernas y minimalistas - Paneles de arte de color suave adecuados para el hogar, oficina, patio, regalos de Navidad y San Valentín - Paneles decorativos de fácil mantenimiento para varias habitaciones para regalos de inauguración de casa','Esculturas modernas y minimalistas',10.40,'f61528f9-5fa2-4baf-b140-5daeb53eab39',70,4.181818181818182),(14,'Decoración','2026-06-05 13:26:46.935252','Conjunto de 3 piezas, Figuras de Cerámica Mate Nórdicas de la Familia de Elefantes, Esculturas de Animales, para Decoración de Estantería, Sala de Estar, Oficina, Gabinete, Mesa de Entrada, Decoración de Habitación, Decoración Moderna del Hogar, Regalos','Figuras de elefantes de Cerámica Mate',15.96,'e6039317-fd53-4535-8416-108a9828fca2',64,4.153846153846154),(15,'Decoración','2026-06-05 13:27:57.523852','1 Estatua de Arte Vintage de Bronce/Negro de Hierro Fundido con Figura de Lectura - Adorno de Estilo Europeo para Decoración de Escena de Lectura Fácil, Adecuado para Decoración Interior/Exterior del Hogar y la Oficina - Obra de Arte Decorativa Retro Moderna - No Requiere Electricidad. Decoración del Hogar','Estatua de Arte Vintage de Bronce',1867.00,'ec171978-205b-4573-93a1-87daa4b094eb',70,4),(16,'Decoración','2026-06-05 13:30:52.698207','Un jarrón de cerámica recién lanzado con elementos geométricos, completamente hecho a mano, con un extremo mate. Este jarrón decorativo de estilo moderno es adecuado para la decoración del hogar, entornos de oficina, fiestas de bodas y más. Sirve como un jarrón elegante.','Jarrón geométrico',22.22,'f1b6636d-ad43-42c4-9d73-38fa70f56709',79,4.090909090909091),(17,'Decoración','2026-06-05 13:33:00.978749','Jarrón de cerámica mate hecho a mano para decoración del hogar - Jarrón texturizado minimalista con opciones de múltiples formas, elegante jarrón moderno para regalos de boda, centro de mesa para estantería, decoraciones de sobremesa para sala de estar, dormitorio, regalos de inauguración de casa','Jarrón de cerámica mate',19.44,'535cceee-2237-4e19-999c-d9942af47081',65,4.3125),(18,'Decoración','2026-06-05 13:36:32.018572','Conjunto de jarrones de cerámica estilo boho, patrón de rayas minimalista moderno, jarrones de capullo cilíndricos de temática geométrica para decoración del hogar, arreglos de flores secas creativas para la mesa de la sala de estar.','Conjunto de jarrones de cerámica',46.02,'5c1758fa-5cde-4b33-b2af-d36d7b5b1600',72,4.368421052631579);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ratings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(1000) DEFAULT NULL,
  `customer_name` varchar(255) NOT NULL,
  `username` varchar(256) NOT NULL,
  `stars` int(11) NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=342 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` VALUES (6,'Muy bonito y bien acabado. Le doy cuatro estrellas porque esperaba que fuera un poco más grande.','josé','jose6527@gmail.com',4,2),(7,'El jarrón es precioso y la calidad supera lo que esperaba. Queda perfecto en el salón.','felipe','felipe4409@gmail.com',5,2),(8,'Muy bonito, el diseño es elegante y queda genial como decoración.','maría','maria2457@gmail.com',5,2),(9,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado.','susana','susana9038@gmail.com',5,2),(10,'Cumple su función y el diseño es agradable, aunque en persona me gustó menos que en las fotos.','teresa','teresa7838@gmail.com',3,2),(11,'El jarrón está bien, pero el acabado tiene algunos detalles que no me convencen.','ignacio','ignacio2079@gmail.com',2,2),(12,'Superó mis expectativas, es incluso más bonito en persona.\"','julián','julian6848@gmail.com',5,2),(13,'Muy buena calidad, es perfecto para la casa. Volvería a comprarlo sin duda','óscar','oscar8016@gmail.com',5,2),(14,'Me decepcionó bastante. El aspecto no se corresponde con lo que esperaba al comprarlo.','inma','inma9536@gmail.com',1,2),(15,'No era lo que esperaba, el acabado deja bastante que desear.','rodrigo','rodrigo3643@gmail.com',1,2),(16,'El producto llegó correctamente, pero la calidad no me convenció','inés','ines9576@gmail.com',1,2),(17,'Un jarrón precioso, queda perfecto con flores secas.','marisa','marisa6974@gmail.com',5,2),(18,'La calidad es excelente y el envío llegó muy rápido.','césar','cesar6132@gmail.com',5,2),(19,'Calidad muy buena, el color es precioso. Lo recomiendo','manuel','manuel9729@gmail.com',5,2),(20,'Muy bonito y decorativo. Quizá esperaba que fuese un poco más grande.','ana','ana270@gmail.com',4,2),(21,'La pieza está bien, aunque no era exactamente lo que imaginaba.','andrés','andres2121@gmail.com',3,2),(22,'La calidad es excelente y el envío llegó muy rápido.','ignacio','ignacio9785@gmail.com',5,2),(26,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra.','josefa','josefa2595@gmail.com',5,2),(27,'El acabado es impecable y queda perfecto en la mesa del comedor','miriam','miriam3590@gmail.com',5,2),(28,'Se nota la calidad de los materiales. Lo volvería a comprar.','adela','adela163@gmail.com',5,2),(29,'Un diseño muy elegante. Ha quedado genial en el salón.','evaristo','evaristo28@gmail.com',5,2),(30,'Llegó perfectamente embalado y sin ningún desperfecto','vicente','vicente9629@gmail.com',5,2),(31,'Queda precioso con flores naturales. Muy recomendable','juan','juan8093@gmail.com',5,2),(32,'El color es exactamente igual al de las fotografías.','manolo','manolo1577@gmail.com',5,2),(33,'Un detalle decorativo que ha llamado la atención de todas las visitas.','amparo','amparo3578@gmail.com',5,2),(34,'Muy buena compra. Solo esperaba que tuviera un poco más de altura.','beatriz','beatriz3161@gmail.com',4,2),(35,'Bonito y bien terminado. Cumple con lo que promete','esteban','esteban5059@gmail.com',4,2),(36,'Está bien, aunque no me sorprendió tanto como esperaba.','santigao','santigao5815@gmail.com',3,2),(37,'En general estoy contento con la compra','maricarmen','maricarmen3897@gmail.com',4,2),(38,'El diseño es bonito, pero el tamaño es algo más reducido.','josé','jose2029@gmail.com',3,2),(39,'Tiene un estilo moderno que combina con cualquier decoración','vicente','vicente8437@gmail.com',4,2),(40,'Muy decorativo. Le doy cuatro estrellas porque pensaba que pesaría un poco menos.','fermín','fermin6117@gmail.com',4,2),(41,'Es un jarrón sencillo, adecuado para una decoración discreta','arantxa','arantxa5267@gmail.com',3,2),(42,'La calidad no cumple mis expectativas. Me ha decepcionado.','imanol','imanol7973@gmail.com',1,2),(43,'El diseño es bonito, pero la calidad podría mejorar.','ignacio','ignacio4075@gmail.com',2,2),(44,'El jarrón llegó rápido y bien protegido. Todo perfecto salvo un pequeño detalle en el acabado','federico','federico6438@gmail.com',4,2),(45,'El color no coincide del todo con las imágenes','felix','felix9962@gmail.com',2,2),(46,'El jarrón es precioso y la calidad supera lo que esperaba. Queda perfecto en el salón','felipe','felipe519@gmail.com',5,6),(47,'Muy bonito y bien acabado. Le doy cuatro estrellas porque esperaba que fuera un poco más grande','maría','maria2667@gmail.com',5,6),(48,'Muy bonito, el diseño es elegante y queda genial como decoración.','susana','susana1780@gmail.com',5,6),(49,'Muy bonito y decorativo. Quizá esperaba que fuese un poco más grande.','teresa','teresa889@gmail.com',4,6),(50,'Buen acabado y diseño moderno. Estoy contento con la compra','ignacio','ignacio9087@gmail.com',4,6),(51,'El diseño está bien, aunque esperaba algo más llamativo','julián','julian2798@gmail.com',3,6),(52,'El jarrón es bonito, pero tiene pequeños detalles en el acabado','oscar','oscar6698@gmail.com',2,6),(53,'No era lo que esperaba, el acabado deja bastante que desear','inma','inma5107@gmail.com',1,6),(54,'Me decepcionó al recibirlo, no se parece tanto a las imágenes','rodrigo','rodrigo5430@gmail.com',1,6),(55,'Un jarrón precioso, queda perfecto con flores secas.','inés','ines1831@gmail.com',5,6),(56,'La calidad es excelente y el envío llegó muy rápido','marisa','marisa2844@gmail.com',5,6),(57,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','césar','cesar8719@gmail.com',5,6),(58,'El acabado es impecable y queda perfecto en la mesa del comedor','manuel','manuel5082@gmail.com',5,6),(59,'Se nota la calidad de los materiales. Lo volvería a comprar','ana','ana9236@gmail.com',5,6),(60,'Un diseño muy elegante. Ha quedado genial en el salón','andrés','andres952@gmail.com',5,6),(61,'Llegó perfectamente embalado y sin ningún desperfecto','esteban','esteban7012@gmail.com',5,6),(62,'Queda precioso con flores naturales. Muy recomendable','rodrigo','rodrigo2226@gmail.com',5,6),(63,'El color es exactamente igual al de las fotografías','patricia','patricia74@gmail.com',5,6),(64,'Un detalle decorativo que ha llamado la atención de todas las visitas','bea','bea3674@gmail.com',5,6),(65,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra.','josé','jose8147@gmail.com',5,1),(66,'El acabado es impecable y queda perfecto en la mesa del comedor.','antonio','antonio9724@gmail.com',5,1),(67,'Se nota la calidad de los materiales. Lo volvería a comprar','matías','matias4191@gmail.com',5,1),(68,'Un diseño muy elegante. Ha quedado genial en el salón','nerea','nerea1762@gmail.com',5,1),(69,'Muy buena compra. Solo esperaba que tuviera un poco más de altura','ambrosio','ambrosio6216@gmail.com',4,1),(70,'Llegó perfectamente embalado y sin ningún desperfecto','josefa','josefa5805@gmail.com',5,1),(71,'Queda precioso con flores naturales. Muy recomendable','miriam','miriam377@gmail.com',5,1),(72,'El color es exactamente igual al de las fotografías','bartolo','bartolo4443@gmail.com',5,1),(73,'Un detalle decorativo que ha llamado la atención de todas las visitas','manolo','manolo1094@gmail.com',5,1),(74,'Bonito y bien terminado. Cumple con lo que promete','bertín','bertin2121@gmail.com',4,1),(75,'Me gusta mucho el diseño, aunque el color varía ligeramente','julián','julian7315@gmail.com',4,1),(76,'Está bien, aunque no me sorprendió tanto como esperaba','damián','damian232@gmail.com',3,1),(77,'El diseño es bonito, pero la calidad podría mejorar','manuel','manuel9175@gmail.com',2,1),(78,'La calidad no cumple mis expectativas. Me ha decepcionado','sebastián','sebastian5209@gmail.com',1,1),(79,'El acabado presenta bastantes imperfecciones','felipe','felipe8500@gmail.com',1,1),(80,'Muy bonito, el diseño es elegante y queda genial como decoración','leticia','leticia6883@gmail.com',5,3),(81,'Muy bonito y decorativo. Quizá esperaba que fuese un poco más grande','arantxa','arantxa8905@gmail.com',4,3),(82,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','imanol','imanol3887@gmail.com',5,3),(83,'Superó mis expectativas, es incluso más bonito en persona','óscar','oscar2700@gmail.com',5,3),(84,'Un jarrón precioso, queda perfecto con flores secas','demetrio','demetrio1829@gmail.com',5,3),(85,'La calidad es excelente y el envío llegó muy rápido','ignacio','ignacio1036@gmail.com',5,3),(86,'Buen acabado y diseño moderno. Estoy contento con la compra','juan','juan9675@gmail.com',4,3),(87,'El diseño está bien, aunque esperaba algo más llamativo','matías','matias5297@gmail.com',3,3),(88,'El jarrón es bonito, pero tiene pequeños detalles en el acabado','nerea','nerea7441@gmail.com',2,3),(89,'No volvería a comprarlo, esperaba una mejor calidad','juliana','juliana1325@gmail.com',1,3),(90,'Me decepcionó al recibirlo, no se parece tanto a las imágenes','ana','ana4272@gmail.com',1,3),(91,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','margarita','margarita7386@gmail.com',5,3),(92,'Se nota la calidad de los materiales. Lo volvería a comprar','josefina','josefina4124@gmail.com',5,3),(93,'Esperaba una pieza más cuidada. No repetiría la compra','josé','jose8440@gmail.com',1,3),(94,'El jarrón llegó rápido y bien protegido. Todo perfecto salvo un pequeño detalle en el acabado.','juan','juan9842@gmail.com',4,4),(95,'El color es exactamente igual al de las fotografías.','antonio','antonio3899@gmail.com',5,4),(96,'Un diseño muy elegante. Ha quedado genial en el salón.','ramón','ramon9940@gmail.com',5,4),(97,'Un detalle decorativo que ha llamado la atención de todas las visitas.','inés','ines8022@gmail.com',5,4),(98,'Llegó perfectamente embalado y sin ningún desperfecto.','adelina','adelina292@gmail.com',5,4),(99,'Esperaba una pieza más cuidada. No repetiría la compra.','raul','raul7353@gmail.com',1,4),(100,'Esperaba un mejor acabado por el precio','tobías','tobias5911@gmail.com',2,4),(101,'Se nota la calidad de los materiales. Lo volvería a comprar.','felipe','felipe7487@gmail.com',5,4),(102,'Queda muy bien en la estantería. Relación calidad-precio correcta.','ambrosio','ambrosio9701@gmail.com',4,4),(103,'Es un jarrón sencillo, adecuado para una decoración discreta.','nerea','nerea6057@gmail.com',3,4),(104,'El jarrón es precioso y la calidad supera lo que esperaba. Queda perfecto en el salón.','margarita','margarita1172@gmail.com',5,4),(105,'Muy bonito y bien acabado. Le doy cuatro estrellas porque esperaba que fuera un poco más grande.','ana','ana7658@gmail.com',4,4),(106,'Me decepcionó bastante. El aspecto no se corresponde con lo que esperaba al comprarlo.','juliana','juliana4797@gmail.com',1,4),(107,'El jarrón está bien, pero el acabado tiene algunos detalles que no me convencen','lucas','lucas1001@gmail.com',2,4),(108,'El jarrón es tal como aparece en las fotos, aunque el color es algo diferente','pedro','pedro593@gmail.com',4,4),(109,'La calidad es excelente y el envío llegó muy rápido','josé','jose9944@gmail.com',5,5),(110,'El jarrón es tal como aparece en las fotos, aunque el color es algo diferente.','felipe','felipe7971@gmail.com',4,5),(111,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','matías','matias22@gmail.com',5,5),(112,'Superó mis expectativas, es incluso más bonito en persona','ana','ana6160@gmail.com',5,5),(113,'Muy bonito, el diseño es elegante y queda genial como decoración.','juan','juan753@gmail.com',5,5),(114,'Un jarrón precioso, queda perfecto con flores secas','maría','maria5258@gmail.com',5,5),(115,'El jarrón es precioso y la calidad supera lo que esperaba. Queda perfecto en el salón.','susana','susana4038@gmail.com',5,5),(116,'Muy bonito y bien acabado. Le doy cuatro estrellas porque esperaba que fuera un poco más grande.','teresa','teresa4408@gmail.com',4,5),(117,'El diseño es bonito, pero el jarrón tiene varios defectos visibles.','ignacio','ignacio9917@gmail.com',1,5),(118,'Llegó bien protegido, aunque el producto no cumplió mis expectativas.','julián','julian6381@gmail.com',2,5),(119,'Cumple su función, pero el acabado podría estar mejor.','oscar','oscar2146@gmail.com',3,5),(120,'Me gusta mucho, solo le quitaría una estrella por el embalaje','julio','julio1568@gmail.com',4,5),(121,'Muy bonito y decorativo. Quizá esperaba que fuese un poco más grande.','josé','jose1393@gmail.com',4,7),(122,'Un jarrón precioso, queda perfecto con flores secas.','rodrigo','rodrigo2250@gmail.com',5,7),(123,'Superó mis expectativas, es incluso más bonito en persona.','inés','ines7063@gmail.com',5,7),(124,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado.','matías','matias8575@gmail.com',5,7),(125,'Muy bonito, el diseño es elegante y queda genial como decoración','césar','cesar1695@gmail.com',5,7),(126,'La calidad es excelente y el envío llegó muy rápido','joaquin','joaquin2721@gmail.com',5,7),(127,'Me gusta mucho, solo le quitaría una estrella por el embalaje','marta','marta8510@gmail.com',4,7),(128,'Correcto por el precio que tiene, sin destacar demasiado','josefa','josefa4407@gmail.com',3,7),(129,'Llegó perfectamente embalado y sin ningún desperfecto.','miriam','miriam6487@gmail.com',5,7),(130,'Se nota la calidad de los materiales. Lo volvería a compra','evaristo','evaristo9214@gmail.com',5,7),(131,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','adela','adela6619@gmail.com',5,7),(132,'Un detalle decorativo que ha llamado la atención de todas las visitas','raul','raul5444@gmail.com',5,7),(133,'Queda precioso con flores naturales. Muy recomendable','vicente','vicente7355@gmail.com',5,7),(134,'Queda precioso con flores naturales. Muy recomendable','almudena','almudena454@gmail.com',5,8),(135,'El color es exactamente igual al de las fotografías.','matías','matias174@gmail.com',5,8),(136,'Un detalle decorativo que ha llamado la atención de todas las visitas.','aaron','aaron9491@gmail.com',5,8),(137,'Llegó perfectamente embalado y sin ningún desperfecto','tobías','tobias6961@gmail.com',5,8),(138,'Un diseño muy elegante. Ha quedado genial en el salón','josué','josue6325@gmail.com',5,8),(139,'El acabado es impecable y queda perfecto en la mesa del comedor.','nerea','nerea739@gmail.com',5,8),(140,'Me gusta mucho el diseño, aunque el color varía ligeramente.','david','david4693@gmail.com',4,8),(141,'Se nota la calidad de los materiales. Lo volvería a comprar.','alfredo','alfredo1257@gmail.com',5,8),(142,'Esperaba un material con un aspecto algo más premium','alfonso','alfonso2188@gmail.com',3,8),(143,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','pedro','pedro7160@gmail.com',5,8),(144,'El producto llegó correctamente, pero la calidad no me convenció.','josefina','josefina9247@gmail.com',1,8),(145,'La calidad es excelente y el envío llegó muy rápido.','teresa','teresa4763@gmail.com',5,8),(146,'Me gusta mucho, solo le quitaría una estrella por el embalaje.','brígida','bridgida6056@gmail.com',4,8),(147,'Llegó bien protegido, aunque el producto no cumplió mis expectativas.','adelina','adelina5993@gmail.com',2,8),(148,'Llegó bien protegido, aunque el producto no cumplió mis expectativas.','óscar','oscar1795@gmail.com',2,9),(149,'El jarrón es tal como aparece en las fotos, aunque el color es algo diferente.','juan','juan976@gmail.com',4,9),(150,'La calidad es excelente y el envío llegó muy rápido.','inés','ines9476@gmail.com',5,9),(151,'Muy bonito, el diseño es elegante y queda genial como decoración.','francisco','francisco4481@gmail.com',5,9),(152,'Un jarrón precioso, queda perfecto con flores secas','ignacio','ignacio3959@gmail.com',5,9),(153,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','víctor','victor6341@gmail.com',5,9),(154,'Superó mis expectativas, es incluso más bonito en persona','susana','susana9830@gmail.com',5,9),(155,'Buen acabado y diseño moderno. Estoy contento con la compra','marisa','marisa147@gmail.com',4,9),(156,'Cumple su función, pero el acabado podría estar mejor.','césar','cesar1206@gmail.com',3,9),(157,'El diseño es bonito, pero el jarrón tiene varios defectos visibles','amparo','amparo5581@gmail.com',1,9),(158,'Correcto para el precio, sin destacar especialmente','josé','jose4298@gmail.com',5,10),(159,'El color es exactamente igual al de las fotografías.','felipe','felipe4734@gmail.com',5,10),(160,'El acabado es impecable y queda perfecto en la mesa del comedor','santiago','santiago780@gmail.com',5,10),(161,'Llegó perfectamente embalado y sin ningún desperfecto','felix','felix9667@gmail.com',5,10),(162,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','julián','julian6025@gmail.com',5,10),(163,'El jarrón llegó rápido y bien protegido. Todo perfecto salvo un pequeño detalle en el acabado','demetrio','demetrio1115@gmail.com',4,10),(164,'Se nota la calidad de los materiales. Lo volvería a comprar','ambrosio','ambrosio7471@gmail.com',5,10),(165,'Llegó perfectamente embalado y sin ningún desperfecto.','felipe','felipe4028@gmail.com',5,10),(166,'Un detalle decorativo que ha llamado la atención de todas las visitas','ignacio','ignacio7711@gmail.com',5,10),(167,'Tiene un estilo moderno que combina con cualquier decoración','jesé','jese6479@gmail.com',4,10),(168,'La relación calidad-precio me ha parecido baja','mateo','mateo9254@gmail.com',1,10),(169,'El diseño es bonito, pero el tamaño es algo más reducido','juan','juan6845@gmail.com',3,10),(170,'El color no coincide del todo con las imágenes','vicente','vicente6453@gmail.com',2,10),(171,'No está mal, pero las fotos hacen que parezca más vistoso','fermín','fermin1728@gmail.com',3,11),(172,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','agustín','agustin9254@gmail.com',5,11),(173,'El acabado es impecable y queda perfecto en la mesa del comedor','andrés','andres1118@gmail.com',5,11),(174,'Se nota la calidad de los materiales. Lo volvería a comprar','inma','inma7785@gmail.com',5,11),(175,'Un diseño muy elegante. Ha quedado genial en el salón','maría','maria5593@gmail.com',5,11),(176,'Llegó perfectamente embalado y sin ningún desperfecto','miriam','miriam4599@gmail.com',5,11),(177,'Queda muy bien en la estantería. Relación calidad-precio correcta','carmen','carmen6207@gmail.com',4,11),(178,'Queda precioso con flores naturales. Muy recomendable','felipe','felipe7240@gmail.com',5,11),(179,'El color es exactamente igual al de las fotografías','josé','jose7577@gmail.com',5,11),(180,'Una compra que, en mi caso, no ha merecido la pena','damián','damian6165@gmail.com',1,11),(181,'Un detalle decorativo que ha llamado la atención de todas las visitas','alfonso','alfonso8087@gmail.com',5,11),(182,'Esperaba un mejor acabado por el precio','david','david1951@gmail.com',2,11),(183,'Tiene un estilo moderno que combina con cualquier decoración','raul','raul5462@gmail.com',4,11),(184,'Bonito y bien terminado. Cumple con lo que promete','alfredo','alfredo1467@gmail.com',4,11),(185,'No recomendaría este modelo. Esperaba mucho más','matías','matias929@gmail.com',1,11),(186,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','aaron','aaron238@gmail.com',5,12),(187,'El acabado es impecable y queda perfecto en la mesa del comedor','marcos','marcos8380@gmail.com',5,12),(188,'Se nota la calidad de los materiales. Lo volvería a comprar','ismael','ismael1219@gmail.com',5,12),(189,'Un diseño muy elegante. Ha quedado genial en el salón','damián','damian923@gmail.com',5,12),(190,'Muy buena compra. Solo esperaba que tuviera un poco más de altura','isaac','isaac950@gmail.com',4,12),(191,'Llegó perfectamente embalado y sin ningún desperfecto','felipe','felipe1973@gmail.com',5,12),(192,'Está bien, aunque no me sorprendió tanto como esperaba','antonio','antonio7004@gmail.com',3,12),(193,'Un detalle decorativo que ha llamado la atención de todas las visitas','pedro','pedro9112@gmail.com',5,12),(194,'El color es exactamente igual al de las fotografías','leonor','leonor4557@gmail.com',5,12),(195,'Queda precioso con flores naturales. Muy recomendable.','lucía','lucia5431@gmail.com',5,12),(196,'Llegó perfectamente embalado y sin ningún desperfecto','nerea','nerea3485@gmail.com',5,12),(197,'No me convenció ni el material ni el acabado','elisa','elisa1123@gmail.com',1,12),(198,'Bonito y bien terminado. Cumple con lo que promete','josefina','josefina5139@gmail.com',4,12),(199,'Las fotografías transmiten una mejor impresión que el producto real','adela','adela2335@gmail.com',2,12),(200,'Muy bonito, el diseño es elegante y queda genial como decoración','sebastián','sebastian6241@gmail.com',5,13),(201,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','éctor','ector4211@gmail.com',5,13),(202,'Superó mis expectativas, es incluso más bonito en persona','alfonso','alfonso2320@gmail.com',5,13),(203,'Un jarrón precioso, queda perfecto con flores secas','ignacio','ignacio8948@gmail.com',5,13),(204,'Muy bonito y decorativo. Quizá esperaba que fuese un poco más grande','lucas','lucas7801@gmail.com',4,13),(205,'El diseño está bien, aunque esperaba algo más llamativo','ismael','ismael2161@gmail.com',3,13),(206,'La calidad es excelente y el envío llegó muy rápido','jesé','jese7371@gmail.com',5,13),(207,'El producto llegó correctamente, pero la calidad no me convenció','demetrio','demetrio394@gmail.com',1,13),(208,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','faustina','faustina9816@gmail.com',5,13),(209,'Bonito y bien terminado. Cumple con lo que promete','josé','jose7930@gmail.com',4,13),(210,'El acabado es impecable y queda perfecto en la mesa del comedor','vicente','vicente204@gmail.com',5,13),(211,'Se nota la calidad de los materiales. Lo volvería a comprar','rosa','rosa7189@gmail.com',5,13),(212,'Un diseño muy elegante. Ha quedado genial en el salón','felipe','felipe5354@gmail.com',5,13),(213,'No está mal, pero las fotos hacen que parezca más vistoso','sebastián','sebastian5194@gmail.com',3,13),(214,'Llegó perfectamente embalado y sin ningún desperfecto','leticia','leticia9898@gmail.com',5,13),(215,'Llegó perfectamente embalado y sin ningún desperfecto','patricia','patricia3930@gmail.com',5,13),(216,'No era exactamente lo que buscaba. Me ha dejado algo indiferente','ignacio','ignacio9924@gmail.com',2,13),(217,'Esperaba un material con un aspecto algo más premium','joaquin','joaquin7851@gmail.com',3,13),(218,'Llegó perfectamente embalado y sin ningún desperfecto','david','david9474@gmail.com',5,13),(219,'Queda muy bien en la estantería. Relación calidad-precio correcta','pedro','pedro3829@gmail.com',4,13),(220,'El jarrón llegó rápido y bien protegido. Todo perfecto salvo un pequeño detalle en el acabado','nerea','nerea701@gmail.com',4,13),(221,'Muy decorativo. Le doy cuatro estrellas porque pensaba que pesaría un poco más','agustín','agustin2000@gmail.com',4,13),(222,'Muy decorativo. Le doy cuatro estrellas porque pensaba que pesaría un poco más','teresa','teresa7887@gmail.com',4,14),(223,'Un detalle decorativo que ha llamado la atención de todas las visitas','susana','susana3455@gmail.com',5,14),(224,'El color es exactamente igual al de las fotografías','martín','martin3593@gmail.com',5,14),(225,'Llegó perfectamente embalado y sin ningún desperfecto','bertín','bertin7592@gmail.com',5,14),(226,'Llegó perfectamente embalado y sin ningún desperfecto','raul','raul7193@gmail.com',5,14),(227,'Un diseño muy elegante. Ha quedado genial en el salón','andrés','andres3188@gmail.com',5,14),(228,'Se nota la calidad de los materiales. Lo volvería a comprar','vicente','vicente4343@gmail.com',5,14),(229,'Cumple su función, aunque el acabado podría ser más fino','josé','jose2150@gmail.com',3,14),(230,'El acabado es impecable y queda perfecto en la mesa del comedor','rosa','rosa7703@gmail.com',5,14),(231,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','cosme','cosme2084@gmail.com',5,14),(232,'El acabado presenta bastantes imperfecciones','felipe','felipe7281@gmail.com',1,14),(233,'Esperaba un mejor acabado por el precio','susana','susana174@gmail.com',2,14),(234,'Queda muy bien en la estantería. Relación calidad-precio correcta','ignacio','ignacio8986@gmail.com',4,14),(235,'Cumple su función, pero el acabado podría estar mejor','julián','julian4438@gmail.com',3,15),(236,'Me gusta mucho, solo le quitaría una estrella por el embalaje','nerea','nerea5216@gmail.com',4,15),(237,'La calidad es excelente y el envío llegó muy rápido','teresa','teresa2763@gmail.com',5,15),(238,'Un jarrón precioso, queda perfecto con flores secas','arantxa','arantxa8148@gmail.com',5,15),(239,'Superó mis expectativas, es incluso más bonito en persona','imanol','imanol2470@gmail.com',5,15),(240,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','federico','federico7880@gmail.com',5,15),(241,'Muy bonito, el diseño es elegante y queda genial como decoración','felix','felix2007@gmail.com',5,15),(242,'El diseño me gusta, pero el material no me convence del todo','leonor','leonor6368@gmail.com',2,15),(243,'Un diseño muy elegante. Ha quedado genial en el salón','raul','raul5829@gmail.com',5,15),(244,'Llegó perfectamente embalado y sin ningún desperfecto','david','david43@gmail.com',5,15),(245,'El color es exactamente igual al de las fotografías','juan','juan2699@gmail.com',5,15),(246,'Una compra que, en mi caso, no ha merecido la pena','felipe','felipe3366@gmail.com',1,15),(247,'Un detalle decorativo que ha llamado la atención de todas las visitas','antonio','antonio8722@gmail.com',5,15),(248,'El jarrón llegó rápido y bien protegido. Todo perfecto salvo un pequeño detalle en el acabado','tomás','tomas3535@gmail.com',4,15),(249,'El producto recibido no se corresponde con la impresión que daban las fotografías','elisa','elisa1489@gmail.com',1,15),(250,'Queda muy bien en la estantería. Relación calidad-precio correcta','pedro','pedro6819@gmail.com',4,16),(251,'Se nota la calidad de los materiales. Lo volvería a comprar','matías','matias9640@gmail.com',5,16),(252,'El acabado es impecable y queda perfecto en la mesa del comedor','damián','damian7755@gmail.com',5,16),(253,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','teresa','teresa9844@gmail.com',5,16),(254,'Un diseño muy elegante. Ha quedado genial en el salón','ignacio','ignacio5967@gmail.com',5,16),(255,'No recomendaría este modelo. Esperaba mucho más','nerea','nerea291@gmail.com',1,16),(256,'Llegó perfectamente embalado y sin ningún desperfecto','arantxa','arantxa3524@gmail.com',5,16),(257,'El color es exactamente igual al de las fotografías','federico','federico6748@gmail.com',5,16),(258,'Un detalle decorativo que ha llamado la atención de todas las visitas','imanol','imanol3178@gmail.com',5,16),(259,'Es un jarrón sencillo, adecuado para una decoración discreta.','josé','jose5626@gmail.com',3,16),(260,'No está mal, pero no volvería a elegir este modelo','vicky','vicky8596@gmail.com',2,16),(261,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','josé','jose6115@gmail.com',5,17),(262,'El acabado es impecable y queda perfecto en la mesa del comedor','pedro','pedro4776@gmail.com',5,17),(263,'Se nota la calidad de los materiales. Lo volvería a comprar','julián','julian5524@gmail.com',5,17),(264,'Un diseño muy elegante. Ha quedado genial en el salón','demetrio','demetrio3294@gmail.com',5,17),(265,'Llegó perfectamente embalado y sin ningún desperfecto','david','david9877@gmail.com',5,17),(266,'El color es exactamente igual al de las fotografías','ambrosio','ambrosio9526@gmail.com',5,17),(267,'Un detalle decorativo que ha llamado la atención de todas las visitas','amparo','amparo7999@gmail.com',5,17),(268,'Bonito y bien terminado. Cumple con lo que promete','inés','ines1418@gmail.com',4,17),(269,'El diseño es bonito, pero el tamaño es algo más reducido','francisco','francisco3061@gmail.com',3,17),(270,'Muy bonito, el diseño es elegante y queda genial como decoración','ana','ana1052@gmail.com',5,17),(271,'Muy bonito y decorativo. Quizá esperaba que fuese un poco más grande','cristina','cristina6058@gmail.com',4,17),(272,'Superó mis expectativas, es incluso más bonito en persona','vicente','vicente7146@gmail.com',5,17),(273,'El diseño me gusta, pero el material no me convence del todo','antonio','antonio7554@gmail.com',2,17),(274,'Un jarrón precioso, queda perfecto con flores secas','marcos','marcos6335@gmail.com',5,17),(275,'La calidad es excelente y el envío llegó muy rápido','felipe','felipe9001@gmail.com',5,17),(276,'Me decepcionó al recibirlo, no se parece tanto a las imágenes','sergio','sergio6011@gmail.com',1,17),(277,'Superó mis expectativas, es incluso más bonito en persona','francisco','francisco3044@gmail.com',5,18),(278,'Me encanta el acabado del jarrón. Se nota que está hecho con mucho cuidado','david','david7165@gmail.com',5,18),(279,'Muy bonito, el diseño es elegante y queda genial como decoración','inés','ines6706@gmail.com',5,18),(280,'La calidad es excelente y el envío llegó muy rápido','teresa','teresa2035@gmail.com',5,18),(281,'Un jarrón precioso, queda perfecto con flores secas','vicente','vicente38@gmail.com',5,18),(282,'El jarrón es tal como aparece en las fotos, aunque el color es algo diferente','ignacio','ignacio4064@gmail.com',4,18),(283,'Me gusta mucho, solo le quitaría una estrella por el embalaje','raul','raul217@gmail.com',4,18),(284,'Llegó perfectamente embalado y sin ningún desperfecto','josé','jose8865@gmail.com',5,18),(285,'El color es exactamente igual al de las fotografías','jeremías','jeremias3705@gmail.com',5,18),(286,'Un detalle decorativo que ha llamado la atención de todas las visitas','david','david1911@gmail.com',5,18),(287,'El jarrón llegó rápido y bien protegido. Todo perfecto salvo un pequeño detalle en el acabado','pedro','pedro8417@gmail.com',4,18),(288,'Un diseño muy elegante. Ha quedado genial en el salón','matías','matias6376@gmail.com',5,18),(289,'Una compra que, en mi caso, no ha merecido la pena','antonio','antonio6618@gmail.com',1,18),(290,'Un diseño muy elegante. Ha quedado genial en el salón','felipe','felipe3964@gmail.com',5,18),(291,'Se nota la calidad de los materiales. Lo volvería a comprar','damián','damian9947@gmail.com',5,18),(292,'El acabado es impecable y queda perfecto en la mesa del comedor','francisco','francisco7864@gmail.com',5,18),(293,'Es incluso más bonito de lo que esperaba. Muy satisfecho con la compra','nacho','nacho9470@gmail.com',5,18),(294,'Esperaba un mejor acabado por el precio','teresa','teresa3769@gmail.com',2,18),(295,'Cumple su función, aunque el acabado podría ser más fino','inés','ines413@gmail.com',3,18),(296,'Muy bonito el jarrón ','josé','jose739@gmail.com',5,12),(297,'Muy bonito y de muy buena calidad','josé','jose2449@gmail.com',5,2),(298,'Muy bonito y de muy buena calidad','josé','jose27@gmail.com',5,3),(299,'Muy bonito el jarroncito','josé','jose2768@gmail.com',5,3),(300,'Es un juego de jarrones precioso, tiene mucha calidad y ha venido muy rápido','josé','jose3760@gmail.com',5,1),(301,'Me ha decepcionado un poco, esperaba más calidad por este precio','josé','jose496@gmail.com',3,1),(302,'El jarrón es precioso y a un precio muy competitivo','josé','jose1181@gmail.com',5,2),(306,'Muy bonito y de mucha calidad','josé','alutecval@gmail.com',5,3),(307,'Es un jarrón precioso, pero esperaba un poco más de calidad','josé','alutecval@gmail.com',4,12),(308,'Un buen jarrón, muy moderno. Quizá el precio es un poco caro','josé','alutecval@gmail.com',4,6),(309,'Dos jarrones preciosos y a un precio inmejorable. Totalmente recomendados','josé','alutecval@gmail.com',5,9),(310,'Llegó demasiado tarde, pero es un jarrón bonito y de calidad','josé','alutecval@gmail.com',4,8),(311,'Un juego de jarrones precioso, precio inmejorable y entrega rápida','josé','alutecval@gmail.com',5,4),(332,'Un jarrón precioso y de mucha calidad. Muy recomendado','josé','alutecval@gmail.com',5,2),(335,'Bonitos jarrones para casa y a un precio contenido','josé','alutecval@gmail.com',5,1),(336,'Un juego de jarrones bonito, pero algo caro','josé','alutecval@gmail.com',5,7),(337,'Me ha gustado mucho la calidad, la estética modernista y el precio que tiene','josé','alutecval@gmail.com',5,5),(338,'Un jarrón precioso y a un precio inmejorable. Muy recomendado','josé','alutecval@gmail.com',5,11),(339,'Un juego de jarrones precioso y a muy buen precio','vicente','vicentemarti31416@gmail.com',5,9),(340,'Un jarrón precioso a un precio muy competitivo','vicente','vicentemarti31416@gmail.com',5,11),(341,'Un juego de jarrones precioso y a muy buen precio','vicente','vicentemarti31416@gmail.com',5,4);
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rsa_key_pairs`
--

DROP TABLE IF EXISTS `rsa_key_pairs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rsa_key_pairs` (
  `id` varchar(255) NOT NULL,
  `private_key` text NOT NULL,
  `public_key` text NOT NULL,
  `created` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_created` (`id`,`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rsa_key_pairs`
--

LOCK TABLES `rsa_key_pairs` WRITE;
/*!40000 ALTER TABLE `rsa_key_pairs` DISABLE KEYS */;
/*!40000 ALTER TABLE `rsa_key_pairs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_tokens`
--

DROP TABLE IF EXISTS `security_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_tokens` (
  `id` binary(16) NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_tokens`
--

LOCK TABLES `security_tokens` WRITE;
/*!40000 ALTER TABLE `security_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `security_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session`
--

DROP TABLE IF EXISTS `spring_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spring_session` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session`
--

LOCK TABLES `spring_session` WRITE;
/*!40000 ALTER TABLE `spring_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spring_session_attributes`
--

DROP TABLE IF EXISTS `spring_session_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session_attributes`
--

LOCK TABLES `spring_session_attributes` WRITE;
/*!40000 ALTER TABLE `spring_session_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `spring_session_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `created_date` datetime(6) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('06bf6eb4-f11b-4b5d-86bb-fe2ad42a5c49','2026-10-14 10:21:08.000000','','{bcrypt}$2a$10$TF9Cb4BZoyQ7HUHqq0Dgce9RUUtUkTkOtkgU7clR.T4MeVwfoNZau','cuentadeprueba13asdf102026@gmail.com'),('4d071a6d-01cf-4b1d-b5de-1abecf8ecfdc','2026-06-12 10:28:12.000000','','{bcrypt}$2a$10$K5P4mPovVflGPkNkRZfyyejoMpFsw4tRdXhY2ddBlzLmJncRgx/ii','vicente6153155416@gmail.com'),('b98f3367-cbaa-45d5-8bc4-320d6b890f4c','2026-07-07 09:41:48.000000','','{bcrypt}$2a$10$n8Tn5Ujlp7iltW4maklpfOvIPQaIiLbt.nI7NLHMVJRIr5d2GPj8O','random6516165164161@gmail.com'),('eb249f31-9ce9-4541-a1cd-bdb118c45f17','2026-10-26 11:30:07.000000','','{bcrypt}$2a$10$DQA5M.A8Tpnt/WnwSwIAguurbYO0ADHaAl8FU66uG8ruFyXVxUWua','vicentemartisender@gmail.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'store'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-16 11:29:37
