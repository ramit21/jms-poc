# jms-poc
Setting up in-memory (ActiveMQ) JMS queues with spring boot application


By just having ActiveMQ on our build path, Spring Boot will automatically set up a ActiveMQ broker.

We need to set a couple properties (see Applcation.properties) to make it an in memory broker, without connection pooling. 

@EnableJms in JmsConfiguration.java enable Jms support to create the JmsTemplate bean.

The listener component (BookMgrQueueListener.java) is using Spring’s @JmsListener annotation with selectors to read the messages with a given Operation header.

To test the configuration we are setting up activeMq broker in a new configuration file, ActiveMqConfiguration.java.

We are setting up a full application context in the testcase but we are replacing the BookService reference in the listener to 
a MockedBookService which we will use to verify whether the correct calls were executed.

