 package ifrn.pi.farmacia.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity 
public class Compra {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany()
	private List<Remedio> remedios;
	
	private String texto;
	
	private Double valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public List<Remedio> getRemedios() {
		return remedios;
	}

	public void setRemedios(List<Remedio> remediosEsc) {
		this.remedios = remediosEsc;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
public void calcularValor(List<Remedio> remedios) {
		
		for(Remedio r : remedios) {
			this.valor = valor + r.getPreco();
		}
	}
	
	
}
