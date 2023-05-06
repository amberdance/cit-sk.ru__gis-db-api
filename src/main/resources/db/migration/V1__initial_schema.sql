CREATE TABLE information_systems
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_information_systems PRIMARY KEY (id)
);

CREATE TABLE organizations
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name          VARCHAR(255)                            NOT NULL,
    address       VARCHAR(255),
    is_government BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_organizations PRIMARY KEY (id)
);

CREATE TABLE user_types
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    type VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_user_types PRIMARY KEY (id)
);

CREATE TABLE users
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    chat_id      VARCHAR(50)                             NOT NULL,
    email        VARCHAR(50),
    phone        VARCHAR(12),
    username     VARCHAR(255)                            NOT NULL,
    first_name   VARCHAR(255)                            NOT NULL,
    user_type_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE information_systems
    ADD CONSTRAINT uc_information_systems_name UNIQUE (name);

ALTER TABLE organizations
    ADD CONSTRAINT uc_organizations_name UNIQUE (name);

ALTER TABLE user_types
    ADD CONSTRAINT uc_user_types_type UNIQUE (type);

ALTER TABLE users
    ADD CONSTRAINT uc_users_chat UNIQUE (chat_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_phone UNIQUE (phone);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_USER_TYPE FOREIGN KEY (user_type_id) REFERENCES user_types (id);