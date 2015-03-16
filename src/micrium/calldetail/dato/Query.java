package micrium.calldetail.dato;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Query {

	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	public static final int RETURN_GENERATED_KEYS = 1;
	private int tipoQuery;

	private static final Logger log = Logger.getLogger(ConectionManager.class);

	public Query(Connection conexion, String sql, int generateKey) throws SQLException {
		this.preparedStatement = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		this.tipoQuery = generateKey;
	}

	public Query(Connection conexion, String sql) throws SQLException {
		this.preparedStatement = conexion.prepareStatement(sql);
		this.tipoQuery = -1;
	}

	public int executeUpdate() throws SQLException {
		int result = preparedStatement.executeUpdate();
		/*if (tipoQuery == RETURN_GENERATED_KEYS) {
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}
		}*/
		return result;
	}

	public ResultSet executeQuery() throws SQLException {
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}

	public void close() {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				log.error("Error al intentar cerrar el PreparedStatement", e);
			}
		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error("Error al intentar cerrar el ResultSet", e);
			}
		}
	}

	public List<Object[]> getResultList() throws SQLException {
		List<Object[]> registros = new ArrayList<Object[]>();
		ResultSet rs = null;
		try {

			rs = preparedStatement.executeQuery();

			int columCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Object[] fila = new Object[columCount];

				for (int i = 1; i <= columCount; i++) {
					fila[i - 1] = rs.getObject(i);
				}
				registros.add(fila);
			}

			log.info("La consulta devolvio " + registros.size() + " registros.");
		} catch (SQLException e) {
			log.error("Error al intentar ejecutar la consulta.", e);

		} finally {

			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("Error al intentar cerrar el ResultSet de la conexion.", e);
				}
			}

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					log.error("Error al intentar cerrar el Statement de la conexion.", e);
				}
			}

		}
		return registros;
	}

	public void setParameter(int parameterIndex, long x) throws SQLException {
		preparedStatement.setLong(parameterIndex, x);
	}

	public void setParameter(int parameterIndex, Timestamp x) throws SQLException {
		preparedStatement.setTimestamp(parameterIndex, x);
	}

	public void setParameter(int parameterIndex, boolean x) throws SQLException {
		preparedStatement.setBoolean(parameterIndex, x);
	}

	public void setParameter(int parameterIndex, int x) throws SQLException {
		preparedStatement.setInt(parameterIndex, x);
	}

	public void setParameter(int parameterIndex, Object object) throws SQLException {
		preparedStatement.setObject(parameterIndex, object);
	}

	public void setParameter(int parameterIndex, String x) throws SQLException {
		preparedStatement.setString(parameterIndex, x);
	}

	public void setParameter(int parameterIndex, Date x) throws SQLException {
		preparedStatement.setDate(parameterIndex, x);
	}

	public void setParameter(int parameterIndex, BigDecimal x) throws SQLException {
		preparedStatement.setBigDecimal(parameterIndex, x);
	}

	public void setNull(int parameterIndex, int type) throws SQLException {
		preparedStatement.setNull(parameterIndex, type);
	}

	public int getTipoQuery() {
		return tipoQuery;
	}

	public void setTipoQuery(int tipoQuery) {
		this.tipoQuery = tipoQuery;
	}

}
