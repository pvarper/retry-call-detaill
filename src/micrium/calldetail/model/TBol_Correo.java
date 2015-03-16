package micrium.calldetail.model;

import java.io.Serializable;
import java.sql.Date;

public class TBol_Correo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String Cod_Ticket;
	private String Correo;
	private String Estado;
	private String Usuario_Creacion;
	private String Usuario_Modificacion;
	private String Usuario_Eliminacion;
	private Date Fecha_Creacion;
	private Date Fecha_Modificacion;
	private Date Fecha_Eliminacion;	
	

	public TBol_Correo() {
	}

	public String getCod_Ticket() {
		return Cod_Ticket;
	}

	public void setCod_Ticket(String cod_Ticket) {
		Cod_Ticket = cod_Ticket;
	}

	public String getCorreo() {
		return Correo;
	}

	public void setCorreo(String correo) {
		Correo = correo;
	}

	public String getEstado() {
		return Estado;
	}



	public void setEstado(String estado) {
		Estado = estado;
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



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



/*	@Override
	public String toString() {
		return "{correoId:" + correoId + ", tipoAdjunto:" + tipoAdjunto + ", adjunto:" + adjunto + ", asunto:" + asunto + ", destTo:" + destTo
				+ ", destCc:" + destCc + ", fecha:" + fecha + ", hash:" + hash + ", mensaje:" + mensaje + ", programado:" + programado + ", enviado:"
				+ enviado + ", pendiente:" + pendiente + ", detalleId:" + detalleId + "}";
	}*/

}