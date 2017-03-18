# Spring Cloud Mail Service #
Spring Cloud Mail Service is microservice for async sending e-mails with retrying in case of fail. It is useful as notification microservice and easy to integrate in your Spring Boot Application

## Spring Cloud Mail Service ##
HTTP-based API for sending emails and getting status

## Spring Cloud Mail Sample ##
Example project that show how to use and customize Spring Cloud Mail Service in your own application

## Spring Cloud Mail Client ##
Example project that show how to use it on the client side

## Quick Start ##
Edit mail settings:
```
$ cd spring-cloud-mail-service
$ nano /src/main/resources/application.yml
```
Start the server:
```
$ cd spring-cloud-mail-service
$ mvn spring-boot:run
```
By default application use port 8081, check that it is available. The server is a Spring Boot application so you can run it from your IDE instead if you prefer (the main class is MailServiceApplication).

Then you can send e-mail:
```
$ curl --data '{"to":"dmitrdv@mail.ru","subject":"hello", "msg": "hello world", "attempts": 1}' http://localhost:8081/api/v1/mail/send --header "Content-Type:application/json"
```
And get it status:
```
$ curl http://localhost:8081/api/v1/mail/status/{id} --header "Content-Type:application/json"
```

## Client Side Usage ##
It is easy to integrate Spring Cloud Mail Service with your Spring Boot Application.

 - Add dependency to your pom
```
<dependency>
	<groupId>com.ddobryak</groupId>
	<artifactId>spring-cloud-mail-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```
 - Add annotation to your Application class:
```
@SpringBootApplication
@EnableMailService
public class MailSampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(MailSampleApplication.class, args);
	}
}
```
 - Add config:
```
spring:
  cloud:
    mail:
      service:
        mail:
          host: smtp.yandex.ru
          port: 465
          username: username@yandex.ru
          password: password
          auth: true
          ssl: true
          protocol: smtp
          allow8bitmime: true
```
And now your application has HTTP API methods for sending e-mails and getting status.

## Advanced Usage ##
API methods:

 - Send email:
    - url: /api/v1/send 
    - method: POST
    - content-Type: application/json
    - parameters: {"to": "some email",
   "subject": "email subject", "msg": "email message", "attempts":
   number_of_attempts} 
    - return: string Id of e-mail
 - Get status:
    - url: /api/v1/status/{id}
    - method: GET
    - content-Type: application/json
    - return: string status "SENDING", "SUCCESS", "FAILED" or 404 if not found.

Settings:

 - E-mail server settings:
    - spring.cloud.mail.service.mail.host 
    - spring.cloud.mail.service.mail.port 
    - spring.cloud.mail.service.mail.username 
    - spring.cloud.mail.service.mail.password 
    - spring.cloud.mail.service.mail.auth 
    - spring.cloud.mail.service.mail.ssl 
    - spring.cloud.mail.service.mail.protocol 
    - spring.cloud.mail.service.mail.allow8bitmime 
 - Shutdown settings
    - spring.cloud.mail.service.wait.await - whether to shutdown application immediately or wait for all emails will send or wait.timeout expired
    - spring.cloud.mail.service.wait.timeout - timeout interval before shutting down application 
 - ThreadPoolTaskExecutor settings for sending e-mails in parallel
    - spring.cloud.mail.service.pool.min
    - spring.cloud.mail.service.pool.max
    - spring.cloud.mail.service.pool.capacity

By default HSQLDB is used to store information about e-mails. To change database add dependency to classpath and database settings.

For example to use Postgres: 

 - Add dependency:
```
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
</dependency>
```
 - Add settings to your configuration file:
```
spring.jpa.database=mail
spring.datasource.platform=postgres
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create
spring.database.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/mail
spring.datasource.username=postgres
spring.datasource.password=postgres
server.port=8080
```

## Compile and Test ##
To build the source you will need to install JDK 1.8, Maven >=3.3.3.
```
$ mvn clean install
```
