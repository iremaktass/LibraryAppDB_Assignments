-- US01 -1
select count(id) from users;   -- actual
select count(distinct id) from users;  -- expected

-- US01 -2
select * from users;

-- US02
SELECT COUNT(*) FROM book_borrow
where is_returned=0;

-- US03
select name from book_categories;

-- US04
select * from books
where name = 'Book Borrow 2';

select b.name, b_c.name from books b
inner join book_categories b_c ON b.book_category_id =b_c.id
where b.name = 'Book Borrow 2';


-- US05
-- step1
select book_id from book_borrow group by book_id order by count(*) desc limit 1;   -- most popular book

-- step2
select book_category_id from books
where id = (select book_id from book_borrow group by book_id order by count(*) desc limit 1); -- most popular books category id

-- step3
select name from book_categories
where id = (select book_category_id from books
            where id = (select book_id from book_borrow group by book_id order by count(*) desc limit 1));

-- US06
select name, author, isbn, year, book_category_id from books
where name = 'ExpectedName';


-- US07
select name from books
join book_borrow bb on books.id = bb.book_id
join users u on u.id = bb.user_id;

-- US08
select count(*) from users
where status = 'INACTIVE';

-- how many INACTIVE AND ACTIVE users we have

select count(*) from users
where status = 'INACTIVE'
union all
select count(*) from users
where status = 'ACTIVE';

select status, count(*) from users
group by status;