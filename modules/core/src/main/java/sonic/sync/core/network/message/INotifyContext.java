package sonic.sync.core.network.message;

import java.util.Set;

public interface INotifyContext {

	Set<String> consumeUsersToNotify();

	BaseNotificationMessageFactory consumeMessageFactory();

}
