CREATE SEQUENCE sq_on_quotation_id;

CREATE TABLE quotation (
    id      bigint          DEFAULT nextval('sq_on_quotation_id') PRIMARY KEY,
    qid     varchar(30)     NOT NULL,
    text    varchar(150)    NOT NULL
);

CREATE UNIQUE INDEX uk_on_quotation_qid ON quotation (qid);
