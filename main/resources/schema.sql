CREATE TABLE app_user (
                      id BIGINT PRIMARY KEY,
                      name VARCHAR(255),
                      email VARCHAR(255) UNIQUE,
                      password VARCHAR(255)
);

CREATE TABLE app_item (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          size VARCHAR(50),
                          available BOOLEAN,
                          description VARCHAR(1000),
                          brand VARCHAR(255),
                          location VARCHAR(100),
                          gender VARCHAR(50),
                          category VARCHAR(50),
                          subcategory VARCHAR(50),
                          zustand VARCHAR(50)
);


CREATE TABLE app_rental (
                            id BIGINT PRIMARY KEY,
                            start_date DATE,
                            end_date DATE,
                            return_date DATE,
                            user_id BIGINT,
                            item_id BIGINT,
                            CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id),
                            CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES app_item(id)
);
