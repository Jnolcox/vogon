/*
 * Vogon personal finance/expense analyzer.
 * License TBD.
 * Author: Dmitry Zolotukhin <zlogic@gmail.com>
 */
package org.zlogic.vogon.ui;

import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.zlogic.vogon.data.CsvImporter;
import org.zlogic.vogon.data.DatabaseManager;
import org.zlogic.vogon.data.FileExporter;
import org.zlogic.vogon.data.FileImporter;
import org.zlogic.vogon.data.FinanceAccount;
import org.zlogic.vogon.data.FinanceData;
import org.zlogic.vogon.data.FinanceTransaction;
import org.zlogic.vogon.data.VogonExportException;
import org.zlogic.vogon.data.VogonImportLogicalException;
import org.zlogic.vogon.data.XmlExporter;
import org.zlogic.vogon.data.XmlImporter;

/**
 * Main window class
 *
 * @author Zlogic
 */
public class MainWindow extends javax.swing.JFrame implements FinanceData.TransactionCreatedEventListener, FinanceData.CurrencyUpdatedEventListener {

	private static final ResourceBundle messages = ResourceBundle.getBundle("org/zlogic/vogon/ui/messages");

	/**
	 * Creates new form MainWindow
	 */
	public MainWindow() {
		initComponents();
		initCustomComponents();
	}

