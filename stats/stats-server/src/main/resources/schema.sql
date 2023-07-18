DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats (
    stats_id INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(100) NOT NULL,
    uri VARCHAR(100) NOT NULL,
    ip VARCHAR(15) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_user PRIMARY KEY (stats_id)
);