CREATE SEQUENCE SEQ_CLAIM_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 10000;

CREATE TABLE T_CLAIM (
  ID          BIGINT                                                        NOT NULL,
  OWNER_ID    BIGINT                                                        NOT NULL,
  CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP                           NOT NULL,
  UPDATE_TIME TIMESTAMP                                                     NULL,
  IS_DELETED  CHAR(1) DEFAULT 'N'                                           NOT NULL,

  TYPE        VARCHAR(64)                                                   NOT NULL,
  VALUE       VARCHAR(100)                                                  NOT NULL,
  PRIMARY_KEY VARCHAR(64)                                                   NOT NULL,
  IS_VERIFIED CHAR(1) DEFAULT 'N'                                           NOT NULL,
  CONSTRAINT PK_CLAIM PRIMARY KEY (ID),
  CONSTRAINT UQ_CLAIM UNIQUE (TYPE, VALUE, PRIMARY_KEY),
  CONSTRAINT FK_CLAIM_OWNER FOREIGN KEY (OWNER_ID) REFERENCES T_IDENTITY (ID)
);
