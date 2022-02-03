package com.example.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageSender
{

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    //Creation of queue name
    private static String queueName = "CUSTOMER_QUEUE";

    public static void main(String[] args) throws JMSException
    {
        System.out.println("url = " + url);

        //Getting JMS connection from the JMS server and starting it
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //Creating a non transactional session to send JMS message.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //The queue will be created automatically on the server.
        Destination destination = session.createQueue(queueName);

        //Destination represents here our queue 'CUSTOMER_QUEUE' on the JMS server.
        //MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);
        TextMessage message = session.createTextMessage("Yo Whatsup");

        //Here we are sending our message
        producer.send(message);

        System.out.println("Message '" + message.getText() + ", Sent Successfully to the Queue");
        connection.close();
    }
}
