package com;  
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.SimpleCalcuService;

@RunWith(JUnitPlatform.class) 
class WebAppUnitTest 
{	
	int a=20,b=10;	
	SimpleCalcuService s = new SimpleCalcuService();
	@Test
	public void testAdd() 
	{
		Assertions.assertEquals(s.Add(a,b), 30);        
	}
	@Test
	public void testSub() 
	{
		Assertions.assertEquals(s.Sub(a,b), 10);    
	}
	@Test
	public void testMul() 
	{
		Assertions.assertEquals(s.Mul(a,b), 200);    
	}
	@Test
	public void testDiv() 
	{
		Assertions.assertEquals(s.Div(a,b), 2);    
	}
	@Test
	public void testRem() 
	{
		Assertions.assertEquals(s.Rem(a,b), 0);    
	}
}