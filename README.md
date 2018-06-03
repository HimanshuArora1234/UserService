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

## Technical requirement document

This microservice implements the following use cases

 - Saves user's page view activities
 - Provides user's behavioural profile (stats) of last 7 days
 - Deletes user's all page view activities
 
### Technical stack

Technical stack to be used for this service consists of
 - **Scala** which brings the power of OOP & Functional programming together. Scala provides a way to reason about performing many operations in parallel– in an efficient and non-blocking way using `Futures` which also concludes in better utilisation of CPU cores.
 - **Play Framework** allows us to create RESTFul API and microservices at ease. It is a non-blocking, asynchronous and stateless framework and desgined in a way to increase the productivity and facilitate fast delivery. Hence a perfect choice for microservices architecture.

 
 ### Desgin & Architecture
 
Since its a microservice implementing a very well defined funtional boundry (aka a domain) so it is logical to use the Domain Driven Design appraoch with onion architecture illustrated as follows:

![Design](/design.png)
 
 

