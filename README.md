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

