package cn.johnyu.jerseyDemo;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.jackson.JacksonFeature;
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
	
	
	@Test
	public void testGetAll() throws Exception {
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
	
	@Test
	public void testPost() throws Exception {
		Customer c=new Customer();
		c.setCname("johnyu");
		
		
		Client client = ClientBuilder.newClient();
		client.register(JacksonFeature.class);
		client.register(ClientResponseFilter.class);
		WebTarget target = client.target("http://localhost:3000/customers");
		
		Customer c1=target
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(c, MediaType.APPLICATION_JSON), Customer.class);
		
		System.out.println(c1.getId());
		client.close();
				
	}
}
