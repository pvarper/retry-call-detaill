package micrium.calldetail.model;

import java.io.Serializable;


public class TBol_Parametro implements Serializable {
	private static final long serialVersionUID = 1L;

	private int Id;
	private String Tipo;
	private String Nombre;
	private String Valor;
	private String Descripcion;	
	

	public TBol_Parametro() {
	}


	public int getId() {
		return Id;
	}


	public void setId(int id) {
		Id = id;
	}


	public String getTipo() {
		return Tipo;
	}


	public void setTipo(String tipo) {
		Tipo = tipo;
	}


	public String getNombre() {
		return Nombre;
	}


	public void setNombre(String nombre) {
		Nombre = nombre;
	}


	public String getValor() {
		return Valor;
	}


	public void setValor(String valor) {
		Valor = valor;
	}


	public String getDescripcion() {
		return Descripcion;
	}


	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}



/*	@Override
	public String toString() {
		return "{correoId:" + correoId + ", tipoAdjunto:" + tipoAdjunto + ", adjunto:" + adjunto + ", asunto:" + asunto + ", destTo:" + destTo
				+ ", destCc:" + destCc + ", fecha:" + fecha + ", hash:" + hash + ", mensaje:" + mensaje + ", programado:" + programado + ", enviado:"
				+ enviado + ", pendiente:" + pendiente + ", detalleId:" + detalleId + "}";
	}*/

}