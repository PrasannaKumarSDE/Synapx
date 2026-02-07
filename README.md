# SynapxAssignment

📄 FNOL Claim Routing System (Backend)
📌 Project Overview

This project implements a First Notice of Loss (FNOL) Claim Routing System using Java Spring Boot.
The system processes claim information from uploaded PDF files, extracts relevant fields, validates mandatory data, and automatically routes claims based on predefined business rules.

The goal is to demonstrate:

PDF parsing

Backend validation

Business rule–based routing

REST API testing

----🛠️ Tech Stack ---

Java 17

Spring Boot 4.x

Apache PDFBox

Spring Web (REST API)

Postman (for testing)

MySQL 

📂 Project Structure
src/main/java/com/example/demo
 ├── controller
 │    └── ClaimController.java
 ├── service
 │    └── ClaimService.java
 └── DemoApplication.java
------------------------------------------------------------------------------------------
📥 Input

Input is a PDF file

PDF contains claim details in label → value format

Example:

POLICY NUMBER
PN-002

NAME OF INSURED
RAMESH

DATE OF LOSS
05/02/2026

--------------------------------------------
 Note:(i implemented the text based pdf)
ACORD FNOL form PDFs are protected and do not expose values programmatically.
Therefore, text-based PDFs were used for extraction and testing.

 Output

The API returns a JSON response containing:

Extracted fields

Missing mandatory fields (if any)

Recommended routing decision

Reason for the decision

Example:

{
  "missingFields": [],
  "recommendedRoute": "Fast-track",
  "reasoning": "Estimated damage below ₹25,000.",
  "extractedFields": {
    "policyNumber": "PN-002",
    "policyholderName": "RAMESH",
    "incidentDate": "05/02/2026",
    "incidentLocation": "Bangalore",
    "estimatedDamage": "20000",
    "claimType": "auto"
  }
}
--------------------------------------------------
🧾 Fields Extracted
Policy Information

Policy Number

Policyholder Name

Incident Information

Date of Loss

Time of Loss

Location of Loss

Description of Accident

Asset Details

Asset Type

Asset ID (VIN)

Estimated Damage

Derived Fields

Claim Type (auto / injury)

Attachments

Initial Estimate

 Mandatory Fields

The following fields are mandatory:

Policy Number

Policyholder Name

Date of Loss

Location of Loss

Estimated Damage

Claim Type

If any mandatory field is missing, the claim is routed to Manual Review.

 Routing Rules

The system applies routing rules in the following order:

Manual Review

If any mandatory field is missing

Investigation Flag

If description contains keywords:

fraud

inconsistent

staged

Specialist Queue

If claim type is injury

Fast-track

If estimated damage < ₹25,000

 Backend Testing Scenarios

Each routing rule was tested using a separate PDF file.
------------------------
 Scenario 1: Manual Review (Missing Fields)

PDF Input

POLICY NUMBER
PN-001

NAME OF INSURED
RAMESH


Result

recommendedRoute = Manual Review
Reason
Missing mandatory fields.
---------------------------------------------------
 Scenario 2: Fast-track (Low Damage)

PDF Input

POLICY NUMBER
PN-002

NAME OF INSURED
RAMESH

DATE OF LOSS
05/02/2026

LOCATION OF LOSS
Bangalore

ESTIMATE AMOUNT
20000


Result

recommendedRoute = Fast-track


Reason
Estimated damage below ₹25,000.
----------------------------------------------------------
Scenario 3: Specialist Queue (Injury Claim)

PDF Input

POLICY NUMBER
PN-003

NAME OF INSURED
RAMESH

DATE OF LOSS
05/02/2026

LOCATION OF LOSS
Bangalore

ESTIMATE AMOUNT
20000

DESCRIPTION OF ACCIDENT
Driver injured in minor accident


Result

recommendedRoute = Specialist Queue


Reason
Claim involves injury.
-------------------------------------------------------------------
 Scenario 4: Investigation Flag (Fraud Detection)

PDF Input

POLICY NUMBER
PN-004

NAME OF INSURED
RAMESH

DATE OF LOSS
05/02/2026

LOCATION OF LOSS
Bangalore

ESTIMATE AMOUNT
40000

DESCRIPTION OF ACCIDENT
Details are inconsistent and appear to be fraud


Result

recommendedRoute = Investigation Flag


Reason
Fraud-related keywords detected.
---------------------------------------------------------------------------
🔧 API Details
Upload Claim PDF

Method: POST

URL:

http://localhost:8080/upload


Body: form-data

Key	Type	Value
file	File	PDF file
