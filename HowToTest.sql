-- 在环境变量中设置密码
$env:JASYPT_ENCRYPTOR_PASSWORD = "123456"

-- 创建一个无需密码登录的user01，并赋予他除了DELETE之外的所有权限  
mysql -u root -p 
输入你的密码
CREATE USER 'user01'@'localhost';  
GRANT ALL PRIVILEGES ON *.* TO 'user01'@'localhost';  
REVOKE DELETE ON *.* FROM 'user01'@'localhost';  
FLUSH PRIVILEGES;  
DROP DATABASE test;  
EXIT
  
--登录user01  
mysql -u user01   
  
-- 创建对话测试的数据库  
CREATE DATABASE test  
CHARACTER SET utf8mb4  
COLLATE utf8mb4_general_ci;  
USE test;  
  
-- 创建数据表  
CREATE TABLE `parent` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `password` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `phone_number` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `user_id_unique` (`user_id`),
    UNIQUE KEY `username_unique` (`username`),
    UNIQUE KEY `phone_number_unique` (`phone_number`),
    KEY `idx_userid_username` (`user_id`, `username`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO parent (username, password, phone_number)
VALUES
    ('Alice', '123456789', '1111'),
    ('Bob', '987654321', '2222'),
    ('Carol', '999999999', '3333');

CREATE TABLE Baby (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(50),
    birthdate VARCHAR(50),
    photo_url VARCHAR(255),
    parent_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES Parent(id)
);

CREATE TABLE alarm (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    baby_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL, 
    alarm_time DATETIME,
    is_recurring BOOLEAN,
    frequency VARCHAR(50),
    is_active BOOLEAN,
    FOREIGN KEY (baby_id) REFERENCES baby(id)  
);

CREATE TABLE growth_tracking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    weight DOUBLE NOT NULL,
    height DOUBLE NOT NULL,
    measurement_date DATE NOT NULL,
    baby_id BIGINT NOT NULL,
    FOREIGN KEY (baby_id) REFERENCES baby(id)
);

CREATE TABLE Immunization (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    baby_id BIGINT NOT NULL,
    vaccine_name VARCHAR(100) NOT NULL,
    date_given DATE,
    next_due DATE,
    FOREIGN KEY (baby_id) REFERENCES Baby(id)
);

CREATE TABLE `article` (
    `article_id` INT NOT NULL auto_increment,
    `user_id` INT NOT NULL,
    `username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `title` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `content` MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `likes` INT UNSIGNED DEFAULT '0',
    `saves` INT UNSIGNED DEFAULT '0',
    `time_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`article_id`),
    KEY `idx_userid_username` (`user_id`,`username`),
    CONSTRAINT `fk_user_article` FOREIGN KEY (`user_id`, `username`) REFERENCES `parent` (`user_id`, `username`) ON DELETE CASCADE ON UPDATE CASCADE
) engine=innodb DEFAULT charset=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO article (user_id, username, title, content, likes, saves, time_created)
VALUES
    (1, 'Alice', '宝宝的成长记录', '宝宝今天学会了走路', 3, 8, '2024-10-28 19:34:03'),
    (1, 'Alice', '宝宝今年5岁了', '今天和小伙伴一起玩游戏', 0, 0, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝的成长点滴', '宝宝学会了自己吃饭', 1, 2, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝的日常生活', '宝宝开始自己穿鞋子了', 2, 3, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝的成长故事', '宝宝学会了数数', 4, 5, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝的成长点滴', '宝宝会模仿大人的动作了', 5, 6, '2024-10-28 09:28:50'),
    (2, 'Bob', '宝宝生病了怎么办？', '宝宝发烧怎么办？', 1, 2, '2024-12-11 11:24:33'),
    (2, 'Bob', '宝宝不舒服', '宝宝是不是胃口不好？', 2, 1, '2024-12-11 11:41:39'),
    (2, 'Bob', '宝宝总是哭闹', '宝宝是不是肚子疼？', 0, 1, '2024-12-11 11:43:44'),
    (2, 'Bob', '宝宝整天不爱玩', '宝宝是不是累了？', 1, 0, '2024-12-11 19:02:12');

