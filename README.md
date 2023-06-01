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

### Started and what?
#### Loadtest
1. Download [JMeter](https://jmeter.apache.org/download_jmeter.cgi)
2. In project folder `kafka-env` you will find file `Local Kafka Test.jmx`. It is template for __JMeter__
    - run __JMeter__
    - click `Open`
    - Choose `Local Kafka Test.jmx`
    - Press `Start`

#### Check database
1. Connect to database:
    - Host: `localhost`
    - Port: `5432`
    - User: `liquibase_user`
    - Password: `liquibase_password`
    - Database: `bank_db`
2. Check tables: `client`, `transaction`.
