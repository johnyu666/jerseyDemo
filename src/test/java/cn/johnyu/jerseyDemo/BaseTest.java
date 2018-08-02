package cn.johnyu.jerseyDemo;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.Test;

import pojo.Customer;

public class BaseTest {
	
	@Test
	public void testGet() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:3000/customers");
		String s=target
				.path("2")
//				.queryParam("page", "1")
				.request()
				.get()
				.readEntity(String.class);
		System.out.println(s);
		client.close();
	}
	
	@Test
	public void testGetOne() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:3000/customers");
		Customer s=target
				.path("2")
//				.queryParam("page", "1")
				.request()
				.get()
				.readEntity(Customer.class);
		System.out.println(s.getCname());
		client.close();
	}
	
	/**
	 * pojo以Map方式出现
	 * @throws Exception
	 */
	@Test
	public void testGetAllMap() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:3000/customers");
		List<Map> s=target
				.request()
				.get()
				.readEntity(List.class);
		for(Map mp:s) {
			for(Object o:mp.keySet()) {
				System.out.println(o+"\t"+mp.get(o));
				
			}
			System.out.println("===================");
		}
		client.close();
	}
	
	/**
	 * 对pojo的集合进行封装处理
	 * @throws Exception
	 */
	@Test
	public void testGetAllPojos() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:3000/customers");
		List<Customer> list=target
				.request(MediaType.APPLICATION_JSON)
				.get()
				.readEntity(new GenericType<List<Customer>>(){});
		for(Customer c:list) {
			System.out.println(c.getCname()+"\t"+c.getId());
		}
		client.close();
	}
	
	/**
	 * rest方式的异步添加数据
	 * @throws Exception
	 */
	@Test
	public void testPost() throws Exception {
		Customer c=new Customer();
		c.setCname("johnyu");
		Client client = ClientBuilder.newClient();
//		client.register(JacksonFeature.class);
//		client.register(ClientResponseFilter.class);
		WebTarget target = client.target("http://localhost:3000/customers");
		
		Customer c1=target
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(c, MediaType.APPLICATION_JSON), Customer.class);
		
		System.out.println(c1.getId());
		client.close();			
	}
	/**
	 * 异步添加数据
	 * @throws Exception
	 */
	@Test
	public void testAsync() throws Exception {
		Customer c=new Customer();
		c.setCname("johnyu");
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:3000/customers");
		Invocation.Builder builder=target.request();
		//返回 JDK，concurrent包中的异步对象
		Future<Customer> future= builder.async()
		.post(Entity.entity(c, MediaType.APPLICATION_JSON)
				,new InvocationCallback<Customer>() {
			@Override
			public void completed(Customer response) {
				System.out.println(response.getId());
			}
			@Override
			public void failed(Throwable throwable) {}
			
		});
		//方法的回调在子线程中执行
		future.get();
		System.out.println("main end....");
		
	}

	
	/**
	 * 异步GET pojo collection
	 * @throws Exception
	 */
	@Test
	public void testFindCollectionAsync() throws Exception {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:3000/customers");
		Invocation.Builder builder=target.request();
		
		//返回 JDK，concurrent包中的异步对象
		Future<List<Customer>> future= builder
				.async()
				.get(new GenericType<List<Customer>>(){});
		//方法的回调在子线程中执行
		List<Customer> list=future.get();
		for(Customer c:list) {
			System.out.println(c.getCname());
		}
		System.out.println("main end....");
		
		
	}
}
