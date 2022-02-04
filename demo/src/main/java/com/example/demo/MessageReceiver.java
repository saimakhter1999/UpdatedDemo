package com.example.demo;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    /*
     * Name of the queue we will receive messages from
     */
    private static String queueName = "CUSTOMER_QUEUE";

    public static void main(String[] args) throws JMSException
    {
        System.out.println("url = " + url);


        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue(queueName);

        MessageConsumer consumer = session.createConsumer(destination);

        /*
         * Here we receive the message.
         */
        Message message = consumer.receive();

        if (message instanceof TextMessage)
        {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Received message '" + textMessage.getText() + "'");
        }
        connection.close();
    }
}
