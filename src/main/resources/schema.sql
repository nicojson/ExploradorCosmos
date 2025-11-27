select local_path from Offline_Content;

CREATE TABLE IF NOT EXISTS Roles (
                                     role_id INT AUTO_INCREMENT PRIMARY KEY,
                                     role_name VARCHAR(50) NOT NULL UNIQUE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Users (
                                     user_id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(100) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    dob DATE,
    country VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS User_Profiles (
                                             profile_id INT AUTO_INCREMENT PRIMARY KEY,
                                             user_id INT NOT NULL UNIQUE,
                                             display_name VARCHAR(100),
    avatar_url TEXT,
    bio TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
    ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS Api_Sources (
                                           source_id INT AUTO_INCREMENT PRIMARY KEY,
                                           source_name VARCHAR(100) NOT NULL UNIQUE,
    api_key VARCHAR(255),
    base_url VARCHAR(255) NOT NULL
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Publications (
    publication_id INT AUTO_INCREMENT PRIMARY KEY,
    source_api_id INT,
    original_id_from_api VARCHAR(255),
    content_type VARCHAR(50),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    main_image_url TEXT,
    published_date DATETIME,
    fetched_at DATETIME,
    is_favorite TINYINT(1) DEFAULT 0,
    local_path TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (source_api_id) REFERENCES Api_Sources(source_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Tags (
                                    tag_id INT AUTO_INCREMENT PRIMARY KEY,
                                    tag_name VARCHAR(100) NOT NULL UNIQUE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Publication_Tags (
                                                publication_id INT NOT NULL,
                                                tag_id INT NOT NULL,
                                                PRIMARY KEY (publication_id, tag_id),
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags(tag_id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS User_Favorites (
                                              user_id INT NOT NULL,
                                              publication_id INT NOT NULL,
                                              PRIMARY KEY (user_id, publication_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Offline_Content (
                                               offline_id INT AUTO_INCREMENT PRIMARY KEY,
                                               user_id INT NOT NULL,
                                               publication_id INT NOT NULL,
                                               download_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                               local_path TEXT NOT NULL,
                                               FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- Configuración y Logs
CREATE TABLE IF NOT EXISTS App_Config (
                                          config_key VARCHAR(100) PRIMARY KEY,
    config_value TEXT,
    is_editable_by_admin TINYINT(1) DEFAULT 1
    ) ENGINE=InnoDB;

INSERT IGNORE INTO Roles (role_id, role_name) VALUES (1, 'admin'), (2, 'user'), (3, 'guest');

INSERT IGNORE INTO Api_Sources (source_id, source_name, base_url) VALUES
(1, 'SpaceX', 'https://api.spacexdata.com/v4'),
(2, 'JWST', 'https://api.jwstapi.com'),
(3, 'NASA', 'https://images-api.nasa.gov'),
(6, 'Astronomy API', 'https://api.astronomyapi.com');

-- escrip para hacer al primer usuario administrador
UPDATE Users
SET role_id = 1
WHERE user_id = 1;