--在环境变量中设置密码
$env:JASYPT_ENCRYPTOR_PASSWORD = "123456"

--创建一个无需密码登录的user01，并赋予他除了DELETE之外的所有权限  
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
  
--创建对话测试的数据库  
CREATE DATABASE test  
CHARACTER SET utf8mb4  
COLLATE utf8mb4_general_ci;  
USE test;  
  
--创建数据表  
CREATE TABLE Parent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    password VARCHAR(255),
    phone_number VARCHAR(255)
);

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

 
CREATE TABLE article (
    id_article INT AUTO_INCREMENT PRIMARY KEY,
    id_user BIGINT NOT NULL,         -- Match the data type of parent.id
    username VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content MEDIUMTEXT NOT NULL,
    likes INT UNSIGNED NOT NULL DEFAULT 0,
    saves INT UNSIGNED NOT NULL DEFAULT 0,
    time DATETIME NOT NULL,
    FOREIGN KEY (id_user) REFERENCES parent(id)  
);

CREATE TABLE article_tag_list (
    id_article_tag_list VARCHAR(36) PRIMARY KEY,
    id_article VARCHAR(36),
    id_article_tag VARCHAR(36)
);

CREATE TABLE comment (
    id_comment VARCHAR(36) PRIMARY KEY,
    id_article INT NOT NULL,                  
    id_user  BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    content MEDIUMTEXT NOT NULL,
    likes INT UNSIGNED NOT NULL DEFAULT 0,
    time DATETIME NOT NULL,
    FOREIGN KEY (id_article) REFERENCES article(id_article),
    FOREIGN KEY (id_user) REFERENCES parent(id)
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
  
