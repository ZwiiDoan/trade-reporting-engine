### Prerequisites
* Install and run [Docker](https://docs.docker.com/engine/install/)

### Build
Give execution permission to `gradlew`:
```shell
chmod +x ./gradlew
```
Execute from repo's root directory
```shell
/.gradlew clean build
```
The build pipeline integrates different tools to verify code quality and security such as:

* [Checkstyle](https://checkstyle.sourceforge.io/) to ensure coding standard using Google Checkstyle
  at [config/checkstyle/checkstyle.xml](config/checkstyle/checkstyle.xml)
* [Spotbugs](https://spotbugs.github.io/) with plugin [FindSecBugs](https://find-sec-bugs.github.io/) that do static
  analysis to look for common bugs and security bugs in Java code
* [Jacoco](https://github.com/jacoco/jacoco) to ensure code and test coverage. The minimum instruction
  and branch coverage of this repo is 95% which is satisfied by unit tests and integration tests.

### Run
From repo root directory, run the Postgresql Docker container:
```shell
./gradlew composeUp
```
Then run the application:
```shell
./gradlew bootRun
```
Open http://localhost:8080/swagger-ui/index.html to try out the endpoints. 
* The `/v1/trade-report/load-data` endpoint needs to be executed first to load data from a local folder (absolute path). Sample events are in folder [src/test/resources/events](src/test/resources/events).
* There are many request samples under repo folder [src/test/resources/requests](src/test/resources/requests).

After stopping the application, stop the Postgresql Docker container:
```shell
./gradlew composeDown
```


### Future Improvements & Considerations
1. JSON Search Criteria syntax can be simplified/improved for better usability
2. Error response schema can be adjusted to include more fields and show/hide details if required by enterprise
   standards or business requirements.
2. More Observability tools can be integrated to provide distributed tracing, logging, metrics
   with [Spring Boot 3 Observability](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3) such as
   [Prometheus](https://prometheus.io/), [Grafana]https://grafana.com/, [Loki](https://github.com/loki4j/loki-logback-appender),
   etc.
