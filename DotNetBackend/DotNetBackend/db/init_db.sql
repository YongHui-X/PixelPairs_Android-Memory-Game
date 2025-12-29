-- Create database AndroidCA 
CREATE DATABASE AndroidCA;

-- Use database AndroidCA 
USE AndroidCA;

-- Create users table
CREATE TABLE Users(
    Id INT AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(50) NOT NULL,
    PRIMARY KEY (Id)
);

-- Create user details table (with UserType)
CREATE TABLE UserDetails(
    Id BIGINT NOT NULL AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL,
    UserType VARCHAR(10) NOT NULL,
    PRIMARY KEY (Id),
    FOREIGN KEY (Username) REFERENCES Users(Username),
    CONSTRAINT UserDetailsConstraint UNIQUE(Id, Username)
);

-- Create scores table
CREATE TABLE Scores(
    Id BIGINT NOT NULL AUTO_INCREMENT,
    UserDetailId BIGINT NOT NULL,
    Score BIGINT NOT NULL,
    PRIMARY KEY (Id),
    FOREIGN KEY (UserDetailId) REFERENCES UserDetails(Id)
);

-- Insert data into table Users
INSERT INTO Users(Username, Password)
VALUES ('johndoe', '12345'),
       ('janedoe', '678910'),
       ('alice123', '12345'),
       ('johnsmith', '12345'),
       ('sarahjane', '12345'),
       ('johnnylim', '678910'),
       ('tin', 'test'),
       ('cherwah', 'test'),
       ('michael', 'test');

-- Insert data into UserDetails
INSERT INTO UserDetails(Username, UserType)
VALUES ('tin', 'Paid'),
       ('cherwah', 'Free'),
       ('michael', 'Free'),
       ('johnsmith', 'Paid'),
       ('sarahjane', 'Free'),
       ('johnnylim', 'Paid'),
       ('johndoe', 'Free'),
       ('janedoe', 'Free'),
       ('alice123', 'Free');

-- Insert data into Scores
INSERT INTO Scores(UserDetailId, Score)
VALUES (1, 100),
       (1, 150),
       (2, 125),
       (3, 10),
       (4, 200),
       (5, 1000),
       (6, 50000);