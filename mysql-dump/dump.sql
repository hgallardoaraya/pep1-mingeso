-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: localhost    Database: topeducation
-- ------------------------------------------------------
-- Server version	8.1.0

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

--
-- Table structure for table `descuento_anio_egreso`
--

DROP TABLE IF EXISTS `descuento_anio_egreso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descuento_anio_egreso` (
  `id` int NOT NULL,
  `cantidad_anios_egreso_inferior` int NOT NULL,
  `cantidad_anios_egreso_superior` int NOT NULL,
  `porcentaje_descuento` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descuento_anio_egreso`
--

LOCK TABLES `descuento_anio_egreso` WRITE;
/*!40000 ALTER TABLE `descuento_anio_egreso` DISABLE KEYS */;
INSERT INTO `descuento_anio_egreso` VALUES (0,0,0,15),(1,1,2,8),(2,3,4,4),(3,5,100,0);
/*!40000 ALTER TABLE `descuento_anio_egreso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descuento_puntaje_prueba`
--

DROP TABLE IF EXISTS `descuento_puntaje_prueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descuento_puntaje_prueba` (
  `id` int NOT NULL,
  `puntaje_inferior` double DEFAULT NULL,
  `puntaje_superior` double DEFAULT NULL,
  `porcentaje_descuento` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descuento_puntaje_prueba`
--

LOCK TABLES `descuento_puntaje_prueba` WRITE;
/*!40000 ALTER TABLE `descuento_puntaje_prueba` DISABLE KEYS */;
INSERT INTO `descuento_puntaje_prueba` VALUES (0,950,1000,10),(1,900,949,5),(2,850,899,2),(3,0,849,0);
/*!40000 ALTER TABLE `descuento_puntaje_prueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descuento_tipo_colegio`
--

DROP TABLE IF EXISTS `descuento_tipo_colegio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descuento_tipo_colegio` (
  `id` int NOT NULL,
  `id_tipo_colegio` int NOT NULL,
  `porcentaje_descuento` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `descuento_tipo_colegio_tipo_colegio_id_fk` (`id_tipo_colegio`),
  CONSTRAINT `descuento_tipo_colegio_tipo_colegio_id_fk` FOREIGN KEY (`id_tipo_colegio`) REFERENCES `tipo_colegio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descuento_tipo_colegio`
--

LOCK TABLES `descuento_tipo_colegio` WRITE;
/*!40000 ALTER TABLE `descuento_tipo_colegio` DISABLE KEYS */;
INSERT INTO `descuento_tipo_colegio` VALUES (0,0,20),(1,1,10),(2,2,0);
/*!40000 ALTER TABLE `descuento_tipo_colegio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `descuento_tipo_pago_arancel`
--

DROP TABLE IF EXISTS `descuento_tipo_pago_arancel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `descuento_tipo_pago_arancel` (
  `id` int NOT NULL,
  `id_tipo_pago_arancel` int NOT NULL,
  `porcentaje_descuento` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `descuento_tipo_pago_arancel_tipo_pago_arancel_id_fk` (`id_tipo_pago_arancel`),
  CONSTRAINT `descuento_tipo_pago_arancel_tipo_pago_arancel_id_fk` FOREIGN KEY (`id_tipo_pago_arancel`) REFERENCES `tipo_pago_arancel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `descuento_tipo_pago_arancel`
--

LOCK TABLES `descuento_tipo_pago_arancel` WRITE;
/*!40000 ALTER TABLE `descuento_tipo_pago_arancel` DISABLE KEYS */;
INSERT INTO `descuento_tipo_pago_arancel` VALUES (0,0,0),(1,1,50);
/*!40000 ALTER TABLE `descuento_tipo_pago_arancel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_razon`
--

DROP TABLE IF EXISTS `estado_razon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_razon` (
  `id` int NOT NULL,
  `estado` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_razon`
--

LOCK TABLES `estado_razon` WRITE;
/*!40000 ALTER TABLE `estado_razon` DISABLE KEYS */;
INSERT INTO `estado_razon` VALUES (0,'PAGADA'),(1,'PENDIENTE'),(2,'ATRASADA');
/*!40000 ALTER TABLE `estado_razon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiante`
--

DROP TABLE IF EXISTS `estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre1` varchar(255) DEFAULT NULL,
  `nombre2` varchar(255) DEFAULT NULL,
  `apellido1` varchar(255) DEFAULT NULL,
  `apellido2` varchar(255) DEFAULT NULL,
  `rut` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `anio_egreso` int DEFAULT NULL,
  `nombre_colegio` varchar(255) DEFAULT NULL,
  `id_tipo_colegio` int NOT NULL,
  `id_tipo_pago_arancel` int NOT NULL,
  `cuotas_pactadas` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tipo_colegio_estudiante_idx` (`id_tipo_colegio`),
  KEY `fk_tipo_pago_arancel_estudiante_idx` (`id_tipo_pago_arancel`),
  CONSTRAINT `estudiante_tipo_colegio_id_fk` FOREIGN KEY (`id_tipo_colegio`) REFERENCES `tipo_colegio` (`id`),
  CONSTRAINT `estudiante_tipo_pago_arancel_id_fk` FOREIGN KEY (`id_tipo_pago_arancel`) REFERENCES `tipo_pago_arancel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiante`
--

LOCK TABLES `estudiante` WRITE;
/*!40000 ALTER TABLE `estudiante` DISABLE KEYS */;
INSERT INTO `estudiante` VALUES (8,'Eugenio','Felipe','Vasquez','Concha','19.234.554-8','2000-06-13',2020,'Santa Isabel de Hungría',1,0,7),(9,'Juan','Ramon','Perez','Diaz','20.234.343-7','2000-10-09',2019,'Santa Margarita',2,0,4),(10,'Felipe','Edmundo','Rodriguez','Velasquez','18.544.321-5','1997-10-27',2016,'Colegio 1',0,0,10),(11,'Nombre 1','Nombre 2 ','Apellido 1','Apellido 2','12.345.234-2','1995-10-09',2015,'Colegio 2',1,0,5),(12,'Raimundo','Bastian','Castro','Silva','19.234.235-8','1998-10-09',2022,'Colegio 3',1,0,2),(13,'Juán','Ramón','Vasquez','Silva','12.345.234-1','1999-10-20',2019,'Instituto Nacional',0,0,10),(14,'Juán','Ramón','Perez','Velasquez','20.285.942-3','1997-10-10',2017,'Colegio 4',1,0,7);
/*!40000 ALTER TABLE `estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiante_descuento`
--

DROP TABLE IF EXISTS `estudiante_descuento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiante_descuento` (
  `id` int NOT NULL,
  `id_tipo_descuento` int NOT NULL,
  `id_estudiante` int NOT NULL,
  `id_razon` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `estudiante_descuento_estudiante_id_fk` (`id_estudiante`),
  KEY `estudiante_descuento_tipo_descuento_id_fk` (`id_tipo_descuento`),
  KEY `estudiante_descuento_razon_null_fk` (`id_razon`),
  CONSTRAINT `estudiante_descuento_estudiante_null_fk` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id`),
  CONSTRAINT `estudiante_descuento_razon_null_fk` FOREIGN KEY (`id_razon`) REFERENCES `razon` (`id`) ON DELETE CASCADE,
  CONSTRAINT `estudiante_descuento_tipo_descuento_id_fk` FOREIGN KEY (`id_tipo_descuento`) REFERENCES `tipo_descuento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiante_descuento`
--

LOCK TABLES `estudiante_descuento` WRITE;
/*!40000 ALTER TABLE `estudiante_descuento` DISABLE KEYS */;
/*!40000 ALTER TABLE `estudiante_descuento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `examen`
--

DROP TABLE IF EXISTS `examen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `examen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `fecha` date NOT NULL,
  `puntaje` int NOT NULL,
  `revision` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_estudiante_examen_idx` (`id_estudiante`),
  CONSTRAINT `examen_estudiante_id_fk` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `examen`
--

LOCK TABLES `examen` WRITE;
/*!40000 ALTER TABLE `examen` DISABLE KEYS */;
INSERT INTO `examen` VALUES (28,8,'2023-09-29',900,0),(29,9,'2023-09-29',860,0),(30,10,'2023-09-29',700,0),(31,11,'2023-09-29',800,0),(32,12,'2023-09-29',988,0),(38,8,'2023-09-29',900,0),(39,9,'2023-09-29',860,0),(40,10,'2023-09-29',700,0),(41,11,'2023-09-29',800,0),(42,12,'2023-09-29',988,0),(43,13,'2023-09-29',700,0),(44,14,'2023-09-29',0,0);
/*!40000 ALTER TABLE `examen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interes_meses_atraso`
--

DROP TABLE IF EXISTS `interes_meses_atraso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interes_meses_atraso` (
  `id` int NOT NULL,
  `meses_atraso` int NOT NULL,
  `porcentaje_interes` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interes_meses_atraso`
--

LOCK TABLES `interes_meses_atraso` WRITE;
/*!40000 ALTER TABLE `interes_meses_atraso` DISABLE KEYS */;
INSERT INTO `interes_meses_atraso` VALUES (0,0,0),(1,1,3),(2,2,6),(3,3,9),(4,4,15);
/*!40000 ALTER TABLE `interes_meses_atraso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `max_cuotas_tipo_colegio`
--

DROP TABLE IF EXISTS `max_cuotas_tipo_colegio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `max_cuotas_tipo_colegio` (
  `id` int NOT NULL,
  `id_tipo_colegio` int NOT NULL,
  `max_cuotas` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tipo_colegio_max_cuotas_tipo_colegio` (`id_tipo_colegio`) /*!80000 INVISIBLE */,
  CONSTRAINT `max_cuotas_tipo_colegio_tipo_colegio_id_fk` FOREIGN KEY (`id_tipo_colegio`) REFERENCES `tipo_colegio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `max_cuotas_tipo_colegio`
--

LOCK TABLES `max_cuotas_tipo_colegio` WRITE;
/*!40000 ALTER TABLE `max_cuotas_tipo_colegio` DISABLE KEYS */;
INSERT INTO `max_cuotas_tipo_colegio` VALUES (0,0,10),(1,1,7),(2,2,4);
/*!40000 ALTER TABLE `max_cuotas_tipo_colegio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `total` int NOT NULL,
  `fecha` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pago_estudiante_id_fk` (`id_estudiante`),
  CONSTRAINT `pago_estudiante_id_fk` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
INSERT INTO `pago` VALUES (4,8,70000,'2023-10-09'),(5,8,175714,'2023-10-09'),(6,8,175714,'2023-10-09'),(7,8,175714,'2023-10-09'),(8,8,175714,'2023-10-09');
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago_razon`
--

DROP TABLE IF EXISTS `pago_razon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago_razon` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_pago` int NOT NULL,
  `id_razon` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pago_pago_razon_idx` (`id_pago`),
  KEY `pago_razon_razon_null_fk` (`id_razon`),
  CONSTRAINT `pago_razon_pago_null_fk` FOREIGN KEY (`id_pago`) REFERENCES `pago` (`id`) ON DELETE CASCADE,
  CONSTRAINT `pago_razon_razon_null_fk` FOREIGN KEY (`id_razon`) REFERENCES `razon` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago_razon`
--

LOCK TABLES `pago_razon` WRITE;
/*!40000 ALTER TABLE `pago_razon` DISABLE KEYS */;
INSERT INTO `pago_razon` VALUES (13,4,86),(14,5,87),(15,6,88),(16,7,89),(17,8,90);
/*!40000 ALTER TABLE `pago_razon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `razon`
--

DROP TABLE IF EXISTS `razon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `razon` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_tipo_razon` int NOT NULL,
  `numero_razon` int NOT NULL,
  `monto` int NOT NULL,
  `id_estado_razon` int NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `id_estudiante` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_tipo_razon_razon_idx` (`id_tipo_razon`),
  KEY `fk_estado_razon_razon_idx` (`id_estado_razon`) /*!80000 INVISIBLE */,
  KEY `fk_estudiante_razon_idx` (`id_estudiante`),
  CONSTRAINT `FK3234l23174ryxivjn258yiofj` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id`),
  CONSTRAINT `razon_estado_razon_id_fk` FOREIGN KEY (`id_estado_razon`) REFERENCES `estado_razon` (`id`),
  CONSTRAINT `razon_tipo_razon_id_fk` FOREIGN KEY (`id_tipo_razon`) REFERENCES `tipo_razon` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=153 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `razon`
