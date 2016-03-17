import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

/**
* To read mq message approach 2 is written for jar it was not working mq read from MQ Receiver code approach 2
*  
 * @author Chetan.Aher
 *
 */
public class MQReceiverApproach2 {

    /**
     * Configuration file path
     */
    static String configProperty = System.getProperty("user.dir")
        + File.separator + "config.properties";
    
    /**
     * logger object
     */
    private static Logger logger = Logger.getLogger(MQReceiverApproach2.class);

    /**
     * Property name to fetched from
     */
    public static final String CONFIG_QUEUE_HOST = "host";
    public static final String CONFIG_QUEUE_PORT = "port";
    public static final String CONFIG_QUEUE_MANAGER = "queue_manager";
    public static final String CONFIG_QUEUE_CHANNEL = "queue_channel";
    public static final String CONFIG_QUEUE_NAME = "queue_name";

    public static void main(String[] args) {
        File configFile = new File(configProperty);
        logger.info("READING CONFIGURATION => " + configFile);

        String host = "";
        int port = 0;
        String queueManagerName = "";
        String queueChannel = "";
        String queueName = "";

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            reader.close();

            host = props.getProperty(CONFIG_QUEUE_HOST);
            queueChannel = props.getProperty(CONFIG_QUEUE_CHANNEL);
            port = Integer.parseInt(props.getProperty(CONFIG_QUEUE_PORT));

            queueManagerName = props.getProperty(CONFIG_QUEUE_MANAGER);
            queueChannel = props.getProperty(CONFIG_QUEUE_CHANNEL);
            queueName = props.getProperty(CONFIG_QUEUE_NAME);

            logger.info("HOST => " + host + " PORT => " + port + " MANAGER => "
                + queueManagerName + " CHANNEL => " + queueChannel);
            MQEnvironment.hostname = host;
            MQEnvironment.channel = queueChannel;
            MQEnvironment.port = port;

            MQQueue queue = null;
            MQQueueManager queueManager = null;

            try {
                queueManager = new MQQueueManager(queueManagerName);

                int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF;
                queue = queueManager.accessQueue(queueName, openOptions);
                MQMessage rcvMessage = new MQMessage();
                MQGetMessageOptions gmo = new MQGetMessageOptions();
                queue.get(rcvMessage, gmo);

                String xmlMessage = rcvMessage.readLine();
                logger.info("Message read => " +xmlMessage);
            } catch (MQException e) {
                logger.error("MQ Exception => ", e);
            } finally {
                try {
                    if (queue != null) {
                        queue.close();
                    }
                    if (queueManager != null) {
                        queueManager.disconnect();
                        queueManager.close();
                    }
                } catch (MQException e1) {
                }
            }
        } catch (IOException e) {
            logger.error("IO Exception => ", e);
        }
    }
}
