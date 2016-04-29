package test;

import com.company.Transaction;
import com.company.TransactionList;
import com.company.TransactionsManager;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

public class TransactionsManagerIntTest extends TestCase {
    TransactionsManager transactionsManager = new TransactionsManager();

    public void testCleaningOfFiles() throws Exception {
        String workingDir = System.getProperty("user.dir") + "/src/test/newFiles/";
        String cleanDir = System.getProperty("user.dir") + "/src/test/newFiles/cleanFiles/";
        String importedDir = System.getProperty("user.dir") + "/src/test/newFiles/importedFiles/";

        transactionsManager.cleanFilesIn(workingDir, cleanDir, importedDir);

        List<File> cleanFiles = transactionsManager.getAllFilesIn(cleanDir);
        TransactionList transactionList = transactionsManager.getContentOfFiles(cleanFiles);
        assertEquals(0, transactionList.size());
        transactionsManager.deleteFilesInFolder(cleanFiles, cleanDir);
    }

    public void testmoveAndMergeLists() throws Exception {
        String fromFolder = System.getProperty("user.dir") + "/src/test/newFiles/moveAndMergeIntTest/fromFolder/";
        String toFolder  = System.getProperty("user.dir") + "/src/test/newFiles/moveAndMergeIntTest/toFolder/";

        TransactionList transactionList = new TransactionList();
        Transaction transactionNew = newTransaction();
        transactionList.addToTransactionList(transactionNew);
        transactionsManager.saveTransactionInFiles(transactionList, toFolder);
        transactionsManager.saveTransactionInFiles(transactionList, fromFolder);
        List<File> filesInFromFolder = transactionsManager.getAllFilesIn(fromFolder);
        transactionsManager.moveAndMergeTransactionFiles(fromFolder, toFolder);
        List<File> filesInToFolder = transactionsManager.getAllFilesIn(toFolder);

        TransactionList mergedList = transactionsManager.getContentOfFiles(filesInToFolder);

        transactionsManager.deleteFilesInFolder(filesInFromFolder,fromFolder);
        transactionsManager.deleteFilesInFolder(filesInToFolder,toFolder);

        assertEquals(2, mergedList.size());
    }

    private Transaction newTransaction() {
        Transaction transaction = new Transaction();
        transaction.setDate("D19/10/2015\n");
        transaction.setAmount("T-60.61\n");
        transaction.setN("N1\n");
        transaction.setDescription("PNew Transaction\n");
        return transaction;
    }
}
