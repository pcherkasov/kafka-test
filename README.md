# Kafka Test Application

## Local start

### Environment preparation
Preinstalled:  
`Docker`,  
`Docker-Compose`;  
_For __Windows__ users: install `Docker Decktop`_

Following ports should be free to use:  
`8087`  
`5432`

### Deployment
1. Clone repo to your local machine;
2. Ensure that the docker demon has been started. (For `Windows` just start `Docker Descktop`);
3. Open terminal in folder `kafka-env`;
4. Enter the following command:
```Shell
docker-compose up -d
```
5. This command will up all services and run load test.

### What next?
#### Check database
1. Connect to database:
   - Host: `localhost`
   - Port: `5432`
   - User: `liquibase_user`
   - Password: `liquibase_password`
   - Database: `bank_db`
2. Check tables: `client`, `transaction`.
