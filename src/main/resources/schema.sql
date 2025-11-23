-- ExplorandoElCosmos Database Schema
-- Version 1.2: Removed default admin user creation. Admin is now created on first registration.

-- Grupo 1: Gestión de Usuarios y Roles
CREATE TABLE IF NOT EXISTS Roles (
    role_id INTEGER PRIMARY KEY AUTOINCREMENT,
    role_name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    hashed_password TEXT NOT NULL,
    email TEXT UNIQUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    role_id INTEGER NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);

CREATE TABLE IF NOT EXISTS User_Profiles (
    profile_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL UNIQUE,
    display_name TEXT,
    avatar_url TEXT,
    bio TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- ... (el resto de las tablas CREATE TABLE permanecen igual) ...

CREATE TABLE IF NOT EXISTS Api_Sources (
    source_id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Publications (
    publication_id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_api_id INTEGER NOT NULL,
    original_id_from_api TEXT,
    content_type TEXT NOT NULL,
    title TEXT NOT NULL,
    main_image_url TEXT,
    fetched_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (source_api_id) REFERENCES Api_Sources(source_id)
);

CREATE TABLE IF NOT EXISTS Publication_Details (
    detail_id INTEGER PRIMARY KEY AUTOINCREMENT,
    publication_id INTEGER NOT NULL,
    detail_key TEXT NOT NULL,
    detail_value TEXT,
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Tags (
    tag_id INTEGER PRIMARY KEY AUTOINCREMENT,
    tag_name TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Publication_Tags (
    publication_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    PRIMARY KEY (publication_id, tag_id),
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tags(tag_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS User_Favorites (
    user_id INTEGER NOT NULL,
    publication_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, publication_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Offline_Content (
    offline_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    publication_id INTEGER NOT NULL,
    download_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    local_path TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (publication_id) REFERENCES Publications(publication_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS User_Collections (
    collection_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    collection_name TEXT NOT NULL,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Collection_Items (
    collection_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    PRIMARY KEY (collection_id, item_id),
    FOREIGN KEY (collection_id) REFERENCES User_Collections(collection_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES Publications(publication_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS App_Config (
    config_key TEXT PRIMARY KEY,
    config_value TEXT,
    is_editable_by_admin BOOLEAN DEFAULT 1
);

CREATE TABLE IF NOT EXISTS User_Settings (
    setting_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    setting_key TEXT NOT NULL,
    setting_value TEXT,
    UNIQUE (user_id, setting_key),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Search_Log (
    search_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    search_term TEXT NOT NULL,
    search_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS SpaceX_Launches (
    launch_id TEXT PRIMARY KEY,
    mission_name TEXT NOT NULL,
    launch_date_utc DATETIME NOT NULL,
    details TEXT,
    is_latest BOOLEAN DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Launch_Reports (
    report_id INTEGER PRIMARY KEY AUTOINCREMENT,
    launch_id TEXT NOT NULL,
    report_type TEXT NOT NULL,
    file_path_or_url TEXT NOT NULL,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (launch_id) REFERENCES SpaceX_Launches(launch_id) ON DELETE CASCADE
);


-- Insertar datos iniciales y por defecto
INSERT OR IGNORE INTO Roles (role_id, role_name) VALUES (1, 'admin'), (2, 'user'), (3, 'guest');
INSERT OR IGNORE INTO Api_Sources (source_id, source_name) VALUES (1, 'SpaceX'), (2, 'JWST'), (3, 'NASA'), (4, 'Planet'), (5, 'SolarSystem');
