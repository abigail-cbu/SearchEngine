package Classes;

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
import org.apache.lucene.store.MMapDirectory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

public class Ranking
{
    private ArrayList<Sites> sites;

    public static void main(String args[])
    {
        try {
            DBReader db = new DBReader();
            db.query("%engineering%");
            Ranking ranker = new Ranking(db.getAllResults());
            ranker.search();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Ranking(ArrayList<Sites> sites)
    {
        this.sites = sites;
    }

    public void search()
    {
        try {
            StandardAnalyzer sa = new StandardAnalyzer();
            Directory ind = new MMapDirectory(FileSystems.getDefault().getPath(".", "test.log"));
            IndexWriterConfig config = new IndexWriterConfig(sa);
            IndexWriter w = new IndexWriter(ind, config);
            for(int i = 0; i < sites.size(); i++) {
                addDoc(w, sites.get(i).getLinkId(), sites.get(i).getSiteName(), sites.get(i).getURL(), sites.get(i).getSourceCode());
            }
            w.close();

            String querystr = "site_name:a*";
            Query query = new QueryParser("site_name", sa).parse(querystr);
            IndexReader rd = DirectoryReader.open(ind);
            IndexSearcher schr = new IndexSearcher(rd);
            TopDocs docs = schr.search(query, 10);
            ScoreDoc page_hits[] = docs.scoreDocs;
            System.out.println(page_hits.length);
            for(int i = 0; i < page_hits.length; i++) {
                int did = page_hits[i].doc;
                Document d = schr.doc(did);
                System.out.println(d.get("site_name"));
                System.out.println(d.get("link_id"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDoc(IndexWriter w, int link_id, String site_name, String url, String source_code) throws IOException
    {
        Document doc = new Document();
        doc.add(new TextField("site_name", site_name, Field.Store.YES));
        doc.add(new StringField("url", url, Field.Store.YES));
        doc.add(new StringField("source_code", source_code, Field.Store.YES));
        doc.add(new StringField("link_id", "" + link_id, Field.Store.YES));
        w.addDocument(doc);

    }
}
