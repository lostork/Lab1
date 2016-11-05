package action;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Item{
	
	 double coef;
	 Map<String,Integer> vars;
	
	public Map<String, Integer> getVars() {
		return vars;
	}
	
	public Item(final Item item){
		this.coef = item.coef;
		this.vars = new TreeMap<>(item.vars);//TODO:unc;
	}
	public  Item() {
		;
	}
	
	public  Item(final double coef, final Map<String, Integer> vars) {
		this.coef = coef;
		this.vars = new TreeMap<>(vars);
	}
	
	public int hashCode()
	{
		int hashcode = 0;
		for (final Map.Entry<String, Integer> entry : vars.entrySet()) {
			hashcode += entry.getKey().hashCode()*entry.getValue().intValue();
		}
		return hashcode;
	}

	public boolean IsPositive() {
		return coef > 0;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && obj instanceof Item) {
			Item item = (Item)obj;
			if (vars.equals(item.vars)) {
				return true;
			}
		}
		return false;
		
	}
	
	public double getCoef() {
		return coef;
	}
	
	public void setCoef(final double coef) {
		this.coef = coef;
	}
	
	public boolean hasVariable(final String var){
		return vars.containsKey(var);
	}
	
	public void removeVariable(final String var) {
		if (hasVariable(var)) {
			vars.remove(var);
		}
	}
	
	public void putVariable(final String var, final int expo) {
		vars.put(var, expo);
	}
	
	public int getVarExponent(final String var){
		int expo = -1;
		if (hasVariable(var)) {
			expo = vars.get(var);
		} 
		return expo;
	}
}

public class Expression{
	
	public static final double EPS = 0.00001;
	
	Set<Item> originalExpression = new HashSet<>();
	Set<Item> derivatedExpression= new HashSet<>();
	Set<Item> simplifiedExpression= new HashSet<>();
	Set<Item> tempExpression = new HashSet<>();
	boolean isInput = false;
	
	private static Expression expression = new Expression();
	
	private Expression(){}
	
	public static Expression instance() {
		return expression;
	}
	
	/**
	 * add item to expression specified by dest, ORI for originalExp,DER for derExp,SIM for simplifiedExp,TMP for tempExp.
	 * ???????????????????????????
	 * @param dest
	 * @param item
	 */
	private void addItem(final Set<Item> Exp, final Item item)
	{
		if (Exp.contains(item)) {
			double coef = 0;
			for (final Item item2 : Exp) {
				if (item2.equals(item)) {
					coef = item2.getCoef() + item.getCoef();
					if (coef < EPS && coef > -EPS) {
						Exp.remove(item);
					} else {
						Exp.remove(item);
						item.setCoef(coef);
						Exp.add(item);
					}
					break;
				}
			}
		}
		else{
			Exp.add(item);
		}
	}
	
	public boolean hasVariable(String string) {
		
		for (Item item : originalExpression) {
			if (item.hasVariable(string)) {
				return true;
			}
		}
		return false;
	}
	
	public void addItemToOri(final Item item) {
		addItem(originalExpression, item);
	}
	
	public void addItemToDer(final Item item) {
		addItem(derivatedExpression, item);
	}
	
	public void addItemToSim(final Item item) {
		addItem(simplifiedExpression, item);
	}
	
	public void addItemToTmp(Item item) {
		addItem(tempExpression, item);
	}
	
	public boolean isNum(final String sss){
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		Matcher isnum = pattern.matcher(sss);
		return isnum.matches();
	}
	
	public void input(String sss) throws java.lang.Exception{
		sss = sss.replaceAll(" ", "");
		final Expression expression = Expression.instance();
		expression.originalExpression.clear();
		final String[] items =  sss.split("\\+");      
		for(int i=0;i<items.length;i++){
			if(items[i].contains("-")){
				 final String[] itemsnext = items[i].split("-");
				 for(int k=0;k<itemsnext.length;k++){	 
					 final Item item = new Item();
					 item.vars = new TreeMap<String,Integer>();
					 if(k==0) {item.coef=1;}
					 else {item.coef = -1;}
					 simpMult(itemsnext[k],item);
					 expression.addItemToOri(item);
				}
			 } else{
				final Item item = new Item();
				item.coef = 1;
				item.vars = new TreeMap<String,Integer>();
				simpMult(items[i], item);
				expression.addItemToOri(item);
			}
		}
		expression.isInput = true;
	}
	
