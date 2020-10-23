# Fuel Consumption Registration Application Guide  
fuel consumption management application for small transportation company. It should be possible to report purchased fuel volumes and retrieve the already inserted information by other contributors. The application initially will have only RESTful API with JSON payloads in response, but won't be limited only to this interface in the future. The fuel consumption data should be stored locally in the file or in embedded database, but it should possible later to swap current data storage to something else.       
URL: https://github.com/vahapgencdal/FuelConsumptionRegistrationJava  
## Project Structure
The project has three different configuration files
*  DEV: Developer Environment
*  TEST: Test Environment
*  PROD: Production Environment

- if you want to change environment, goto application.properties file and change spring.profiles.active property

## LOGBACK.xml
* if you are developer environment
you dont need to change logback-spring.xml. all the logs will write to console. 
* if you are in prod enviroment you need to give right file path to logback. please change logback like below   
 <property name="LOG_FILE" value="/users/vahap/logs/fuel_consumption/app.log"/>
 
   

## H2 Console
if you want to access h2 database goto web page with below url   
http://localhost:8080/h2-console   
change JDBC url like "jdbc:h2:mem:fuel_consumption" and after that connect. 


##Spring Fox
Spring Fox is using for show the all APIs after you run application successfully. please use the below URL     
http://localhost:8080/swagger-ui/#/   
All the services can be test with swagger-ui.


## About Application
* in API; I have three POST and 6 GET rest services. I didn't implement any security for that because of general use.  
and not more complexity.
there are three type of authorization type anyone can be use here.
    * HTTP Basic Authentication
    * JSON Web Tokens (JWT)
    * Standard OAuth 2.0 
they are discussable.
###About APIS
* Register Single Record of Fuel Consumption.
    * Request URL: http://localhost:8080/api/v1/fuel/expenses/single
    * Request Body Example:
    ```
    {
      "fuelType":"DIESEL",
      "fuelPrice":"1.12",
      "fuelVolume":"40.4",
      "consumptionDate":"2018-12-01",
      "driverId":"123"
    
    }
    ```
        * There are FuelType Enum Validation so Fuel Type have to be DIESEL,P95,P98 and LPG
        * Fuel Price have to be positive number
        * FuelVolume have to be positive number
        * consumptionDate have to yyyy-MM-dd format
        * driverId have to be positive number
* Register list of Fuel Consumptions with JSON List
    * Request URL: http://localhost:8080/api/v1/fuel/expenses/list     
    * Request Body Example:
    ```
        [
            {
              "fuelType":"DIESEL",
              "fuelPrice":"1.12",
              "fuelVolume":"10.3",
              "consumptionDate":"2017-10-02",
              "driverId":"123"
        
            },
            {
              "fuelType":"LPG",
              "fuelPrice":"2.12",
              "fuelVolume":"20.30",
              "consumptionDate":"2017-11-02",
              "driverId":"1234"
            }
        ]
    ```
    * There are same validations of one list record.
* Register list of Fuel Consumptions with JSON File
    * Request URL: http://localhost:8080/api/v1/fuel/expenses/file     
    * Request Body Example:
        there is a file under src/test/resources/test-data/json_util_success_case.json   
        can be use for file upload
    * There are same validations of one list record.
* Retrieve total spent amount of money grouped by month  
    in this api I put MONTH/YEAR choice with requestParam parameter.   
    * Request URL: http://localhost:8080/api/v1/report/expense?period=MONTH
    * if you want query driverId request Url will be change like below   
      Request URL: http://localhost:8080/api/v1/report/expense{driverId}?period=MONTH  
    * Response Body for 200:
    ```
    [
      {
        "time": "JANUARY",
        "totalAmount": 1487
      },
      {
        "time": "FEBRUARY",
        "totalAmount": 585
      },
      {
        "time": "MARCH",
        "totalAmount": 170
      }
    ]        
    ```    
    * Validations have been used for period request param and driverId path variable; 
        * request param has Enum Validation. so you can use just MONTH/YEAR for period
        * driverId have to be positive number
        
