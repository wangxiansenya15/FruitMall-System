CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `username` varchar(75) NOT NULL,
                        `password` varchar(200) NOT NULL,
                        `email` varchar(200) DEFAULT NULL COMMENT '电子邮箱',
                        `phone` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                        `role` enum('ADMIN','SUPER_ADMIN','USER') DEFAULT 'USER',
                        `nickname` varchar(50) DEFAULT NULL,
                        `register_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                        `updateInfo_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `enabled` tinyint(1) NOT NULL DEFAULT '1',
                        `account_non_expired` tinyint(1) NOT NULL DEFAULT '1',
                        `account_non_locked` tinyint(1) NOT NULL DEFAULT '1',
                        `credentials_non_expired` tinyint(1) NOT NULL DEFAULT '1',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `username` (`username`),
                        UNIQUE KEY `email` (`email`),
                        UNIQUE KEY `email_2` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO fruitdb.user (id, username, password, email, register_time, updateInfo_time, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES (1, 'admin', 'pwd123',  'admin@wxs.org', '2025-04-20 11:38:50', '2025-05-18 02:04:39', 1, 1, 1, 1);

