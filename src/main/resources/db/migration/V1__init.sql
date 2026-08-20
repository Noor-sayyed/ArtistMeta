CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE app_user (
                          userid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          username VARCHAR(255) NOT NULL UNIQUE,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE artist (
                        artistid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        stagename VARCHAR(255) NOT NULL UNIQUE,
                        artistname VARCHAR(255) NOT NULL,
                        alias TEXT,
                        version DECIMAL,
                        updatedby UUID,
                        updatedtimestamp TIMESTAMP NOT NULL DEFAULT now(),
                        createddate DATE,
                        sequenceno INT,

                        CONSTRAINT fk_artist_updatedby
                            FOREIGN KEY (updatedby)
                                REFERENCES app_user(userid)
);

CREATE TABLE track (
                       trackid UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       title VARCHAR(255) NOT NULL,
                       genre VARCHAR(255),
                       length INT,
                       artistid UUID NOT NULL,
                       updatedby UUID,
                       updatedtimestamp TIMESTAMP NOT NULL DEFAULT now(),

                       CONSTRAINT fk_track_artist
                           FOREIGN KEY (artistid)
                               REFERENCES artist(artistid),

                       CONSTRAINT fk_track_updatedby
                           FOREIGN KEY (updatedby)
                               REFERENCES app_user(userid)
);

CREATE INDEX IF NOT EXISTS idx_artist_createddate ON artist (createddate, artistid);