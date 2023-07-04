# Client

This page compiles technical information about the client.

## Purpose

The client serves as proxy to access to original ```time``` resource of the **Resource Server**, on behalf of the **Resource Owner**.  
The service does not actually perform anything purposeful on the time information, as OAuth clients, usually would. But the proxy access to a protected resource is sufficient to illustrate the protocol.

## Functioning

The client is implemented as a SpringMVC application, where static server page templates are defined using JSP. The templates reside in [```Client/src/main/webapp/WEB-INF/jsp/```](../Client/src/main/webapp/WEB-INF/jsp/):

 * ```GetTime```: A static form where vie user agent the **Resource Owner** provides their ID. Upon submit access to the original resource at the **Resource Server** is attempted, which initiates the OAuth2 dance.
 * ```DisplayTime```: In case of success, this webpage shows the value retreived from the protected resource. This reflects the proxy access to the secured information.
