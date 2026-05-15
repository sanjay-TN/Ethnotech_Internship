CREATE DATABASE IF NOT EXISTS concept_clarity;
USE concept_clarity;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL,
    password_salt VARCHAR(32) NOT NULL,
    created_at DATETIME NOT NULL,
    INDEX idx_users_email (email)
);

CREATE TABLE IF NOT EXISTS concept_queries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    topic VARCHAR(240) NOT NULL,
    level VARCHAR(40) NOT NULL,
    explanation_type VARCHAR(60) NOT NULL,
    created_at DATETIME NOT NULL,
    INDEX idx_concept_user_created (user_id, created_at),
    INDEX idx_concept_user_topic (user_id, topic),
    INDEX idx_concept_user_level (user_id, level),
    CONSTRAINT fk_concept_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS explanations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    query_id BIGINT NOT NULL UNIQUE,
    content LONGTEXT NOT NULL,
    favorite BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    INDEX idx_explanations_favorite (favorite),
    CONSTRAINT fk_explanation_query FOREIGN KEY (query_id) REFERENCES concept_queries(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    explanation_id BIGINT NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    CONSTRAINT uq_favorite_user_explanation UNIQUE (user_id, explanation_id),
    INDEX idx_favorites_user (user_id),
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_explanation FOREIGN KEY (explanation_id) REFERENCES explanations(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS knowledge_base (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    topic VARCHAR(160) NOT NULL,
    domain VARCHAR(80) NOT NULL,
    summary TEXT NOT NULL,
    keywords VARCHAR(500) NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT uq_knowledge_topic_domain UNIQUE (topic, domain),
    INDEX idx_knowledge_domain (domain),
    FULLTEXT INDEX ft_knowledge_topic_summary (topic, summary, keywords)
);

CREATE TABLE IF NOT EXISTS search_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    topic VARCHAR(240) NOT NULL,
    detected_domain VARCHAR(80) NOT NULL,
    created_at DATETIME NOT NULL,
    INDEX idx_search_user_created (user_id, created_at),
    INDEX idx_search_domain (detected_domain),
    CONSTRAINT fk_search_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conversation_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    message VARCHAR(1000) NOT NULL,
    topic VARCHAR(240) NOT NULL,
    level VARCHAR(40) NOT NULL,
    topic_frequency INT NOT NULL,
    reply LONGTEXT NOT NULL,
    created_at DATETIME NOT NULL,
    INDEX idx_conversation_user_created (user_id, created_at),
    INDEX idx_conversation_user_topic (user_id, topic),
    CONSTRAINT fk_conversation_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS topic_tracking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    topic VARCHAR(240) NOT NULL,
    normalized_topic VARCHAR(240) NOT NULL,
    frequency INT NOT NULL,
    current_level VARCHAR(40) NOT NULL,
    last_asked_at DATETIME NOT NULL,
    CONSTRAINT uq_topic_tracking_user_topic UNIQUE (user_id, normalized_topic),
    INDEX idx_topic_tracking_user (user_id),
    CONSTRAINT fk_topic_tracking_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS learning_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    total_interactions INT NOT NULL DEFAULT 0,
    beginner_count INT NOT NULL DEFAULT 0,
    intermediate_count INT NOT NULL DEFAULT 0,
    advanced_count INT NOT NULL DEFAULT 0,
    expert_count INT NOT NULL DEFAULT 0,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_learning_progress_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (id, name, email, password_hash, password_salt, created_at)
VALUES
    (1, 'Demo Learner', 'demo@conceptclarity.com', '0c65ff5e88c3448ed9ab6a8adcd852eadd5cfff623896feb76a8127481a4ee26', '0123456789abcdef0123456789abcdef', NOW())
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO knowledge_base (topic, domain, summary, keywords, created_at)
VALUES
    ('Recursion', 'Algorithms', 'A function solves a problem by calling itself with smaller inputs until a base condition stops the process.', 'recursion,base condition,recursive call,call stack,algorithm', NOW()),
    ('DBMS Normalization', 'DBMS', 'Database tables are organized to reduce duplicate data, protect consistency, and clarify relationships.', 'dbms,normalization,primary key,foreign key,normal forms', NOW()),
    ('REST APIs', 'Networking', 'A REST API exposes resources through predictable URLs, HTTP methods, status codes, and structured responses.', 'rest,http,api,endpoint,status code,resource', NOW()),
    ('OOP', 'Java', 'Object-oriented programming organizes software around objects that combine data, behavior, and responsibility.', 'oop,class,object,encapsulation,polymorphism,abstraction', NOW()),
    ('Machine Learning', 'Machine Learning', 'Models learn patterns from data and use those patterns to make predictions on new examples.', 'machine learning,dataset,model,training,prediction,evaluation', NOW())
ON DUPLICATE KEY UPDATE summary = VALUES(summary), keywords = VALUES(keywords);

INSERT INTO concept_queries (id, user_id, topic, level, explanation_type, created_at)
VALUES
    (1, 1, 'Recursion', 'Beginner', 'Auto', NOW()),
    (2, 1, 'DBMS Normalization', 'Intermediate', 'Auto', NOW())
ON DUPLICATE KEY UPDATE topic = VALUES(topic);

INSERT INTO explanations (id, query_id, content, favorite, created_at)
VALUES
    (1, 1, '## Recursion\n\n### 1. Short Definition\nRecursion is a technique where a function solves a problem by calling itself with a smaller version of the same problem.\n\n### 2. Detailed Explanation\nRecursion is useful when the same problem pattern repeats. It needs a base condition and a recursive call.\n\n### 3. Step-by-Step Understanding\n1. Identify the repeating problem.\n2. Define the smallest stopping case.\n3. Call the function with a smaller input.\n4. Combine the returned result.\n\n### 4. Real-world Analogy\nIt is like standing between two mirrors where an image repeats, but the explanation still needs a stopping point.\n\n### 5. Example\nfactorial(3) becomes 3 x factorial(2), then 2 x factorial(1), then stops.\n\n### 6. Key Points Summary\n- Recursion needs a base condition.\n- Each call should move toward the base condition.\n- Deep recursion can use stack memory.', TRUE, NOW()),
    (2, 2, '## DBMS Normalization\n\n### 1. Short Definition\nDBMS Normalization organizes database tables to reduce duplicate data and improve consistency.\n\n### 2. Detailed Explanation\nNormalization separates related facts into focused tables and connects them with keys.\n\n### 3. Step-by-Step Understanding\n1. Identify repeated data.\n2. Split facts into focused tables.\n3. Add primary and foreign keys.\n4. Query related data through joins.\n\n### 4. Real-world Analogy\nIt is like keeping student details in one file and course details in another instead of copying the same facts everywhere.\n\n### 5. Example\nA students table, courses table, and enrollments table work together without duplicate course information.\n\n### 6. Key Points Summary\n- Reduces duplication.\n- Improves consistency.\n- Requires thoughtful relationship design.', FALSE, NOW())
ON DUPLICATE KEY UPDATE content = VALUES(content), favorite = VALUES(favorite);

INSERT INTO favorites (id, user_id, explanation_id, created_at)
VALUES
    (1, 1, 1, NOW())
ON DUPLICATE KEY UPDATE created_at = VALUES(created_at);

INSERT INTO search_history (user_id, topic, detected_domain, created_at)
VALUES
    (1, 'Recursion', 'Algorithms', NOW()),
    (1, 'DBMS Normalization', 'DBMS', NOW());

INSERT INTO topic_tracking (user_id, topic, normalized_topic, frequency, current_level, last_asked_at)
VALUES
    (1, 'Recursion', 'recursion', 1, 'Beginner', NOW()),
    (1, 'DBMS Normalization', 'dbms normalization', 1, 'Beginner', NOW())
ON DUPLICATE KEY UPDATE frequency = VALUES(frequency), current_level = VALUES(current_level);

INSERT INTO learning_progress (user_id, total_interactions, beginner_count, intermediate_count, advanced_count, expert_count, updated_at)
VALUES
    (1, 2, 2, 0, 0, 0, NOW())
ON DUPLICATE KEY UPDATE total_interactions = VALUES(total_interactions), updated_at = VALUES(updated_at);
