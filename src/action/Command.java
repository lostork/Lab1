package action;

import java.util.Map;
import java.util.TreeMap;

public abstract class Command {
	public abstract void execute() throws java.lang.Exception;
	public abstract void input(String s) throws java.lang.Exception;
}

class SimplifyCommand extends Command{
	
	private static SimplifyCommand  simCommand  = new SimplifyCommand();
	Map<String, Double> parameters;
	
	private SimplifyCommand(){
		;
	}
	
	public static SimplifyCommand instance() {
		return simCommand;
	}

	public void execute() throws java.lang.Exception
	{
		final Expression expression = Expression.instance();
		expression.simplify(parameters);
	}
	
	public void input(String sss) throws java.lang.Exception
	{
		sss = sss.substring("!simplify".length());
		if(sss.charAt(0) == ' ') {
			sss = sss.substring(1);
		}
		String[] paras = sss.split(" ");
		this.parameters = new TreeMap<String,Double>();
		for(int i=0;i<paras.length;i++){
			final String[] var = paras[i].split("=");
			this.parameters.put(var[0], Double.parseDouble(var[1]));
		}	
		final Expression expression = Expression.instance();
		if(!expression.isInput)
		{
			throw new Exception("No Expression Input!");
		}
		for (final Map.Entry<String, Double> entry : this.parameters.entrySet()) {
			if (!expression.hasVariable(entry.getKey())) {
				throw new Exception("No variable " + entry.getKey() +"!");
			}
		}
	}
}

class DerivationCommand extends Command{
	
	private static DerivationCommand derCommand = new DerivationCommand();
	private String derVar;
	
	private DerivationCommand(){
		;
	}
	
	public static DerivationCommand instance() {
		return derCommand;
	}
	
	public void execute() throws java.lang.Exception
	{
		final Expression expression = Expression.instance();
		expression.derivate(derVar);
	}
	
	public void input(String sss) throws Exception
	{	
		sss = sss.replaceAll(" ", "");
		sss = sss.substring("!d/d".length());
		final Expression expression = Expression.instance();
		if(!expression.isInput)
		{
			throw new Exception("No Expression Input!");
		}
		if (!expression.hasVariable(sss)) {
			throw new Exception("No variable " + sss +"!");
		}else{
			this.derVar = sss;
		}
	}
}
