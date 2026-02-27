create database fooddelivery;
drop database fooddelivery;
use fooddelivery;

create table customer(cid int auto_increment primary key,cname varchar(10),cphone varchar(10)); 

create table fooditem(fid int primary key,fname varchar(20),cid int, foreign key (cid) references customer(cid));

create table restaurant(rid int primary key,rname varchar(10),fid int,foreign key (fid) references fooditem(fid));

create table orders(oid int primary key,fid int,cid int,foreign key(fid) references fooditem(fid),foreign key(cid) references customer(cid));


create table delivery(did int auto_increment primary key,dname varchar(10),oid int,foreign key (oid) references orders(oid));

create table orderitems(itemsid int primary key,itemsname varchar(10),rid int,foreign key(rid) references restaurant(rid));