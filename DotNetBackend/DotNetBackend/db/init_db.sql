# Create database AndroidCA 
CREATE DATABASE AndroidCA;

# Use database adoWorkshop 
USE AndroidCA;

# Create users table
CREATE TABLE Users(
	Username varchar(50) NOT NULL,
    UserPassword varchar(50) NOT NULL,
	PRIMARY KEY (Username)
);

# Create user details table
CREATE TABLE UserDetails(
	Id bigint NOT NULL auto_increment,
    Username varchar(50) NOT NULL,
    UserType varchar(50) NOT NULL,
    PRIMARY KEY (Id),
    FOREIGN KEY (Username) REFERENCES Users(Username),
    CONSTRAINT UserDetailsConstraint UNIQUE(Id, Username)
);

# Create scores table
CREATE TABLE Scores(
    Id bigint NOT NULL auto_increment,
	UserDetail bigint NOT NULL,
	Score bigint NOT NULL,
	PRIMARY KEY (Id),
    FOREIGN KEY (UserDetail) REFERENCES UserDetails(Id)
);

# Insert data into table Users
INSERT INTO Users(Username, UserPassword)
VALUES ('johndoe', '12345'),
		('janedoe', '678910'),
        ('alice123', '12345'),
        ('johnsmith', '12345'),
        ('sarahjane', '12345'),
        ('johnnylim', '678910');


INSERT INTO UserDetails(Id, Username, UserType) 
VALUES (1, 'johndoe', 'Free'),
	   (2, 'janedoe', 'Paid'),
		(3, 'alice123', 'Free'),
        (4, 'johnsmith', 'Paid'),
        (5, 'sarahjane', 'Free'),
        (6, 'johnnylim', 'Paid');
        
INSERT INTO Scores(UserDetail, Score) 
VALUES (1, 100),
       (1, 150),
       (2, 125),
       (3, 10),
       (4, 200),
       (5, 1000),
       (6, 50000);
