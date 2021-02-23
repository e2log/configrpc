# ConfigRPC
A Distributed Configuration Service Based on gRPC. 

Each comfiguration is composed of a key/value pair, where the key can be any string and value is a *JSON* document so that nested structured configurations can be stored.

This is the **ConfigRPC** *server* component, it provides a [gRpc](https://grpc.io/docs/guides/) API for retrieving partial or total configuration objects from the main value using **JSON Path** expressions.

## JSON Path gRPC API.

```protobuf
service ConfigrpcService {
    rpc findString( FindStringRequest ) returns ( FindStringResponse ) {}
    rpc findObject( FindObjectRequest ) returns ( FindObjectResponse ) {}
}

message FindStringRequest{
    string key = 1;
    google.protobuf.StringValue jsonPath = 2;
}

message FindStringResponse{
    KeyString kv = 1;
}

message KeyString {
    string key = 1;
    string value = 2;
}

```

## Examples

### Given the following configuration data pair

**key**: `/service/data`

**value**:
```json
{
  "environments" : [
    {
      "environment": "TEST", 
      "database": {"host": "10.0.0.79", "user": "sa", "password_key": "test-db-pwd"}, 
      "admin": "sam@svc.com"
     },
    {
      "environment": "STAGING",
      "database": {"host": "10.1.0.237", "user": "sa", "password_key": "staging-db-pwd"},
      "admin": "ana@svc.com"
    }
  ]
}
```
#### Query 1: All database properties of 1st environment

    key : `/service/data` 

    jsonPath : `$.environments[0].database`
    
  Returns:
 `{host=10.0.0.79, user=sa, password_key=test-db-pwd}`

#### Query 2: The host property of the database of 1st environment 

    key : `/service/data` 
   
    jsonPath : `$.environments[0].database.host`
    
  Returns:
 `10.0.0.79`
 
 #### Query 3: The property admin of all the database configurations

    key : `/service/data` 
   
    jsonPath : `$..admin`
    
  Returns:
 `["sam@svc.com","ana@svc.com"]`

All admins JsonPath query:   
![GRPC-UI All Admins Query](all-admins-grpc-ui-query.png) 
  
  
All admins result:  
![GRPC-UI All Admins Result](all-admins-grpc-ui-result.png) 



## How do I get set up? ###
 
- java 11 or newer 

## Dependencies
This server component depends on the interface submodule [configrpc-api.jar](https://github.com/e2log/configrpc-api)  

![Architecture](grpc-interface.svg)

## Install the dependency `configrpc-api.jar` locally, see [project](https://github.com/e2log/configrpc-api) for install details 


## Run
`./mvnw spring-boot:run`

Note: this project uses Google protocol buffers

## Run gRPC UI using docker

- todo

