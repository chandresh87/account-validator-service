# Account Validator Service

# Requirement
You need to implement a rest service that accepts requests to validate an account and returns information if the requested account is valid. 

The service doesn't store any data but instead sends requests to other account data sources, aggregates this data and returns to the client.
Response is an array of objects, each object has two fields: source and isValid. Source is a string and is the name of a data source, isValid is a boolean value that data source returned.

# Solution

## Technology Stack:

     1. Java
	 2. Spring webflux
	 3. MapStruct
 	 4. Maven

