CREATE TABLE USERS
(
    id       BIGINT NOT NULL,
    login    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);