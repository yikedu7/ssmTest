create table BOOK
(
	book_id bigint(20) not null auto_increment comment '图书ID',
	book_name varchar(100) not null comment '图书名称',
	book_number int(11) not null comment '图书数量',
	primary key(book_id)
)engine=innodb auto_increment=1000 default char set=utf8;

insert into BOOK (book_id, book_name, book_number)
value
	(1000, 'bookA', 10),
    (1001, 'bookB', 10),
    (1002, 'bookC', 10),
    (1003, 'bookD', 10);
    
create table APPOINTMENT
 (
	book_id bigint(20) not null comment '图书ID',
    student_id bigint(20) not null comment '学生ID',
    appointment_time timestamp not null default current_timestamp on update current_timestamp comment '预约时间',
    primary key(book_id, student_id),
    index idx_atime(appointment_time)
 )engine=innodb default char set=utf8