# Work Buddy

## Task Summary

This application aims to identify pairs of employees collaborating on shared projects, offering detailed insights into the exact duration they have worked together on each project. The input data, extracted from a CSV file, undergoes robust error handling, addressing potential issues such as date errors and parsing errors. The parsing process and the calculations are conducted with robust algorithms, ensuring accurate results and precise calculation of collaboration durations.

The application systematically analyzes collaborations between every pair of employees, exploring connections such as employee IDs 101 and 102, 101 and 103, 102 and 103, and so on. The primary focus remains on finding the longest collaborating pair of employees, emphasizing the uniqueness of the days they have worked together across these projects.

For each identified pair, the application provides detailed information about the collaborations, including projectID, duration, dateFrom and dateTo. This output format captures the exact details of the projects where the pair has collaborated. 

The application also allows to create, read, update and delete employees.

## Input Data
###  File Upload
- Upload CSV file project work entries.
  
    ```bash
    POST /api/upload
    ```
    - Parameters:
        - `file`: CSV file
        - `hasHeader` (optional): Boolean indicating whether the CSV file has a header (default: false)
        - `storeFile` (optional): Boolean indicating whether to store the uploaded file (default: false)


#### **Header Option:** Allows users to specify whether the CSV has a header or not.
#### **Upload Option:** Allows users to choose whether to upload the file to the database.

The CSV file is designed for versatility, handling diverse scenarios. The application validates the input data to ensure its integrity, checking for the following conditions:

- **Empty File:** Validates if the CSV file is empty.
- **Correct Format:** Ensures that the inputted file is a CSV file and is in the correct CSV format.
- **File Size:** Checks if the file size is within acceptable limits, with a maximum size set at 10 MB.
- **IO Exception:** Addresses potential input-output exceptions during the file processing.

This comprehensive validation process guarantees that the input data is reliable and suitable for further analysis.




## Output Format
The output presents a detailed list of employee ID pairs, showing the total days they worked together on each project. It considers the unique nature of collaboration days, recognizing that employees may work on multiple projects simultaneously with different colleagues.

###  **Collaboration Endpoints:**

---
#### Getting all the possible pair combinations (every employee with every employee) and their project collaborations with each different project, the duration of the project, from and to date, and most imporantly, the total collaborations days of each pair.
```bash
GET /api/collaboration/all
GET /api/collaboration/all?sortOrder=asc
GET /api/collaboration/all?sortOrder=desc
 ```
- Parameters:
  - `sortOrder`: (optional) Specifies the sorting order of collaboration results. Default is set to descending (desc). Possible values: asc (ascending) or desc (descending).
---
#### The same as the previous one, but also is a hashmap and every pair combinations is a value and its unique key is in the format of "employee1ID_employee2ID" for efficient data retrieval with average time efficiency of O(1).
```bash
GET /api/collaboration/all/map
 ```
---
#### This searches through that hashmap and returns the collaboration data between the two inputted employee IDs. The order of input does not matter. You can input 101/102 or 102/101, the result will be the same.
```bash
GET /api/collaboration/{empID1}/{empID2}
 ```
---
#### The main functionality of the application. This returns the pair of employees with the longest unique collaboration days and each of the projects they collaborated on with the id of the project, the duration in days that they worked together on that project, the from and to date. Only unique days are added to the total collaboration days. It checks if the projects are overlapping, if the employees are working on multiple projects at the same time, if the employees have started a project and then stopped doing it and then started doing it again. The algorithm is robust and accurate.
```bash
GET /api/collaboration/longest
 ```
---
## Demo of the main functionality
### Input Data Example (File input)

```csv
101, 1, 2021-01-01, NULL
102, 1, 2021-03-01, 2022-02-28
102, 1, 2023-02-02, NULL
101, 2, 2021-02-02, NULL
102, 2, 2023-02-02, NULL
103, 2, 2023-01-01, NULL
```

### Output Data Example for /collaborations/longest

```json
{
   "empID1": 101,
   "empID2": 102,
   "projectCollaborations": [
      {
         "projectID": 1,
         "duration": 364,
         "dateFrom": "2021-03-01",
         "dateTo": "2022-02-28"
      },
      {
         "projectID": 1,
         "duration": 324,
         "dateFrom": "2023-02-02",
         "dateTo": "2023-12-23"
      },
      {
         "projectID": 2,
         "duration": 324,
         "dateFrom": "2023-02-02",
         "dateTo": "2023-12-23"
      }
   ],
   "totalCollaborationDays": 688
}
```



