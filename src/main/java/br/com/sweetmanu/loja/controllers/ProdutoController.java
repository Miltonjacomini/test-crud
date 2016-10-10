package br.com.sweetmanu.loja.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.sweetmanu.loja.daos.CategoriaDao;
import br.com.sweetmanu.loja.daos.ProdutoDao;
import br.com.sweetmanu.loja.infra.FileSaver;
import br.com.sweetmanu.loja.models.Foto;
import br.com.sweetmanu.loja.models.Produto;

@Controller
@RequestMapping("/produto")
@Transactional
public class ProdutoController {

	@Autowired
	private ProdutoDao productDao;
	@Autowired
	private FileSaver fileSaver;
	@Autowired
	private CategoriaDao categoriaDao;

	@RequestMapping("/form")
	public ModelAndView form(Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produto/form-add");
		return carregarDependencias(modelAndView);
	}

	private ModelAndView carregarDependencias(ModelAndView modelAndView) {
		modelAndView.addObject("categoriaList", categoriaDao.all());
		
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView salvar(MultipartFile foto,@Valid Produto produto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return form(produto);
		}
		
		String caminho = fileSaver.write("produto-fotos", foto);
		Foto novaFoto = new Foto(foto.getOriginalFilename(), caminho);
		produto.getFotos().add(novaFoto);
		
		productDao.salvar(produto);

		return new ModelAndView("redirect:/produto");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ModelAndView load(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("produto/form-update");
		modelAndView.addObject("produto", productDao.findById(id));
		carregarDependencias(modelAndView);
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "0", required = false) int page) {
		ModelAndView modelAndView = new ModelAndView("produto/list");
		modelAndView.addObject("paginatedList", productDao.paginated(page, 10));
		return modelAndView;
	}

	// just because get is easier here. Be my guest if you want to change.
	@RequestMapping(method = RequestMethod.GET, value = "/remover/{id}")
	public String remover(@PathVariable("id") Integer id) {
		Produto produto = productDao.findById(id);
		productDao.remover(produto);
		return "redirect:/produto";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}")
	public ModelAndView atualizar(@PathVariable("id") Integer id, @Valid Produto produto, BindingResult bindingResult) {
		produto.setId(id);
		if (bindingResult.hasErrors()) {
			return carregarDependencias(new ModelAndView("produto/form-update"));
		}
		productDao.atualizar(produto);
		return new ModelAndView("redirect:/produto");
	}
}
