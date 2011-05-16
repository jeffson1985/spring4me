package org.osforce.spring4me.task;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:54:00 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public abstract class AbstractTask implements Task {

	protected PlatformTransactionManager transactionManager;

	public AbstractTask() {
	}

	@Autowired
	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Async
	public void doAsyncTask(final Map<Object, Object> context) {
		new TransactionTemplate(transactionManager)
		.execute(new TransactionCallbackWithoutResult(){
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
					try {
						doTask(context);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});
	};

	public void doSyncTask(final Map<Object, Object> context) {
		try {
			doTask(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected abstract void doTask(final Map<Object, Object> context) throws Exception;

}
