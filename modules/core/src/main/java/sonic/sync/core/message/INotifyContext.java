package sonic.sync.core.message;

import java.util.Set;

public interface INotifyContext {

	Set<String> consumeUsersToNotify();

	BaseNotificationMessageFactory consumeMessageFactory();

}
