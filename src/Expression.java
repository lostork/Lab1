import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



class Item{//2*x^4*y^2;
	
	 double coef;//系数
	 Map<String,Integer> vars;
	
	public Map<String, Integer> getVars() {
		return vars;
	}
	
	public Item(Item i){
		this.coef = i.coef;
		this.vars = new TreeMap<>(i.vars);//TODO:unc;
	}
	public  Item() {
		;
	}
	
	public  Item(double coef, Map<String, Integer> vars) {
		this.coef = coef;
		this.vars = new TreeMap<>(vars);
	}
	
	public int hashCode()
	{
		int hashcode = 0;
		for (Map.Entry<String, Integer> entry : vars.entrySet()) {
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
	
	public void setCoef(double coef) {
		this.coef = coef;
	}
	
	public boolean hasVariable(String var){
		return vars.containsKey(var);
	}
	
	public void removeVariable(String var) {
		if (hasVariable(var)) {
			vars.remove(var);
		}
	}
	
	/**
	 * ���var������key�У�����expo���ı�
	 * @param var
	 * @param expo
	 */
	public void putVariable(String var, int expo) {
		vars.put(var, expo);
	}
	
	public int getVarExponent(String var){
		if (hasVariable(var)) {
			return vars.get(var);
		} else {
			//throw exception;
			return -1;
		}
	}
	
}

public class Expression{
	private static Expression expression = new Expression();
	
	
	private Expression(){}
	
	public static Expression instance() {
		return expression;
	}
	
	public static final double EPS = 0.00001;
	
	Set<Item> originalExpression = new HashSet<>();
	Set<Item> derivatedExpression= new HashSet<>();
	Set<Item> simplifiedExpression= new HashSet<>();
	Set<Item> tempExpression = new HashSet<>();
	boolean isInput = false;
	
	/**
	 * add item to expression specified by dest, ORI for originalExp,DER for derExp,SIM for simplifiedExp,TMP for tempExp.
	 * ��������ÿ�����ʱ���ϲ��˿ɺϲ���
	 * @param dest
	 * @param item
	 */
	
	
	
	private void addItem(Set<Item> Exp, Item item)
	{
		
		if (!Exp.contains(item)) {// according to Map equals or not;
			Exp.add(item);
		} else {
			double coef = 0;
			for (Item item2 : Exp) {
				if (item2.equals(item)) {
					
					
					//coef = item2.coef + item.coef;
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
		
		
	}
	
	public boolean hasVariable(String s) {
		
		for (Item item : originalExpression) {
			if (item.hasVariable(s)) {
				return true;
			}
		}
		return false;
	}
	
	
	public void addItemToOri(Item item) {
		addItem(originalExpression, item);
	}
	
	public void addItemToDer(Item item) {
		addItem(derivatedExpression, item);
	}
	
	public void addItemToSim(Item item) {
		addItem(simplifiedExpression, item);
	}
	
	public void addItemToTmp(Item item) {
		addItem(tempExpression, item);
	}
	
	public boolean isNum(String s){
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		Matcher isnum = pattern.matcher(s);
		return isnum.matches();
	}
	
	public void input(String s){
		s = s.replaceAll(" ", "");
		Expression expression = Expression.instance();
		expression.originalExpression.clear();
		String[] items =  s.split("\\+");
		for(int i=0;i<items.length;i++){
			if(items[i].contains("-")){
				 String[] itemsnext = items[i].split("-");
				 for(int k=0;k<itemsnext.length;k++){	 
					 Item item = new Item();
					 item.vars = new TreeMap<String,Integer>();
					 if(k==0) item.coef=1;
					 else item.coef = -1;
					 simpMult(itemsnext[k],item);
					 expression.addItemToOri(item);
				}
			 } else{
				Item item = new Item();
				item.coef = 1;
				item.vars = new TreeMap<String,Integer>();
				simpMult(items[i], item);
				expression.addItemToOri(item);
			}
		}
		expression.isInput = true;
	}
	
	public void simpMult(String s, Item item){
		String[] paras = s.split("\\*");
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
	
	private void printExpression(Set<Item> Exp){
		boolean isFirstItem = true;
		for (Item item : Exp) {
			
			boolean CoefAbsEqualsToOne = Math.abs(item.getCoef()) < 1 + EPS 
					&& Math.abs(item.getCoef()) > 1 - EPS;
			if (!isFirstItem) {
//				System.out.print(" + ");
				if (item.IsPositive()) {
					System.out.print(" + ");
					if (!CoefAbsEqualsToOne) {
						System.out.print(item.getCoef());
					}
				} else {
					System.out.print(" - ");
					if (!CoefAbsEqualsToOne) {
						System.out.print(-item.getCoef());
					}
					
				}
			} else {

				if (!CoefAbsEqualsToOne) {
					System.out.print(item.getCoef());
				} else if (!item.IsPositive()) {
					System.out.print("-");
				}
				
				isFirstItem = false;
			}
			
			boolean isFirstVar = true;
			for (Map.Entry<String, Integer> entry : item.getVars().entrySet()) {
				if (!isFirstVar) {
					System.out.print("*");
				}
				 
				if (entry.getValue() == 1) {
					System.out.print(entry.getKey());
				} else {
					System.out.print(entry.getKey() + "^" + entry.getValue());//TODO:unc;
				}
				isFirstVar = false;
			}
			
		}
		System.out.println("");
	}
	//TODO: �Ķ���new�����Ķ����ϸĶ�
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
					//tempItem.coef *= Math.pow(para, item.getVarExponent(var));
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
		printExpression(derivatedExpression);
	}
	
	public void printSim(){
		printExpression(simplifiedExpression);
	}
}