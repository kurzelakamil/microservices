CREATE TABLE IF NOT EXISTS MESSAGE (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  response varchar(20)
);
insert into
  MESSAGE (response)
values
  ('helloWorld');