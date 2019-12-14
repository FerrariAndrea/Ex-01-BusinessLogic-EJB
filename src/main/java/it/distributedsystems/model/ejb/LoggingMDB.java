package it.distributedsystems.model.ejb;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
        name = "LoggingMDBHandler",
        activationConfig = {
                @ActivationConfigProperty( propertyName = "destinationType",
                        propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty( propertyName = "destination",
                        propertyValue ="/queue/Logging")
        }
)
class LoggingMDB implements MessageListener {

    public LoggingMDB(){}

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(message.getStringProperty("msg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
