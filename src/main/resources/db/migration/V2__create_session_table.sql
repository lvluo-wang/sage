CREATE SEQUENCE SEQ_SESSION_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1;

CREATE TABLE T_SESSION (
  ID          INTEGER                                                       NOT NULL,
  OWNER_ID    INTEGER                                                       NOT NULL,
  CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP                           NOT NULL,
  UPDATE_TIME TIMESTAMP                                                     NULL,
  IS_DELETED  CHAR(1) DEFAULT 'N'                                           NOT NULL,

  SESSION_ID  VARCHAR(100)                                                  NOT NULL,
  EXPIRE_TIME TIMESTAMP                                                     NOT NULL,
  IP          VARCHAR(100)                                                  NULL,
  CLIENT_ID   VARCHAR(100)                                                  NULL,
  TIME_ZONE   VARCHAR(100)                                                  NULL,

  CONSTRAINT PK_SESSION PRIMARY KEY (ID),
  CONSTRAINT UQ_SESSION UNIQUE (SESSION_ID),
  CONSTRAINT FK_SESSION_OWNER FOREIGN KEY (OWNER_ID) REFERENCES T_IDENTITY (ID)
);
