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
    ('Bob', '987654321', '2222');

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
    (1, 'Alice', '我已经被修改了', '宝宝已黑化', 3, 8, '2024-10-28 19:34:03'),
    (1, 'Alice', '宝宝今年5岁了', '今天已完成吃喝拉撒', 0, 0, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝今年5岁了', '今天已完成吃喝拉撒', 0, 0, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝今年5岁了', '今天已完成吃喝拉撒', 0, 0, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝今年5岁了', '今天已完成吃喝拉撒', 0, 0, '2024-10-28 09:28:50'),
    (1, 'Alice', '宝宝今年5岁了', '今天已完成吃喝拉撒', 0, 0, '2024-10-28 09:28:50'),
    (2, 'Bob', '急急急！！！', '宝宝上吐下泻怎么办？', 0, 0, '2024-12-11 11:24:33'),
    (2, 'Bob', '宝宝一直哭', '宝宝是不是饿了？', 0, 0, '2024-12-11 11:41:39'),
    (2, 'Bob', '宝宝一直哭闹', '宝宝是不是没吃饱？', 0, 0, '2024-12-11 11:43:44'),
    (2, 'Bob', '宝宝还是好无聊', '宝宝一整天都在睡觉的吗？', 0, 0, '2024-12-11 19:02:12');


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
    (2, 1, 'Alice', '今天完成吃喝拉lalal', 1, '2024-11-24 17:36:33'),
    (3, 1, 'Alice', '今天已完成吃喝拉撒', 0, '2024-11-24 17:23:20'),
    (4, 1, 'Alice', '今天完成吃喝拉撒', 0, '2024-11-24 17:23:57'),
    (5, 1, 'Alice', '今天完成吃喝拉撒', 0, '2024-11-24 17:24:27'),
    (6, 1, 'Alice', '今天完成吃喝拉撒', 0, '2024-11-24 17:24:33');

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
