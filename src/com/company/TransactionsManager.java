package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionsManager {
    public TransactionList removeAuthorisationOnlyTransactionsFrom(TransactionList transactionList) {
        TransactionList cleanTransactionList = new TransactionList();
        List<Transaction> transactionListIterator = transactionList.getTransactionList();

        for (Transaction transaction : transactionListIterator) {
            if (!transaction.getDescription().contains("AUTHORISATION ONLY")) {
                cleanTransactionList.addToTransactionList(transaction);
            }
        }
        return cleanTransactionList;
    }

    public TransactionList mergeLists(TransactionList list1, TransactionList list2) {
        List<Transaction> list1Iterator = list1.getTransactionList();

        for (Transaction transaction : list1Iterator) {
            list2.addToTransactionList(transaction);
        }
        return list2;
    }

    public void deleteFilesWithNameInFolder(List<String> filesToBeDeleted, String filePath) {
        for (String fileName : filesToBeDeleted) {
            File fileToBeDeleted = new File(filePath + fileName);
            fileToBeDeleted.delete();
        }
    }

    public void deleteFilesInFolder(List<File> filesToBeDeleted, String filePath) {
        deleteFilesWithNameInFolder(getFileNamesOf(filesToBeDeleted), filePath);
    }

    public List<File> getAllFilesIn(String filePath) {
        final File folder = new File(filePath);
        return listFilesForFolder(folder);
    }

    public TransactionList getContentOfFiles(List<File> files) {
        TransactionList transactionList = new TransactionList();

        for (File file : files) {
            try {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String line;
                Transaction transaction = new Transaction();
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("D")) transaction.setDate(line);
                    if (line.startsWith("T")) transaction.setAmount(line);
                    if (line.startsWith("N")) transaction.setN(line);
                    if (line.startsWith("P")) transaction.setDescription(line);
                    if (line.startsWith("^")) {
                        transactionList.addToTransactionList(transaction);
                        transaction = new Transaction();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return transactionList;
    }

    public List<String> getFileNamesOf(List<File> files) {
        List<String> fileNames = new ArrayList<String>();
        for (File file : files) {
            fileNames.add(file.getName());
        }
        return fileNames;
    }

    public TransactionList filterOutAlreadyImportedTransactions(TransactionList importedTransaction, TransactionList newTransactions) {
        TransactionList filteredTransactionList = newTransactions.removeTransactionsThatAlreadyExistIn(importedTransaction);
        return filteredTransactionList;
    }

    public void cleanFilesIn(String workingDirectory, String cleanTransactionsFolder, String importedTransactionsFolder) {
        List<File> newFiles = getAllFilesIn(workingDirectory);
        TransactionList newTransactionList = getContentOfFiles(newFiles);
        Integer numberOfDownloadedTransactions = newTransactionList.size();
        System.out.println(numberOfDownloadedTransactions + "transactions were downloaded.");

        TransactionList onlyBookedTransactionList = removeAuthorisationOnlyTransactionsFrom(newTransactionList);
        Integer numberOfAuthorisedOnly = numberOfDownloadedTransactions - onlyBookedTransactionList.size();
        System.out.println(numberOfAuthorisedOnly + " authorisation-only transactions were removed.");

        List<File> importedFiles = getAllFilesIn(importedTransactionsFolder);
        TransactionList importedTransactionList = getContentOfFiles(importedFiles);

        TransactionList onlyNewTransactionList = filterOutAlreadyImportedTransactions(importedTransactionList, onlyBookedTransactionList);
        System.out.println("There are " + onlyNewTransactionList.size() + " new transactions.");

        saveTransactionInFiles(onlyNewTransactionList, cleanTransactionsFolder);
    }

    public List<String> saveStringsInFiles(List<String> stringsToBeSaved, String destinedPath) {
        List<String> filesSaved = new ArrayList<String>();
        Integer iterator = 0;
        for (String stringToBeSaved : stringsToBeSaved) {
            String counterForUniqueFileName = String.valueOf(iterator);
            String actualFileName = saveToFile(stringToBeSaved, destinedPath, counterForUniqueFileName);
            filesSaved.add(actualFileName);
            iterator++;
        }
        return filesSaved;
    }

    public List<String> saveTransactionInFiles(TransactionList transactionList, String folder) {
        List<String> stringList = new ArrayList<String>();
        String transactionListAsString = transactionList.getTransactionListAsString();
        stringList.add(transactionListAsString);
        List<String> filesSaved = saveStringsInFiles(stringList, folder);
        return filesSaved;
    }

    private String saveToFile(String stringToBeSaved, String filePath, String fileName) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filePath + fileName + ".qif", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.print(stringToBeSaved);
        writer.close();
        return fileName + ".qif";
    }

    private List<File> listFilesForFolder(final File folder) {
        List<File> filesFound = new ArrayList<File>();

        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (!fileEntry.getName().equals(".DS_Store"))
                    filesFound.add(fileEntry);
            } else {
                //skip directories and DS_Store
            }
        }
        return filesFound;
    }

    public TransactionList removeOutdatedTransactionsFrom(TransactionList transactionList, Integer durationInDays) {
        int before = transactionList.size();
        transactionList.removeTransactionsOlderThan(transactionList, durationInDays);
        int after = transactionList.size();
        Integer olderThan = before - after;
        System.out.println("There were " + olderThan + "transactions older than " + durationInDays + ".");
        return transactionList;
    }

    public void moveAndMergeTransactionFiles(String fromFolder, String toFolder) {
        List<File> filesToBeMoved = getAllFilesIn(fromFolder);
        TransactionList transactionsToBeMoved = getContentOfFiles(filesToBeMoved);
        int toBeMoved = transactionsToBeMoved.size();
        System.out.println("There are " + toBeMoved + " transactions to be stored as imported.");

        List<File> filesToKeep = getAllFilesIn(toFolder);
        TransactionList transactionsToKeep = getContentOfFiles(filesToKeep);

        TransactionList mergedList = mergeLists(transactionsToBeMoved, transactionsToKeep);
        int merged = mergedList.size();
        System.out.println("There are " + merged + " transactions stored as imported.");

        saveTransactionInFiles(mergedList, toFolder);
    }
}