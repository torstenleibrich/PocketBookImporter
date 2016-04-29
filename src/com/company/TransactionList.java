package com.company;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tleibri on 24/10/15.
 */
public class TransactionList {
    private List<Transaction> transactionList = new CopyOnWriteArrayList<Transaction>();
    private String header = "!Type:Bank\n";
    private String objectDelimiter = "^";

    public String getObjectDelimiter() {
        return objectDelimiter;
    }

    public void setObjectDelimiter(String objectDelimiter) {
        this.objectDelimiter = objectDelimiter;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public Transaction getTransactionInList(Integer index) {
        return transactionList.get(index);
    }

    public void addToTransactionList(Transaction transaction) {
        transactionList.add(transaction);
    }

    public String getTransactionListAsString() {
        String transactionListAsString = "";
        transactionListAsString += getHeader();
        for (Transaction transaction : transactionList) {
            transactionListAsString += transaction.getDate() + "\n";
            transactionListAsString += transaction.getAmount() + "\n";
            transactionListAsString += transaction.getN() + "\n";
            transactionListAsString += transaction.getDescription() + "\n";
            transactionListAsString += getObjectDelimiter() + "\n";
        }
        return transactionListAsString;
    }

    public void remove(Transaction transaction) {
        transactionList.remove(transaction);
    }

    public int size() {
        return transactionList.size();
    }

    public TransactionList removeTransactionsThatAlreadyExistIn(TransactionList transactionListToBeRemoved) {
        for (Transaction transaction : this.transactionList) {
            int i = 0;
            int size = transactionListToBeRemoved.size();
            while (i < size) {
                Transaction transactionToBeRemoved = transactionListToBeRemoved.get(i);
                if (transaction.getDate().equals(transactionToBeRemoved.getDate()) && transaction.getDescription().equals(transactionToBeRemoved.getDescription()) && transaction.getAmount().equals(transactionToBeRemoved.getAmount())) {
                    this.remove(transaction);
                }
                i++;
            }
        }
        return this;
    }

    public Transaction get(int i) {
        return transactionList.get(i);
    }

    public TransactionList removeTransactionsOlderThan(TransactionList transactionList, Integer days) {
        TransactionList newTransactionList = new TransactionList();

        for (Transaction transaction : this.transactionList) {
            if (transactionBeforeXDaysFromNow(transaction, days)) {
            } else {
                transactionList.remove(transaction);
            }
        }
        return newTransactionList;
    }

    private Boolean transactionBeforeXDaysFromNow(Transaction transaction, Integer days) {
        if (transaction.getDate() == null) {
            System.out.println("Transaction not having date. Manual fix up in imported file required" + transaction.getDescription());
            return true; //workaround for date not being in new line
        }
        DateTime tranactionDate = DateTime.parse(transaction.getDate().substring(1, 11), DateTimeFormat.forPattern("dd/MM/yyyy"));
        DateTime borderLineDate = DateTime.now().minusDays(days);
        boolean after = tranactionDate.isAfter(borderLineDate);
        return after;
    }
}