## Algorithm Explanation: Step-by-Step Breakdown
### 1. Grouping Project Entries by Project ID

The algorithm initiates by organizing project work entries into groups based on their project IDs. This step is essential to isolate collaborations within the context of specific projects, streamlining subsequent calculations.

### 2. Iterating Through Project Groups

For each project group, the algorithm systematically examines each pair of entries. This sequential iteration ensures that collaboration calculations are focused on entries within the same project.

### 3. Checking Collaboration Criteria

During the pair-wise comparison, the algorithm verifies whether the two entries represent a collaboration. The criteria for collaboration include:
- Distinct employee IDs.
- Both entries have defined end dates (`DateTo`).
- Overlapping date ranges, indicating a period of joint work.

### 4. Calculating Collaboration Duration

Upon identifying a collaboration, the algorithm computes the duration by determining the overlap in start and end dates. This precision in duration calculation captures the exact timeframe during which employees collaborated on a given project.

### 5. Updating Collaboration Results Map

A map is employed to efficiently manage collaboration results. For each collaboration, a unique key is generated using employee IDs. If a collaboration result for a specific pair exists, it is updated with additional project details. Otherwise, a new collaboration result is created and added to the map.

### 6. Ensuring Unique Total Collaboration Days

To maintain accuracy, the algorithm guarantees the uniqueness of total collaboration days. This means that each day of collaboration is considered only once, even if employees work on multiple projects simultaneously. This approach prevents the double-counting of collaborative efforts.

### 7. Retrieving Collaboration Results

The algorithm offers various retrieval methods to access collaboration insights:
- Finding the pair with the longest collaboration duration.
- Sorting collaboration results based on total days (ascending or descending).
- Generating a map providing an overview of all collaboration results.
- Retrieving collaboration details for a specific pair of employees.


## **Employee Endpoints:**

---
#### Add an employee
```bash
POST /api/employees/add
```
- Parameters:
    - Body: (required) Employee object in the request body.
---
#### Get employee by id
```bash
GET /api/employees/{id}
```
- Parameters:
    - id: (required) Employee ID.
---
#### Get all employees 
```bash
GET /api/employees
```
---
#### Delete employee by id
```bash
DELETE /api/employees/{id}
```
- Parameters:
    - id: (required) Employee ID.
---
#### Update employee
```bash
PUT /api/employees/{id}
```
- Parameters:
    - id: (required) Employee ID.
    - Body: (required) Updated Employee object in the request body.
---


## Work Buddy Main Features

### 1. Longest Collaborating Pair of Employees
 - Identifies the pair of employees who have worked together on common projects for the longest period of time and that time in days, as well the time for each of those projects, the projects IDs, fromDate, toDate.

### 2. CRUD for Employees 
 - Support for creating, reading, updating and deleting employees.

### 2. Header Flexibility
- Support for both CSV files with and without headers.

### 3. Work Duration Calculation
- Calculate the duration of all collaboration between all pairs of employees.

### 4. Sorting Options
- Sort collaboration results based on specified criteria (e.g., ascending or descending order).

### 5. Date Format Handling
- Flexible handling of multiple date formats in the input data.

### 6. NULL Date Handling
- Treat NULL in the `DateTo` field as equivalent to today's date.

### 7. Error Handling
- Provide robust error handling and validation for input data and user interactions.

### 8. File Storage (Optional)
- Optionally store uploaded files for future reference.

### 9. Customizable Output Formats
- Allow customization of the output format for different use cases.

### 10. Easily Extendable
- The well-structured codebase and the comprehensive data it produces offer abundant opportunities for seamless integration of new features and functionalities.


## Database Configuration for Spring Boot Application

To connect the Spring Boot application to a PostgreSQL database, follow these steps:

1. **Create a PostgreSQL Database:**
    - Open a terminal or command prompt.
    - Run the following command to access the PostgreSQL command-line interface:

      ```bash
      psql -U postgres
      ```

    - Enter the PostgreSQL superuser password when prompted.
    - Once in the PostgreSQL command-line interface, create a new database (replace `WorkBuddy` with your desired database name):

      ```sql
      CREATE DATABASE WorkBuddy;
      ```

    - Exit the PostgreSQL command-line interface:

      ```sql
      \q
      ```

2. Open `application.properties` file in your Spring Boot project.

3. Add the following configuration properties to the file:

```properties
# DataSource Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/WorkBuddy
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```


