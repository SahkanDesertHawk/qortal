package org.qora.repository.hsqldb.transaction;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.qora.data.transaction.IssueAssetTransactionData;
import org.qora.data.transaction.TransactionData;
import org.qora.repository.DataException;
import org.qora.repository.hsqldb.HSQLDBRepository;
import org.qora.repository.hsqldb.HSQLDBSaver;

public class HSQLDBIssueAssetTransactionRepository extends HSQLDBTransactionRepository {

	public HSQLDBIssueAssetTransactionRepository(HSQLDBRepository repository) {
		this.repository = repository;
	}

	TransactionData fromBase(long timestamp, int txGroupId, byte[] reference, byte[] creatorPublicKey, BigDecimal fee, byte[] signature) throws DataException {
		try (ResultSet resultSet = this.repository.checkedExecute(
				"SELECT owner, asset_name, description, quantity, is_divisible, asset_id FROM IssueAssetTransactions WHERE signature = ?", signature)) {
			if (resultSet == null)
				return null;

			String owner = resultSet.getString(1);
			String assetName = resultSet.getString(2);
			String description = resultSet.getString(3);
			long quantity = resultSet.getLong(4);
			boolean isDivisible = resultSet.getBoolean(5);

			// Special null-checking for asset ID
			Long assetId = resultSet.getLong(6);
			if (resultSet.wasNull())
				assetId = null;

			return new IssueAssetTransactionData(timestamp, txGroupId, reference, creatorPublicKey, assetId, owner, assetName, description, quantity, isDivisible,
					fee, signature);
		} catch (SQLException e) {
			throw new DataException("Unable to fetch issue asset transaction from repository", e);
		}
	}

	@Override
	public void save(TransactionData transactionData) throws DataException {
		IssueAssetTransactionData issueAssetTransactionData = (IssueAssetTransactionData) transactionData;

		HSQLDBSaver saveHelper = new HSQLDBSaver("IssueAssetTransactions");

		saveHelper.bind("signature", issueAssetTransactionData.getSignature()).bind("issuer", issueAssetTransactionData.getIssuerPublicKey())
				.bind("owner", issueAssetTransactionData.getOwner()).bind("asset_name", issueAssetTransactionData.getAssetName())
				.bind("description", issueAssetTransactionData.getDescription()).bind("quantity", issueAssetTransactionData.getQuantity())
				.bind("is_divisible", issueAssetTransactionData.getIsDivisible()).bind("asset_id", issueAssetTransactionData.getAssetId());

		try {
			saveHelper.execute(this.repository);
		} catch (SQLException e) {
			throw new DataException("Unable to save issue asset transaction into repository", e);
		}
	}

}