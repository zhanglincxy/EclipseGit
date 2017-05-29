package com.itheima.lucene.first;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class LuceneFirst {
	

	@Test
	public void createIndex() throws Exception {
		// 1）创建一个java工程
		// 2）把lucene使用的jar包添加到工程中。
		// lucene-analyzers-common-4.10.3.jar
		// lucene-core-4.10.3.jar
		// commons-io.jar
		// 3）创建一个Directory对象。可以保存到内存中。通常都是保存到磁盘上。
		//把索引库保存到内存
		//Directory directory = new RAMDirectory();
		//保存到磁盘
		Directory directory = FSDirectory.open(new File("D:/test/index"));
		// 4）创建一个IndexWriter对象两个参数Directory对象、IndexWriterConfig对象（Version，Analyzer）
		//创建标准分析器
		//Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer = new IKAnalyzer();
		//参数1：lucene当前版本号
		//参数2：分析器对象
		IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, conf);
		// 5）读取磁盘上的文件
		File dir = new File("E:/张琳/黑马49期视频/框架阶段/lucene&solr/00.参考资料/searchsource");
		File[] listFiles = dir.listFiles();
		for (File file : listFiles) {
			// 6）取文件属性
			String fileName = file.getName();
			String filePath = file.getPath();
			String fileContent = FileUtils.readFileToString(file);
			long fileSize = FileUtils.sizeOf(file);
			// 7）创建一个Document对象，向Document中添加域。
			Document document = new Document();
			//参数1：域的名称
			//参数2：域的内容
			//参数3：是否保存内容，只有保存后将来才能从Document中取field的内容。
			Field fieldName = new TextField("name", fileName, Store.YES);
			Field filedPath = new StoredField("path",filePath);
			Field filedContent = new TextField("content", fileContent, Store.NO);
			Field filedSize = new LongField("size", fileSize, Store.YES);
			document.add(fieldName);
			document.add(filedPath);
			document.add(filedContent);
			document.add(filedSize);
			// 8）把Document对象写入索引库
			indexWriter.addDocument(document);
		}
		// 9）关闭IndexWriter对象。
		indexWriter.close();
	}
	@Test
	public void searchIndex() throws Exception{
		// 1）创建一个Directory对象，指定索引库的位置
		Directory directory = FSDirectory.open(new File("D:/test/index"));
		// 2）创建一个IndexReader对象。
		IndexReader indexReader = DirectoryReader.open(directory);
		// 3）创建一个IndexSearcher对象，参数IndexReader。
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 4）创建一个Query对象，包含要搜索域及要搜索的内容。
		Query query = new TermQuery(new Term("name", "apache"));
		
		//5.执行查询  得到一个TopDocs对象
		TopDocs topDocs = indexSearcher.search(query, 10);
		//6.topDocs
		int totalHits = topDocs.totalHits;
		System.out.println("总记录数"+totalHits);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			//7.获得文档id
			int id = scoreDoc.doc;
			//根据id获得文档对象
			Document document = indexSearcher.doc(id);
			//8.根据id取field内容
			System.out.println(document.get("name"));
			System.out.println(document.get("path"));
			System.out.println(document.get("content"));
			System.out.println(document.get("size"));
			
			System.out.println("**************************************************************");
		}
		indexReader.close();
		
	}
	@Test
	public void testTokenStream() throws Exception{
		//1.创建一个分析器
		//Analyzer anlyzer=new StandardAnalyzer();
		Analyzer anlyzer=new IKAnalyzer();
		
		//2.调用分析器对象的tokenStream方法
		TokenStream tokenStream = anlyzer.tokenStream("", "工具软件不要以为“庖丁”只是个厨子，他还有一个身份，就是“帝王”。在《庄子.逍遥游》里，有个故事叫“越俎代庖”，在这个故事里，“庖”是尧帝放勋。在《庄子.大宗师》里，有个故事叫“傅说相武丁”，在这个故事里“丁”是商帝武丁。所以“庖丁”不是一般的厨子，他是上帝的厨子，人间的帝王，人身的主宰，说白了就是上面解释了半天的，人的“真性”。庖丁解牛的“牛”，也不是一般的牛，是《易经》的坤卦，是大地，也是人身。把“庖丁解牛”拆开讲，意思就清楚了，用真性解脱身体的束缚，获得逍遥的法门。这个题目概括了下面的故事。");
		//3.遍历tokenStream对象
			//a.设置引用  代表当前关键字 相当于指针
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			//b.调用reset方法  相当于把指针调到第一个
		tokenStream.reset();
			//c.循环获得tokenStream中的内容
		while(tokenStream.incrementToken()){
			System.out.println(charTermAttribute);
		}
		tokenStream.close();
	}
	
}
