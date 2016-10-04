# investment-tax-relief-subscription-dynamic-stub

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html) [![Build Status](https://travis-ci.org/hmrc/investment-tax-relief-subscription-dynamic-stub.svg?branch=master)](https://travis-ci.org/hmrc/investment-tax-relief-subscription-dynamic-stub) [ ![Download](https://api.bintray.com/packages/hmrc/releases/investment-tax-relief-subscription-dynamic-stub/images/download.svg) ](https://bintray.com/hmrc/releases/investment-tax-relief-subscription-dynamic-stub/_latestVersion)

This is a stub for the Tax Relief Subscription Service. The stub is a test double that supports the Tax Relief subscription Service REST API in development or test environments, this enables testing of clients of the service without requiring a full end-to-end test environment that has all the backend services and systems available.

The stub is a Play/Scala application backed by a Mongo database for the test data, which is dynamically created (hence it is termed a dynamic stub, because it does not contain hardcoded, static test data). The test data can be set up either by making requests to the relevant apply or amend operations of the API, or directly loaded into the database using e.g. `mongoimport`. 

The stub supports these Tax Relief Subscription Service API operations:

GET, PUT and POST REST Endpoints to be defined below  (the below is an example only).

- `GET /individual/{nino}/enrolment` - gets the enrolemnt for a particular individual with the specified nino

 ..etc.....



The stub attempts to apply the same business rules as the full service.

(The stub also supports DELETE operations for the purposes of making it easy to tear down test data - these are not supported on the production API of the service.)

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")


/*** Example Json ***/
class SubscriptionRequest {
  {
    "acknowledgementReference": "ABCD123",
    "subscriptionType": {
      "correspondenceDetails": {
      "contactName": {
      "name1": "John",
      "name2": "Smith"
    },
      "contactDetails": {
      "phoneNumber": "01214567896",
      "mobileNumber": "07999056789",
      "faxNumber": "01216754321",
      "emailAddress": "john.smith@gmail.com"
    },
      "contactAddress": {
      "addressLine1": "38 UpperMarshall Street",
      "addressLine2": "Post Box Aptms",
      "addressLine3": "Birmingham",
      "addressLine4": "WarwickShire",
      "countryCode": "GB",
      "postalCode": "B1 1LA"
    }
    }
    }
  }