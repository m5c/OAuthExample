# OAuth2 Sample Configuration

A minimal OAuth2 sample, to illustrate standard roles and communciation for resource access
authorization.

> Note: This repository is currently under development / refactoring and not in a stable state. I
> cannot make it private since it is a fork.

## About

The OAuth2 protocol is an industrial standard to allow access of REST resources through a third
party service, on behalf of their owner. The purpose is to strip the need for credential sharing, by
means of impersonation via cryptographic tokens.
The key entities in any OAuth2 interplay are three RESTful services:

* A **Resource Server**: It offers a resource, belonging to a *Resource Owner*. The latter is
  usually a biological or legal person, not an actually service.
* A **Client**: It needs to access a protected resource of the *Resource Server*, on behalf of the
  *Resource Owner*.
* An **Authorization Server**: It is the center part of the OAuth2 protocol and provides secure
  tokens that allow impersonation of the *Resource Owner* by the *Proxy Service*, without credential
  sharing.

> Note that depending on the protocol variant, the **Authorization Server** may be replaced by an
> existing entitiy e.g. an Authorization server provider by Google, Spotify, etc...*

This repository hosts sample implementations of the three services, that allow a play-though of the
protocol. There is no implementation of the **Rersource Owner**, which is the human player in the
OAuth2 dance.

## Services

This repository reflects the [standard protocol entities](#about) as follows:

* A [modified version](ResourceServer) of the TimeService as **Resource Server**: Sample users (
  resource owners) have their proper time resource, which upon access tells the time in their
  customized format.
* A newly coded [Time Proxy Service](Client) as OAuth2 **Client**, which attempts to access the
  TimeService on behalf of the user and therefore needs to be granted access. Permission to access the protected time resource is obtained, using the OAuth2
  Protocol. This component likewise contains a minimal Web Frontent, to allow for interaction with
  the **Resource Owner** by means of a interpreting user-agent.
* An (almost) off-the-shelf [Authorization Service](AuthorizationServer) which keeps track of users,
  services, granted access and tokens, to allow for a secured resource access following the OAuth2
  dance.

## Communication Layout

The effective OAuth2 communication layout varries, depending on how roles are sperated or fused:

* In essence, these variants differ in *how the granted authorization* is transferred back from
  **Authorization Server** to **Client**.
* The above process of transferring the authorization is called [**Authorization Grant
  **](https://datatracker.ietf.org/doc/html/rfc6749#section-1.3) in protocol jargon.
* There are different **Authorization Grant** types, but here we only deal with the standard case:
    * Parties place minimal trust in one another.
    * Parties are fully separated executables (services).
* This standard type is called [**Authorization Code Grant**](https://datatracker.ietf.org/doc/html/rfc6749#section-4.1), in protocol jargon.

Below schema illustrates the communication flow for the standard **Authorization Code** type:

```
     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |     (is a redirect to AS)     |     Owner     |
     |        |<------------------------------|               |
     |        | (B.3) RO forwards Auth. Code) +---------------+
     |        |                                  ^   ^
     |        |            (B.1) Page Forward    |   |  (B.2) RO grants auth.
     |        |             & grant form reply   v   v   & AS returns Auth. Code
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+
```

> Note: The above layout is based on the official protocol specifiation. Additional arrows were
> added to better illustrate the *Request Reply* nature of the underlying HTTP protocol.
> Steps ```B.1-B.3``` reflect the **Authorization Code** communcation layout.

## Further links

* Initial version of the [Legacy Time Service](https://github.com/m5c/TimeServiceLegacy)
* Initial version of the [Secured RESTful Time Service](https://github.com/m5c/OAuth2SpringBootDemo)
* Initial version of
  the [Authorized RESTful Time Service](https://github.com/m5c/TimeServiceOAuth2Modular), where
  proxy and resource server are still fused.
* OAuth2 [Protocol Specification](https://datatracker.ietf.org/doc/html/rfc6749#section-4.1)
* Spring
  Boot [OAuth2 Sample Tutorial Page](https://howtodoinjava.com/spring-boot2/oauth2-auth-server/)

## Authors

* Implementation Draft, based
  on [monolithic OAuth2 Time Service](https://github.com/m5c/OAuth2SpringBootDemo): [Khabiir](https://github.com/khabiirk) ([Original Repo](https://github.com/khabiirk/OAuthExample)), [Maximilian](https://www.cs.mcgill.ca/~mschie3/) ([monolithic OAuth2 Time Service](https://github.com/m5c/OAuth2SpringBootDemo))
* Documentation and Refactoring: [Maximilian](https://www.cs.mcgill.ca/~mschie3/), (This Repo)
