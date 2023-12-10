package Red;

import java.io.Serializable;

public class Paquete implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int c1,c2,c3;	//Valores que traslada el paquete de cliente a servidor.
	private int pc,pm,ti,td;	//Valores que traslada el paquete de servidor a cliente.
	
	public Paquete(int c1,int c2,int c3)
	{
		this.setC1(c1);
		this.setC2(c2);
		this.setC3(c3);
	}
	
	public Paquete(int pc,int pm,int ti, int td)
	{
		this.setPc(pc);
		this.setPm(pm);
		this.setTi(ti);
		this.setTd(td);
	}

	public int getC1() {
		return c1;
	}

	public void setC1(int c1) {
		this.c1 = c1;
	}

	public int getC2() {
		return c2;
	}

	public void setC2(int c2) {
		this.c2 = c2;
	}

	public int getC3() {
		return c3;
	}

	public void setC3(int c3) {
		this.c3 = c3;
	}

	public int getTi() {
		return ti;
	}

	public void setTi(int ti) {
		this.ti = ti;
	}

	public int getPm() {
		return pm;
	}

	public void setPm(int pm) {
		this.pm = pm;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getTd() {
		return td;
	}

	public void setTd(int td) {
		this.td = td;
	}
}
