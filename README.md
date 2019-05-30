# ToDoApplication
This repo contains rest service for managing todo notes and console based java application (Folder: /console) which can be used to test rest endpoints.

# ToDo Rest Service
This service makes use of mysql database. As of now it is configured to use local mysql db. You can change that to point to any db by changing MYSQL secion of /src/main/resources/application.properties. 

This sql script is used to create necessary tables 
~~~~
CREATE TABLE IF NOT EXISTS users (
	id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,
    user_id VARCHAR(256) NOT NULL);

CREATE TABLE IF NOT EXISTS todo (
	id INT PRIMARY KEY AUTO_INCREMENT,
    note_id VARCHAR(256) NOT NULL,
    user_id VARCHAR(256) NOT NULL,
    note VARCHAR(1024) NOT NULL,
    priority INT,
    status INT, # 0 - todo, 1-completed
    created_on DATETIME NOT NULL,	
    complete_by DATETIME
);
~~~~

This rest service exposes following endpoints
* POST /user/login --> To login
* POST /user/create --> To Create new user
* GET /todo --> To Get all the ToDo Items
* POST /todo/create --> To create new ToDo item
* DELETE /todo/delete?noteId={noteId} ==> To Delete existing ToDo Item.
* 

# ToDo Console Application
You can build and run the console application which is very intutive.