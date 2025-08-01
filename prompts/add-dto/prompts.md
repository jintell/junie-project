Analyze the file `/prompts/add-dto/requirements-draft.md` and inspect the project. Improve and rewrite the 
draft requirements to a new file `/prompts/add-dto/requirements.md`.

--------------------------------------------------------------------------------

Analyze the file `/prompts/add-dto/requirements.md` and create a detailed plan for the improvements of this project.
Write the plan to a new file `/prompts/add-dto/plan.md`.

--------------------------------------------------------------------------------

Create a detailed enumerated task list according to the suggested enhancements plan in the `/prompts/add-dto/plan.md`. 
Task items should have a placeholder [ ] for marking as done [*] upon task completion.
Write the task list to a new file `/prompts/add-dto/tasks.md`.

--------------------------------------------------------------------------------

Complete the task list `/prompts/add-dto/tasks.md`. Inspect the requirements.md, plan.md and tasks.md (task list).
Implement the tasks in the task list. Focus on completing the task in order. Mark completed tasks [*] as done. as each 
step is completed, it is crucial to update the task list mark and the task as done [*].

--------------------------------------------------------------------------------

Inspect `.junie/guidelines.md`. Add a brief section about Flyway migrations with Spring Boot. Include information about 
the default directory and version naming

---------------------------------------------------------------------------------

Inspect the `model` package and create flyway migration scripts. Put them in the flyway migration default directory.
Verify the migration table `flyway_schema_history` was created and the `success` column value is 1  Create integration
tests and verify the migrations work correctly.