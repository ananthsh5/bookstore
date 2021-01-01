INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (1, 'First', 'Customer', 'customer@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'CUSTOMER', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (2, 'First', 'Publisher', 'publisher@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'PUBLISHER', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (3, 'Bookstore', 'Admin', 'admin@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '000-000-0000', '1, Jalan Bangsar,Kuala Lumpur', 'ADMIN', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (4, 'Second', 'Customer', 'customer2@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'CUSTOMER', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (5, 'Third', 'Customer', 'customer3@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'CUSTOMER', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (6, 'Second', 'Publisher', 'publisher2@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'PUBLISHER', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (7, 'Third', 'Publisher', 'publisher3@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'PUBLISHER', now());

INSERT INTO customer (id, points, user_id) VALUES (1, 0, 1);

INSERT INTO customer (id, points, user_id) VALUES (2, 0, 4);

INSERT INTO customer (id, points, user_id) VALUES (3, 0, 5);

INSERT INTO publisher (id, name, description, user_id, accstatus, picture)
VALUES (1, 'Apress', 'Apress publisher', 2, 'APPROVED', '/img/publisher/apress.jpg');

INSERT INTO publisher (id, name, description, user_id, accstatus, picture)
VALUES (2, 'Manning', 'Manning publisher', 6, 'APPROVED', '/img/publisher/manning.jpg');

INSERT INTO publisher (id, name, description, user_id, accstatus, picture)
VALUES (3, 'PactPub', 'PactPub publisher', 7, 'PENDING', '/img/publisher/pactpub.png');

INSERT INTO subscriber (customer_id, publisher_id)
VALUES (1, 1);

INSERT INTO subscriber (customer_id, publisher_id)
VALUES (2, 1);

INSERT INTO subscriber (customer_id, publisher_id)
VALUES (3, 1);

INSERT INTO bookcategory (id, name)
VALUES (1, 'Java');

INSERT INTO bookcategory (id, name)
VALUES (2, 'Spring');

INSERT INTO bookcategory (id, name)
VALUES (3, 'Microservices');


INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (1, 'Pro Spring 5',
        'Master Spring basics and core topics, and share the authors’ insights and real–world experiences with remoting, Hibernate, and EJB.',
        '28.49', 100, '/img/book/ProSpring5.jpg', 2, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (2, 'Pro Spring Boot 2',
        'Quickly and productively develop complex Spring applications and microservices out of the box, with minimal concern over things like configurations.',
        '32.99', 100, '/img/book/ProSpringBoot2.jpg', 2, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (3, 'Pro Spring Security',
        'Build and deploy secure Spring Framework and Spring Boot-based enterprise Java applications with the Spring Security Framework.',
        '25.49', 100, '/img/book/ProSpringSecurity.jpg', 1, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (4, 'Spring 5 Recipes',
        'Solve all your Spring 5 problems using complete and real-world code examples. When you start a new project, you’ll be able to copy the code and configuration files from this book, and then modify them for your needs.',
        '40.33', 100, '/img/book/Spring5Recipes.jpg', 1, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (5, 'Spring In Action',
        'Spring in Action, 5th Edition</i> guides you through Spring''s core features, explained in Craig Walls'' famously clear style. You''ll roll up your sleeves and build a secure database-backed web app step by step.',
        '29.99', 100, '/img/book/SpringInAction.jpg', 2, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (6, 'Spring Microservices In Action',
        'SpringMicroservicesInAction',
        '59.99', 100, '/img/book/SpringMicroservicesInAction.jpg', 2, 1, now(), 'Admin');
INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (7, 'Microservices In Action',
        'Microservices in Action is a practical book about building and deploying microservice-based applications.',
        '36.88', 100, '/img/book/MicroservicesInAction.jpg', 3, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (8, 'Java 8 in Action',
        'Java 8 in Action is a clearly written guide to the new features of Java 8. The book covers lambdas, streams, and functional-style programming.',
        '38.49', 100, '/img/book/Java8InAction.jpg', 1, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (9, 'Spring Boot In Action',
        'A developer-focused guide to writing applications using Spring Boot. ',
        '34.00', 100, '/img/book/SpringBootInAction.jpg', 2, 1, now(), 'Admin');

INSERT INTO book (id, name, description, price, available, image, bookcategory_id, publisher_id, created_date, created_by)
VALUES (10, 'Modern Java in Action',
        'In Modern Java in Action, readers build on their existing Java language skills with the newest features and techniques. The release of Java 9 builds on what made Java 8 so exciting.',
        '47.70', 100, '/img/book/ModernJavaInAction.jpg', 3, 1, now(), 'Admin');


INSERT INTO cart_item (id, book_id, quantity, customer_id) VALUES (1, 1, 1, 1);

INSERT INTO cart_item (id, book_id, quantity, customer_id) VALUES (2, 2, 1, 1);

INSERT INTO cart_item (id, book_id, quantity, customer_id) VALUES (3, 3, 1, 1);

INSERT INTO orders (id, total_amount, customer_id, shipping_address, billing_address, payment_method, payment_info, status, ordered_date, using_points)
VALUES (1, '28.49', 1, '1, Jalan Bangsar,Kuala Lumpur ', '1, Jalan Bangsar,Kuala Lumpur,Malaysia', 'DEBIT CARD', 'Card number XXXX XXXX XXXX 5556 used for payment', 'NEW', now(), false);

INSERT INTO orders (id, total_amount, customer_id, shipping_address, billing_address, payment_method, payment_info, status, ordered_date, using_points)
VALUES (2, '32.99', 2, '1, Jalan Bangsar,Kuala Lumpur', '1, Jalan Bangsar,Kuala Lumpur,Malaysia', 'CREDIT CARD', 'Card number XXXX XXXX XXXX 1234 used for payment', 'COMPLETED', now(), false);

INSERT INTO orders (id, total_amount, customer_id, shipping_address, billing_address, payment_method, payment_info, status, ordered_date, using_points)
VALUES (3, '25.49', 3, '1, Jalan Bangsar,Kuala Lumpur', '1, Jalan Bangsar,Kuala Lumpur,Malaysia', 'CREDIT CARD', 'Card number XXXX XXXX XXXX 3456 used for payment', 'COMPLETED', now(), false);

INSERT INTO order_item (id, book_id, order_id, quantity, rating, order_status)
VALUES (1, 8, 1, 1, 4, 'ORDERED');

INSERT INTO order_item (id, book_id, order_id, quantity, rating, order_status)
VALUES (2, 9, 1, 1, 4, 'ORDERED');

INSERT INTO order_item (id, book_id, order_id, quantity, rating, order_status)
VALUES (3, 10, 1, 1, 4, 'ORDERED');

INSERT INTO order_item (id, book_id, order_id, quantity, rating, order_status)
VALUES (4, 8, 2, 1, 1, 'DELIVERED');

INSERT INTO order_item (id, book_id, order_id, quantity, rating, order_status)
VALUES (5, 8, 3, 1, 3, 'DELIVERED');

INSERT INTO message (id, content, received_date, read, user_id)
VALUES (1, 'New book added by Apress Publisher', now(), false, 1);

INSERT INTO message (id, content, received_date, read, user_id)
VALUES (2, 'New book added by Manning Publisher', now(), false, 1);

INSERT INTO promotion (id, title, description , image, url)
VALUES (1 ,'banner1', 'Tech unlocked 2021', '/img/promotions/promotion_1.png', 'pactpub.com');

INSERT INTO promotion (id, title, description , image, url)
VALUES (2, 'banner2', 'Free ebook', '/img/promotions/manning.jpg', 'pactpub.com');

INSERT INTO promotion (id, title, description , image, url)
VALUES (3, 'banner3', 'Free ebook 2', '/img/promotions/promotion_2.png', 'pactpub.com');