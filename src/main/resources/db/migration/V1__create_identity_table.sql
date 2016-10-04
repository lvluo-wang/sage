CREATE SEQUENCE SEQ_IDENTITY_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 10000;

CREATE TABLE T_IDENTITY (
  -- BaseEntity
  ID          BIGINT                                            NOT NULL,
  OWNER_ID    BIGINT DEFAULT 0                                  NOT NULL,
  CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP               NOT NULL,
  UPDATE_TIME TIMESTAMP                                         NULL,
  IS_DELETED  CHAR(1) DEFAULT 'N'                               NOT NULL,
  --
  CREATE_ID   BIGINT                                            NOT NULL,
  SALT        CHAR(16) DEFAULT '----------------'               NOT NULL,
  PASSWORD    VARCHAR(64) DEFAULT '-'                           NOT NULL,
  TYPE        VARCHAR(64) DEFAULT 'MEMBER'                      NOT NULL,
  DESCRIPTION VARCHAR(200)                                      NULL,
  EXTENSION   VARCHAR(2048)                                     NULL,
  IS_BLOCKED  CHAR(1) DEFAULT 'N'                               NOT NULL,
  CONSTRAINT PK_IDENTITY PRIMARY KEY (ID),
  CONSTRAINT FK_IDENTITY_OWNER FOREIGN KEY (OWNER_ID) REFERENCES T_IDENTITY (ID)
);

-- System 0 => ROLE_ADMIN
INSERT INTO T_IDENTITY (ID, OWNER_ID, CREATE_ID, TYPE) VALUES (0, 0, 0, 'SYSTEM');

-- Client 1000
INSERT INTO T_IDENTITY (ID, OWNER_ID, CREATE_ID, TYPE, EXTENSION)
VALUES (1000, 0, 0, 'CLIENT', '{"validSeconds":3600}');