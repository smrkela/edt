package com.evola.edt.managers;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class RegistrationManagerTest {
	
	private RegistrationManager target;
	
	@Before
	public void setup(){
		
		target = new RegistrationManager();
	}
	
	@Test
	public void generateUsername_test(){
		
		Assert.assertEquals(target.USERNAME_MIN_CHARS, target.generateUsername(null, null).length());
		Assert.assertEquals("Mrkela", target.generateUsername("Mrkela", ""));
		Assert.assertEquals("Sasa", target.generateUsername(null, "Sasa"));
		Assert.assertEquals("MiroslavLaza", target.generateUsername("Miroslav", "Lazanski"));
		
		
	}

}
