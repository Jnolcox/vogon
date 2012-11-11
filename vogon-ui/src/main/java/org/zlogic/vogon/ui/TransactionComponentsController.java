/*
 * Vogon personal finance/expense analyzer.
 * License TBD.
 * Author: Dmitry Zolotukhin <zlogic@gmail.com>
 */
package org.zlogic.vogon.ui;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import org.zlogic.vogon.data.FinanceAccount;
import org.zlogic.vogon.data.FinanceData;
import org.zlogic.vogon.data.FinanceTransaction;
import org.zlogic.vogon.data.TransactionComponent;
import org.zlogic.vogon.ui.adapter.AccountModelAdapter;
import org.zlogic.vogon.ui.adapter.AmountModelAdapter;
import org.zlogic.vogon.ui.adapter.TransactionComponentModelAdapter;
import org.zlogic.vogon.ui.cell.AmountCellEditor;
import org.zlogic.vogon.ui.cell.StringValidatorDouble;

/**
 * Controller for editing a transaction's components.
 *
 * @author Dmitry Zolotukhin
 */
public class TransactionComponentsController implements Initializable {

	private java.util.ResourceBundle messages = java.util.ResourceBundle.getBundle("org/zlogic/vogon/ui/messages");
	/**
	 * The FinanceData instance
	 */
	protected FinanceData financeData;
	/**
	 * The edited transaction
	 */
	protected FinanceTransaction transaction;
	/**
	 * The transaction components table
	 */
	@FXML
	private TableView<TransactionComponentModelAdapter> transactionComponents;
	/**
	 * The transaction type combo box
	 */
	@FXML
	private ComboBox<TransactionTypeComboItem> transactionType;
	/**
	 * The account column
	 */
	@FXML
	private TableColumn<TransactionComponentModelAdapter, AccountModelAdapter> columnAccount;
	/**
	 * The amount column
	 */
	@FXML
	private TableColumn<TransactionComponentModelAdapter, AmountModelAdapter> columnAmount;

	/**
	 * Initializes the Transaction Components editor
	 *
	 * @param url the FXML URL
	 * @param rb the FXML ResourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		transactionComponents.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		//TODO: use ChangeListeners in TransactionComponentModelAdapter instead
		//Cell editors
		columnAccount.setCellFactory(new Callback<TableColumn<TransactionComponentModelAdapter, AccountModelAdapter>, TableCell<TransactionComponentModelAdapter, AccountModelAdapter>>() {
			@Override
			public TableCell<TransactionComponentModelAdapter, AccountModelAdapter> call(TableColumn<TransactionComponentModelAdapter, AccountModelAdapter> p) {
				ComboBoxTableCell cell = new ComboBoxTableCell<>();
				cell.getItems().addAll(getAccountsComboList());
				return cell;
			}
		});
		columnAccount.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TransactionComponentModelAdapter, AccountModelAdapter>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<TransactionComponentModelAdapter, AccountModelAdapter> t) {
				t.getRowValue().setAccount(t.getNewValue().getAccount());
			}
		});

		columnAmount.setCellFactory(new Callback<TableColumn<TransactionComponentModelAdapter, AmountModelAdapter>, TableCell<TransactionComponentModelAdapter, AmountModelAdapter>>() {
			@Override
			public TableCell<TransactionComponentModelAdapter, AmountModelAdapter> call(TableColumn<TransactionComponentModelAdapter, AmountModelAdapter> p) {
				AmountCellEditor cell = new AmountCellEditor<>(new StringValidatorDouble());
				cell.setAlignment(Pos.CENTER_RIGHT);
				return cell;
			}
		});
		columnAmount.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TransactionComponentModelAdapter, AmountModelAdapter>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<TransactionComponentModelAdapter, AmountModelAdapter> t) {
				t.getRowValue().setAmount(t.getNewValue().getAmount());
			}
		});
	}

	/**
	 * Sets the edited transaction
	 *
	 * @param transaction the edited transaction
	 */
	public void setTransaction(FinanceTransaction transaction) {
		this.transaction = transaction;
		updateTransactionTypeCombo(transaction.getType());
		updateComponents();
	}

