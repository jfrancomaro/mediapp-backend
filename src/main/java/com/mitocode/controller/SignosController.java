package com.mitocode.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Signos;
import com.mitocode.service.ISignosService;

@RestController
@RequestMapping("/signos")
public class SignosController {

	@Autowired
	private ISignosService service;
	
	@GetMapping
	public ResponseEntity<List<Signos>> listar() throws Exception{
		List<Signos> obj = service.listar();
		return new ResponseEntity<List<Signos>>(obj, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Signos> listarPorId(@PathVariable("id") Integer id) throws Exception{
		Signos signos = service.listarPorId(id);
		if(signos != null) {
			return new ResponseEntity<Signos>(signos, HttpStatus.OK);
		}else {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
	}
	
	@PostMapping
	public ResponseEntity<Signos> registrar(@Valid @RequestBody Signos signos) throws Exception{
		Signos obj = service.registrar(signos);
		return new ResponseEntity<Signos>(obj, HttpStatus.CREATED);
		
	}
	
	@PutMapping
	public ResponseEntity<Signos> modificar(@Valid @RequestBody Signos signos) throws Exception{
		Signos obj = service.modificar(signos);
		return new ResponseEntity<Signos>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) throws Exception{
		Signos obj = service.listarPorId(id);
		if(obj != null) {
			service.eliminar(id);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}else {
			throw new ModeloNotFoundException("ID no encontrado: " + id);
		}
		
	}
}
