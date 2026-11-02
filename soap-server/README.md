# SOAP Server (WSDL-First, Apache CXF)

The WSDL is the contract. Java code is generated from it (`wsdl2java`), and the server is built by implementing the generated interface.

## Layout
| Path | Role |
|---|---|
| `src/main/resources/wsdl/UserService.wsdl` | Service contract (WSDL) |
| `target/generated-sources/cxf/...` | Generated at build time (do not edit) |
| `src/main/java/com/example/soap/UserServiceImpl.java` | Business logic implementation |
| `src/main/java/com/example/soap/ServerMain.java` | Starts the embedded Jetty server |

## Run
```bash
mvn package
java -jar target/soap-server-1.0.0.jar
# WSDL: http://localhost:8080/ws/user?wsdl
# Change the port: java -Dsoap.address=http://0.0.0.0:9090/ws/user -jar target/soap-server-1.0.0.jar
```

## Test call
```bash
curl -H 'Content-Type: text/xml; charset=utf-8' \
     -H 'SOAPAction: http://example.com/user/GetUser' \
     --data '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:u="http://example.com/user"><soapenv:Body><u:GetUserRequest><u:id>1</u:id></u:GetUserRequest></soapenv:Body></soapenv:Envelope>' \
     http://localhost:8080/ws/user
```

## Using your own WSDL
1. Replace the file in `src/main/resources/wsdl/` (if you rename it, update `<wsdl>` and `<wsdlLocation>` in `pom.xml`)
2. Run `mvn generate-sources`, then check the `*PortType` interface that was generated
3. Change `UserServiceImpl` to implement that interface, and make `serviceName`, `portName`, `targetNamespace` and `endpointInterface` in `@WebService` match the WSDL
