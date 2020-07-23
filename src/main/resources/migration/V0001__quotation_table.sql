CREATE SEQUENCE SQ_on_quotation_id;

CREATE TABLE quotation (
    id      bigint          PRIMARY KEY DEFAULT nextval('SQ_on_quotation_id'),
    qid     varchar(30)     NOT NULL,
    text    varchar(150)    NOT NULL
);

CREATE UNIQUE INDEX UK_on_quotation_qid ON quotation (qid);
