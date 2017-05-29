package com.itheima.lucene.first;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class My_ManagerIndex {
private IndexWriter indexWriter;
	
	@Before
	public void init() throws Exception {
		indexWriter = new IndexWriter(FSDirectory.open(new File("D:/temp/JavaEE49/index")),
				new IndexWriterConfig(Version.LATEST, new IKAnalyzer()));
	}
	//1.添加文档
	@Test
	public void addDocument() throws Exception{
		/*//1.创建一个目录对象  打开索引库
		Directory directory=FSDirectory.open(new File("d:/test/index"));
		//2.创建一个 Analyzer
		Analyzer analyzer=new IKAnalyzer();
		//3.创建一个IndexWriterConfig
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
		//4,创建一个IndexWriter打开数据库
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);*/
		//5.创建
		Document document = new Document();
		Field name = new TextField("name1","新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档新添加的文档aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa apache",Store.YES);
		name.setBoost(10f);
		document.add(name);
		document.add(new TextField("name1","新添加的文档3",Store.YES));
		document.add(new TextField("name1","新添加的文档4",Store.YES));
		//6.文档对象添加到索引库
		indexWriter.addDocument(document);
		//7.提交
		indexWriter.commit();
		//8.关闭
		indexWriter.close();
		
	}
	//2.删除文档
	@Test
	public void deleteDocument() throws Exception{
		Query query = new TermQuery(new Term("name1", "新添加的文档3"));
		indexWriter.deleteDocuments(query);
		//提交修改
		indexWriter.commit();
		indexWriter.close();
	}
	@Test
	public void updateDocument() throws Exception{
		Document document = new Document();
		document.add(new TextField("name","更新后的文档1",Store.YES));
		document.add(new TextField("content","更新后的文档1内容",Store.YES));
		document.add(new TextField("name1","更新后的文档1",Store.YES));
		indexWriter.updateDocument(new Term("name","mybatis"), document);
		indexWriter.commit();
		indexWriter.close();
	}
	@Test
	public void test() throws Exception{
		System.out.println("master   ");
	}

}
