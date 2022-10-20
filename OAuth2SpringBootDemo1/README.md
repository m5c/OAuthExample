OAuth2 with Spring Boot

A minimial OAuth2 code demo with Spring Boot.

## About

This is an **OAuth2 enabled version** of a minimal REST service, the *TimeService*. This service has just a single resource: ```/api/time```. Calling a ```GET``` on that resource returns the current date and time.
The interest of this repository is to illustrate the integration of standard OAuth2 resource protection, on the example of a minimal Spring Boot REST service.

 * This demo **does not** explain the OAuth2 Protocol. [See here](https://tools.ietf.org/html/rfc6749#section-4.1) for the official protocol specifications.
 * This demo **does not** explain OAuth2 specific spring configurations. [See here](https://howtodoinjava.com/spring-boot2/oauth2-auth-server/) for explanations on that.
 * This demo **does** provide a minimal, working codebase and explains how to run and use it.

### Origins

This repository is partially based on [an existing OAuth2 tutorial](http://websystique.com/spring-security/secure-spring-rest-api-using-oauth2/). However, I found that tutorial hard to follow in practice. I would have preferred a spring boot enabled version, configured for current spring versions, with more detailed hands-on instructions. 
I therefore forked the tutorial and made the following changes:

 * Removed the (in my opinion overly complicated) case study, replaced it with a minimal REST service. (The *TimeService*)
 * Updated the project build configuration (```pom.xml```) to spring boot, so that it now generates a self contained ```jar``` instead of a ```war``` file. (Easier to deploy and test now, no extra tomcat is required any more)
 * Updated spring security dependencies to most recent versions and updated the java code, where deprecated API features were called.
 * Enabled password hashing. (Passwords should never be stored in plain so I added a [state of the art hasher](https://docs.spring.io/spring-security/site/docs/4.2.14.RELEASE/apidocs/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoder.html) that automatically applies [salt](https://en.wikipedia.org/wiki/Salt_(cryptography)) and [pepper](https://en.wikipedia.org/wiki/Pepper_(cryptography)) on persisted passwords)
 * Added a dockerfile.This eases deployment, as it allows to run the service, even if the required JDK is not installed on the host.

### Disclaimer

OAuth2 communicates cryptographic tokens as URL parameters. However, [without HTTPS](https://stackoverflow.com/questions/499591/are-https-urls-encrypted), such parameters are not protected when communicated over the network.
On top of this demo application you should [therefore always enable https](https://www.thomasvitale.com/https-spring-boot-ssl-certificate/).

## Hands-on

This section provides instructions on how to power up the service and follow the OAuth2 protocol in practice.  
Bellow subsections line out the standard 3-staged service delivery pipeline, and oauth2 protocol phases:

 * [Build](#build): Compile the sources and obtain an executable jar
 * [Deploy](#deploy): Power up the service, so you can actually interact with its functionality
 * [Access](#access): Send HTTP requests to interact with the service's REST interface

   * [Test access on a protected resources without token (reject)](#unauthorized-access)
   * [Obtain a token, for later authorized access on the protected resource](#token-retrieval)
   * [Actually use that token to access the protected resource](#authorized-access)
   * [Renew the token, once it has expired](#token-renewal)

### Build

The java code is configured as a standard maven project. To obtain a self contained jar, run:  

```
mvn clean install
```

This will generate a new file: ```target/Timeservice-OAuth2.jar```

### Deploy

Since the ```pom.xml``` is configured to output a self contained jar, you do not need an extra application container (tomcat).  
To deploy and run the service, simply run:  

```bash
java -jar target/Timeservice-OAuth2.jar
```

### Access

Now that the service is running you can test the OAuth2 protocol procedure. You can either...

  * ...use the [Advanced Rest Client](docs/arc.md), a gui based tool, with all queries prepared for you.
  * ...stick to the below in-line ```curl``` statements.

#### Unauthorized access

In a first test, we verify that an unauthorized ```GET``` request on the ```/api/time``` resource gets rejected.

```bash
curl -X GET http://127.0.0.1:8084/api/time; echo
```

Will result in:

```json
{
    "error": "unauthorized",
    "error_description": "An Authentication object was not found in the SecurityContext"
}
```

#### Token retrieval

Use your credentials to get an *access token*:

```bash
curl -X POST \
  --user timeservice-client:password-for-timeservice-client \
  "http://127.0.0.1:8084/oauth/token?grant_type=password&username=max&password=abc123"
```

 > Note: The credentials originate the hard coded strings in [```AuthorizationServerConfiguration.java```](src/main/java/eu/kartoffelquadrat/timeservice/AuthorizationServerConfiguration.java) and [```OAuth2SecurityConfiguration.java```](src/main/java/eu/kartoffelquadrat/timeservice/OAuth2SecurityConfiguration.java)

You will then receive your tokens wrapped up in a JSON response object:

```json
{
    "access_token": "eN5j0EumnotjtSOtd36DD8UmfIA=",
    "expires_in": 299,
    "refresh_token": "qMQEuZRnMWIuA2MghtsN8JojPEA=",
    "scope": "read write trust",
    "token_type": "bearer"
}
```

#### Authorized access

In [```AuthorizationServerConfiguration.java```](src/main/java/eu/kartoffelquadrat/timeservice/AuthorizationServerConfiguration.java) we configured access tokens to expire after 5 minutes.

Before the expiry, you can use the above *access token*, to query the protected resource:

```bash
curl "http://127.0.0.1:8084/api/time" -H 'Authorization:Bearer d2rOyaSEYCgQkkQEqE4O9VyQN94='; echo
```

Alternaticely you can also pass the token as a query parameter:

```bash
curl "http://127.0.0.1:8084/api/time?access_token=d2rOyaSEYCgQkkQEqE4O9VyQN94="; echo
```

> Note: The trailing ```=``` is part of the access token.

> Note: In rare cases, an access token can obtain special characters, e.g. a ```+``` sign. If that is the case, you have to manually escape that character, when you paste it as a URL parameter. (```+``` becomes ```%2B```, etc.)

If the token has not yet expired, you get acccess to the time resource, and receive the current date and time as a string:

```bash
2020-06-06 17:12:31.909
```

If you use the *access token* after its expiry, the server will reply with a corresponding error message:

```json
{
    "error": "invalid_token",
    "error_description": "Access token expired: d2rOyaSEYCgQkkQEqE4O9VyQN94="
}
```

#### Token renewal

The *refresh token* is configured to expire 5 minutes later, so you can still use the *refresh token*, to obtain new *access tokens*:


```bash
curl -X POST \
  --user timeservice-client:password-for-timeservice-client \
"http://127.0.0.1:8084/oauth/token?grant_type=refresh_token&refresh_token=qMQEuZRnMWIuA2MghtsN8JojPEA="
```

Again, the reply is JSON object with a new pair of *access token* and *refresh token*. The expiry is reset to the initial value (300 seconds).

```json
{
    "access_token": "tYMBUMYMmUoLJsguZ3Ac2uabrQ0=",
    "expires_in": 300,
    "refresh_token": "b93D+4uODXfK6CnayTYlZhBdnng=",
    "scope": "read write trust",
    "token_type": "bearer"
}
```

#### Dynamic restrictions

If your access restrictions are user scoped, you need to identify a user based on an oauth2 token. Corresponding sample code is placed in an extra controller, the [```IdentifyUserController```](src/main/java/eu/kartoffelquadrat/timeservice/IdentifyUserController.java).

To identify a user, use the ```/api/username``` endpoint and pass the token as either a header or query parameter.  
Sample code for a *Header based* resolving.

```bash
curl "http://127.0.0.1:8084/api/username" -H 'Authorization:Bearer tYMBUMYMmUoLJsguZ3Ac2uabrQ0='; echo
```

The result of above query is simply the username associated to the joined token:

```bash
max
```

## Docker

This repo also contains a [Dockerfile](Dockerfile) for convenient docker image and container support. This comes in handy when you want to dpeloy your servive on a machine that does not bring the required prerequisites, e.g. a JDK.  
The following commands use the self contained jar to build a deployable docker container, with integrated JDK.


```bash
# compile sources as usual, build a self contained JAR with maven
mvn clean install

# create a docker image, containing the JAR (uses instructions of the Dockerfile)
docker build --rm -t time-service-oauth:latest .

# create and deploy docker container, using image
docker rm time-service-oauth &> /dev/null
docker run -it -p 8084:8084 --name time-service-oauth time-service-oauth
```

## Corrections

Don't hesitate to contact me for corrections / pull requests:

 * Github: [Kartoffelquadrat](https://github.com/kartoffelquadrat)
 * Webpage: https://www.cs.mcgill.ca/~mschie3
