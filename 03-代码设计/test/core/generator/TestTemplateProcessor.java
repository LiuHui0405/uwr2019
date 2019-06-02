package core.generator;
import core.common.*;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataSourceConfig.class)
@PowerMockIgnore("javax.management.*")
public class TestTemplateProcessor implements DataSourceType{
	//待测试类(SUT)的一个实例。
	private TemplateProcessor tp;
	//依赖类(DOC)的一个实例。
	private DataSourceConfig dsc;

	@Test
	public void testStaticVarExtract() throws Exception {

		//设置待测试类的状态（测试目标方法）
		tp.staticVarExtract("resource/newtemplatezzz.doc");
		//以下进行检查点设置
		DataSource ds = dsc.getConstDataSource();

		List<DataHolder> dhs = ds.getVars();
		DataHolder dh1 = ds.getDataHolder("sex");
		assertNotNull("变量sex解析为空", dh1);
		assertEquals("变量sex值获取错误","Female",dh1.getValue());

		DataHolder dh2 = ds.getDataHolder("readme");
		assertNotNull("变量readme解析为空", dh2);
		assertEquals("变量readme值获取错误","5",dh2.getValue());

		DataHolder dh3 = ds.getDataHolder("testexpr");
		assertNotNull("变量testexpr", dh3);
		assertEquals("变量testexpr的表达式解析错误","${num}+${readme}",dh3.getExpr());
		dh3.fillValue();
		assertEquals("变量testexpr","5.0",dh3.getValue());

		//检测SUT的实际行为模式是否符合预期
		PowerMock.verifyAll();
	}

	@Before
	public void setUp() throws Exception {
		/*
		 *要求使用EasyMock和PowerMock框架完成对被测类TemplateProcessor中的方法staticVarExtract()中的
		 *依赖类DataSourceConfig进行模拟，使得testStaticVarExtract()的测试成功通过。
        */
		ConstDataSource source=new ConstDataSource();//创建ConstDataSource对象
		DataSourceConfig dsc = EasyMock.createMock(DataSourceConfig.class);
		TemplateProcessor professor=new TemplateProcessor();//创建TemplateProcessor对象
		
		EasyMock.expect(dsc.getConstDataSource()).andReturn( source);//EasyMock录制
		EasyMock.expect(dsc.getConstDataSource()).andStubReturn(null);
		
		PowerMock.mockStatic(DataSourceConfig.class); //静态方法录制
        		EasyMock.expect(DataSourceConfig.newInstance()).andReturn(dsc);  
    
		PowerMock.replayAll(dsc);//重放所有的行为。
                     
		tp = new TemplateProcessor();//初始化一个待测试类（SUT）的实例
	}
}