--

LOCK TABLES `razon` WRITE;
/*!40000 ALTER TABLE `razon` DISABLE KEYS */;
INSERT INTO `razon` VALUES (86,0,0,70000,0,'2023-02-24','2023-03-01',8),(87,1,1,175714,0,'2023-04-05','2023-04-10',8),(88,1,2,175714,0,'2023-05-05','2023-05-10',8),(89,1,3,175714,0,'2023-06-05','2023-06-10',8),(90,1,4,175714,0,'2023-07-05','2023-07-10',8),(91,1,5,209109,2,'2023-08-05','2023-08-10',8),(92,1,6,209109,2,'2023-09-05','2023-09-10',8),(93,1,7,209109,2,'2023-10-05','2023-10-10',8),(94,0,0,70000,1,'2023-02-24','2023-03-01',9),(95,1,1,629642,2,'2023-04-05','2023-04-10',9),(96,1,2,629642,2,'2023-05-05','2023-05-10',9),(97,1,3,629642,2,'2023-06-05','2023-06-10',9),(98,1,4,629642,2,'2023-07-05','2023-07-10',9),(110,0,0,70000,1,'2023-02-24','2023-03-01',11),(111,1,1,514731,2,'2023-04-05','2023-04-10',11),(112,1,2,514731,2,'2023-05-05','2023-05-10',11),(113,1,3,514731,2,'2023-06-05','2023-06-10',11),(114,1,4,514731,2,'2023-07-05','2023-07-10',11),(115,1,5,514731,2,'2023-08-05','2023-08-10',11),(116,0,0,70000,1,'2023-02-24','2023-03-01',12),(117,1,1,743906,2,'2023-04-05','2023-04-10',12),(118,1,2,743906,2,'2023-05-05','2023-05-10',12),(119,0,0,70000,1,'2023-02-24','2023-03-01',13),(120,1,1,114000,1,'2023-04-05','2023-04-10',13),(121,1,2,114000,1,'2023-05-05','2023-05-10',13),(122,1,3,114000,1,'2023-06-05','2023-06-10',13),(123,1,4,114000,1,'2023-07-05','2023-07-10',13),(124,1,5,114000,1,'2023-08-05','2023-08-10',13),(125,1,6,114000,1,'2023-09-05','2023-09-10',13),(126,1,7,114000,1,'2023-10-05','2023-10-10',13),(127,1,8,114000,1,'2023-11-05','2023-11-10',13),(128,1,9,114000,1,'2023-12-05','2023-12-10',13),(129,1,10,114000,1,'2024-01-05','2024-01-10',13),(134,0,0,70000,2,'2023-02-24','2023-03-01',14),(135,1,1,192857,2,'2023-04-05','2023-04-10',14),(136,1,2,192857,2,'2023-05-05','2023-05-10',14),(137,1,3,192857,2,'2023-06-05','2023-06-10',14),(138,1,4,192857,2,'2023-07-05','2023-07-10',14),(139,1,5,192857,2,'2023-08-05','2023-08-10',14),(140,1,6,192857,2,'2023-09-05','2023-09-10',14),(141,1,7,192857,2,'2023-10-05','2023-10-10',14),(142,0,0,70000,2,'2023-02-24','2023-03-01',10),(143,1,1,120000,2,'2023-04-05','2023-04-10',10),(144,1,2,120000,2,'2023-05-05','2023-05-10',10),(145,1,3,120000,2,'2023-06-05','2023-06-10',10),(146,1,4,120000,2,'2023-07-05','2023-07-10',10),(147,1,5,120000,2,'2023-08-05','2023-08-10',10),(148,1,6,120000,2,'2023-09-05','2023-09-10',10),(149,1,7,120000,2,'2023-10-05','2023-10-10',10),(150,1,8,120000,1,'2023-11-05','2023-11-10',10),(151,1,9,120000,1,'2023-12-05','2023-12-10',10),(152,1,10,120000,1,'2024-01-05','2024-01-10',10);
/*!40000 ALTER TABLE `razon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_colegio`
--

DROP TABLE IF EXISTS `tipo_colegio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_colegio` (
  `id` int NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_colegio`
--

LOCK TABLES `tipo_colegio` WRITE;
/*!40000 ALTER TABLE `tipo_colegio` DISABLE KEYS */;
INSERT INTO `tipo_colegio` VALUES (0,'MUNICIPAL'),(1,'SUBVENCIONADO'),(2,'PRIVADO');
/*!40000 ALTER TABLE `tipo_colegio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_descuento`
--

DROP TABLE IF EXISTS `tipo_descuento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_descuento` (
  `id` int NOT NULL,
  `tipo` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_descuento`
--

LOCK TABLES `tipo_descuento` WRITE;
/*!40000 ALTER TABLE `tipo_descuento` DISABLE KEYS */;
INSERT INTO `tipo_descuento` VALUES (0,'TIPO_COLEGIO'),(1,'ANIO_EGRESO'),(2,'PUNTAJE_PRUEBAS'),(3,'TIPO_PAGO_ARANCEL');
/*!40000 ALTER TABLE `tipo_descuento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_pago_arancel`
--

DROP TABLE IF EXISTS `tipo_pago_arancel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_pago_arancel` (
  `id` int NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tipo_UNIQUE` (`tipo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_pago_arancel`
--

LOCK TABLES `tipo_pago_arancel` WRITE;
/*!40000 ALTER TABLE `tipo_pago_arancel` DISABLE KEYS */;
INSERT INTO `tipo_pago_arancel` VALUES (1,'AL CONTADO'),(0,'CUOTAS');
/*!40000 ALTER TABLE `tipo_pago_arancel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_razon`
--

DROP TABLE IF EXISTS `tipo_razon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_razon` (
  `id` int NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='		';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_razon`
--

LOCK TABLES `tipo_razon` WRITE;
/*!40000 ALTER TABLE `tipo_razon` DISABLE KEYS */;
INSERT INTO `tipo_razon` VALUES (0,'MATRICULA'),(1,'ARANCEL');
/*!40000 ALTER TABLE `tipo_razon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `total_razon`
--

DROP TABLE IF EXISTS `total_razon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `total_razon` (
  `id` int NOT NULL,
  `id_tipo_razon` int NOT NULL,
  `total` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `total_razon_tipo_razon_id_fk` (`id_tipo_razon`),
  CONSTRAINT `total_razon_tipo_razon_id_fk` FOREIGN KEY (`id_tipo_razon`) REFERENCES `tipo_razon` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `total_razon`
--

LOCK TABLES `total_razon` WRITE;
/*!40000 ALTER TABLE `total_razon` DISABLE KEYS */;
INSERT INTO `total_razon` VALUES (0,0,70000),(1,1,1500000);
/*!40000 ALTER TABLE `total_razon` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-14 13:04:25
