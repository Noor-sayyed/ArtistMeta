Make sure you have these three things installed:
Java 17, (or any Java 17 build). You can check what you have by opening a terminal/PowerShell and typing:

java -version

It should say something like `17.0.16`.
PostgreSQL, a database server running on your machine, with a tool like pgAdmin to manage it.
An IDE IntelliJ IDEA is recommended, but any Java IDE works.

**Step 1: Create the database**
Open pgAdmin and make sure you're connected to your local Postgres server.
Rightclick Databases then Create then Database.
Name it `meta` (this matches the name the app expects see Step 2).
Click Save.

To double check it worked, rightclick the new `meta` database → Query Tool, and run:
sql
SELECT current_database();

If it returns `meta`, you're good.

**Step 2: Check the app's configuration file**
Open the file `src/main/resources/application.yml`. It should look like this:

yaml
spring:
  application:
    name: ArtistMeta

  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
      - org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration

  datasource:
    url: jdbc:postgresql://${PGHOST:localhost}:${PGPORT:5432}/${PGDATABASE:meta}
    username: ${PGUSER:postgres}
    password: ${PGPASSWORD:postgres}

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration

What this means in plain terms:
The app will try to connect to a Postgres database called `meta`, running on `localhost`, port `5432`.
The username and password default to `postgres` / `postgres` — if your local Postgres uses a different password, you need to change it here, or set it using environment variables instead (see the note below).
`ddl-auto: validate` means the app will not create or change any database tables on its own — it only checks that the tables already match what the code expects. The actual tables are created separately, by Flyway (see Step 3).
If your Postgres password is different from `postgres`:
Either edit the line directly:
yaml
password: your_actual_password_here

or set an environment variable named `PGPASSWORD` before running the app, which the `${PGPASSWORD:postgres}` syntax will pick up automatically instead of the default.

**Step 3: Run the app for the first time**

The database itself is empty right now, no tables exist yet. The app creates them automatically on startup, using a tool called Flyway.
Open the project in your IDE.
Find the main class: `ArtistMetaApplication.java`.
Click the green Run button next to it (or right-click → Run).
What should happen:
The app starts up.
Flyway runs automatically and creates all the necessary tables (`app_user`, `artist`, `artist_alias`, `track`, and so on).
You'll see log lines mentioning `Flyway` and `Successfully applied migration`.
Near the end, you'll see a line like:
 
  Started ArtistMetaApplication in X seconds
 
This means the app is up and running.
To double-check the tables were created: go back to pgAdmin, expand `meta` then Schemas then public then Tables. You should see the tables listed there.
 
**Step 4: Confirm the app is working**
Once it's running, the app listens on:
 
http://localhost:8080
 
You can test it's alive by opening this in a browser or using a tool like Postman:
 
http://localhost:8080/actuator/health
 
It should return something like:
 json
{"status":"UP"}
 

**Step 5: Using the actual features**
Here are the main things the app can do, and how to try them (using Postman, curl, or any API testing tool):
Add a new track
 
POST http://localhost:8080/tracks
Content-Type: application/json

{
  "stagename": "prince",
  "title": "Purple Rain",
  "genre": "Rock",
  "lengthSeconds": 245
}
 
Get all tracks for an artist
 
GET http://localhost:8080/artists/name/prince/tracks
 
Edit an artist's display name
 
POST http://localhost:8080/artists/name/update
Content-Type: application/json

{
  "stagename": "prince",
  "artistname": "The Artist Formerly Known As Prince"
}
 
See today's featured artist
 
GET http://localhost:8080/artists/of-the-day
 
Unit tests are also included in the project. You can run them from your IDE or using Maven/Gradle commands.

1: Artistservicetests.java
This test checks that each artist gets shown once per cycle, with no repeats or skips.
After all artists have been shown, it checks that the cycle starts again from the first artist.

2: Trackservicetests.java
This test checks that tracks are correctly returned when an artist exists.
Tests that an error is thrown when the artist doesn't exist.
Tests that an artist with no tracks returns an empty result.
Tests that pagination (Pageable) is passed correctly to the repository.
Overall, it checks that TrackService behaves correctly in normal and error situations.

-all 5 test cases pass, and you can see the results in your IDE's test runner or in the console output.



**Production ready notes:**

I am assuming the application will be deployed on AWS using ECS Fargate with Docker, 
RDS PostgreSQL for the database, API Gateway and an internal ALB for handling traffic, 
Secrets Manager for sensitive information, and GitHub Actions for CI CD. 
The application will run across multiple Availability Zones to provide better availability 
and allow deployments without taking the system offline.

- Users will access the application through Route 53, then API Gateway, then the internal ALB, then ECS Fargate, 
and finally RDS. Route 53 will handle the domain, API Gateway will manage and protect the API traffic, 
the ALB will send requests to the containers, ECS will run the Spring Boot application, and RDS will store 
the PostgreSQL database.
- The Spring Boot application will run on ECS Fargate. We will build the application into a Docker image, 
push the image to Amazon ECR, and then run at least three copies of the application across three Availability Zones. 
This means that if one task or one Availability Zone goes down, the application can still keep running.
- The application and database should stay private. The ECS containers and RDS database will be inside private subnets. 
API Gateway will communicate with the internal ALB through a VPC Link, so the ALB and ECS containers do not 
need to be directly accessible from the internet.
- PostgreSQL will run on Amazon RDS. We will enable Multi AZ so that AWS can automatically fail over 
if the main database has a problem. If the application receives a lot of read traffic, we can also add a read 
replica and use it for heavy read operations while keeping the main database for writes.
- Passwords and other sensitive information should be stored in AWS Secrets Manager. We should not put database passwords,
API keys, or other secrets directly inside application.yml or the GitHub repository. 
ECS can retrieve these secrets when the application starts.
- GitHub and CI CD will be used to deploy new versions of the application. When we push new code to GitHub, 
GitHub Actions can run the tests, build the Docker image, push it to ECR, and then deploy the new version to ECS.
- We will use health checks to make sure that new versions are working before removing the old ones. 
ECS will start the new containers and wait until they are healthy. Once the new containers are ready, 
the old containers can be removed. This allows us to deploy new versions without taking the application offline.
- We will also add monitoring and logging. Application logs can be sent to CloudWatch, where we can monitor 
things such as CPU usage, memory usage, request numbers, response times, errors, ECS tasks, and RDS health. 
CloudWatch alarms can notify us when something goes wrong.
- API Gateway will be the public entry point for the application. We can configure rate limiting, JWT authentication, 
and a custom domain such as api.yourdomain.com. This means users interact with API Gateway while the actual application
infrastructure remains private.
- The overall production setup will be Route 53 connected to API Gateway, API Gateway connected through VPC Link to 
the internal ALB, the ALB connected to multiple ECS Fargate tasks, and the ECS tasks connected to RDS PostgreSQL. 
This gives us a setup that is secure, scalable, highly available, and easier to maintain.