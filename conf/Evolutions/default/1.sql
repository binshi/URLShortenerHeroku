# URLs schema
 
# --- !Ups
CREATE SEQUENCE shorturl_id;
CREATE TABLE shorturl (
    id bigint NOT NULL default nextval('shorturl_id'),
    shortURLPath varchar(30) NOT NULL, 
    longURL varchar(500) NOT NULL,
    numClicks bigint NOT NULL DEFAULT 0,
    created timestamp NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE user_id;
CREATE TABLE userlist (
    id bigint NOT NULL default nextval('user_id'),
    shortURLPath varchar(30) NOT NULL, 
    ipAddr varchar(30),
    clickTime timestamp NOT NULL,
    PRIMARY KEY (id)
);

 
# --- !Downs
DROP TABLE shorturl;
DROP SEQUENCE shorturl_id;
DROP TABLE userlist;
DROP SEQUENCE user_id;