package com.example.spring.utils;

public class SqlServerInitializer implements DBInitializer {

    @Override
    public String sqlDropBook() {
        return "IF OBJECT_ID('dbo.book', 'U') IS NOT NULL DROP TABLE dbo.book;";
    }

    @Override
    public String sqlDropCard() {
        return "IF OBJECT_ID('dbo.card', 'U') IS NOT NULL DROP TABLE dbo.card;";
    }

    @Override
    public String sqlDropBorrow() {
        return "IF OBJECT_ID('dbo.borrow', 'U') IS NOT NULL DROP TABLE dbo.borrow;";
    }

    @Override
    public String sqlCreateBook() {
        return "create table book (\n" +
                "    book_id int not null identity,\n" +
                "    category varchar(63) not null,\n" +
                "    title varchar(63) not null,\n" +
                "    press varchar(63) not null,\n" +
                "    publish_year int not null,\n" +
                "    author varchar(63) not null,\n" +
                "    price decimal(7, 2) not null default 0.00,\n" +
                "    stock int not null default 0,\n" +
                "    primary key (book_id),\n" +
                "    unique (category, press, author, title, publish_year)\n" +
                ");";
    }

    @Override
    public String sqlCreateCard() {
        return "create table card (\n" +
                "    card_id int not null identity,\n" +
                "    name varchar(63) not null,\n" +
                "    department varchar(63) not null,\n" +
                "    type char(1) not null,\n" +
                "    primary key (card_id),\n" +
                "    unique (department, type, name),\n" +
                "    check ( type in ('T', 'S') )\n" +
                ");";
    }

    @Override
    public String sqlCreateBorrow() {
        return "create table borrow (\n" +
                "    card_id int not null,\n" +
                "    book_id int not null,\n" +
                "    borrow_time bigint not null,\n" +
                "    return_time bigint not null default 0,\n" +
                "    primary key (card_id, book_id, borrow_time),\n" +
                "    foreign key (card_id) references card(card_id) on delete cascade on update cascade,\n" +
                "    foreign key (book_id) references book(book_id) on delete cascade on update cascade\n" +
                ");";
    }
}
