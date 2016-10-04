CREATE SEQUENCE SEQ_GRANT_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1;

CREATE TABLE T_GRANT (
  ID          BIGINT                                       NOT NULL,
  OWNER_ID    BIGINT                                       NOT NULL,
  CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP          NOT NULL,

  GROUP_ID    BIGINT                                       NOT NULL,
  CONSTRAINT PK_GRANT PRIMARY KEY (ID),
  CONSTRAINT FK_GRANT_OWNER FOREIGN KEY (OWNER_ID) REFERENCES T_IDENTITY (ID),
  CONSTRAINT FK_GRANT_GROUP FOREIGN KEY (GROUP_ID) REFERENCES T_IDENTITY (ID),
  CONSTRAINT UQ_GRANT UNIQUE (OWNER_ID, GROUP_ID)
);
