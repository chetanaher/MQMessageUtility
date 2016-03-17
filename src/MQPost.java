import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * Post message to MQ 
 * @author Chetan.Aher
 *
 */
public class MQPost {
    /**
     * Configuration file path
     */
    static String configProperty = System.getProperty("user.dir")
        + File.separator + "config.properties";
    /**
     * Property name to fetched from
     */
    public static final String CONFIG_QUEUE_HOST = "host";
    public static final String CONFIG_QUEUE_PORT = "port";
    public static final String CONFIG_QUEUE_MANAGER = "queue_manager";
    public static final String CONFIG_QUEUE_CHANNEL = "queue_channel";
    public static final String CONFIG_QUEUE_NAME = "queue_name";

    static MQQueueConnectionFactory cf = new MQQueueConnectionFactory();

    /**
     * logger object
     */
    private static Logger logger = Logger.getLogger(MQPost.class);

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

            cf.setHostName(host);
            cf.setPort(port);
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE,
                WMQConstants.WMQ_CM_CLIENT);
            cf.setQueueManager(queueManager);
            cf.setChannel(queueChannel);
            MQQueueConnection connection = (MQQueueConnection) cf
                .createQueueConnection();
            MQQueueSession session = (MQQueueSession) connection
                .createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) session.createQueue("queue:///" + queueName);
            MQQueueSender sender = (MQQueueSender) session.createSender(queue);
            sender.setTimeToLive(10000);
            TextMessage message = (TextMessage) session
                .createTextMessage(getMessage());
            connection.start();
            sender.send(message);
            sender.close();
            session.close();
            connection.close();
            System.out.println("Message Sent OK.\n");

        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("Message Send Failure\n");
        }

    }

    /**
     * Get message to post
     * 
     * @return
     * @throws Exception
     */
    public static String getMessage() throws Exception {
        String everything = "Message to post";
        return everything;
    }
}