	public void simpMult(final String sss,final Item item) throws java.lang.Exception {
		final String[] paras = sss.split("\\*");
		for(int j=0;j<paras.length;j++){
			if(isNum(paras[j])){
				item.coef = item.coef*Double.parseDouble(paras[j]);
			}else{
				String[] paratemp = paras[j].split("\\^");
				if(paratemp.length!=1){
					if(item.vars.containsKey(paratemp[0])){
						item.vars.put(paratemp[0], item.vars.get(paratemp[0])+Integer.parseInt(paratemp[1]));
					}else{
						item.vars.put(paratemp[0], Integer.parseInt(paratemp[1]));
					}
				}else{
					if(item.vars.containsKey(paras[j])){
						item.vars.put(paras[j], item.vars.get(paras[j])+1);
					}else{
						item.vars.put(paras[j], 1);
					}
				}
			}
		}
	}	
	
	private void printExpression(final Set<Item> Exp){
		boolean isFirstItem = true;
		for (final Item item : Exp) {
			final boolean equalsToOne = Math.abs(item.getCoef()) < 1 + EPS 
					&& Math.abs(item.getCoef()) > 1 - EPS;
			if (isFirstItem==false) {
				if (item.IsPositive()) {
					System.out.print(" + ");
					if (!equalsToOne || item.vars.isEmpty()) {
						System.out.print(item.getCoef());
					}
				} else {
					System.out.print(" - ");
					if (!equalsToOne  || item.vars.isEmpty()) {
						System.out.print(-item.getCoef());
					}
				}
			} else {
				if (equalsToOne==false  || item.vars.isEmpty()) {
					System.out.print(item.getCoef());
				} else if (!item.IsPositive()) {
					System.out.print("-");
				}
				isFirstItem = false;
			}
			boolean isFirstVar = true;
			for (final Map.Entry<String, Integer> entry : item.getVars().entrySet()) {
				if (!isFirstVar) {
					System.out.print("*");
				}
				if (entry.getValue() == 1) {
					System.out.print(entry.getKey());
				} else {
					System.out.print(entry.getKey() + "^" + entry.getValue());
				}
				isFirstVar = false;
			}
		}
		System.out.println("");
	}

public void derivate(String derVar) throws Exception {//TODO: unc;
		
		if (!isInput) {
			throw new Exception("No Expression Input!");
		}
		
		derivatedExpression.clear();
		
		for (Item item : originalExpression) {
			if (item.hasVariable(derVar)) {
				Item itemTemp = new Item();
				int expo = item.getVarExponent(derVar);
				
				//itemTemp.coef = item.coef * expo;
				itemTemp.setCoef(item.getCoef()*expo);
				itemTemp.vars = new TreeMap<String,Integer>(item.vars);
				
				if ((--expo) == 0) {//TODO:unc;
					itemTemp.removeVariable(derVar);
				} else {
					itemTemp.putVariable(derVar, expo);
				}
				
				addItemToDer(itemTemp);
			}
		}
	}
	
	
	
	public void simplify(Map<String, Double> parameters) throws Exception {
		if (!isInput) {
			throw new Exception("No Expression Input!");
		}
		simplifiedExpression = new HashSet<>(originalExpression);//TODO:unc;
		tempExpression = new HashSet<>();
		for (Map.Entry<String, Double> entry : parameters.entrySet()) {
			String var = entry.getKey();
			Double para = entry.getValue();
			
			for (Item item : simplifiedExpression) {
				
				Item tempItem = new Item(item);
				if (item.hasVariable(var)) {
					
					tempItem.setCoef(tempItem.getCoef() * Math.pow(para, item.getVarExponent(var)));
					
					tempItem.removeVariable(var);
				}
				
				addItemToTmp(tempItem);
			}
			
			simplifiedExpression = tempExpression;
			tempExpression = new HashSet<>();
		}
	}
	
	public void printOri(){
		printExpression(originalExpression);
	}
	
	public void printDer(){
		//System.out.println(derivatedExpression);
		printExpression(derivatedExpression);
	}
	
	public void printSim(){
		printExpression(simplifiedExpression);
	}
}