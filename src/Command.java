import java.lang.reflect.Executable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;



abstract public class Command {
	public abstract void execute() throws Exception;
	public abstract void input(String s) throws Exception;
}




class SimplifyCommand extends Command{
	
	private static SimplifyCommand  simplifyCommand  = new SimplifyCommand();
	
	
	private  SimplifyCommand() {
		;// TODO Auto-generated constructor stub
	}
	
	public static SimplifyCommand instance() {
		return simplifyCommand;
	}

	
	
	
	
	Map<String, Double> parameters;
	
	public void execute() throws Exception
	{
		Expression expression = Expression.instance();
		expression.simplify(parameters);
	}
	
	public void input(String s) throws Exception
	{
		s = s.substring("!simplify".length());
		if(s.charAt(0) == ' ') s = s.substring(1);
		String[] paras = s.split(" ");
		this.parameters = new TreeMap();
		for(int i=0;i<paras.length;i++){
			String[] var = paras[i].split("=");
			this.parameters.put(var[0], Double.parseDouble(var[1]));
		}	
		
		Expression expression = Expression.instance();
		
		for (Map.Entry<String, Double> entry : this.parameters.entrySet()) {
			if (!expression.hasVariable(entry.getKey())) {
				throw new Exception("No variable " + entry.getKey() +"!");
			}
		}
	}
}

class DerivationCommand extends Command{
	
	private static DerivationCommand derivationCommand = new DerivationCommand();
	
	
	private DerivationCommand() {
		;// TODO Auto-generated 
	}
	//modification
	public static DerivationCommand instance() {
		return derivationCommand;
	}

	
	
	
	private String derVar;
	
	public void execute() throws Exception
	{
		Expression expression = Expression.instance();
		expression.derivate(derVar);
	}
	
	public void input(String s) throws Exception
	{	
		s = s.replaceAll(" ", "");
		s = s.substring("!d/d".length());
		Expression expression = Expression.instance();
		if (!expression.hasVariable(s)) {
			throw new Exception("No variable " + s +"!");
		}else{
			this.derVar = s;
		}
	}
}