* Retrieve list of fuel consumption records for specified month  
    * Request URL: http://localhost:8080/api/v1/report/fuel/{monthId}   
        Example : http://localhost:8080/api/v1/report/fuel/2
    * if you want query driverId request Url will be change like below   
      Request URL: http://localhost:8080/api/v1/report/fuel/{monthId}/{driverId}   
      Example : http://localhost:8080/api/v1/report/fuel/2/3
    * Response Body for 200:
    ```
    [
      {
        "fuelType": "LPG",
        "fuelVolume": 30,
        "fuelPrice": 1.5,
        "totalPrice": 45,
        "consumptionDate": "2020-02-07",
        "driverId": 3
      },
      {
        "fuelType": "LPG",
        "fuelVolume": 20,
        "fuelPrice": 2.5,
        "totalPrice": 50,
        "consumptionDate": "2020-02-09",
        "driverId": 3
      },
      {
        "fuelType": "P95",
        "fuelVolume": 30,
        "fuelPrice": 1.5,
        "totalPrice": 45,
        "consumptionDate": "2020-02-11",
        "driverId": 3
      },
      {
        "fuelType": "P95",
        "fuelVolume": 20,
        "fuelPrice": 4,
        "totalPrice": 80,
        "consumptionDate": "2020-02-13",
        "driverId": 3
      }
    ]   
    ```    
    * Validations have been used for monthId and driverId path variable; 
        * monthId have to be between 1 and 12
        * driverId have to be positive number
* Retrieve Statistics for each month, list fuel consumption records grouped by Fuel Type
    * Request URL: http://localhost:8080/api/v1/report/fuel/months?groupBy=FUEL_TYPE
        * if you want query driverId request Url will be change like below   
          Request URL: http://localhost:8080/api/v1/report/fuel/months/{driverId}?groupBy=FUEL_TYPE   
          Example : http://localhost:8080/api/v1/report/fuel/months/3?groupBy=FUEL_TYPE
        * Response Body for 200:
        ```
        [
          {
            "month": "JANUARY",
            "fuelTypeStatistics": [
              {
                "fuelType": "P95",
                "totalVolume": 360,
                "averagePrice": 2.95,
                "totalPrice": 1062
              }
            ]
          },
          {
            "month": "FEBRUARY",
            "fuelTypeStatistics": [
              {
                "fuelType": "LPG",
                "totalVolume": 50,
                "averagePrice": 1.9,
                "totalPrice": 95
              },
              {
                "fuelType": "P95",
                "totalVolume": 50,
                "averagePrice": 2.5,
                "totalPrice": 125
              }
            ]
          },
          {
            "month": "MARCH",
            "fuelTypeStatistics": [
              {
                "fuelType": "LPG",
                "totalVolume": 20,
                "averagePrice": 1.5,
                "totalPrice": 30
              }
            ]
          }
        ]   
        ```    
        * Validations have been used for groupBy request param and driverId path variable; 
            * group by has Enum Validation it has to be FUEL_TYPE for now in the future we can add more group by with this parameter.
            * driverId have to be positive number
### About Application framework change
 for now; I had not much time to create architecture for the fully changeable application. 
but I can explain how to do;    
we can create fuel-consumption-api interface application with Raml and after that our main application   the fuel-consumption-service application would use dependency of  fuel-consumption-api.
in fuel-consumption-api we could use Raml to the jax-rs plugin will create our resource APIs.   
when we implement the API application our Rest API would be ready. then we would not need to use spring application for creating Rest API.

in the other way, we can use google guice for dependency injection then we would not use spring dependency application.   
I created my old application example we can check how I implemented jax-rs and google-guice there.
URL : https://github.com/vahapgencdal/ebank 

      
      