	/**
	 * Completes user configuration of form
	 */
	private void initCustomComponents() {
		//Restore settings
		lastDirectory = preferenceStorage.get("lastDirectory", null) == null ? null : new java.io.File(preferenceStorage.get("lastDirectory", null)); //NOI18N

		//Load data from DB
		transactionsTableModel.setFinanceData(financeData);
		accountsTableModel.setFinanceData(financeData);
		currenciesTableModel.setFinanceData(financeData);
		transactionEditor.setFinanceData(financeData);
		transactionEditor.updateAccountsCombo();
		financeData.addTransactionCreatedListener(this);
		financeData.addTransactionCreatedListener(transactionsTableModel);
		financeData.addTransactionUpdatedListener(transactionsTableModel);
		financeData.addAccountCreatedListener(accountsTableModel);
		financeData.addAccountUpdatedListener(accountsTableModel);
		financeData.addAccountDeletedListener(accountsTableModel);
		financeData.addAccountCreatedListener(transactionEditor);
		financeData.addAccountUpdatedListener(transactionEditor);
		financeData.addAccountDeletedListener(transactionEditor);
		financeData.addCurrencyUpdatedListener(this);
		financeData.addCurrencyUpdatedListener(currenciesTableModel);

		jTableTransactions.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (jTableTransactions.getSelectedRow() >= 0)
					transactionEditor.editTransaction(transactionsTableModel.getTransaction(jTableTransactions.convertRowIndexToModel(jTableTransactions.getSelectedRow())));
				else
					transactionEditor.editTransaction(null);
			}
		});

		updateDefaultCurrencyCombo();

		jTableAccounts.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox(accountsTableModel.getCurrenciesComboList())));
		jTableAccounts.getColumnModel().getColumn(1).setCellRenderer(SumTableCell.getRenderer());

		jTableTransactions.getColumnModel().getColumn(3).setCellRenderer(SumTableCell.getRenderer());

		analyticsViewer.setFinanceData(financeData);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelTransactions = new javax.swing.JPanel();
        jSplitPaneTransactions = new javax.swing.JSplitPane();
        transactionEditor = new org.zlogic.vogon.ui.TransactionEditor();
        jPanelTransactionsList = new javax.swing.JPanel();
        jPanelTransactionControls = new javax.swing.JPanel();
        jButtonDeleteTransaction = new javax.swing.JButton();
        jScrollPaneTransactions = new javax.swing.JScrollPane();
        jTableTransactions = new javax.swing.JTable();
        jPanelAnalytics = new javax.swing.JPanel();
        analyticsViewer = new org.zlogic.vogon.ui.AnalyticsViewer();
        jPanelAccounts = new javax.swing.JPanel();
        jPanelAccountsControls = new javax.swing.JPanel();
        jButtonAddAccount = new javax.swing.JButton();
        jButtonDeleteAccount = new javax.swing.JButton();
        jScrollPaneAccounts = new javax.swing.JScrollPane();
        jTableAccounts = new javax.swing.JTable();
        jPanelCurrencies = new javax.swing.JPanel();
        jPanelCurrenciesControls = new javax.swing.JPanel();
        javax.swing.JLabel jLabelDefaultCurrency = new javax.swing.JLabel();
        jComboBoxDefaultCurrency = new javax.swing.JComboBox();
        jScrollPaneCurrencies = new javax.swing.JScrollPane();
        jTableCurrencies = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemImport = new javax.swing.JMenuItem();
        jMenuItemExport = new javax.swing.JMenuItem();
        jMenuTools = new javax.swing.JMenu();
        jMenuItemRecalculateBalance = new javax.swing.JMenuItem();
        jMenuItemCleanupDB = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(messages.getString("MAINWINDOW_TITLE")); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 768));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanelTransactions.setLayout(new java.awt.BorderLayout());

        jSplitPaneTransactions.setDividerLocation(300);
        jSplitPaneTransactions.setTopComponent(transactionEditor);

        jPanelTransactionsList.setLayout(new java.awt.BorderLayout());

        jPanelTransactionControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButtonDeleteTransaction.setText(messages.getString("DELETE")); // NOI18N
        jButtonDeleteTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteTransactionActionPerformed(evt);
            }
        });
        jPanelTransactionControls.add(jButtonDeleteTransaction);

        jPanelTransactionsList.add(jPanelTransactionControls, java.awt.BorderLayout.NORTH);

        jTableTransactions.setAutoCreateRowSorter(true);
        jTableTransactions.setModel(transactionsTableModel);
        jTableTransactions.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableTransactions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneTransactions.setViewportView(jTableTransactions);

        jPanelTransactionsList.add(jScrollPaneTransactions, java.awt.BorderLayout.CENTER);

        jSplitPaneTransactions.setBottomComponent(jPanelTransactionsList);

        jPanelTransactions.add(jSplitPaneTransactions, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(messages.getString("TRANSACTIONS"), jPanelTransactions); // NOI18N

        jPanelAnalytics.setLayout(new java.awt.BorderLayout());
        jPanelAnalytics.add(analyticsViewer, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(messages.getString("ANALYTICS"), jPanelAnalytics); // NOI18N

        jPanelAccounts.setLayout(new java.awt.BorderLayout());

        jPanelAccountsControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButtonAddAccount.setText(messages.getString("ADD_ACCOUNT")); // NOI18N
        jButtonAddAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddAccountActionPerformed(evt);
            }
        });
        jPanelAccountsControls.add(jButtonAddAccount);

        jButtonDeleteAccount.setText(messages.getString("DELETE_ACCOUNT")); // NOI18N
        jButtonDeleteAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteAccountActionPerformed(evt);
            }
        });
        jPanelAccountsControls.add(jButtonDeleteAccount);

        jPanelAccounts.add(jPanelAccountsControls, java.awt.BorderLayout.NORTH);

        jTableAccounts.setAutoCreateRowSorter(true);
        jTableAccounts.setModel(accountsTableModel);
        jTableAccounts.setFillsViewportHeight(true);
        jScrollPaneAccounts.setViewportView(jTableAccounts);

        jPanelAccounts.add(jScrollPaneAccounts, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(messages.getString("ACCOUNTS"), jPanelAccounts); // NOI18N

        jPanelCurrencies.setLayout(new java.awt.BorderLayout());

        jPanelCurrenciesControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabelDefaultCurrency.setLabelFor(jComboBoxDefaultCurrency);
        jLabelDefaultCurrency.setText(messages.getString("DEFAULT_CURRENCY")); // NOI18N
        jPanelCurrenciesControls.add(jLabelDefaultCurrency);

        jComboBoxDefaultCurrency.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxDefaultCurrencyItemStateChanged(evt);
            }
        });
        jPanelCurrenciesControls.add(jComboBoxDefaultCurrency);

        jPanelCurrencies.add(jPanelCurrenciesControls, java.awt.BorderLayout.NORTH);

        jTableCurrencies.setAutoCreateRowSorter(true);
        jTableCurrencies.setModel(currenciesTableModel);
        jTableCurrencies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneCurrencies.setViewportView(jTableCurrencies);

        jPanelCurrencies.add(jScrollPaneCurrencies, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(messages.getString("CURRENCIES"), jPanelCurrencies); // NOI18N

        getContentPane().add(jTabbedPane1);

        jMenuFile.setText(messages.getString("MAINWINDOW_MENU_FILE")); // NOI18N

        jMenuItemImport.setText(messages.getString("MAINWINDOW_MENU_IMPORT")); // NOI18N
        jMenuItemImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemImportActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemImport);

        jMenuItemExport.setText(messages.getString("EXPORT")); // NOI18N
        jMenuItemExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExportActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExport);

        jMenuBar.add(jMenuFile);

        jMenuTools.setText(messages.getString("TOOLS")); // NOI18N

        jMenuItemRecalculateBalance.setText(messages.getString("RECALCULATE_BALANCE")); // NOI18N
        jMenuItemRecalculateBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRecalculateBalanceActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemRecalculateBalance);

        jMenuItemCleanupDB.setText(messages.getString("CLEANUP_DB")); // NOI18N
        jMenuItemCleanupDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCleanupDBActionPerformed(evt);
            }
        });
        jMenuTools.add(jMenuItemCleanupDB);

        jMenuBar.add(jMenuTools);

        setJMenuBar(jMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImportActionPerformed
		// Prepare file chooser dialog
		JFileChooser fileChooser = new JFileChooser((lastDirectory != null && lastDirectory.exists()) ? lastDirectory : null);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle(messages.getString("CHOOSE_FILES_TO_IMPORT"));
		//Prepare file chooser filter
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(messages.getString("XML_FILES"), "xml"));//NOI18N
		fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(messages.getString("CSV_FILES_(COMMA-SEPARATED)"), "csv"));//NOI18N
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			lastDirectory = selectedFile.isDirectory() ? selectedFile : selectedFile.getParentFile();
			preferenceStorage.put("lastDirectory", lastDirectory.toString()); //NOI18N

			//Test code for printing data
			FileImporter importer = null;

			if (fileChooser.getFileFilter().getDescription().equals(messages.getString("CSV_FILES_(COMMA-SEPARATED)")))
				importer = new CsvImporter(selectedFile);
			else if (fileChooser.getFileFilter().getDescription().equals(messages.getString("XML_FILES")))
				importer = new XmlImporter(selectedFile);
			try {
				if (importer == null)
					throw new VogonImportLogicalException(messages.getString("UNKNOWN_FILE_TYPE"));
				financeData.importData(importer);
				transactionsTableModel.setFinanceData(financeData);
				accountsTableModel.setFinanceData(financeData);
			} catch (org.zlogic.vogon.data.VogonImportLogicalException ex) {
				Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
				JOptionPane.showMessageDialog(this, new MessageFormat(messages.getString("IMPORT_EXCEPTION_DIALOG_TEXT")).format(new Object[]{ex.getLocalizedMessage(), org.zlogic.vogon.data.Utils.getStackTrace(ex)}), messages.getString("IMPORT_EXCEPTION_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
				JOptionPane.showMessageDialog(this, new MessageFormat(messages.getString("IMPORT_EXCEPTION_DIALOG_TEXT")).format(new Object[]{ex.getLocalizedMessage(), org.zlogic.vogon.data.Utils.getStackTrace(ex)}), messages.getString("IMPORT_EXCEPTION_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_jMenuItemImportActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		try {
			transactionEditor.saveChanges();
			DatabaseManager.getInstance().shutdown();
		} catch (Exception ex) {
			Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
		}
    }//GEN-LAST:event_formWindowClosing

    private void jButtonDeleteTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteTransactionActionPerformed
		int selectedRow = jTableTransactions.convertRowIndexToModel(jTableTransactions.getSelectedRow());
		if (selectedRow >= 0) {
			transactionEditor.editTransaction(null);
			transactionsTableModel.deleteTransaction(selectedRow);
		}
    }//GEN-LAST:event_jButtonDeleteTransactionActionPerformed

    private void jMenuItemExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExportActionPerformed
		// Prepare file chooser dialog
		JFileChooser fileChooser = new JFileChooser((lastDirectory != null && lastDirectory.exists()) ? lastDirectory : null);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogTitle(messages.getString("CHOOSE_FILES_TO_EXPORT"));
		//Prepare file chooser filter
		fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(messages.getString("XML_FILES"), "xml"));//NOI18N
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			lastDirectory = selectedFile.isDirectory() ? selectedFile : selectedFile.getParentFile();
			preferenceStorage.put("lastDirectory", lastDirectory.toString()); //NOI18N

			//Test code for printing data
			FileExporter exporter = null;

			if (fileChooser.getFileFilter().getDescription().equals(messages.getString("XML_FILES")))
				exporter = new XmlExporter(selectedFile);
			try {
				financeData.exportData(exporter);
			} catch (VogonExportException ex) {
				Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
				JOptionPane.showMessageDialog(this, new MessageFormat(messages.getString("EXPORT_EXCEPTION_DIALOG_TEXT")).format(new Object[]{ex.getLocalizedMessage(), org.zlogic.vogon.data.Utils.getStackTrace(ex)}), messages.getString("EXPORT_EXCEPTION_DIALOG_TITLE"), JOptionPane.ERROR_MESSAGE);
			}
		}
    }//GEN-LAST:event_jMenuItemExportActionPerformed

    private void jMenuItemRecalculateBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRecalculateBalanceActionPerformed
		for (FinanceAccount account : financeData.getAccounts())
			financeData.refreshAccountBalance(account);
		updateAccounts();
    }//GEN-LAST:event_jMenuItemRecalculateBalanceActionPerformed

    private void jMenuItemCleanupDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCleanupDBActionPerformed
		financeData.cleanup();
		updateAccounts();
		updateTransactions();
    }//GEN-LAST:event_jMenuItemCleanupDBActionPerformed

    private void jComboBoxDefaultCurrencyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxDefaultCurrencyItemStateChanged
		if (evt.getStateChange() == ItemEvent.SELECTED && jComboBoxDefaultCurrency.isEnabled()) {
			CurrencyComboItem selectedItem = (CurrencyComboItem) evt.getItem();
			if (selectedItem != null)
				financeData.setDefaultCurrency(selectedItem.getCurrency());
		}
    }//GEN-LAST:event_jComboBoxDefaultCurrencyItemStateChanged

    private void jButtonAddAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddAccountActionPerformed
		int newAccountIndex = accountsTableModel.addAccount();
		jTableAccounts.setRowSelectionInterval(newAccountIndex, newAccountIndex);
    }//GEN-LAST:event_jButtonAddAccountActionPerformed

    private void jButtonDeleteAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteAccountActionPerformed
		if (jTableAccounts.getSelectedRow() >= 0)
			accountsTableModel.deleteAccount(jTableAccounts.convertRowIndexToModel(jTableAccounts.getSelectedRow()));
    }//GEN-LAST:event_jButtonDeleteAccountActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/*
		 * Set the Nimbus look and feel
		 */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) { //NOI18N
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/*
		 * Configure logging to load config from classpath
		 */
		String loggingFile = System.getProperty("java.util.logging.config.file"); //NOI18N
		if (loggingFile == null || loggingFile.isEmpty()) {
			try {
				java.net.URL url = ClassLoader.getSystemClassLoader().getResource("logging.properties"); //NOI18N
				if (url != null)
					java.util.logging.LogManager.getLogManager().readConfiguration(url.openStream());
			} catch (IOException | SecurityException e) {
			}
		}
		/*
		 * Create and display the form
		 */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainWindow().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.zlogic.vogon.ui.AnalyticsViewer analyticsViewer;
    private javax.swing.JButton jButtonAddAccount;
    private javax.swing.JButton jButtonDeleteAccount;
    private javax.swing.JButton jButtonDeleteTransaction;
    private javax.swing.JComboBox jComboBoxDefaultCurrency;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemCleanupDB;
    private javax.swing.JMenuItem jMenuItemExport;
    private javax.swing.JMenuItem jMenuItemImport;
    private javax.swing.JMenuItem jMenuItemRecalculateBalance;
    private javax.swing.JMenu jMenuTools;
    private javax.swing.JPanel jPanelAccounts;
    private javax.swing.JPanel jPanelAccountsControls;
    private javax.swing.JPanel jPanelAnalytics;
    private javax.swing.JPanel jPanelCurrencies;
    private javax.swing.JPanel jPanelCurrenciesControls;
    private javax.swing.JPanel jPanelTransactionControls;
    private javax.swing.JPanel jPanelTransactions;
    private javax.swing.JPanel jPanelTransactionsList;
    private javax.swing.JScrollPane jScrollPaneAccounts;
    private javax.swing.JScrollPane jScrollPaneCurrencies;
    private javax.swing.JScrollPane jScrollPaneTransactions;
    private javax.swing.JSplitPane jSplitPaneTransactions;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableAccounts;
    private javax.swing.JTable jTableCurrencies;
    private javax.swing.JTable jTableTransactions;
    private org.zlogic.vogon.ui.TransactionEditor transactionEditor;
    // End of variables declaration//GEN-END:variables
	/**
	 * Last opened directory
	 */
	private File lastDirectory = null;
	/**
	 * Easy access to preference storage
	 */
	protected java.util.prefs.Preferences preferenceStorage = java.util.prefs.Preferences.userNodeForPackage(MainWindow.class);
	/**
	 * Finance Data instance
	 */
	protected FinanceData financeData = new FinanceData();
	/**
	 * Transactions table model
	 */
	private TransactionsTableModel transactionsTableModel = new TransactionsTableModel();
	/**
	 * Accounts table model
	 */
	private AccountsTableModel accountsTableModel = new AccountsTableModel();
	/**
	 * Currencies table model
	 */
	private CurrenciesTableModel currenciesTableModel = new CurrenciesTableModel();

	@Override
	public void transactionCreated(FinanceTransaction newTransaction) {
		int newTransactionIndex = transactionsTableModel.getTransactionIndex(newTransaction);
		jTableTransactions.getSelectionModel().setSelectionInterval(newTransactionIndex, newTransactionIndex);
		jTableTransactions.scrollRectToVisible(jTableTransactions.getCellRect(newTransactionIndex, 0, true));
	}

	/**
	 * Forces an update of the accounts table
	 */
	protected void updateAccounts() {
		accountsTableModel.fireTableDataChanged();
	}

	/**
	 * Forces an update of the transactions table
	 */
	protected void updateTransactions() {
		transactionEditor.editTransaction(null);
		transactionsTableModel.fireTableDataChanged();
	}

	/**
	 * Updates the values displayed in the currency combo
	 */
	protected void updateDefaultCurrencyCombo() {
		jComboBoxDefaultCurrency.removeAllItems();
		jComboBoxDefaultCurrency.setEnabled(false);
		for (Object currency : currenciesTableModel.getCurrenciesComboList())
			jComboBoxDefaultCurrency.addItem(currency);
		if (financeData.getDefaultCurrency() != null)
			jComboBoxDefaultCurrency.setSelectedItem(currenciesTableModel.getDefaultCurrency());
		else
			jComboBoxDefaultCurrency.setSelectedItem(-1);
		jComboBoxDefaultCurrency.setEnabled(true);
	}

	@Override
	public void currenciesUpdated() {
		updateDefaultCurrencyCombo();
	}
}
