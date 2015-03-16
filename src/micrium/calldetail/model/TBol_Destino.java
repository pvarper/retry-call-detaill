package micrium.calldetail.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class TBol_Destino implements Serializable {
	private static final long serialVersionUID = 1L;

	private int Codigo;
	private String Descripcion;
	private String Destino;
	private String Usuario_Creacion;
	private String Usuario_Modificacion;
	private String Usuario_Eliminacion;
	private Timestamp Fecha_Creacion;
	private Timestamp Fecha_Modificacion;
	private Timestamp Fecha_Eliminacion;	
	

	public TBol_Destino() {
	}


	public int getCodigo() {
		return Codigo;
	}



	public void setCodigo(int codigo) {
		Codigo = codigo;
	}



	public String getDestino() {
		return Destino;
	}



	public void setDestino(String destino) {
		Destino = destino;
	}



	public String getDescripcion() {
		return Descripcion;
	}

	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
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



	public Timestamp getFecha_Creacion() {
		return Fecha_Creacion;
	}



	public void setFecha_Creacion(Timestamp fecha_Creacion) {
		Fecha_Creacion = fecha_Creacion;
	}



	public Timestamp getFecha_Modificacion() {
		return Fecha_Modificacion;
	}



	public void setFecha_Modificacion(Timestamp fecha_Modificacion) {
		Fecha_Modificacion = fecha_Modificacion;
	}



	public Timestamp getFecha_Eliminacion() {
		return Fecha_Eliminacion;
	}



	public void setFecha_Eliminacion(Timestamp fecha_Eliminacion) {
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