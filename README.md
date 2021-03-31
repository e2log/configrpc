# ConfigRPC
A Distributed Configuration Service Based on gRPC


This the Configrpc Service Server component based on [gRpc](https://grpc.io/docs/guides/), it depends on the interface `configrpc-api.jar`  

![Architecture](grpc-interface.svg)

## How do I get set up? ###
 
- java 11 or newer 

## Install the dependency `configrpc-api.jar` locally, see project for install details 


## Run
`./mvnw spring-boot:run`

Note: this project uses Google protocol buffers

## Run gRPC UI using docker

Find out more about grpc-ui [here](https://github.com/fullstorydev/grpcui)

- Run using docker-compose

```
docker-compose up -d grpcui
```

- Run using Docker directly

```
docker run -eGRPCUI_SERVER=<YOUR_HOSTNAME_OR_LOCAL_IP>:9090 -p8080:8080 wongnai/grpcui
```

- Navigate to [http://localhost:8080](http://localhost:8080)

## Use grpcurl

Install grpcurl using instructions provided [here](https://github.com/fullstorydev/grpcurl)

- List available services

```
grpcurl -plaintext localhost:9090 list
```

- List available methods for service

```
grpcurl -plaintext localhost:9090 list e2log.configrpc.ConfigrpcService
```

- Query service

```
grpcurl -plaintext -d '{ "key": "/service/data" }' localhost:9090 e2log.configrpc.ConfigrpcService.findString
```

## Sample JsonPath queries:

- Given the following config data
```json
{
  "environments" : [
    {"environment": "TEST", "database": {"host": "10.0.0.79", "user": "sa", "password_key": "test-db-pwd"}, "admin": "sam@svc.com"},
    {"environment": "STAGING", "database": {"host": "10.1.0.237", "user": "sa", "password_key": "staging-db-pwd"}, "admin": "ana@svc.com"}
  ]
}
```
- Query 1: All database properties of 1st environment

    key : `/service/data` 

    jsonPath : `$.environments[0].database`
    
  Returns:
 `{host=10.0.0.79, user=sa, password_key=test-db-pwd}`
  
  
- Query 2: The host property of the database of 1st environment 

    key : `/service/data` 
   
    jsonPath : `$.environments[0].database.host`
    
  Returns:
 `10.0.0.79`
  
  
- Query 3: The property admin of all the database configurations

    key : `/service/data` 
   
    jsonPath : `$..admin`
    
- Returns:
 `["sam@svc.com","ana@svc.com"]`

All admins JsonPath query:   
![GRPC-UI All Admins Query](all-admins-grpc-ui-query.png) 
  
  
All admins result:  
![GRPC-UI All Admins Result](all-admins-grpc-ui-result.png) 

