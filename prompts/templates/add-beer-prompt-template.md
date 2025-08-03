# Prompt Variable
Apply the following variables to placeholders in the prompt. Placeholders are denoted as `${variable}` syntax.

# Placeholders Definition
The following key value pairs are used to replace placeholders in the prompt. Format variable defines the variable name
and value defines the value to replace the placeholder with. Defined as `variable name` = `value` pairs in the following
list:

* controller_name: `BeerController`
* parameter_name: `beerName`

# Task Description
Your task is to implement query parameters to the list operation of existing controllers. Once all the Java code is
implemented, the OpenAPI documentation should be updated to reflect the changes. Use guidelines from `.junie/guidelines.md`

# Task Steps
* Inspect the existing controller `${controller_name}` and identify the list operation.
* The existing list operation would be modified to accept query parameter `${parameter_name}` using Reactive Spring
  RestController / Spring Data.
* The type of `${parameter_name}` should match the data type of the corresponding property in the DTO been returned.
* The query parameter should be optional and default to `null` if not provided.
* Update the existing list operation, do not create a new or new API path.
* The controller list operation should return `Pageable<T>` object from the `org.springframework.data.domain` package.
* The service layer should be updated to support `${parameter_name}` on the list operation. Update the list method to
  accept `${parameter_name}` parameter.
* The service layer should consider the query parameters as optional values, which may be default to empty or `null`.
* A new Reactive Spring Data Repository method should be created to support the query parameter in a `findAll` method
  accepting a `Pageable` and the `${parameter_name}` and return a paged resultset like `Page<T>`
* Excluding the parameter values for `page` and `size` which are required for pagination, logic should be added to allow
  for any combination of the other parameters to be `null` or empty. For example, if there are two values for query
  parameters, provide logic for `parameter1` or `parameter2` or if both are `null`. This is to allow for flexibility in
  combinations of query parameter.
* Update the unit tests for the controller, service implementation, and the repositories to cover the new paging
  functionality.
* Create the unit tests for the controller, service implementation, and the repositories to cover the new paging
  functionality.
* Verify the updated unit tests are passing.
* Update the OpenAPI documentation to reflect changes made to the controller.
* Update the parameters of the list operation in the OpenAPI documentation to include the new query parameter.
* Verify the OpenAPI documentation is valid.