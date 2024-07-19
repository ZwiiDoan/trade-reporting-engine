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
  and branch coverage of this repo is 100% which is satisfied by unit tests and integration tests.

### Run

From repo root directory, run the application:

```shell
./gradlew bootRun
```

Open http://localhost:8080/swagger-ui/index.html to try out the endpoints.

* The `/v1/trade-report/load-data` endpoint needs to be executed first to load data from a local folder (absolute path).
  Sample request
  is [src/test/resources/requests/load_data/valid_request.json](src/test/resources/requests/load_data/valid_request.json)
  which will load sample events from folder [src/test/resources/events](src/test/resources/events)
* After that, the other 2 endpoints can be used to get the static DEFAULT report or generate dynamic reports with sample
  requests in [src/test/resources/requests/generate_report](src/test/resources/requests/generate_report).

### Future Improvements & Considerations

* JSON Search Criteria syntax can be simplified/improved for better usability
* Error response schema can be adjusted to include more fields and show/hide details if required by enterprise standards
  or business requirements.
* More Observability tools can be integrated to provide distributed tracing, logging, metrics
  with [Spring Boot 3 Observability](https://spring.io/blog/2022/10/12/observability-with-spring-boot-3) such
  as [Prometheus](https://prometheus.io/), [Grafana](https://grafana.com/), [Loki](https://github.com/loki4j/loki-logback-appender),
  etc.
