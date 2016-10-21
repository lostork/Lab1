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
	
	public Item(final Item ipp){
		this.coef = ipp.coef;
		this.vars = new TreeMap<>(ipp.vars);//TODO:unc;
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
	
	public boolean equals(final Object obj) {
		boolean xia = false;
		if (this == obj) {
			xia = true;
		}
		if (obj instanceof Item) {
			final Item item = (Item)obj;
			if (vars.equals(item.vars)) {
				xia = true;
			}
		}
		return xia;
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
		int has = -1;
		if (hasVariable(var)) {
			has = vars.get(var);
		} 
		return has;
	}
}

public class Expression{
	
	public static final double EPS = 0.00001;
	
	Set<Item> originalExpression = new HashSet<>();
	Set<Item> derivatedExpression= new HashSet<>();
	Set<Item> simplifiedExpression= new HashSet<>();
	boolean isInput = false;
	
	private static Expression expressioning = new Expression();
	
	private Expression(){}
	
	public static Expression instance() {
		return expressioning;
	}
	
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
	
	public boolean hasVariable(final String sss) {
		boolean has = false;
		for (final Item item : originalExpression) {
			if (item.hasVariable(sss)) {
				has = true;
				break;
			}
		}
		return has;
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
	
	public boolean isNum(final String sss){
		try {
			   Integer.parseInt(sss);
			   return true;
			  } catch (NumberFormatException e) {
			   return false;
			  }
	}
	
	public void input(String sss) throws java.lang.Exception{
		sss = sss.replaceAll(" ", "");
		final Expression expression = Expression.instance();
		expression.originalExpression.clear();
		final String[] mono =  sss.split("\\+");      
		for(int i=0;i<mono.length;i++){
			if(mono[i].contains("-")){
				 final String[] itemsnext = mono[i].split("-");
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
				simpMult(mono[i], item);
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
				final String[] paratemp = paras[j].split("\\^");
				if(paratemp.length>1){
					if(isNum(paratemp[0]))
					{
						throw new Exception("invalid input.（底数不能为数字）");
					}
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
			final boolean EqualsToOne = Math.abs(item.getCoef()) < 1 + EPS 
					&& Math.abs(item.getCoef()) > 1 - EPS;
			if (isFirstItem==false) {
				if (item.IsPositive()) {
					System.out.print(" + ");
					if (!EqualsToOne) {
						System.out.print(item.getCoef());
					}
				} else {
					System.out.print(" - ");
					if (!EqualsToOne) {
						System.out.print(-item.getCoef());
					}
				}
			} else {
				if (EqualsToOne==false) {
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

	public void derivate(final String derVar) throws java.lang.Exception {
		derivatedExpression.clear();
		for (final Item item : originalExpression) {
			if (item.hasVariable(derVar)) {
				int expo = item.getVarExponent(derVar);
				item.setCoef(item.getCoef()*item.getVarExponent(derVar));
				if ((--expo) == 0) {
					item.removeVariable(derVar);
				} else {
					item.putVariable(derVar, expo);
				}
				addItemToDer(item);
			}
		}
	}
	
	public void simplify(final Map<String, Double> parameters) throws java.lang.Exception {
		simplifiedExpression = new HashSet<>(originalExpression);
		Set<Item> tempExpression = new HashSet<>();
		for (final Map.Entry<String, Double> entry : parameters.entrySet()) {
			final String var = entry.getKey();
			final Double para = entry.getValue();
			for (final Item item : simplifiedExpression) {
				final Item tempItem = new Item(item);
				if (item.hasVariable(var)) {
					tempItem.setCoef(tempItem.getCoef() * Math.pow(para, item.getVarExponent(var)));
					tempItem.removeVariable(var);
				}
				addItem(tempExpression,tempItem);
			}
			simplifiedExpression = tempExpression;
			tempExpression = new HashSet<>();
		}
	}
	
	public void printOri(){
		printExpression(originalExpression);
	}
	
	public void printDer(){
		printExpression(derivatedExpression);
	}
	
	public void printSim(){
		printExpression(simplifiedExpression);
	}
}