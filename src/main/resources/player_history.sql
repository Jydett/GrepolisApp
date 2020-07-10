
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS grepolis;

USE grepolis;

DROP TABLE IF EXISTS `alliance`;
CREATE TABLE IF NOT EXISTS `alliance` (
  `id` bigint(20) NOT NULL WITHOUT SYSTEM VERSIONING,
  `allKillPoint` bigint(20) DEFAULT NULL,
  `allKillRank` bigint(20) DEFAULT NULL,
  `attackKillPoint` bigint(20) DEFAULT NULL,
  `attackKillRank` bigint(20) DEFAULT NULL,
  `defKillPoint` bigint(20) DEFAULT NULL,
  `defKillRank` bigint(20) DEFAULT NULL,
  `membersCount` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL WITHOUT SYSTEM VERSIONING,
  `points` bigint(20) DEFAULT NULL,
  `rank` bigint(20) DEFAULT NULL,
  `villageCount` int(11) DEFAULT NULL,
  `start_timestamp` TIMESTAMP(6) GENERATED ALWAYS AS ROW START,
  `end_timestamp` TIMESTAMP(6) GENERATED ALWAYS AS ROW END,
  PERIOD FOR SYSTEM_TIME(`start_timestamp`, `end_timestamp`),
  PRIMARY KEY (`id`)
)  WITH SYSTEM VERSIONING, ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `player`;
CREATE TABLE IF NOT EXISTS `player` (
    `id` bigint(20) NOT NULL WITHOUT SYSTEM VERSIONING,
    `allKillPoint` bigint(20) DEFAULT NULL,
    `allKillRank` bigint(20) DEFAULT NULL,
    `attackKillPoint` bigint(20) DEFAULT NULL,
    `attackKillRank` bigint(20) DEFAULT NULL,
    `defKillPoint` bigint(20) DEFAULT NULL,
    `defKillRank` bigint(20) DEFAULT NULL,
    `name` varchar(255) DEFAULT NULL WITHOUT SYSTEM VERSIONING,
    `points` bigint(20) NOT NULL,
    `rank` int(11) NOT NULL,
    `townCount` int(11) NOT NULL,
    `alliance_id` bigint(20) DEFAULT NULL,
    `start_timestamp` TIMESTAMP(6) GENERATED ALWAYS AS ROW START,
    `end_timestamp` TIMESTAMP(6) GENERATED ALWAYS AS ROW END,
    PERIOD FOR SYSTEM_TIME(`start_timestamp`, `end_timestamp`),
    PRIMARY KEY (`id`),
    KEY `FKhyk17jmcujmo1992pv7ibty3y` (`alliance_id`)
) WITH SYSTEM VERSIONING, ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `player`
    ADD CONSTRAINT `FKhyk17jmcujmo1992pv7ibty3y` FOREIGN KEY (`alliance_id`) REFERENCES `alliance` (`id`);
COMMIT;