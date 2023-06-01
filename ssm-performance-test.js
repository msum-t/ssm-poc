import http from "k6/http";
import { sleep } from "k6";
import { SharedArray } from "k6/data";

export const options = {
  ext: {
    loadimpact: {
      projectID: 3633668,
      // Test runs with the same name groups test runs together
      name: "SSM-LOAD-TEST-1"
    }
  },
stages: [
    { duration: '20m', target: 50 }, // simulate ramp-up of traffic from 1 to 60 users over 5 minutes.
    { duration: '10m', target: 60 }, // stay at 60 users for 10 minutes
    { duration: '3m', target: 100 }, // ramp-up to 100 users over 3 minutes (peak hour starts)
    { duration: '10m', target: 100 }, // stay at 100 users for short amount of time (peak hour)
    { duration: '3m', target: 60 }, // ramp-down to 60 users over 3 minutes (peak hour ends)
    { duration: '10m', target: 60 }, // continue at 60 for additional 10 minutes
    { duration: '5m', target: 0 }, // ramp-down to 0 users
  ],
  thresholds: {
    http_req_duration: ['p(99.99)<1500'], // 99% of requests must complete below 1.5s
  },
};


var data = new SharedArray("ssm", function() {
    return JSON.parse(open('ssm-data.json')); // return array of tags
});

// Reading random customerData from the JSON file
var customerData = data[Math.floor(Math.random() * data.length)];

var rootUrl="http://statemachine:8083/ssm-yaml/"
//var rootUrl="http://sumit8400/ssm-poc:8080/ssm-yaml/"
export default function () {
  const saveCustomer = rootUrl+"saveCustomer";

  //const payload = JSON.stringify({ name: "test item" });
  const headers = { "Content-Type": "application/json" };

  // Send POST request and get the response
  const customerResponse = http.post(saveCustomer,  JSON.stringify(customerData),{ headers: headers });

  // Get the ID from the response
  const customerId= JSON.parse(customerResponse.body).customerId;
  // Send the next six requests using the ID
   const salesEvent=rootUrl+customerId+"/salesEvent"
   const rmEvent=rootUrl+customerId+"/rmEvent"
   const docEvent=rootUrl+customerId+"/docEvent"
   const creditEvent=rootUrl+customerId+"/creditEvent"
   const sdcEvent=rootUrl+customerId+"/sdcEvent"
   const welcome=rootUrl+customerId+"/welcome"

   const salesResponse=http.post(salesEvent, customerResponse.body ,{ headers: headers });

   const rmResponse=http.post(rmEvent, salesResponse.body ,{ headers: headers });

   const docEventResponse=http.post(docEvent, rmResponse.body ,{ headers: headers });

   const creditEventResponse=http.post(creditEvent, docEventResponse.body ,{ headers: headers });

   const sdcEventResponse=http.post(sdcEvent,creditEventResponse.body , { headers: headers });

   http.post(welcome,sdcEventResponse.body ,{ headers: headers })

   sleep(1);

}