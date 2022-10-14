package pacote1;
public class Info {
	
	//Mudar para ArrayList é muito trabalhoso
	//Programação de iniciante já que não conheciamos ArrayList no início do ano
	
	/*
	 * A classe consegue formar uma lista de diversos tipos de informação,
	 * originalmente para inserir com os tipos certos no banco de dados
	 */
	private char ch; //Atributo fantasma.
	private String str;
	private int integ;
	private double dbl;
	public String typeof; //Determina tipo do objeto, para apenas permitir inserção e leitura
	// do atributo desse tipo
	
	
	public char getCh() {
		return ch;
	}
	public void setCh(char ch) {
		if(typeof == "char") {
			this.ch = ch;
		}
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		if(typeof == "String" ) {
			this.str = str;
		}
	}
	public int getInteg() {
		return integ;
	}
	public void setInteg(int d) {
		if(typeof == "int") {
			this.integ = d;
		}
	}
	public double getDbl() {
		return dbl;
	}
	public void setDbl(double dbl) {
		if(typeof == "double") {
			this.dbl = dbl;
		}
	}	
}