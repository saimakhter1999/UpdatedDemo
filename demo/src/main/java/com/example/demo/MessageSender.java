package com.example.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.jms.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        String xml = readXmlFromFile();
        TextMessage message = session.createTextMessage(xml);

        //Here we are sending our message
        producer.send(message);

        System.out.println("Message '" + message.getText() + ", Sent Successfully to the Queue");
        connection.close();
    }
    private static String readXmlFromFile() {
        // read XML from file
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("customer.xml");

            NodeList list = doc.getElementsByTagName("Customer");
            Element element = null;
            String Name = null;
            String Age = null;
            String Phone = null;
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    Name = element.getElementsByTagName("Name").item(0).getTextContent();
                    Age = element.getElementsByTagName("Age").item(0).getTextContent();
                    Phone = element.getElementsByTagName("Phone").item(0).getTextContent();
                }
            }
            return Name + " " + Age + " "+ Phone;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
    }
}
