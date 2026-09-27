# Backend Performance Testing - Gatling Task | Mentoring

## What should be done

Goal
Develop and submit a Gatling script that simulates user interactions with the Performance Testing Essentials application, following the defined load model.

## Prerequisites
Deploy the Performance Testing Essentials training application locally using Docker by following the instructions in the repository (https://git.epam.com/epm-perf/performance-testing-essentials).

## Description
Create a Gatling script based on the load model from the Performance Testing Introduction module:

## Open the application.
Navigate to the "Tables" tab.
Open a table product card (click on a table).
Add the table to the cart (click "Add to Cart" button).
50% of users:
Click the "Chairs" tab.
Open a random chair product card.
Add the chair to the cart.
30% of all users (those who added a chair and those who didn't):
Open the cart.
Click "Place an order."
Fill in all required fields and click "Place an order" to complete the process.
After creating the script:

Run the test with users = 100.

## Expected Output
Archived Gatling project with the created script and one Gatling report in /target/gatling/${your_project_name} (final report only).

## Requirements
Project generated using Maven + Java.
The scenario includes 100% backend requests.
All reusable methods/constants/variables are part of the BaseHelpers object.
All requests for a page are grouped and named properly.
Implement one injection with 100 users.
Configure pauses between pages.
Use the following blocks appropriately: flushCache, flushCookies, exitBlockOnFail.
Simulation class contains the commented-out command used to run the test.
Only the final Gatling report is included in /target/gatling/${your_project_name}.
Use System.getProperty to define execution parameters.

## Advanced Requirements (Optional)
Eliminate code duplication.
Implement one inject with 100 users for both open and closed models.
The scenario includes 100% backend requests; add a comment in code with your opinion on whether preflight requests are required.
