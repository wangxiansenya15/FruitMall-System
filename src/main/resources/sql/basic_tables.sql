







-- 员工表
CREATE TABLE `staff` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `name` varchar(50) NOT NULL,
                         `role` enum('admin','service','warehouse','delivery') NOT NULL,
                         `phone` varchar(20) NOT NULL,
                         `email` varchar(100) NOT NULL,
                         `password` varchar(100) NOT NULL,
                         `status` enum('active','inactive') DEFAULT 'active',
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `phone` (`phone`),
                         UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;