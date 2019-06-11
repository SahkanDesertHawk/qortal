package org.qora.test.common.transaction;

import org.qora.account.PrivateKeyAccount;
import org.qora.data.transaction.GroupBanTransactionData;
import org.qora.data.transaction.TransactionData;
import org.qora.repository.DataException;
import org.qora.repository.Repository;

public class GroupBanTestTransaction extends TestTransaction {

	public static TransactionData randomTransaction(Repository repository, PrivateKeyAccount account, boolean wantValid) throws DataException {
		final int groupId = 1;
		String member = account.getAddress();
		String reason = "banned for testing";
		final int timeToLive = 3600;

		return new GroupBanTransactionData(generateBase(account), groupId, member, reason, timeToLive);
	}

}