	/**
	 * Assigns the FinanceData instance
	 *
	 * @param financeData the FinanceData instance
	 */
	public void setFinanceData(FinanceData financeData) {
		this.financeData = financeData;
	}

	/**
	 * Add component button
	 */
	@FXML
	private void handleAddComponent() {
		TransactionComponent component = new TransactionComponent(null, transaction, 0);
		financeData.createTransactionComponent(component);
		TransactionComponentModelAdapter newComponentAdapter = new TransactionComponentModelAdapter(component, financeData);
		transactionComponents.getItems().add(newComponentAdapter);
		transactionComponents.getSelectionModel().select(newComponentAdapter);
	}

	/**
	 * Delete component button
	 */
	@FXML
	private void handleDeleteComponent() {
		TransactionComponentModelAdapter selectedItem = transactionComponents.getSelectionModel().getSelectedItem();
		if (selectedItem != null)
			financeData.deleteTransactionComponent(selectedItem.getTransactionComponent());
		transactionComponents.getItems().remove(selectedItem);
	}

	/**
	 * Transaction type combo box has changed
	 */
	@FXML
	private void handleSetTransactionType() {
		TransactionTypeComboItem newType = transactionType.getSelectionModel().getSelectedItem();
		if (newType != null)
			financeData.setTransactionType(transaction, newType.getType());
	}

	/**
	 * Updates the transaction components table from database
	 */
	private void updateComponents() {
		transactionComponents.getItems().removeAll(transactionComponents.getItems());
		transactionComponents.getItems().clear();
		for (TransactionComponent component : transaction.getComponents())
			transactionComponents.getItems().add(new TransactionComponentModelAdapter(component, financeData));
	}

	/**
	 * Updates the transaction type combo box
	 *
	 * @param type the transaction type to be selected
	 */
	private void updateTransactionTypeCombo(FinanceTransaction.Type type) {
		transactionType.setDisable(true);
		transactionType.getItems().removeAll(transactionType.getItems());
		transactionType.getItems().clear();
		TransactionTypeComboItem selectedItem = null;
		for (FinanceTransaction.Type currentType : FinanceTransaction.Type.values())
			if (currentType != FinanceTransaction.Type.UNDEFINED) {
				TransactionTypeComboItem currentItem = new TransactionTypeComboItem(currentType);
				transactionType.getItems().add(currentItem);
				if (type == currentType)
					selectedItem = currentItem;
			}
		transactionType.getSelectionModel().select(selectedItem);
		transactionType.setDisable(false);
	}

	/**
	 * Returns a list of account items which can be rendered in a Combo box
	 * (used to specifically detect the selected item)
	 *
	 * @return the list of account items
	 */
	public List<AccountModelAdapter> getAccountsComboList() {
		//TODO: check how this handles adding/hiding of accounts. Swing simply updates the accounts combo box on any changes.
		List<AccountModelAdapter> items = new LinkedList<>();
		for (FinanceAccount account : financeData.getAccounts())
			if (account.getIncludeInTotal())
				items.add(new AccountModelAdapter(account, financeData));
		return items;
	}

	/**
	 * Transaction type combo box item
	 */
	protected class TransactionTypeComboItem {

		/**
		 * The transaction type
		 */
		protected FinanceTransaction.Type type;

		/**
		 * Default constructor
		 *
		 * @param type the transaction type
		 */
		public TransactionTypeComboItem(FinanceTransaction.Type type) {
			this.type = type;
		}

		@Override
		public String toString() {
			switch (type) {
				case EXPENSEINCOME:
					return messages.getString("TRANSACTION_EXPENSE_INCOME");
				case TRANSFER:
					return messages.getString("TRANSACTION_TRANSFER");
				case UNDEFINED:
					return messages.getString("INVALID_TRANSACTION_TYPE");
			}
			return messages.getString("INVALID_TRANSACTION_TYPE");
		}

		/**
		 * Returns the transaction type
		 *
		 * @return the transaction type
		 */
		public FinanceTransaction.Type getType() {
			return type;
		}
	}
}
