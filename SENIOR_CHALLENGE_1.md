

WorkMotion (www.workmotion.com) is a global HR platform enabling companies to hire & onboard their employees internationally, at the push of a button. It is our mission to create opportunities for anyone to work from anywhere. As work is becoming even more global and remote, there has never been a bigger chance to build a truly global HR-tech company.


As a part of our backend engineering team, you will be responsible for building our core platform including an  employees managment system.

The employees on this system are assigned to different states, Initially when an employee is added it will be assigned "ADDED" state automatically .


The other states (State machine) for A given Employee are:
- ADDED
- IN-CHECK
- APPROVED
- ACTIVE

The allowed state transitions are:

ADDED -> IN-CHECK <-> APPROVED -> ACTIVE

Our backend stack is:
- Java 11 
- Spring Framework 

The allowed state transitions are:

ADDED -> IN-CHECK *-> APPROVED -> ACTIVE

Furthermore, IN-CHECK state is special and has the following orthogonal child substates:

  - SECURITY_CHECK_STARTED
  - SECURITY_CHECK_FINISHED
  

  - WORK_PERMIT_CHECK_STARTED
  - WORK_PERMIT_CHECK_FINISHED

with allowed state transitions:
- SECURITY_CHECK_STARTED -> SECURITY_CHECK_FINISHED
- WORK_PERMIT_CHECK_STARTED -> WORK_PERMIT_CHECK_FINISHED

This means that a complete state of an employee in the IN_CHECK state could look like (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED).

Examples of permitted transition:
* (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED) -> (IN_CHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED)
* (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED) -> (IN_CHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED)

Transition from IN_CHECK state to APPROVED state happens automatically when the complete state is (IN_CHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_FINISHED).
Transition from IN_CHECK state to APPROVED without meeting the condition above is not allowed.


Your task is to build  Restful API doing the following:
- An Endpoint to support adding an employee with very basic employee details including (name, contract information, age, you can decide.) With initial state "ADDED" which incidates that the employee isn't active yet.

- Another endpoint to change the state of a given employee to any of the states defined above in the state machine respecting the transition rules 

- An Endpoint to fetch employee details


Please provide a solution with the  above features with the following consideration.

- Being simply executable with the least effort Ideally using Docker and docker-compose or any smailiar approach.
- For state machine could be as simple as of using ENUM or by using https://projects.spring.io/spring-statemachine/ 
- Please provide testing for your solution.
- Providing an API Contract e.g. OPENAPI spec. is a big plus







