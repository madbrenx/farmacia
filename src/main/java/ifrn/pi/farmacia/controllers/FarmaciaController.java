package ifrn.pi.farmacia.controllers;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ifrn.pi.farmacia.models.Compra;
import ifrn.pi.farmacia.models.Remedio;
import ifrn.pi.farmacia.models.Usuario;
import ifrn.pi.farmacia.repositorys.CompraRepository;
import ifrn.pi.farmacia.repositorys.RemedioRepository;
import ifrn.pi.farmacia.repositorys.UsuarioRepository;

@Controller
@RequestMapping("/farmacia")
public class FarmaciaController {

	@Autowired
	private UsuarioRepository ur;

	@Autowired
	private RemedioRepository rr;

	@Autowired
	private CompraRepository cr;

	@GetMapping("/logar")
	public ModelAndView logar() {
		ModelAndView md = new ModelAndView();
		Usuario us = new Usuario();
		md.addObject(us);
		md.setViewName("login");
		return md;
	}

	@GetMapping("/cadastrar")
	public String cadastrar() {
		return "cadastro";
	}

	@GetMapping("/cadastrarF")
	public String cadastrarF() {
		return "cadastroFun";
	}

	@PostMapping("/salvarUsuarios")
	public String salvarUsuarios(Usuario usuario) {

		List<Usuario> usuarios = ur.findAll();

		for (Usuario us : usuarios) {
			if (us.getEmail().equals(usuario.getEmail())) {
				System.out.println("Email já está em uso");
				return "lista";

			}

		}
		usuario.setTipo("U");
		if (usuarios.isEmpty()) {
			usuario.setTipo("A");
		}

		ur.save(usuario);
		System.out.println("Salvo com sucesso");
		return "redirect:/farmacia/logar";
	}

	@PostMapping("/salvarFuncionario")
	public String salvarFuncionario(Usuario usuario) {

		List<Usuario> usuarios = ur.findAll();

		for (Usuario us : usuarios) {
			if (us.getEmail().equals(usuario.getEmail())) {
				System.out.println("Email já está em uso");
				return cadastrarF();

			}

		}
		usuario.setTipo("F");

		ur.save(usuario);
		System.out.println("Salvo com sucesso");
		return "redirect:/farmacia/remedios";
	}

	@PostMapping("/entrar")
	public String entrar(String email, String senha) {
		System.out.println(email);
		System.out.println(senha);
		Usuario us = ur.findByEmail(email);
		if (us == null) {
			System.out.println("Email errado");
			return "redirect:/farmacia/logar";
		}

		if (us.getSenha().equals(senha)) {
			System.out.println("Senha Certa");
			if (us.getTipo().equals("A")) {
				return "redirect:/farmacia/remedios";
			} else if (us.getTipo().equals("F")) {
				return "redirect:/farmacia/estoqueF";
			}
			return "redirect:/farmacia/catalogo";
		}
		System.out.println("Senha Errada");
		return "redirect:/farmacia/logar";
	}

	@GetMapping("/remedios")
	public ModelAndView form(Remedio remedio) {
		ModelAndView md = new ModelAndView();

		md.setViewName("remedios");
		List<Remedio> remedios = rr.findAll();
		md.addObject("remedios", remedios);

		return md;
	}

	@PostMapping("/remedios/salvar")
	public ModelAndView salvarRemedio(Remedio remedio) {
		ModelAndView md = new ModelAndView();
		remedio.setQuantidade("0");
		remedio.setTexto(remedio.getNome() + " Quantidade pedida: " + "Valor Unitário: " + remedio.getPreco() + "\n");
		rr.save(remedio);
		md.setViewName("redirect:/farmacia/remedios");
		return md;
	}

	@GetMapping("/remedios/{id}/deletar")
	public String apagarRemedio(@PathVariable Long id) {
		Optional<Remedio> opt = rr.findById(id);
		List<Compra> compras = cr.findAll();
		if(!opt.isEmpty()) {
			int i = 0;
			for (Compra compra : compras) {
				if(compra.getRemedios().get(i).equals(opt.get())) {
					compra.getRemedios().remove(i);
					cr.save(compra);
				}
				i++;
			}
			rr.delete(opt.get());

		}
		
		return "redirect:/farmacia/remedios";
	}

