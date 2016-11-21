package action;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WhiteBoxTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Rule  
	public ExpectedException thrown= ExpectedException.none();
	
	
	@Test
	public void testRoute1() throws Exception {
		DerivationCommand derivationCommand = DerivationCommand.instance();
		thrown.expect(Exception.class);
		thrown.expectMessage("No Expression Input!");
		derivationCommand.execute();
	}
	
	
	
	@Test
	public void testRoute2() throws Exception{
		DerivationCommand derivationCommand = DerivationCommand.instance();
		Expression expression = Expression.instance();
		expression.input("x");
		derivationCommand.input("!d/dx");
		derivationCommand.execute();
		expression.printDer();
		assertEquals("1.0\r\n", outContent.toString());
	}
	
	@Test
	public void testRoute3() throws Exception{
		DerivationCommand derivationCommand = DerivationCommand.instance();
		Expression expression = Expression.instance();
		expression.input("x^2");
		derivationCommand.input("!d/dx");
		derivationCommand.execute();
		expression.printDer();
		assertEquals("2.0x\r\n", outContent.toString());
	}
	
	@Test
	public void testRoute4() throws Exception{
		DerivationCommand derivationCommand = DerivationCommand.instance();
		Expression expression = Expression.instance();
		expression.input("x^2");
		derivationCommand.input("!d/dy");
		derivationCommand.execute();
		expression.printDer();
		assertEquals("0\r\n", outContent.toString());
	}

}
