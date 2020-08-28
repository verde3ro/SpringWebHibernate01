package mx.gob.queretaro.repository.impl;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import mx.gob.queretaro.bean.CityBean;
import mx.gob.queretaro.exception.InternalException;
import mx.gob.queretaro.mapper.CityRowMapper;
import mx.gob.queretaro.model.City;
import mx.gob.queretaro.repository.ICityRepository;

@Repository
public class CityRepositoryImpl implements ICityRepository {

	private final JdbcOperations jdbc;
	private final Logger log = Logger.getLogger(getClass().getName());

	@Autowired
	public CityRepositoryImpl(JdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public List<City> obtenerTodos() throws InternalException {
		try {
			return jdbc.query("SELECT city_id, city FROM city",
					(rs, rowNum) -> new CityBean(rs.getLong("city_id"), rs.getString("city")));
		} catch (Exception ex) {
			log.error("Ocurrio un eror al obtener las ciudades", ex);
			throw new InternalException("Ocurrio un eror al obtener las ciudades");
		}
	}

	@Override
	public City obtenerPorId(long id) throws InternalException {
		try {
			return jdbc.queryForObject("select * from city where city_id = ?", new CityRowMapper(), id);
		} catch (Exception ex) {
			log.error(String.format("Ocurrio un eror al obtener la ciudad con el %d", id), ex);
			throw new InternalException(String.format("Ocurrio un eror al obtener la ciudad con el %d", id));
		}
	}

	@Override
	public void guardar(City city) throws InternalException {
		try {
			String sql = "insert into city (city, country_id, last_update, status) VALUES (?, ?, ?, ?)";

			jdbc.update(sql, city.getCity(), city.getCountry().getCountryId(), city.getLastUpdate(),
					city.getStatus());
		} catch (Exception ex) {
			log.error("Ocurrio un eror al guardar la ciudad", ex);
			throw new InternalException("Ocurrio un eror al guardar la ciudad");
		}

	}

}

