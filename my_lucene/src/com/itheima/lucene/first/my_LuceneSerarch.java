package com.itheima.lucene.first;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class my_LuceneSerarch {
	private void searchResult(Query query) throws Exception {
		// 1）创建一个IndexReader对象
		IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File("D:/test/index")));
		// 2）创建一个IndexSearcher对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 4）执行查询，得到TopDocs对象
		TopDocs topDocs = indexSearcher.search(query, 10);
		System.out.println("查询结果的总记录数：" + topDocs.totalHits);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		// 4）遍历文档列表
		for (ScoreDoc scoreDoc : scoreDocs) {
			int doc = scoreDoc.doc;
			// 5）根据id取文档对象
			Document document = indexSearcher.doc(doc);
			// 6）从文档对象中取Field
			System.out.println(document.get("name"));
			System.out.println(document.get("content"));
			System.out.println(document.get("path"));
			System.out.println(document.get("size"));
		}
		// 7）关闭IndexReader对象
		indexReader.close();
	}
	@Test
	public void testTermQuery() throws Exception{
		Query query = new TermQuery(new Term("content","apache"));
		System.out.println(query);
		searchResult(query);
	}
	@Test
	public void testMatchAllDocsQuery() throws Exception{
		Query query = new MatchAllDocsQuery();
		System.out.println(query);
		searchResult(query);
	}
	@Test
	public void testNumbericRangeQuery() throws Exception{
		Query query=NumericRangeQuery.newLongRange("size",1000l,10000l,false,false);
		System.out.println(query);
		searchResult(query);
	}
	@Test
	public void testBooleanQuery() throws Exception{
		BooleanQuery query = new BooleanQuery();
		Query query1= new TermQuery(new Term("content","apache"));
		//添加查询条件
		 Query query2= NumericRangeQuery.newLongRange("size",1000l,10000l,false,false);
		 //
		 query.add(query1, Occur.SHOULD);//相当于OR
		 query.add(query2, Occur.MUST_NOT);//相当于NOT
		 System.out.println(query);
		 searchResult(query);
	}
	@Test
	public void testQueryParser() throws Exception{
		QueryParser queryParser = new QueryParser("content",new IKAnalyzer());
		Query query = queryParser.parse("name:apache");
		System.out.println(query);
		 searchResult(query);
	}
	@Test
	public void testMultiFieldQueryParser() throws Exception{
		String[] fields={"name","content"};
		MultiFieldQueryParser multiFieldQuery = new MultiFieldQueryParser(fields,new IKAnalyzer());
		Query query = multiFieldQuery.parse("lucene是一个基于java开发全文检索工具包");
		System.out.println(query);
		 searchResult(query);
	}
}