CREATE TABLE `comment` (
    `comment_id` INT NOT NULL auto_increment,
    `article_id` INT NOT NULL,
    `user_id` INT NOT NULL,
    `username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `content` MEDIUMTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `likes` INT UNSIGNED DEFAULT '0',
    `time_created` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`comment_id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_userid_username` (`user_id`,`username`),
    CONSTRAINT `fk_articleid` FOREIGN KEY (`article_id`) REFERENCES `article` (`article_id`) ON DELETE cascade on UPDATE CASCADE,
    CONSTRAINT `fk_user_comment` FOREIGN KEY (`user_id`, `username`) REFERENCES `parent` (`user_id`, `username`) ON DELETE CASCADE ON UPDATE CASCADE
) engine=innodb DEFAULT charset=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO comment (article_id, user_id, username, content, likes, time_created)
VALUES
    (1, 1, 'Alice', '今天去超市买了些水果和零食', 3, '2024-11-24 17:36:33'),
    (2, 1, 'Alice', '完成了今天的健身计划，很有成就感！', 2, '2024-11-24 17:23:20'),
    (2, 1, 'Alice', '今天早上做了瑜伽，感觉很放松', 1, '2024-11-24 17:23:57'),
    (1, 1, 'Alice', '今晚和朋友聚会，聊了很多事情', 4, '2024-11-24 17:24:27'),
    (3, 1, 'Alice', '今天学习了新的编程知识，收获满满', 5, '2024-11-24 17:24:33'),
    (2, 2, 'Bob', '今天吃喝拉撒完了', 2, '2024-11-24 18:12:45'),
    (1, 2, 'Bob', '完成了吃喝拉撒的任务', 1, '2024-11-24 18:15:08'),
    (1, 2, 'Bob', '今天吃喝拉撒都顺利完成了', 0, '2024-11-24 18:17:10'),
    (1, 2, 'Bob', '今天没有吃喝拉撒', 0, '2024-11-24 18:18:22'),
    (3, 2, 'Bob', '吃喝拉撒已经完成', 1, '2024-11-24 18:20:05');

CREATE TABLE liked_article(
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    PRIMARY KEY (user_id, article_id ),
    FOREIGN KEY (user_id) REFERENCES parent(user_id) ON DELETE CASCADE,
    FOREIGN KEY (article_id ) REFERENCES article(article_id ) ON DELETE CASCADE
);

CREATE TABLE saved_article(
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    PRIMARY KEY (user_id, article_id ),
    FOREIGN KEY (user_id) REFERENCES parent(user_id) ON DELETE CASCADE,
    FOREIGN KEY (article_id ) REFERENCES article(article_id ) ON DELETE CASCADE
);

CREATE TABLE liked_comment(
    user_id INT NOT NULL,
    comment_id INT NOT NULL,
    PRIMARY KEY (user_id, comment_id ),
    FOREIGN KEY (user_id) REFERENCES parent(user_id) ON DELETE CASCADE,
    FOREIGN KEY (comment_id ) REFERENCES comment(comment_id ) ON DELETE CASCADE
);

CREATE TABLE conversation (  
    conversation_id INT AUTO_INCREMENT PRIMARY KEY,  
    user1_id BIGINT NOT NULL,  
    user2_id BIGINT NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (user1_id) REFERENCES Parent(id),  
    FOREIGN KEY (user2_id) REFERENCES Parent(id),  
    UNIQUE (user1_id, user2_id)  
);  
  
CREATE TABLE message (  
    message_id INT AUTO_INCREMENT PRIMARY KEY,  
    conversation_id INT NOT NULL,  
    sender_id BIGINT NOT NULL,  
    receiver_id BIGINT NOT NULL,  
    content VARCHAR(1000) NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id),  
    FOREIGN KEY (sender_id) REFERENCES Parent(id),  
    FOREIGN KEY (receiver_id) REFERENCES Parent(id)  
);  
  
CREATE TABLE fcm_token (
    user_id BIGINT NOT NULL,
    token VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Parent(id),
    UNIQUE INDEX idx_user_id (user_id)
);

CREATE TABLE aiconversation (
    conversation_id VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    user_id BIGINT NOT NULL, 
    conversation_name VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (conversation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE aimessage (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL,
    query VARCHAR(2000) CHARACTER SET utf8mb4 NOT NULL,
    answer VARCHAR(4000) CHARACTER SET utf8mb4 NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES aiconversation(conversation_id),
    INDEX idx_message_conversation_timestamp (conversation_id, created_at DESC)
);
