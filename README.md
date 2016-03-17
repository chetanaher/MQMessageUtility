# MQMessageUtility

### Introduction

> This code is created to read IBM MQ 7.5.It is developed for public use. Libraries may or may not be public :shipit:
> Used log4j for logs

#### Set up
1. Download file
2. Change config.properties values of host, port, queue_manager, queue_channel, queue_name according to your queue.
3. Run file by your purpose (Post Message to queue, Recieve message from queue).
4. Two files of receiver has been created because approach one (Receiver.java) not working when I have created jar file of that code.

### Libraries required
1. CL3Export.jar 
2. CL3Nonexport.jar 
3. SIBXJndiLookupEAR.ear 
4. com.ibm.mq.axis2.jar 
5. com.ibm.mq.commonservices.jar 
6. com.ibm.mq.defaultconfig.jar 
7. com.ibm.mq.headers.jar 
8. com.ibm.mq.jar 
9. com.ibm.mq.jmqi.jar 
10. com.ibm.mq.jms.Nojndi.jar 
11. com.ibm.mq.pcf.jar 
12. com.ibm.mq.postcard.jar 
13. com.ibm.mq.soap.jar 
14. com.ibm.mq.tools.ras.jar 
15. com.ibm.mqjms.jar 
16. connector.jar 
17. dhbcore.jar 
18. fscontext.jar 
19. jms.jar 
20. jndi.jar 
21. jta.jar 
22. ldap.jar 
23. log4j-1.2.17.jar 
24. providerutil.jar 
25. rmm.jar

Above files I found at<IBM MQ Installation folder>/java/lib/







