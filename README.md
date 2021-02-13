## REST API with Spring Boot
#### Banking System

This project features a banking system backend implementation. 
It's based in Java, using the Spring Boot lightweight infrastructure with Spring JPA + Hibernate and Spring Security.

The API developed here let end users to register new customers, open accounts and associate with them, so it's 
possible to make money transfers between these accounts, once proved their ownership using basic authentication and authorization.

Some additional features are:

 - Different types of accounts and owners
 - Fraud detection on suspected transactions
 - Automated interests and fees application 
 - Secured and admin based management
 
#### API Specification
 
##### Admin only
```
/bank/users
```
 - `GET` Read list of all users
``` 
/bank/users/admins
```
 - `GET` Read list of all admins

 - `POST` Create new admin user 
```
/bank/users/owners
```
 - `GET` Read list of all owners
 ```
/bank/users/owners/ah
``` 
 - `POST` Create new account holder
```
/bank/users/owners/tpu
``` 
 - `POST` Create new third party user
 ```
/bank/users/owners/{id}
``` 
 - `GET` Read owner
 ```
/bank/users/owners/{id}/accounts
``` 
 - `GET` Read accounts of an owner
 ```
/bank/accounts
``` 
 - `GET` Read list of all accounts
 ```
/bank/accounts/{id}
 ``` 
 - `PATCH` Update balance of the account
 ```
/bank/accounts/checking/{id}
 ``` 
 - `POST` Create new checking account with owner
 ```
/bank/accounts/checking/{id}/{id}
 ``` 
 - `POST` Create new checking account with two owners
 ```
/bank/accounts/savings/{id}
 ``` 
 - `POST` Create new savings account with owner
 ```
 /bank/accounts/savings/{id}/{id}
 ```
 - `POST` Create new savings account with two owners
 ```
 /bank/accounts/creditcard/{id}
 ```
 - `POST` Create new credit card account with owner
 ```
 /bank/accounts/creditcard/{id}/{id}
 ```
 - `POST` Create new credit card account with two owners
 ```
 /bank/transactions
 ```
 - `GET`  Read list of all transactions
 
##### Auth owner
 ``` 
 /accounts/{id}
 ``` 
 - `GET`  Read account

 - `POST` Create money transfer from account
 ```
 /accounts/{id}/transactions
 ```
 - `GET`  Read list of all transactions of account