To run poller-backend run `./gradlew run`. You can also run the pre built fat jar in build/libs by running `java -jar poller-1.0.0-fat.jar`. It was built with java 11.

To run poller-client run `npm install && npm start`

Was unable to make this work with Docker so you would need to set up the database manually:


```
MySQLConnectOptions()
  .setPort(3306)
  .setHost("127.0.0.1")
  .setDatabase("poller")
  .setUser("dev")
  .setPassword("secret");
```
