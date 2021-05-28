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
mvn spring-boot:run

**3. Import the project in IDE**
and then run the file StatementProcessorApplication


The application can be accessed using  `http://localhost:8081/statement/v1/process`.
![image](https://user-images.githubusercontent.com/50258267/119981815-1d8f5400-bfcf-11eb-99ab-b6b0cbcd9182.png)


