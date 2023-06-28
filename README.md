<h1 align="center">Url Shortner</h1>


<p align="left">
  <i>
    Web application that converts long URLs into shorter URLs of 8 characters and stores them in a database. The shorter URL can then be used to access the original long URL
  </i>
</p>
 
- Scheduler Microservice <a href="https://github.com/sufiyan0211/Schedular-Microservice" alt="">Repository</a>

## Design 
<img src="staticImages/URL Shortener Design.png" alt="">

## Database 
- Just one Table name as Url.
- Description of Url Table:
  |Long_Url|Short_Url|Created_Date|
  |-|-|-|
  |2048 Bytes|8 Byte|7 Byte|
- Size of one row is <b>2063 Bytes</b>.
  
## System
 - Write speed = 10 <i>write per seconds</i>.
 - Expiry of each short url is 7 days.</br>
```
  Count of Short Urls = 10 * 60 * 60 * 24 * 7 days </br>
                       = 60,48,000
                       = 6 * 10^6
                       = 6 millions
```
- Means in 7 days database will have 6 million short urls.
```
  Database Storage after 7 days = 6 * 10^6 * 2063 Bytes
                                = 12,378 * 10^6 Bytes
                                = 12.37 * 10^9 Bytes
                                = 13 GB
```
- As there are 8 characters length short url so there will be 36<sup>8</sup> unique short urls presents.</br>
```
            = 28,21,10,99,07,456
            = 2.82 * 10^12
            = 2.82 trillion short urls
```

## Concurrency
- <b>Initial Approach</b> (Single Threaded)</br>
  <img src="staticImages/Concurrency- Initial Design.png" alt=""></br>
- <b>Scale Approach</b> (Multiple Threaded support)</br>
  <img src="staticImages/Concurrency- Final Design.png" alt=""></br>

  

## Description
- Developed a web application that converts long URLs into shorter URLs of 8 characters and stores them in a database. The shorter URL can then be used to access the original long URL.
- Utilized [Murmur 32-bit Hashing](https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/digest/MurmurHash3.html) to generate a unique 8-digit string from a long URL.
- Built Database replicas to optimize the performance of searching for short URLs.
- Developed a [Scheduler Microservice](https://github.com/sufiyan0211/Schedular-Microservice) that deletes expired URLs at a scheduled time.
- Deployed URL Shortener Microservice and Scheduler Microservice on separate EC2 instances and configured Master database and Slave database using AWS RDS to prevent single-point failures.



