* Quick summary

Client receives monthly deliveries of customer statement records.This informationis delivered in JSON Format. These
records need to be validated as per below rules.
  
     1. All transaction references should be unique
     2. The end balance needs to be validated ( Start Balance +/- Mutation = End Balance )


**1. Clone the repository** 

```bash
https://github.com/pradeepgupta4989/customer-statement-processor.git
```

**2. Run the app using maven**


The application can be accessed using  `http://localhost:8081/statement/v1/process`.

