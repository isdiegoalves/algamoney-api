package com.example.algamoneyapi.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoneyapi.model.Categoria;
import com.example.algamoneyapi.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	@GetMapping
	public List<Categoria> listar(){
		
		return categoriaRepository.findAll();
	}
	
	
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo){
		
		return this.categoriaRepository.findById(codigo)
			      .map(categoria -> ResponseEntity.ok(categoria))
			      .orElse(ResponseEntity.notFound().build());
			}
	
	
	
	@PostMapping
	//@ResponseStatus(HttpStatus.CREATED) 
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva =	categoriaRepository.save(categoria);
		
		/*ServletUriComponentsBuilder irá pegar a partir da uri da requisição atual, adicionar o codigo e setar o header Location no URI*/
		
		URI uri =	ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
		.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		
		response.setHeader("Location", uri.toASCIIString());
	
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
}
