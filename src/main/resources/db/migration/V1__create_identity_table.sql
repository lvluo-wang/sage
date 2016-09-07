CREATE SEQUENCE SEQ_IDENTITY_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 10000;

CREATE TABLE T_IDENTITY (
  -- BaseEntity
  ID            INTEGER                                                              NOT NULL,
  OWNER_ID      INTEGER DEFAULT 0                                                    NOT NULL,
  CREATE_TIME   TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                  NOT NULL,
  UPDATE_TIME   TIMESTAMP                                                            NULL,
  IS_DELETED    CHAR(1) DEFAULT 'N'                                                  NOT NULL,
  --
  SALT          VARCHAR(16) DEFAULT '-'                                              NOT NULL,
  PASSWORD      VARCHAR(64) DEFAULT '-'                                              NOT NULL,
  CREATE_BY     INTEGER                                                              NOT NULL,
  TYPE          VARCHAR(64) DEFAULT 'USER'                                           NOT NULL,
  VALID_SECONDS INTEGER                                                              NULL,
  IS_BLOCKED    CHAR(1) DEFAULT 'N'                                                  NOT NULL,
  CONSTRAINT PK_IDENTITY PRIMARY KEY (ID),
  CONSTRAINT FK_IDENTITY_OWNER FOREIGN KEY (OWNER_ID) REFERENCES T_IDENTITY (ID),
  CONSTRAINT FK_IDENTITY_CREATE_BY FOREIGN KEY (CREATE_BY) REFERENCES T_IDENTITY (ID)
);

-- System 0
INSERT INTO T_IDENTITY (ID, OWNER_ID, CREATE_BY, TYPE) VALUES (0, 0, 0, 'SYSTEM');

-- Client 1000
INSERT INTO T_IDENTITY (ID, OWNER_ID, CREATE_BY, TYPE, VALID_SECONDS) VALUES (1000, 0, 0, 'CLIENT', 86400 * 7);