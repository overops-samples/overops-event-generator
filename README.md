# Demo

clean and package the jar.

```bash
./mvnw clean package -DskipTests
```

start the application and enjoy...

```
java -agentlib:TakipiAgent -jar overops-event-generator-0.0.1-SNAPSHOT.jar
```
or 

```
nohup java -agentlib:TakipiAgent -jar overops-event-generator-0.0.1-SNAPSHOT.jar &
```

See https://bintray.com/takipi/maven/takipi-sdk for details on downloading Takipi SDK


deploy app using PCF

```
mvn clean package
cf cups takipi -p '{ "secret_key": "<YOUR_KEY>" }'
cf push overops-event-generator -p target/overops-event-generator-0.0.1-SNAPSHOT.jar
cf bind-service overops-event-generator takipi
cf restage overops-event-generator

```