package micrium.calldetail.model;

import java.io.Serializable;

public class TBol_ProgramacionCompleta implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Cod_Ticket;
	private String Id_Client;
	private String Id_Contrato;
	private String Linea;
	private String Estado;
	private String Estado_Actual;
	private String Periodicidad;
	private String Tipo_solicitud;
	private java.util.Date Fecha_inicial;
	private java.util.Date  Fecha_Final;
	private java.util.Date Fecha_ejecucion;

	public TBol_ProgramacionCompleta() {
	}

	public String getCod_Ticket() {
		return Cod_Ticket;
	}

	public void setCod_Ticket(String cod_Ticket) {
		Cod_Ticket = cod_Ticket;
	}

	public String getId_Client() {
		return Id_Client;
	}

	public void setId_Client(String id_Client) {
		Id_Client = id_Client;
	}

	public String getId_Contrato() {
		return Id_Contrato;
	}

	public void setId_Contrato(String id_Contrato) {
		Id_Contrato = id_Contrato;
	}

	public String getLinea() {
		return Linea;
	}

	public void setLinea(String linea) {
		Linea = linea;
	}

	public String getEstado() {
		return Estado;
	}

	public void setEstado(String estado) {
		Estado = estado;
	}

	public String getEstado_Actual() {
		return Estado_Actual;
	}

	public void setEstado_Actual(String estado_Actual) {
		Estado_Actual = estado_Actual;
	}

	public String getPeriodicidad() {
		return Periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		Periodicidad = periodicidad;
	}

	public String getTipo_solicitud() {
		return Tipo_solicitud;
	}

	public void setTipo_solicitud(String tipo_solicitud) {
		Tipo_solicitud = tipo_solicitud;
	}

	public java.util.Date getFecha_inicial() {
		return Fecha_inicial;
	}

	public void setFecha_inicial(java.util.Date fecha_inicial) {
		Fecha_inicial = fecha_inicial;
	}

	public java.util.Date getFecha_Final() {
		return Fecha_Final;
	}

	public void setFecha_Final(java.util.Date fecha_Final) {
		Fecha_Final = fecha_Final;
	}

	public java.util.Date getFecha_ejecucion() {
		return Fecha_ejecucion;
	}

	public void setFecha_ejecucion(java.util.Date fecha_ejecucion) {
		Fecha_ejecucion = fecha_ejecucion;
	}

	


	


/*	@Override
	public String toString() {
		return "{correoId:" + correoId + ", tipoAdjunto:" + tipoAdjunto + ", adjunto:" + adjunto + ", asunto:" + asunto + ", destTo:" + destTo
				+ ", destCc:" + destCc + ", fecha:" + fecha + ", hash:" + hash + ", mensaje:" + mensaje + ", programado:" + programado + ", enviado:"
				+ enviado + ", pendiente:" + pendiente + ", detalleId:" + detalleId + "}";
	}*/

}