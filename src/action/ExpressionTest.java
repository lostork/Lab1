package action;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExpressionTest {
	private final Expression expression = Expression.instance();

	@Before
	public void setUp() throws Exception {
		expression.originalExpression.clear();
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testInput1() throws Exception {
		System.out.println("期望输出："+"3.5x*y + 4.0foo + x^5 - 3.0y");
		System.out.print("实际输出：");
		expression.input("3.5*x*y+x^5-3*y+4*foo");
		expression.printExpression(expression.originalExpression);	
	}
	@Test
	public void testInput2() throws Exception {
		System.out.println("期望输出："+"2.0x^4 - 5.6y");
		System.out.print("实际输出：");
		expression.input("2.0*x^4-   5.6*y");
		expression.printExpression(expression.originalExpression);	
	} 

	@Test
	public void testInput3() throws Exception {
		System.out.println("期望输出："+"不是合法表达式");
		System.out.print("实际输出：");
		expression.input("3&5+6*4");
		expression.printExpression(expression.originalExpression);	
	}


	@Test
	public void testInput4() throws Exception {
			System.out.println("期望输出："+"Error! String index out of range: 0");
			System.out.print("实际输出：");
			expression.input("");
			expression.printExpression(expression.originalExpression);	
		}

}
