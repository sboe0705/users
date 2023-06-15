CREATE TABLE _user (
  id VARCHAR(255) NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255)
);

ALTER TABLE _user
ADD CONSTRAINT pk_user_id PRIMARY KEY(id);
