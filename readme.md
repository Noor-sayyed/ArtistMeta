Make sure you have these three things installed:
Java 17 — specifically Eclipse Temurin 17 (or any Java 17 build). You can check what you have by opening a terminal/PowerShell and typing:

java -version

It should say something like `17.0.16`.
PostgreSQL — a database server running on your machine, with a tool like pgAdmin to manage it.
An IDE — IntelliJ IDEA is recommended, but any Java IDE works.

**Step 1: Create the database**
Open pgAdmin and make sure you're connected to your local Postgres server.
Right-click Databases → Create → Database.
Name it `meta` (this matches the name the app expects — see Step 2).
Click Save.

To double check it worked, right-click the new `meta` database → Query Tool, and run:
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

The database itself is empty right now — no tables exist yet. The app creates them automatically on startup, using a tool called Flyway.
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
To double-check the tables were created: go back to pgAdmin, expand `meta` → Schemas → public → Tables. You should see the tables listed there.
 
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
 
