# AppD-AWS-AppRegistry

Prototype utility of an [AppDynamics](https://www.appdynamics.com) integration with [AWS AppRegistry](https://aws.amazon.com/blogs/mt/increase-application-visibility-governance-using-aws-service-catalog-appregistry/).  It can be used to pull the meta-data of one or more applications from an AppDynamics Controller and push it to the AppRegistry.

<br>

## Dependecies 

- Java 1.8
  - Needed to run the utility Jar file [AppD-AWS-AppRegistry.jar](https://github.com/Appdynamics/AppD-AWS-AppRegistry/blob/main/AppD-AWS-AppRegistry.jar)
- AWS Account
  - An AWS user in the account with appropriate permissions for AppRegistry  
- Maven - ( Optional )
  - Used for re-building the Jar file 

<br>

## Usage 

The utility Jar file uses the list of application names along with the connection details for an AppDynamics Controller that are defined in the [application-config.yaml](https://github.com/Appdynamics/AppD-AWS-AppRegistry/blob/main/application-config.yaml) configuration file.

The utiltiy Jar file can be run with one of the two supported options:

- **Create**
  - The create option pulls the meta-data of the applications defined in the configuration file from the AppDynamics Controller defined in the configuration file and publishes that data to AppRegistry.
- **List**
  - The list option simply lists the details of all the applications contained within AppRegistry.

<br>

## Configuration File

<br>

## Create Functionality

![image](images/ad-travel-app-01.png)

<br>

## List Functionality
