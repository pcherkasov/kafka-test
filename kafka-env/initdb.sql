create USER liquibase_user with PASSWORD 'liquibase_password';

create DATABASE bank_db ENCODING 'UTF-8';
grant all privileges on DATABASE bank_db TO liquibase_user;
