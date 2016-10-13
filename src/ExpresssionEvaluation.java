import java.awt.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;





public class ExpresssionEvaluation {
	private static final char COM_MARK = '!';
	private static final String INPUT_PROMPT = ">";
	private static final String END_COM = "###";
	
	public static void main(String[] args)
	{
		Expression expression = Expression.instance();
		SimplifyCommand simplifyCommand = SimplifyCommand.instance();
		DerivationCommand derivationCommand = DerivationCommand.instance();
		
		System.out.print(INPUT_PROMPT);
		String line;
		Scanner reader = new Scanner(System.in);
		line = reader.nextLine();
		
		
		while(!line.equals(END_COM)){
			try{
			if (line.charAt(0) != COM_MARK) {//expression condition
				expression.input(line);
				expression.printOri();// print no-para simplified result.
			}else if (line.substring(1, 4).equals("d/d")) {//DerCom condition.
				derivationCommand.input(line);
				derivationCommand.execute();
				expression.printDer();
			}else if (line.substring(1,9).equals("simplify")) {//SimCom condition ,check contains " " at the end;
				simplifyCommand.input(line);
				simplifyCommand.execute();
				expression.printSim();
			
			}else {
				System.out.println("Error, invalid input.");
			}
			}catch (Exception e) {
				System.out.println("Error! "+e.getMessage());
			}
			
			System.out.print(INPUT_PROMPT);
			line = reader.nextLine();
		}
		reader.close();//TODO:unc;
		
	}
}




