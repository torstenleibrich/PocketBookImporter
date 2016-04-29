package test;

import com.company.Transaction;
import com.company.TransactionList;
import com.company.TransactionsManager;
import junit.framework.TestCase;
import org.joda.time.LocalDateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionsManagerTest extends TestCase {

    private TransactionsManager transactionsManager = new TransactionsManager();
    private String testFolder = System.getProperty("user.dir") + "/src/test/testFolder/";
    private List<String> stringsToBeSaved;
    private String stringOne = "1";
    private String stringTwo = "2";

    public void testReadAllFilesIn() throws Exception {
        stringsToBeSaved = Arrays.asList(stringOne, stringTwo);

        transactionsManager.saveStringsInFiles(stringsToBeSaved, testFolder);

        List<File> files = transactionsManager.getAllFilesIn(testFolder);
        List<String> filenames = transactionsManager.getFileNamesOf(files);
        assertEquals("0.qif", filenames.get(0));
        assertEquals("1.qif", filenames.get(1));

        transactionsManager.deleteFilesWithNameInFolder(filenames, testFolder);
    }

    public void testDeleteSpecificFilesIn() throws Exception {
        stringsToBeSaved = Arrays.asList(stringOne, stringTwo);

        List<String> savedFiles = transactionsManager.saveStringsInFiles(stringsToBeSaved, testFolder);

        transactionsManager.deleteFilesWithNameInFolder(savedFiles, testFolder);

        List<File> fileContents = transactionsManager.getAllFilesIn(testFolder);
        assertEquals(0, fileContents.size());
    }

    public void testAuthorisationOnlyAreRemoved() {
        TransactionList defaultTransactionList = getTransactionListWith1AuthenticationOnlyAnd1BookedOne();

        TransactionList transactions = transactionsManager.removeAuthorisationOnlyTransactionsFrom(defaultTransactionList);
        assertEquals(1, transactions.size());
    }

    public void testSaveMultipleFiles() throws Exception {
        stringsToBeSaved = Arrays.asList(stringOne, stringTwo);

        transactionsManager.saveStringsInFiles(stringsToBeSaved, testFolder);

        List<File> files = transactionsManager.getAllFilesIn(testFolder);
        assertEquals(2, files.size());
        transactionsManager.deleteFilesInFolder(files, testFolder);
    }

    public void testSaveMultipleFilesAsTransaction() {
        TransactionList defaultTransactionList = getDefaultTransactionList();

        transactionsManager.saveTransactionInFiles(defaultTransactionList, testFolder);
        List<File> files = transactionsManager.getAllFilesIn(testFolder);
        TransactionList content = transactionsManager.getContentOfFiles(files);

        assertEquals("PVIRGIN MOBILE H/O        MACQUARIE PRKNS", content.getTransactionInList(0).getDescription());
        transactionsManager.deleteFilesInFolder(files, testFolder);
    }

    public void testSaveStringInFiles() throws Exception {
        TransactionList defaultTransactionList = getDefaultTransactionList();

        List<String> listToBeSaved = new ArrayList<String>();
        String transactionListAsString = defaultTransactionList.getTransactionListAsString();
        listToBeSaved.add(transactionListAsString);

        transactionsManager.saveStringsInFiles(listToBeSaved, testFolder);
        List<File> files = transactionsManager.getAllFilesIn(testFolder);
        TransactionList content = transactionsManager.getContentOfFiles(files);

        assertEquals("PVIRGIN MOBILE H/O        MACQUARIE PRKNS", content.getTransactionInList(0).getDescription());
        transactionsManager.deleteFilesInFolder(files, testFolder);
    }

    public void testAddListToExistingList() throws Exception {
        TransactionList transactionList1 = getDefaultTransactionListWith2Entries();
        TransactionList transactionList2 = getDefaultTransactionListWith2Entries();

        TransactionList mergedLists = transactionsManager.mergeLists(transactionList1, transactionList2);

        assertEquals(4, mergedLists.size());
    }

    public void testEmptyListProperlyWhenListIsEmpty() throws Exception {
        String emptyFileScenarioFolder = System.getProperty("user.dir") + "/src/test/emptyFileScenario";
        String importedFolder = System.getProperty("user.dir") + "/src/test/importedFolder";

        TransactionList expectedTransactionList = transactionsManager.getContentOfFiles(transactionsManager.getAllFilesIn(importedFolder));
        transactionsManager.moveAndMergeTransactionFiles(emptyFileScenarioFolder, importedFolder);
        TransactionList actualTransactionList = transactionsManager.getContentOfFiles(transactionsManager.getAllFilesIn(importedFolder));

        assertEquals(expectedTransactionList.getTransactionListAsString(), actualTransactionList.getTransactionListAsString());
    }

    public void testEmptyListProperlyWhenListIsEmptyForRemovingTransactions() throws Exception {
        String emptyFileScenarioFolder = System.getProperty("user.dir") + "/src/test/emptyFileScenario";

        TransactionList emptyList = transactionsManager.getContentOfFiles(transactionsManager.getAllFilesIn(emptyFileScenarioFolder));
        TransactionList transactionList = transactionsManager.removeOutdatedTransactionsFrom(emptyList, 90);

        assertEquals(0, transactionList.size());
    }

    public void testRemovingFilesOlderThan90DaysAgo() throws Exception {
        LocalDateTime todaysDate = new LocalDateTime();
        String year = String.valueOf(todaysDate.getYear());
        String dayOfMonth = String.valueOf(todaysDate.getDayOfMonth());
        dayOfMonth = addPaddingZero(todaysDate.getDayOfMonth(), dayOfMonth);
        String monthOfYear = String.valueOf(todaysDate.getMonthOfYear());
        monthOfYear = addPaddingZero(todaysDate.getMonthOfYear(), monthOfYear);

        Transaction transactionStillIncluded = new Transaction();
        transactionStillIncluded.setDate("D" + dayOfMonth + "/" + monthOfYear + "/" + year + "\n");
        transactionStillIncluded.setAmount("T-4.29\n");
        transactionStillIncluded.setN("N1\n");
        transactionStillIncluded.setDescription("Still Included Transaction\n");

        Transaction tooOldTransaction = new Transaction();
        int oneYearBeforeNow = todaysDate.getYear()-1;
        tooOldTransaction.setDate("D" + dayOfMonth + "/" + monthOfYear + "/" +  oneYearBeforeNow + "\n");
        tooOldTransaction.setAmount("T-4.29\n");
        tooOldTransaction.setN("N1\n");
        tooOldTransaction.setDescription("Old Transaction\n");

        TransactionList transactionList = getTransactionList();
        transactionList.addToTransactionList(transactionStillIncluded);
        transactionList.addToTransactionList(tooOldTransaction);

        Integer durationInDays = 90;
        TransactionList removeOutdatedTransactionsList = transactionsManager.removeOutdatedTransactionsFrom(transactionList, durationInDays);

        assertEquals(1, removeOutdatedTransactionsList.size());
        assertEquals(transactionList.get(0).getDescription(), removeOutdatedTransactionsList.get(0).getDescription());
    }

    private String addPaddingZero(Integer integer, String monthOrDay) {
        if (integer < 10) {
            monthOrDay = "0" + monthOrDay;
        }
        return monthOrDay;
    }

    public void testFilterOutAlreadyImportedTransactions() throws Exception {
        TransactionList importedList = getDefaultTransactionList();
        TransactionList newList = getDefaultTransactionListWith2Entries();

        TransactionList results = transactionsManager.filterOutAlreadyImportedTransactions(importedList, newList);
        assertEquals(1, results.size());
    }

    private TransactionList getDefaultTransactionList() {
        TransactionList newTransactionList = getTransactionList();
        newTransactionList.addToTransactionList(getDefaultTransaction());
        return newTransactionList;
    }

    private TransactionList getDefaultTransactionListWith2Entries() {
        TransactionList newTransactionList = getTransactionList();
        newTransactionList.addToTransactionList(getDefaultTransaction());
        newTransactionList.addToTransactionList(getDefaultTransaction2());
        return newTransactionList;
    }

    private TransactionList getTransactionListWith1AuthenticationOnlyAnd1BookedOne() {
        TransactionList newTransactionList = getTransactionList();
        newTransactionList.addToTransactionList(getAuthenticationOnlyTransaction());
        newTransactionList.addToTransactionList(getDefaultTransaction());
        return newTransactionList;
    }

    private TransactionList getTransactionList() {
        TransactionList newTransactionList = new TransactionList();
        newTransactionList.setHeader("!Type:Bank\n");
        newTransactionList.setObjectDelimiter("^\n");
        return newTransactionList;
    }

    private Transaction getDefaultTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDate("D19/10/2015\n");
        transaction.setAmount("T-4.29\n");
        transaction.setN("N1\n");
        transaction.setDescription("PVIRGIN MOBILE H/O        MACQUARIE PRKNS\n");
        return transaction;
    }

    private Transaction getDefaultTransaction2() {
        Transaction transaction = new Transaction();
        transaction.setDate("D20/10/2015\n");
        transaction.setAmount("T-5.29\n");
        transaction.setN("N1\n");
        transaction.setDescription("PVIRGIN MOBILE H/O     2   MACQUARIE PRKNS\n");
        return transaction;
    }


    private Transaction getAuthenticationOnlyTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDate("D19/10/2015\n");
        transaction.setAmount("T-60.61\n");
        transaction.setN("N1\n");
        transaction.setDescription("PAUTHORISATION ONLY - WOOLWORTHS W2696      S  PRING HILL    AU\n");
        return transaction;
    }
}