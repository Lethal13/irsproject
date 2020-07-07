import com.opencsv.CSVWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.deeplearning4j.models.embeddings.learning.impl.elements.SkipGram;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @Author Ioannis Alexandris p3140304
 */

public class Main
{
    private static final String INDEX_DIR = ("index");

    public static void main(String[] args) throws Exception
    {
        int k = 50;
        int n = 3;
        ArrayList<Text> data = loadFile("data_input/IR2020/documents.txt"); //Stores data
        ArrayList<Text> queries = loadFile("data_input/IR2020/queries.txt"); //Stores queries
        ArrayList<Document> documents = new ArrayList<>(); //Stores 'data' documents
        ArrayList<Document> queriesDoc = new ArrayList<>(); //Stores 'queries' documents
        ArrayList<Document> results = new ArrayList<>(); //Stores the results.
        ArrayList<Float> scores = new ArrayList<>(); //Stores the scores.

        makeCsv("documents.csv", data);

        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR)); //Create directory

        String filePath = "/documents.csv";

        Analyzer defaultAnalyzer = new EnglishAnalyzer(); //Create Analyzer
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(defaultAnalyzer); //Create index writer configuration
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig); //Create indexWriter

        //Traverse through all texts. Each text contains a file.
        for(Text text: data)
        {
            documents.add(createDocument(text));
        }


        System.out.println("Index writer phasing is starting.");
        //Traverse through each 'data' document and write it to the directory.
        for(Document doc: documents)
        {
            if(indexWriter.getConfig().getOpenMode()== IndexWriterConfig.OpenMode.CREATE)
                indexWriter.addDocument(doc);
        }
        System.out.println("Index writer phase just ended.");
        indexWriter.close(); //close the indexWriter since we will not need it again.


        IndexReader reader = DirectoryReader.open(dir);
        FieldValuesSentenceIterator iterator = new FieldValuesSentenceIterator(reader,"text");

        // note that in practice using a csv as plain text input is not a good idea
        //SentenceIterator iter = new BasicLineIterator("documents.csv");
        SkipGram MLP = new SkipGram();
        Word2Vec vec = new Word2Vec.Builder()
                .layerSize(60)
                .windowSize(5)
                .epochs(5)
                .elementsLearningAlgorithm(MLP)
                .tokenizerFactory(new DefaultTokenizerFactory())
                .iterate(iterator)
                .build();

        vec.fit();




        Collection<String> queries1 = Arrays.asList(new String[]{"multimodal", "travel", "services"});
        Collection<String> queries2 = Arrays.asList(new String[]{"Big", "Data", "Mobility"});
        Collection<String> queries3 = Arrays.asList(new String[]{"European", "logistics", "applications"});
        Collection<String> queries4 = Arrays.asList(new String[]{"Architectures", "Big", "Data", "Analytics"});
        Collection<String> queries5 = Arrays.asList(new String[]{"Architecture", "Industrial", "IoT"});
        Collection<String> queries6 = Arrays.asList(new String[]{"Mobility-as-a-Service", "tools"});
        Collection<String> queries7 = Arrays.asList(new String[]{"fragmentation", "IoT", "through", "federation"});
        Collection<String> queries8 = Arrays.asList(new String[]{"Seamless", "Efficient", "European", "Travelling"});
        Collection<String> queries9 = Arrays.asList(new String[]{"cross-domain", "orchestration", "services"});
        Collection<String> queries10 = Arrays.asList(new String[]{"Community", "networks"});
        ArrayList<String> newQueries = new ArrayList<>();

        String newQuery = queries1.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries1)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query1: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries2.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");


        for(String word: queries2)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query2: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries3.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");


        for(String word: queries3)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query3: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries4.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");


        for(String word: queries4)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query4: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries5.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries5)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query5: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries6.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries6)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query6: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries7.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries7)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query7: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries8.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries8)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query8: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries9.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries9)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query9: " + search);
        }

        newQueries.add(newQuery);
        newQuery = queries10.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");

        for(String word: queries10)
        {
            Collection<String> search = vec.wordsNearestSum(word, n);
            newQuery += search.toString().replace(",", " ").replace("[", " ").replace("]", " ").replaceAll("\"", "'").replace(":", " ");
            System.out.println("Query10: " + search);
        }

        newQueries.add(newQuery);
        newQuery = new String();

        ArrayList<Document> queries_V2 = new ArrayList<>();
        int y = 0;

        for(String string : newQueries)
        {
            Text local_text = new Text(y,string);
            Document doc_query = createDocument(local_text);
            queries_V2.add(doc_query);
            System.out.println(string);
            ++y;
        }

        //Traverse through all queries.
        for(Text text: queries)
        {
            queriesDoc.add(createDocument(text));
        }

        IndexSearcher searcher = createSearcher(dir);
        for(Document doc: queries_V2)
        {
            TopDocs docs = searchByText(doc.get("text"), searcher, k);
            ScoreDoc[] hits = docs.scoreDocs;

            for(ScoreDoc hit :hits)
            {
                Document hitDoc = searcher.doc(hit.doc);
                results.add(hitDoc);
                System.out.println("\tScore " + hit.score + "\tid=" + hitDoc.get("id"));
                scores.add(hit.score);
            }

            System.out.println("-------------------------------------------------------");

        }


        createFile("output"+ k +".txt", results, scores, k);


        reader.close();
        dir.close();
    }


    private static void makeCsv(String path, ArrayList<Text> queries)
    {
        File file = new File(path);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"text"};
            writer.writeNext(header);

            // add data to csv

            for(Text text: queries)
            {
                String[] data1 = {text.getText().replaceAll("\"", "'")};
                writer.writeNext(data1);
            }

            // closing writer connection
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("Csv file completed successfully.");
    }

    /**
     * Transforms a Text object to a Document object.
     * @param text The object that we want to tranform it into a Document.
     * @return Returns an Document object.
     */
    private static Document createDocument(Text text)
    {
        Document document = new Document();
        document.add(new StringField("id", String.valueOf(text.getId()), Field.Store.YES));
        document.add(new TextField("text", text.getText(), Field.Store.YES));
        return document;
    }

    /**
     * Creates a searcher.
     * @param dir The directory of the files.
     * @return An IndexSearcher.
     * @throws IOException If the directory does not exist.
     */
    private static IndexSearcher createSearcher(Directory dir) throws IOException
    {

        IndexReader reader = DirectoryReader.open(dir);
        //IndexSearcher searcher = new IndexSearcher(reader);
        return new IndexSearcher(reader);
    }

    /**
     * Method that searches through the directory and gets k documents.
     * @param text The string that our search will be based on.
     * @param searcher An object IndexSearcher.
     * @param k The amount of documents that we want to retrieve.
     * @return k TopDocs
     * @throws Exception
     */
    private static TopDocs searchByText(String text, IndexSearcher searcher, int k) throws Exception
    {
        EnglishAnalyzer englishAnalyzer = new EnglishAnalyzer();
        QueryParser qp = new QueryParser("text", englishAnalyzer);
        Query textQuery = qp.parse(text);
        //TopDocs hits = searcher.search(textQuery, k);
        return searcher.search(textQuery, k);
    }


    /**
     * Reads the input, i.e. data,queries
     * @param data The input that we want to read.
     * @return An ArrayList<Text></Text\> object that each index has a different Text.
     */
    private static ArrayList<Text> loadFile(String data)
    {
        System.out.println("Opening the file in order to store the data.");
        int counter = 0;
        File f = null;
        BufferedReader reader = null;
        String line = null;
        ArrayList<Text> text = new ArrayList<>();

        try
        {
            f = new File(data);

        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            //System.err.println("File not found");
        }

        try
        {
            reader = new BufferedReader( new FileReader(f));
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            //System.err.println("ERROR: File couldn't be opened.");
        }

        try
        {

            line = "start";
            System.out.println("Reading the file is beginning now!");
            while(line!=null)
            {
                Text file = new Text();
                String input = "";
                line = reader.readLine();
                //System.out.println(line);
                if(line==null) break;
                while ( !line.trim().equals("///") || line.isEmpty() )
                {
                    if(counter  ==0)
                    {
                        file.setId(Integer.parseInt(line));
                        ++counter;
                    }
                    else
                    {
                        input += " " +line;
                    }

                    line = reader.readLine();

                    if(line==null) break;
                }

                file.setText(input);
                text.add(file);
                counter = 0;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //System.out.println("Error at reading line " + counter );
        }

        System.out.println("The file was properly read and stored.");
        try
        {
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //System.out.println("Error at closing file.");
        }

        return text;
    } //End of function loadFile


    /**
     * Writes a file with the results from the queries, in an appropriate format,
     * so the tool 'trec_eval' can handle it, without the need of extra modification.
     * @param path Output's path.
     * @param results data
     * @param scores data
     * @param k the number of the documents. It is a constant.
     */
    private static void createFile(String path, ArrayList<Document> results, ArrayList<Float> scores, int k)
    {
        System.out.println("The createFile method has just started.");
        File f = null;
        BufferedWriter writer = null;
        int counter=0;

        try
        {
            f = new File(path);
        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }

        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        }
        catch(FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        }

        for(Document doc: results)
        {
            if(counter<k)
            {
                try
                {
                    writer.write("Q01 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*2)
            {
                try
                {
                    writer.write("Q02 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*3)
            {
                try
                {
                    writer.write("Q03 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*4)
            {
                try
                {
                    writer.write("Q04 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*5)
            {
                try
                {
                    writer.write("Q05 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*6)
            {
                try
                {
                    writer.write("Q06 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*7)
            {
                try
                {
                    writer.write("Q07 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*8)
            {
                try
                {
                    writer.write("Q08 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(counter<k*9)
            {
                try
                {
                    writer.write("Q09 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    writer.write("Q10 Q0 " + doc.get("id") + " 0 " + scores.get(counter) + " myMethod" + "\n");
                    ++counter;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        try
        {
            writer.close();
        }
        catch ( IOException e )
        {
            System.err.println( "Error closing file." );
        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }

        System.out.println("File " + path + " was successfully created.");
    }
}
