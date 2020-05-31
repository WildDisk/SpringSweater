# SpringSweater
Just a clone Twitter, like home project, for Spring Boot learning on Kotlin

## How to started
Before started job needed create worker db, extension "pgcrypto" for this db and superuser role.

### Create database and superuser
CREATE DATABASE springsweater;
CREATE USER springroot WITH password '1Qazxcvb';
GRANT ALL ON DATABASE springsweater TO springroot;
ALTER ROLE springroot SUPERUSER;

### Create extension
Connecting to worker database
CREATE EXTENSION IF NOT EXISTS pgcrypto;