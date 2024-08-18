create table category
(
    id            bigint auto_increment
        primary key,
    createdAt     datetime(6)  null,
    isDeleted     bit          not null,
    lastUpdatedAt datetime(6)  null,
    categoryName  varchar(255) null
);



create table orders
(
    id            bigint auto_increment
        primary key,
    createdAt     datetime(6)  null,
    isDeleted     bit          not null,
    lastUpdatedAt datetime(6)  null,
    email         varchar(255) not null,
    orderDate     date         null,
    orderStatus   varchar(255) null,
    totalAmount   double       null,
    payment_id    bigint       null
);

create table product
(
    id            bigint auto_increment
        primary key,
    createdAt     datetime(6)  null,
    isDeleted     bit          not null,
    lastUpdatedAt datetime(6)  null,
    description   varchar(255) null,
    imgURL        varchar(255) null,
    price         double       not null,
    quantity      int          null,
    title         varchar(255) null,
    category_id   bigint       null,
    constraint FKexqqeaksnmmku5py194ywp140
        foreign key (category_id) references category (id)
);

create table order_items
(
    id                  bigint auto_increment
        primary key,
    createdAt           datetime(6) null,
    isDeleted           bit         not null,
    lastUpdatedAt       datetime(6) null,
    discount            double      not null,
    orderedProductPrice double      not null,
    quantity            int         null,
    order_id            bigint      null,
    product_id          bigint      null,
    constraint FKbioxgbv59vetrxe0ejfubep1w
        foreign key (order_id) references orders (id),
    constraint FKlt5wcq2pwo68vnldjreqj2c2t
        foreign key (product_id) references product (id)
);

create table users
(
    id            bigint auto_increment
        primary key,
    createdAt     datetime(6)  null,
    isDeleted     bit          not null,
    lastUpdatedAt datetime(6)  null,
    Name          varchar(255) null,
    email         varchar(255) not null,
    password      varchar(255) null,
    roles         varchar(255) null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email)
);

create table carts
(
    id            bigint auto_increment
        primary key,
    createdAt     datetime(6) null,
    isDeleted     bit         not null,
    lastUpdatedAt datetime(6) null,
    totalPrice    double      null,
    user_id       bigint      null,
    constraint UK64t7ox312pqal3p7fg9o503c2
        unique (user_id),
    constraint FKb5o626f86h46m4s7ms6ginnop
        foreign key (user_id) references users (id)
);

create table cart_items
(
    cartItemId   bigint auto_increment
        primary key,
    discount     double not null,
    productPrice double not null,
    quantity     int    null,
    cart_id      bigint null,
    product_id   bigint null,
    constraint FKa4hyo227sp3nru9qo0fbfkuq4
        foreign key (product_id) references product (id),
    constraint FKpcttvuq4mxppo8sxggjtn5i2c
        foreign key (cart_id) references carts (id)
);


