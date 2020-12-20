package com.mitocode.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.model.Consulta;
import com.mitocode.repo.IConsultaExamenRepo;
import com.mitocode.repo.IConsultaRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.IConsultaService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ConsultaServiceImpl extends CRUDimpl<Consulta, Integer> implements IConsultaService{

	@Autowired
	private IConsultaRepo repo;
	
	@Autowired
	private IConsultaExamenRepo ceRepo;
	
	@Override
	protected IGenericRepo<Consulta, Integer> getRepo() {
		return repo;
	}
	
	@Transactional
	@Override
	public Consulta registrarTransaccional(ConsultaListaExamenDTO dto) throws Exception {		
		dto.getConsulta().getDetalleConsulta().forEach(det -> det.setConsulta(dto.getConsulta()));
		
		repo.save(dto.getConsulta());
		
		dto.getLstExamen().forEach(exa -> ceRepo.registrar(dto.getConsulta().getIdConsulta(), exa.getIdExamen()));
		
		return dto.getConsulta();
	}

	@Override
	public List<Consulta> buscar(FiltroConsultaDTO filtro) {
		return repo.buscar(filtro.getDni(), filtro.getNombreCompleto());
	}

	@Override
	public List<Consulta> buscarFecha(FiltroConsultaDTO filtro) {
		return repo.buscarFecha(filtro.getFechaConsulta(), filtro.getFechaConsulta().plusDays(1));
	}
	
	@Override
	public List<ConsultaResumenDTO> listarResumen() {
		//cantidad		fecha
		//List<Object[]>
		//[2,	"05/09/2020"]
		//[2,	"12/09/2020"]
		//[6,	"22/08/2020"]
		
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		repo.listarResumen().forEach(x -> {
			ConsultaResumenDTO cr = new ConsultaResumenDTO();
			cr.setCantidad(Integer.parseInt(String.valueOf(x[0])));
			cr.setFecha(String.valueOf(x[1]));
			consultas.add(cr);
		});
		
		return consultas;

	}

	@Override
	public byte[] generarReporte() {
		byte[] data = null;
		
		//Parámetros en el reporte
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("txt_titulo", "Prueba de titulo");
		
		try {
			File file = new ClassPathResource("/reportes/consultas.jasper").getFile();
			
			//JasperReport jasperReport = JasperCompileManager.compileReport("/home/abcd/report/R_D1.jrxml");
	        //JasperFillManager.fillReportToFile(jasperReport,new HashMap<String, Object> (), con);
			
			//JRBeanCollectionDataSource espera la misma estructura que hay en el reporte (fecha y cantidad)
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), parametros, new JRBeanCollectionDataSource(this.listarResumen()));
			data = JasperExportManager.exportReportToPdf(print); //devuelve byte[]
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return data;
	}

}