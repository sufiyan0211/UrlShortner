<h1 align="center">Url Shortner</h1>


<p align="center">
  <i>
    Web application that converts long URLs into shorter URLs of 8 characters and stores them in a database. The shorter URL can then be used to access the original long URL
  </i>
</p>
 
- Scheduler Microservice <a href="https://github.com/sufiyan0211/Schedular-Microservice" alt="">Repository</a>

## Design 
<img src="URL Shortner Design.png" alt="">


## Description
- Developed a web application that converts long URLs into shorter URLs of 8 characters and stores them in a database. The shorter URL can then be used to access the original long URL.
- Utilized [Murmur 32-bit Hashing](https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/digest/MurmurHash3.html) to generate a unique 8-digit string from a long URL.
- Built Database replicas to optimize the performance of searching for short URLs.
- Developed a [Scheduler Microservice](https://github.com/sufiyan0211/Schedular-Microservice) that deletes expired URLs at a scheduled time.
- Deployed URL Shortener Microservice and Scheduler Microservice on separate EC2 instances and configured Master database and Slave database using AWS RDS to prevent single-point failures.



