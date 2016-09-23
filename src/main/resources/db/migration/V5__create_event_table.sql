CREATE SEQUENCE SEQ_EVENT_ID INCREMENT BY 1 MINVALUE 1 NO MAXVALUE START WITH 1;

CREATE TABLE T_EVENT (
  ID               BIGINT                                                              NOT NULL,
  EVENT_ID         CHAR(36) UNIQUE                                                     NOT NULL,
  PARENT_EVENT_ID  CHAR(36)                                                            NULL,
  CREATE_TIME      TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                 NOT NULL,
  NEXT_SCAN_TIME   TIMESTAMP DEFAULT CURRENT_TIMESTAMP                                 NOT NULL,
  UPDATE_TIME      TIMESTAMP                                                           NULL,
  STATUS           VARCHAR(64) DEFAULT 'CREATED'                                       NOT NULL,
  ASYNC_EVENT_TYPE VARCHAR(100)                                                        NOT NULL,
  BODY             VARCHAR(2000) DEFAULT '{}'                                          NOT NULL,
  CONSTRAINT PK_EVENT PRIMARY KEY (ID),
  CONSTRAINT FK_EVENT_ID FOREIGN KEY (PARENT_EVENT_ID) REFERENCES T_EVENT (EVENT_ID)
);
