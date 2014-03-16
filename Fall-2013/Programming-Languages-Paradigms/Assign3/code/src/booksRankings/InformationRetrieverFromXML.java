package booksRankings;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class InformationRetrieverFromXML {
    public static void main(String[] args){
        try{
            List<String> finalisbnList = getDataFromFile("isbn.txt");
            timeAndPrint("Sequential", (xmlGenerator) -> getTitleRankForIsbnValuesSequential(xmlGenerator, finalisbnList));
            timeAndPrint("Concurent", (xmlGenerator) -> getTitleRankForIsbnValuesConcurrent(xmlGenerator, finalisbnList));
        }catch (Exception e){
            printMessage("Error in the request.");
        }
    }

    public static void timeAndPrint(String runType, Function<XmlGenerator, TreeMap<Integer, List<String>>> function) {

        System.out.println("\n" + runType + " process: ");
        XmlGenerator xmlGenerator = new XmlGenerator();
        setParametersForAmazonRequest(xmlGenerator);

        long startTime = System.nanoTime();

        TreeMap<Integer, List<String>> compiledData = function.apply(xmlGenerator);

        long endTime = System.nanoTime();

        System.out.println("\n The " + runType + " Process took " + (endTime - startTime)/1.0e9 + " seconds.\n\n");
        printElementsFromTheDoc(compiledData);
    }

    public static void setParametersForAmazonRequest(XmlGenerator xmlGenerator){
        xmlGenerator.setAccessID("AKIAINAKPWYBS6VNHB5A");
        xmlGenerator.setSecretAccessKey("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");
        xmlGenerator.setEndPoint("webservices.amazon.com");
    }

    private static TreeMap<Integer, List<String>> getTitleRankForIsbnValuesSequential(XmlGenerator xmlGenerator,
                                                                                      List<String> finalContentsFromInputFile){
        TreeMap<Integer, List<String>> indivdualElements = new TreeMap<>();
        for(String isbn : finalContentsFromInputFile){
            try{
                Entry<Integer, List<String>> entry =
                        parseXMLdocument(xmlGenerator.getSignedUrl(isbn), isbn);
                indivdualElements.put(entry.getKey(), entry.getValue());
            }catch (Exception e){
                indivdualElements.put(2,new ArrayList<String>(){{add("-NA-"); add("Error: couldn't find Title rank and Title in Sequential.");}});
            }
        }
        return indivdualElements;
    }

    private static TreeMap<Integer, List<String>> getTitleRankForIsbnValuesConcurrent(XmlGenerator xmlGenerator,
                                                                                      List<String> finalContentsFromInputFile) {

        ExecutorService executorPool= Executors.newCachedThreadPool();

        TreeMap<Integer, List<String>> indivdualElements = new TreeMap<>();

        executorPool.execute(new Runnable() {
            public void run() {
                for (String isbnNo : finalContentsFromInputFile) {
                    try {
                        Entry<Integer, List<String>> entry = parseXMLdocument(xmlGenerator.getSignedUrl(isbnNo), isbnNo);
                        synchronized (indivdualElements) {
                            indivdualElements.put(entry.getKey(), entry.getValue());
                        }
                    } catch (Exception e) {
                        indivdualElements.put(1,new ArrayList<String>(){{add("-NA-"); add("Error: couldn't find Title rank and Title in Concurrent.");}});
                    }
                }
            }
        });
        executorPool.shutdown();
        try {
            executorPool.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return indivdualElements;
        }

        return indivdualElements;
    }

    public static List<String> getDataFromFile(String fileName){
        try{
            File sourceFile = new File(fileName);
            List<String> isbnList = new ArrayList<>();

            FileInputStream fileStream = new FileInputStream(sourceFile.getAbsolutePath());

            DataInputStream grabDataFromInput = new DataInputStream(fileStream);
            BufferedReader bufferForData = new BufferedReader(new InputStreamReader(grabDataFromInput));
            String strLine;

            while((strLine = bufferForData.readLine()) != null)   {
                isbnList.add(strLine);
            }
            bufferForData.close();
            return isbnList;
        } catch(Exception e){
            return new ArrayList<String>(){{add("-NA-"); add("Error: couldn't find File.");}};
        }
    }

    public static Entry<Integer, List<String>> parseXMLdocument(String url, String isbn) {
        List<String> outputFields = new ArrayList<>();
        Entry<Integer, List<String>> entry;

        outputFields.add(isbn);
        try{
            DocumentBuilderFactory objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();
            Document doc = objDocumentBuilder.parse(new URL(url).openConnection().getInputStream());

            NodeList descNodes = doc.getElementsByTagName("SalesRank");
            NodeList titleNodes = doc.getElementsByTagName("Title");

            outputFields.add(titleNodes.item(0).getTextContent());
            entry = new SimpleEntry<>(Integer.parseInt(descNodes.item(0).getTextContent()), outputFields);
        }catch(Exception e){
            outputFields.add("The requested book doesn't exist");
            entry = new SimpleEntry<>(0, outputFields);
        }
        return entry;
    }

    public static void printMessage(String errorMsg){
        System.out.println("\n" + errorMsg);
    }

    private static void printElementsFromTheDoc(Map<Integer, List<String>> elements){
        System.out.println(" ISBN        Ratings           Title");
        elements.forEach((k,v)-> System.out.println(v.get(0)+  "    " + k + " \t\t"+ v.get(1)));
    }
}