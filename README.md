# UserService
Microservice that receives data about page views and compute behavioural stats about users.



## Product requirements

### Endpoints

Create a API service with 2 API routes.

Call to send page view events
Send page view information to update the behavioral stats for a user.

`POST /v1/page`

Body of the POST:
```
{
  "user_id": "019mr8mf4r",
  "name": "Pricing Page",
  "timestamp": "2012-12-02T00:30:12.984Z"
}
```

Call to get a summary of the behavioral profile
Return the behavioral profile of a user.

`GET v1/user/:userid`

Returns
```
{
  "user_id": "019mr8mf4r",
  "number_pages_viewed_the_last_7_days": 21,
  "time_spent_on_site_last_7_days": 18,
  "number_of_days_active_last_7 _days": 3,
  "most_viewed_page_last_7_days": "Blog: better B2B customer experience"
}
```

Bonus: Call to delete the profile of a user (GDPR)
Delete all data about a user, ie. the raw events data if stored, and the aggregated profile data. This is to be in compliance with GDPR requirements.

`DELETE v1/user/:userid`

----------------------------------------------------------------------------------------------------------------------------------------

## Technical requirement document

This microservice implements the following use cases

 - Saves user's page view activities
 - Provides user's behavioural profile (stats) of last 7 days
 - Deletes user's all page view activities
 
### Technical stack

Technical stack to be used for this service consists of
 - **Scala** which brings the power of OOP & Functional programming together. Scala provides a way to reason about performing many operations in parallel– in an efficient and non-blocking way using `Futures` which also concludes in better utilisation of CPU cores.
 - **Play Framework** allows us to create RESTFul API and microservices at ease. It is a non-blocking, asynchronous and stateless framework and designed in a way to increase the productivity and facilitate fast delivery. Hence a perfect choice for microservices architecture.
 - **Akka actors** simplify the construction of concurrent and distributed applications on the JVM. This microservice uses actors for tracking the user login & logout events. Detailed description available later in the document.

 
 ### Design & Architecture
 
Since its a microservice implementing a very well defined functional boundry (aka a domain) so it is logical to use the Domain Driven Design(DDD) appraoch with onion architecture illustrated as follows:

![Design](/design.png)

 - **Domain Model layer**, where our entities and classes closely related to them reside.
 - **Application Services layer**, where application-specific logic i.e. our use cases reside.
 - **Interface layer**, where the rest endpoints, UI etc. reside. This layer is directly accessed by users and other applications.
 - **Infrastructure layer**, where the implementations of data repositories, database access, dependency wirings etc. reside.

Between the layers of the Onion, there is a strong dependency rule: outer layers can depend on lower layers, but no code in the lower layer can depend directly on any code in the outer layer.
 
 ### Data persistence
 
User's page view activity data will be stored into memory. Since we are using DDD approach so it's quite easy to have another implementation of page view data repository using a database and plug it in the infrastructure layer instead of memory implementation in future.

### Session management

To compute the stats of time spent by user in last 7 days we will need to track and persist user's login and logout events. When the service receives a page view event from a user then user is considered as logged-in and if no event is received during the following 2 mins then user is assumed to be logged-out. 

Akka actors come handy to implement this logic. As soon as the 1st page view for a user is recevied an actor will be created. This actor has a particularity that if it stays idle for 2 min i.e. no message processed for 2 mins then it considers user logged out, stores login and logout timestamps and kills itself. If a subsequent page view for the user under consideration is received then actor does nothing which means that actor's idle time is reset to zero. So that's how we track user session in this microservice.

