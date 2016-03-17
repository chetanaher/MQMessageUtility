import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueReceiver;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueReceiver;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * Read MQ message approach 1
 * 
 * @author Chetan.Aher
 * 
 */
public class Receiver {
    /**
     * Configuration file path
     */
    static String configProperty = System.getProperty("user.dir")
        + File.separator + "config.properties";

    /**
     * logger object
     */
    private static Logger logger = Logger.getLogger(Receiver.class);

    /**
     * Property name to fetched from
     */
    public static final String CONFIG_QUEUE_HOST = "host";
    public static final String CONFIG_QUEUE_PORT = "port";
    public static final String CONFIG_QUEUE_MANAGER = "queue_manager";
    public static final String CONFIG_QUEUE_CHANNEL = "queue_channel";
    public static final String CONFIG_QUEUE_NAME = "queue_name";

    /**
     * Main method to validate xml and xsd
     * 
     * @param args
     */
    public static void main(String[] args) {

        String host = "";
        int port = 0;
        String queueManager = "";
        String queueChannel = "";
        String queueName = "";
        MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

        File configFile = new File(configProperty);
        logger.info("READING CONFIGURATION => " + configFile);

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            reader.close();

            host = props.getProperty(CONFIG_QUEUE_HOST);
            port = Integer.parseInt(props.getProperty(CONFIG_QUEUE_PORT));
            queueManager = props.getProperty(CONFIG_QUEUE_MANAGER);
            queueChannel = props.getProperty(CONFIG_QUEUE_CHANNEL);
            queueName = props.getProperty(CONFIG_QUEUE_NAME);

            logger.info("HOST => " + host + " PORT => " + port + " MANAGER => "
                + queueManager + " CHANNEL => " + queueChannel);

            cf.setHostName(host);
            cf.setPort(port);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE,
                WMQConstants.WMQ_CM_CLIENT);
            cf.setQueueManager(queueManager);
            cf.setChannel(queueChannel);
            cf.setTransportType(1);

            MQQueueConnection connection = (MQQueueConnection) cf
                .createQueueConnection();
            MQQueueSession session = (MQQueueSession) connection
                .createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            logger.info("QUEUE LISTENING => " + queueName);

            Queue reply = (Queue) session.createQueue("queue:///" + queueName);
            connection.start();
            QueueReceiver receiver = (MQQueueReceiver) session
                .createConsumer(reply);

            TextMessage replyMsg = null;
            
            replyMsg = (TextMessage) receiver.receive();
            String xmlMessage = replyMsg.getText();
            logger.info("Mesage Read" + xmlMessage);
        } catch (FileNotFoundException ex) {
            logger.error("FILE NOT FOUND EXECPTION" + ex.getMessage());
        } catch (IOException ex) {
            logger.error("IO EXECPTION" + ex.getMessage());
        } catch (JMSException e) {

            logger.error("JMS EXECPTION" + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("EXECPTION" + e.getMessage());
        }
    }
}
