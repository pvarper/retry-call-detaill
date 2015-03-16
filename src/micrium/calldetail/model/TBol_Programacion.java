package micrium.calldetail.model;

import java.io.Serializable;
import java.sql.Date;


public class TBol_Programacion implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Cod_Ticket;
	private String Id_Client;
	private String Operation;
	private String Ticket_Prioridad;
	private String Ticket_Descripcion;
	private String Email_Cliente;
	private String Telefono_Cliente;
	private String Periodicidad;
	private String Tipo_Solicitud;
	private Date Fecha_Inicial;
	private Date Fecha_Final;
	private Date Fecha_Registro;
	private Date Fecha_Ejecucion;
	private Date Fecha_Fin;
	private String Estado;
	private String Estado_Actual;
	private String Usuario_Creacion;
	private String Usuario_Modificacion;
	private String Usuario_Eliminacion;
	private Date Fecha_Creacion;
	private Date Fecha_Modificacion;
	private Date Fecha_Eliminacion;


		
	

	public TBol_Programacion() {
	}


	public String getOperation() {
		return Operation;
	}


	public void setOperation(String operation) {
		Operation = operation;
	}


	public String getTicket_Prioridad() {
		return Ticket_Prioridad;
	}
	public String getTelefono_Cliente() {
		return Telefono_Cliente;
	}


	public void setTelefono_Cliente(String telefono_Cliente) {
		Telefono_Cliente = telefono_Cliente;
	}


	public void setTicket_Prioridad(String ticket_Prioridad) {
		Ticket_Prioridad = ticket_Prioridad;
	}


	public String getTicket_Descripcion() {
		return Ticket_Descripcion;
	}


	public void setTicket_Descripcion(String ticket_Descripcion) {
		Ticket_Descripcion = ticket_Descripcion;
	}


	public String getEmail_Cliente() {
		return Email_Cliente;
	}


	public void setEmail_Cliente(String email_Cliente) {
		Email_Cliente = email_Cliente;
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


	public String getPeriodicidad() {
		return Periodicidad;
	}


	public void setPeriodicidad(String periodicidad) {
		Periodicidad = periodicidad;
	}


	public String getTipo_Solicitud() {
		return Tipo_Solicitud;
	}


	public void setTipo_Solicitud(String tipo_Solicitud) {
		Tipo_Solicitud = tipo_Solicitud;
	}


	public Date getFecha_Inicial() {
		return Fecha_Inicial;
	}


	public void setFecha_Inicial(Date fecha_Inicial) {
		Fecha_Inicial = fecha_Inicial;
	}


	public Date getFecha_Final() {
		return Fecha_Final;
	}


	public void setFecha_Final(Date fecha_Final) {
		Fecha_Final = fecha_Final;
	}


	public Date getFecha_Registro() {
		return Fecha_Registro;
	}


	public void setFecha_Registro(Date fecha_Registro) {
		Fecha_Registro = fecha_Registro;
	}


	public Date getFecha_Ejecucion() {
		return Fecha_Ejecucion;
	}


	public void setFecha_Ejecucion(Date fecha_Ejecucion) {
		Fecha_Ejecucion = fecha_Ejecucion;
	}


	public Date getFecha_Fin() {
		return Fecha_Fin;
	}


	public void setFecha_Fin(Date fecha_Fin) {
		Fecha_Fin = fecha_Fin;
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


	public String getUsuario_Creacion() {
		return Usuario_Creacion;
	}


	public void setUsuario_Creacion(String usuario_Creacion) {
		Usuario_Creacion = usuario_Creacion;
	}


	public String getUsuario_Modificacion() {
		return Usuario_Modificacion;
	}


	public void setUsuario_Modificacion(String usuario_Modificacion) {
		Usuario_Modificacion = usuario_Modificacion;
	}


	public String getUsuario_Eliminacion() {
		return Usuario_Eliminacion;
	}


	public void setUsuario_Eliminacion(String usuario_Eliminacion) {
		Usuario_Eliminacion = usuario_Eliminacion;
	}


	public Date getFecha_Creacion() {
		return Fecha_Creacion;
	}


	public void setFecha_Creacion(Date fecha_Creacion) {
		Fecha_Creacion = fecha_Creacion;
	}


	public Date getFecha_Modificacion() {
		return Fecha_Modificacion;
	}


	public void setFecha_Modificacion(Date fecha_Modificacion) {
		Fecha_Modificacion = fecha_Modificacion;
	}


	public Date getFecha_Eliminacion() {
		return Fecha_Eliminacion;
	}


	public void setFecha_Eliminacion(Date fecha_Eliminacion) {
		Fecha_Eliminacion = fecha_Eliminacion;
		
	}

	


/*	@Override
	public String toString() {
		return "{correoId:" + correoId + ", tipoAdjunto:" + tipoAdjunto + ", adjunto:" + adjunto + ", asunto:" + asunto + ", destTo:" + destTo
				+ ", destCc:" + destCc + ", fecha:" + fecha + ", hash:" + hash + ", mensaje:" + mensaje + ", programado:" + programado + ", enviado:"
				+ enviado + ", pendiente:" + pendiente + ", detalleId:" + detalleId + "}";
	}*/

}