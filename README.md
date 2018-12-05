#Microservices

This project represents two simple integrated microservices. There are some features as below:

- First service can send request to Second by two ways: RESTTemplate and Kafka
- In REST Template way First service looks for Second by spring.application.name, not address
- Both Services are registered in Netflix Eureka Service Registry
- Both Services load their simple configuration(for Netflix Eureka connection) from Server Config(which stores properties files in git repo, not local)
- Second service creates H2 database by Flyway, and takes data(simple 'helloWorld') from db, then sends it to First Service as a response for request
- In Kafka there is used ReplyingKafkaTemplate - First Service(with Kafka Producer) sends request to Second(where is Kafka Consumer) and Second Service synchronously reply with data taken from db

  
