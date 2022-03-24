[![CircleCI](https://circleci.com/gh/jucron/spring5-mvc-rest/tree/master.svg?style=svg)](https://circleci.com/gh/jucron/spring5-mvc-rest/tree/master)
[![codecov](https://codecov.io/gh/jucron/spring5-mvc-rest/branch/master/graph/badge.svg?token=SCGLGCHWQ6)](https://codecov.io/gh/jucron/spring5-mvc-rest)

# Spring Framework 5 MVC Rest Application - JWT SECURITY

A REST API application built in my Spring Framework 5 course, simulating a Store with Vendors, Customers, etc. I have upgraded the Application with the implementation of a JWT Token security layer, and permissions are based on roles such as User, Admin and Manager. This API can easily work with a frontend application.

Check the Swagger Api Documentation: [Live Server Test](https://mvc-rest-security.herokuapp.com/)
Please NOTE: Swagger documentation will return 'Forbidden 403' response, since fetching Data is limited by security.

It is more properly to Test using the Postman with Authorization Header:
[Postman api-rest-security Workspace](https://go.postman.co/workspace/My-Workspace~b20ca6f3-aa32-48c5-8c9c-bb8dc06af4dd/collection/18570764-335e581f-b4ff-4667-a0e8-d4f2b15456c9)
