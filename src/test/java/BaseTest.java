import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mark.entity.User;
import com.mark.mapper.UserMapper;
import com.mark.service.UserInter;

/**
 * 测试Mybatis
 * @author Mark
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)//表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})//加载配置文件
public class BaseTest {

	private static Logger logger = Logger.getLogger(BaseTest.class);  
	
	private ApplicationContext ac = null;  
	
	@Autowired
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	UserMapper userMapper;
	
//	@Before  
//	public void before() {  
//		ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");  
//		userInterImpl = (UserInter) ac.getBean("userInterImpl");  
//	}  
  
	/**
	 * 测试mybatis
	 * @throws IOException
	 */
    @Test  
    public void test1() throws IOException {  
    	 SqlSession session = sqlSessionFactory.openSession();
    	 //获取了mapper映射DAO接口
    	 UserMapper userMapper =session.getMapper(UserMapper.class);
    	 List<User> list =userMapper.findAll();
    	 System.out.println(list.get(0).toString().toString());
	}
    
    /**
     * 测试Service接口
     */
    @Test
    public void testService(){
    	List<User> list =userMapper.findAll();
    	System.out.println(list.get(0).toString().toString());
    }
}
