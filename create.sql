 create table file(
 id varchar(100),
 fsize varchar(40),
 ftype varchar(10),
 fname varchar(32),
 createtime timestamp,
 filepath varchar(32),
 envo varchar(1000),
 PRIMARY KEY(id)
 )
     SELECT * FROM ( SELECT ROW_NUMBER() OVER() AS R,  fsize, ftype, fname,createtime FROM log_table where 1=1 order by logtime desc ) AS tmp WHERE R > 0 and R <= 10