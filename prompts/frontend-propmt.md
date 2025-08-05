# JetBrains AI Prompt

Inspect the requirements document. Analyze for accuracy and completeness. Make recommendation for 
how we can improve this document. Implement the improvement in a revised version.

---------------------------------------------------

For a React project using shadcn, inspect the technologies used. Are there any missing dependencies?

-----------------------------------------------------

What information is needed for the vite configuration.

----------------------------------------------------

Can the guide outline be improved?

# Junie Prompt

Inspect the file `/prompts/requirements-prompt-draft.md`. Use this file to create a developer guide to implement 
a React front end for this project. Update and improve this developer guide using the context of this project. 
The guide should be organized into clear actionable steps.

Write the improved guide to a file `/prompts/requirements.md`

-------------------------------------------------------

Inspect the requirements.md file. Generate prompt to create an implementation plan from this file

-------------------------------------------------------
**Improved Prompt from Gemini - Using AI to generate Ai prompt**

The implementation plan must focus on the remaining work: building the UI components and pages to satisfy all the 
functional requirements. The plan should be a logical, actionable checklist that a developer can follow to complete 
the application.
**Instructions:**
1. **Use as the single source of truth.`/prompts/requirements.md`** The plan must adhere strictly to the project 
 structure, technology stack (`React`, `TypeScript`, `Shadcn UI`, `Tailwind CSS`), and coding patterns 
 (API service usage, routing) defined in the document.
2. **Break down the work into logical phases.** Structure the plan into three main phases based on the functional requirements:
    - Phase 1: Core Layout and Beer Inventory
    - Phase 2: Beer Order Management (CRUD operations)
    - Phase 3: Beer Order Shipment Management

3. **Define actionable tasks for each phase.** Each task should be a specific, concrete step. For each task, specify:
    - The file(s) to be created or modified (e.g., ). `src/pages/beer/BeerListPage.tsx`
    - The primary goal of the task (e.g., "Fetch and display a list of beers").
    - Key `Shadcn UI` components to use (e.g., `Table` for lists, `Card` for details, `Button`, `Input`, for forms). `Dialog`
    - How to use the predefined services from to interact with the backend. `src/services/api.ts`

4. **Ensure all functional requirements are covered.** The plan must map directly to the features outlined in section 2 
of the file. `requirements.md`
5. **Present the plan in a clear Markdown format.** Use headings for phases and a checklist for tasks to make the plan 
6. easy to read and track.

The final output should be a complete implementation plan that guides a developer from the established baseline to a 
fully functional application."

Write the plan to the `/prompts/plan.md` file.

--------------------------------------------------------------------------------

Create a detailed enumerated task list according to the suggested enhancements plan in the `/prompts/plan.md`.
Task items should have a placeholder [ ] for marking as done [x] upon task completion.
Write the task list to a new file `/prompts/tasks.md`.

--------------------------------------------------------------------------------

Complete the task list `/prompts/tasks.md`. Inspect the requirements.md, plan.md and tasks.md (task list).
Implement the tasks in the task list. Focus on completing the task in order. Mark completed tasks [x] as done. as each
step is completed, it is crucial to update the task list mark and the task as done [x].

----------------------------------------------------------------------------------
**RUN THIS ASK MODE**
Inspect the files `/prompts/requirements.md` and `/prompts/plan.md`. These changes have been implemented in the project.
Review the project as needed. Plan additional sections in the `guideline.md` file for the changes which have been 
implemented in the project. Include instructions for the project structure, and for building and testing the frontend project.
Also identify any best practice for the front end code.

-----------------------------------------------------------------------------------
The frontend project has errors. Fix errors, and verifying they are passing.
