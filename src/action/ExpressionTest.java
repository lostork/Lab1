package action;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ExpressionTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Rule  
	public ExpectedException thrown= ExpectedException.none();
	
	
	@Before
	public void setUp() throws Exception {
		 System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(null);
	}
	@Test
	public void testInput1() throws Exception {
		Expression expression = Expression.instance();
		//expression.originalExpression.clear();
		expression.input("3.5*x*y+x^5-3*y+4*foo");
		expression.printOri();
		assertEquals("3.5x*y + 4.0foo + x^5 - 3.0y\r\n", outContent.toString());
	}
	@Test
	public void testInput2() throws Exception {
		Expression expression = Expression.instance();
		//expression.originalExpression.clear();
		expression.input("2.0*x^4-   5.6*y");
		expression.printOri();
		assertEquals("2.0x^4 - 5.6y\r\n", outContent.toString());
	} 

	@Test
	public void testInput3() throws Exception {
		Expression expression = Expression.instance();
		//expression.originalExpression.clear();
		thrown.expect(Exception.class);
		thrown.expectMessage("Invalid Expression!");
		expression.input("3&5+6*4");
		
	}


	@Test
	public void testInput4() throws Exception {
		Expression expression = Expression.instance();
		//expression.originalExpression.clear();
		thrown.expect(Exception.class);
		thrown.expectMessage("String index out of range: 0");
		expression.input("");
		
		}

}
