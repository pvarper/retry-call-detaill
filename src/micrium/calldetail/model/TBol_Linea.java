package micrium.calldetail.model;

import java.io.Serializable;

public class TBol_Linea implements Serializable {
	private static final long serialVersionUID = 1L;

	private String contrato;
	private String linea;
	private String cod_ticket;
	private String id_client;
	private String estado;
	private String reintentador;
	private java.util.Date Fecha_Inicial;
	private java.util.Date  Fecha_Final;
	public TBol_Linea() {
	}
	
	

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getContrato() {
		return contrato;
	}
	
	public String getCod_ticket() {
		return cod_ticket;
	}

	public void setCod_ticket(String cod_ticket) {
		this.cod_ticket = cod_ticket;
	}

	public String getId_client() {
		return id_client;
	}



	public void setId_client(String id_client) {
		this.id_client = id_client;
	}



	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	public String getLinea() {
		return linea;
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}



	public String getReintentador() {
		return reintentador;
	}



	public void setReintentador(String reintentador) {
		this.reintentador = reintentador;
	}



	public java.util.Date getFecha_Inicial() {
		return Fecha_Inicial;
	}



	public void setFecha_Inicial(java.util.Date fecha_Inicial) {
		Fecha_Inicial = fecha_Inicial;
	}



	public java.util.Date getFecha_Final() {
		return Fecha_Final;
	}



	public void setFecha_Final(java.util.Date fecha_Final) {
		Fecha_Final = fecha_Final;
	}

	
}