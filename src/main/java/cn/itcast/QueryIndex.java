package cn.itcast;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;

//Lucene查询
public class QueryIndex {
    @Test
    public void test1() throws Exception {
        /**
         * - 创建索引搜索工具
         - 指定索引目录
         - 创建读取流工具
         - 创建搜索工具
         - 创建查询条件
         - 创建查询解析器
         - 解析用户搜索语句，得到查询条件对象
         */
        //创建分词器
        QueryParser queryParser = new QueryParser("title",new StandardAnalyzer());
        Query query=queryParser.parse("谷歌地图之父");
        search(query);
    }
    //词条查询
    //精确查询
    @Test
    public void TermQueryTest() throws IOException {
       Query query= new TermQuery(new Term("title","加盟"));
       search(query);
    }
    //通配符查询
    @Test
    public void testWildCardQuery() throws IOException {
        WildcardQuery query = new WildcardQuery(new Term("title", "?之父"));
        search(query);
    }
    //模糊查询,只能匹配少于两个字母的错误
    @Test
    public void fuzzyQueryTest() throws IOException {
        FuzzyQuery query = new FuzzyQuery(new Term("title", "feaceBook"));
        search(query);
    }
    //范围查询
    @Test
    public void numberRangQuery() throws IOException {
        Query query = NumericRangeQuery.newIntRange("id",1,3,false,true);
        search(query);
    }
    //布尔查询
    @Test
    public void booleanQuery(){

    }



    private void search(Query query) throws IOException {
        //需要索引库
        FSDirectory directory = FSDirectory.open(new File("E:\\soft\\Lucene_demo\\indexLucene"));
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建索引对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //需要查询索引器
        TopDocs topDocs=indexSearcher.search(query,10);
        //获取查询的文档集合
        ScoreDoc[] scoreDocs=topDocs.scoreDocs;
        System.out.println("查询的总条数"+topDocs.totalHits);
        //读出文件、进行迭代展示
        for (ScoreDoc scoreDoc : scoreDocs) {
            //读取文件内容
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println("文档id"+document.get("id"));
            System.out.println("文档title"+document.get("title"));
        }
    }
}
