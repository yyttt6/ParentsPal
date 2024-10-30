--在环境变量中设置密码
$env:JASYPT_ENCRYPTOR_PASSWORD = "123456"

--创建一个无需密码登录的user01，并赋予他除了DELETE之外的所有权限  
mysql -u root -p  
CREATE USER 'user01'@'localhost';  
GRANT ALL PRIVILEGES ON *.* TO 'user01'@'localhost';  
REVOKE DELETE ON *.* FROM 'user01'@'localhost';  
FLUSH PRIVILEGES;  
DROP DATABASE conv_test;  
EXIT  
  
--登录user01  
mysql -u user01   
  
--创建对话测试的数据库  
CREATE DATABASE conv_test  
CHARACTER SET utf8mb4  
COLLATE utf8mb4_general_ci;  
USE conv_test;  
  
--创建数据表  
CREATE TABLE user (  
    user_id INT AUTO_INCREMENT PRIMARY KEY,  
    name VARCHAR(255) NOT NULL UNIQUE  
);  
  
CREATE TABLE conversation (  
    conversation_id INT AUTO_INCREMENT PRIMARY KEY,  
    user1_id INT NOT NULL,  
    user2_id INT NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (user1_id) REFERENCES user(user_id),  
    FOREIGN KEY (user2_id) REFERENCES user(user_id),  
    UNIQUE (user1_id, user2_id)  
);  
  
CREATE TABLE message (  
    message_id INT AUTO_INCREMENT PRIMARY KEY,  
    conversation_id INT NOT NULL,  
    sender_id INT NOT NULL,  
    receiver_id INT NOT NULL,  
    content VARCHAR(1000) NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id),  
    FOREIGN KEY (sender_id) REFERENCES user(user_id),  
    FOREIGN KEY (receiver_id) REFERENCES user(user_id)  
);  
  
--初始化用户  
INSERT INTO user (name) VALUES ('Alice');  
INSERT INTO user (name) VALUES ('Bob');  
INSERT INTO user (name) VALUES ('Charlie');  
INSERT INTO user (name) VALUES ('David');  
  
--接下来是手动发送网络请求  
  
--建立对话  
URL: /api/conversations/create?username1=Alice&username2=Bob  
Method: POST  
URL: /api/conversations/create?username1=Alice&username2=Charlie  
Method: POST  
URL: /api/conversations/create?username1=Charlie&username2=David  
Method: POST  
  
--发送消息  
URL: /api/conversations/message?senderUsername=Alice&receiverUsername=Bob&content=Hello%20Bob!  
Method: POST  
URL: /api/conversations/message?senderUsername=Bob&receiverUsername=Alice&content=Hi%20Alice!  
Method: POST  
URL: /api/conversations/message?senderUsername=Charlie&receiverUsername=David&content=Hey%20David!  
Method: POST  
URL: /api/conversations/message?senderUsername=David&receiverUsername=Charlie&content=Hey%20Charlie!  
Method: POST  
URL: /api/conversations/message?senderUsername=Alice&receiverUsername=Charlie&content=Hello%20Charlie!  
Method: POST  
URL: /api/conversations/message?senderUsername=Alice&receiverUsername=Charlie&content=😊  
Method: POST  
  
  
--显示所有对话的最新一条消息  
URL: /api/conversations/latest-messages?username=Alice  
Method: GET  
URL: /api/conversations/latest-messages?username=David  
Method: GET  
--结果  
\{  
        "data": \[  
            \{  
            "sender_name": "Bob",  
            "receiver_name": "Alice",  
            "content": "Hi Alice!",  
            "created_at": "2024-10-08T23:14:26"  
            \},  
            \{  
                "sender_name": "Alice",  
                "receiver_name": "Charlie",  
                "content": "😊",  
                "created_at": "2024-10-08T23:15:34"  
            \}  
        \],  
        "success": true,  
        "errorMsg": null  
\}  
  
--获取两个用户之间的对话的最后100条消息  
URL: /api/conversations/messages-between-users?username1=Alice&username2=Charlie  
Method: GET  
URL: /api/conversations/messages-between-users?username1=Charlie&username2=David  
Method: GET  
--结果  
\{  
"data": \[  
    \{  
        "sender_name": "Alice",  
        "receiver_name": "Charlie",    
        "content": "😊",  
        "created_at": "2024-10-08T23:15:34"  
    \},  
    \{  
        "sender_name": "Alice",  
        "receiver_name": "Charlie",  
        "content": "Hello Charlie!",  
        "created_at": "2024-10-08T23:15:22"  
    \}  
    \],  
    "success": true,  
    "errorMsg": null  
\}  
  
