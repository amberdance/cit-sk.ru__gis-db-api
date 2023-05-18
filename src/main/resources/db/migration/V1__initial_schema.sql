CREATE SEQUENCE IF NOT EXISTS categories_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS organizations_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS questions_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE categories
(
    id   BIGINT       NOT NULL,
    name VARCHAR(128) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id       BIGINT        NOT NULL,
    question VARCHAR(1024) NOT NULL,
    answer   VARCHAR(32600),
    user_id  BIGINT        NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE organizations
(
    id            BIGINT       NOT NULL,
    name          VARCHAR(128) NOT NULL,
    address       VARCHAR(128),
    is_government BOOLEAN      NOT NULL,
    CONSTRAINT pk_organizations PRIMARY KEY (id)
);

CREATE TABLE questions
(
    id          BIGINT         NOT NULL,
    label       VARCHAR(2048)  NOT NULL,
    answer      VARCHAR(32600) NOT NULL,
    category_id BIGINT         NOT NULL,
    CONSTRAINT pk_questions PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT      NOT NULL,
    chat_id  VARCHAR(12) NOT NULL,
    username VARCHAR(32) NOT NULL,
    email    VARCHAR(32),
    role     VARCHAR(32) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE categories
    ADD CONSTRAINT uc_categories_name UNIQUE (name);

ALTER TABLE organizations
    ADD CONSTRAINT uc_organizations_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT uc_users_chat UNIQUE (chat_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE questions
    ADD CONSTRAINT FK_QUESTIONS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);