	@GetMapping("/estoque")
	public ModelAndView estoque(Remedio remedio) {
		ModelAndView md = new ModelAndView();

		md.setViewName("estoque");
		List<Remedio> remedios = rr.findAll();
		md.addObject("remedios", remedios);

		return md;
	}

	@GetMapping("/estoqueF")
	public ModelAndView estoqueF(Remedio remedio) {
		ModelAndView md = new ModelAndView();

		md.setViewName("estoqueF");
		List<Remedio> remedios = rr.findAll();
		md.addObject("remedios", remedios);

		return md;
	}

	@GetMapping("/estoque/{id}/editar")
	public ModelAndView selecionarItem(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Remedio> opt = rr.findById(id);

		if (opt.isEmpty()) {
			md.setViewName("/farmacia/estoque");
		} else {
			md.setViewName("estoque");
			md.addObject("remedio", opt.get());
			List<Remedio> remedios = rr.findAll();
			md.addObject("remedios", remedios);
		}

		return md;
	}

	@GetMapping("/estoqueF/{id}/editar")
	public ModelAndView selecionarFItem(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Remedio> opt = rr.findById(id);

		if (opt.isEmpty()) {
			md.setViewName("/farmacia/estoqueF");
		} else {
			md.setViewName("estoqueF");
			md.addObject("remedio", opt.get());
			List<Remedio> remedios = rr.findAll();
			md.addObject("remedios", remedios);
		}

		return md;
	}

	@PostMapping("/estoque/salvar")
	public ModelAndView salvarEstoque(Remedio remedio) {
		ModelAndView md = new ModelAndView();
		rr.save(remedio);
		md.setViewName("redirect:/farmacia/estoque");
		return md;
	}

	@PostMapping("/estoqueF/salvar")
	public ModelAndView salvarEstoqueF(Remedio remedio) {
		ModelAndView md = new ModelAndView();
		rr.save(remedio);
		md.setViewName("redirect:/farmacia/estoqueF");
		return md;
	}

	@GetMapping("/catalogo")
	public ModelAndView catalogo() {
		ModelAndView md = new ModelAndView();

		md.setViewName("catalogo");
		List<Remedio> remedios = rr.findAll();
		List<Remedio> remediosEsc = rr.findBySelecionado(true);
		md.addObject("remedios", remedios);
		md.addObject("remediosEsc", remediosEsc);

		return md;
	}

	@GetMapping("/catalogo/{id}/selecionar")
	public ModelAndView adicionarRemedio(@PathVariable Long id) {
		Optional<Remedio> opt = rr.findById(id);

		if (!opt.isEmpty()) {
			Remedio remedio = opt.get();
			remedio.setSelecionado(true);
			rr.save(remedio);
		}

		return catalogo();
	}

	@GetMapping("/catalogo/{id}/retirar")
	public ModelAndView retirarRemedio(@PathVariable Long id) {
		Optional<Remedio> opt = rr.findById(id);

		if (!opt.isEmpty()) {
			Remedio remedio = opt.get();
			remedio.setSelecionado(false);
			rr.save(remedio);
		}

		return catalogo();
	}

	@GetMapping("/compras")
	public ModelAndView compras() {

		List<Compra> lista = cr.findAll();
		Compra compra = lista.get(0);
		ModelAndView mv = new ModelAndView("compra");
		mv.addObject("compra", compra);
		return mv;

	}

	@GetMapping("/compras/sair")
	public String sair() {
		cr.deleteAll();

		return "redirect:/farmacia/catalogo";
	}

	@GetMapping("/catalogo/comprar")
	public ModelAndView comprar(@Valid Compra compra) {
		List<Remedio> remediosEsc = rr.findBySelecionado(true);

		if (!remediosEsc.isEmpty()) {
			compra.setRemedios(remediosEsc);
			String texto = "Remédios da Farmácia XXX \n" + "Endereço: Rua Fulano de Tal Numexo XX \n";
			for (Remedio r : remediosEsc) {
				System.out.println(r.getTexto());
				texto += r.getTexto();
			}
			Double valor = 0.0;
			for (Remedio r : remediosEsc) {
				valor = valor + r.getPreco();
			}
			compra.setValor(valor);
			texto += "Valor da Sacola: " + compra.getValor() + "\n";
			compra.setTexto(texto);
			cr.save(compra);

		}
		for (Remedio r : remediosEsc) {
			r.setSelecionado(false);
			rr.save(r);
		}

		return compras();
	}
}
