package mx.gob.queretaro.repository.impl;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import mx.gob.queretaro.bean.CountryBean;
import mx.gob.queretaro.exception.InternalException;
import mx.gob.queretaro.model.Country;
import mx.gob.queretaro.repository.ICountryRepository;

@Repository
public class CountryRepositoryImpl implements ICountryRepository {

	private final JdbcOperations jdbc;
	private final Logger log = Logger.getLogger(getClass().getName());

	@Autowired
	public CountryRepositoryImpl(JdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public List<Country> obtenerTodos() throws InternalException {
		try {
			return jdbc.query(
					"SELECT country.country_id, country.country, country.last_update FROM country",
					(rs, rowNum) -> new CountryBean(rs.getLong("country_id"), rs.getString("country"), rs.getDate("last_update"))
					);
		} catch (Exception ex) {
			log.error("Ocurrio un error al obtener los paises", ex);
			throw new InternalException("Ocurrio un error al obtener los paises");
		}
	}

}
