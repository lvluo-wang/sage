CREATE SEQUENCE SEQ_TOKEN_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1;

CREATE TABLE T_TOKEN (
  ID            BIGINT                                                        NOT NULL,
  OWNER_ID      BIGINT                                                        NOT NULL,
  CREATE_TIME   TIMESTAMP DEFAULT CURRENT_TIMESTAMP                           NOT NULL,
  UPDATE_TIME   TIMESTAMP                                                     NULL,
  IS_DELETED    CHAR(1) DEFAULT 'N'                                           NOT NULL,

  CLIENT_ID     BIGINT                                                        NOT NULL,
  ACCESS_SECRET VARCHAR(64)                                                   NOT NULL,
  SESSION_ID    VARCHAR(100)                                                  NULL,
  EXPIRE_TIME   TIMESTAMP                                                     NULL,
  CONSTRAINT PK_TOKEN PRIMARY KEY (ID),
  CONSTRAINT FK_TOKEN_OWNER FOREIGN KEY (OWNER_ID) REFERENCES T_IDENTITY (ID),
  CONSTRAINT FK_TOKEN_CLIENT FOREIGN KEY (CLIENT_ID) REFERENCES T_IDENTITY (ID)
);
