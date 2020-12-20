package com.mitocode.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.Consulta;

public interface IConsultaRepo extends IGenericRepo<Consulta, Integer>{

	//@Query(JPQL)
	//SELECT p.* FROM CONSULTA C INNER JOIN PACIENTE P ON C.ID_PACIENTE = P.ID_PACIENTE WHERE P.DNI = ?
	//Voy a la ENTIDAD consulta
	//@Param para identificar parámetros
	@Query("FROM Consulta c WHERE c.paciente.dni = :dni OR LOWER(c.paciente.nombres) LIKE %:nombreCompleto% OR LOWER(c.paciente.apellidos) LIKE %:nombreCompleto%")
	List<Consulta> buscar(@Param("dni") String dni, @Param("nombreCompleto") String nombreCompleto);
	
	// X>= BETWEEN Y< | 12-09-2020 - 13-09-2020
	@Query("FROM Consulta c WHERE c.fecha BETWEEN :fechaConsulta AND :fechaSgte")
	List<Consulta> buscarFecha(@Param("fechaConsulta") LocalDateTime fechaConsulta, @Param("fechaSgte") LocalDateTime fechaSgte);
	
	@Query(value = "select * from fn_listarResumen()", nativeQuery = true)
	List<Object[]> listarResumen();
	
	//cantidad		fecha
	//[2,	"05/09/2020"]
	//[2,	"12/09/2020"]
	//[6,	"22/08/2020"]
}