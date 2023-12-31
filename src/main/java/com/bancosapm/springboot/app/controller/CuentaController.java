package com.bancosapm.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancosapm.springboot.app.models.dao.ICuentaDao;
import com.bancosapm.springboot.app.models.entity.Cuenta;

@Controller
public class CuentaController {

	@Autowired
	private ICuentaDao cuentaDao;

	@RequestMapping(value = "lista", method = RequestMethod.GET)
	public String cuentaLista(Model model) {
		model.addAttribute("titulo", "Lista de cuentas");
		model.addAttribute("cuentas", cuentaDao.findAll());
		return "lista";
	}
	
	@RequestMapping(value = "form-cuenta")
	public String crear(Map<String, Object> model) {
		Cuenta cuenta = new Cuenta();
		model.put("cuenta", cuenta);
		model.put("titulo", "Nueva cuenta, llene los datos");
		return "form-cuenta";
	}
	
	@RequestMapping(value = "form-cuenta/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {
		Cuenta cuenta = null;
		
		if(id > 0) {
			cuenta = cuentaDao.findOne(id);
		} else {
			return "redirect:/lista";
		}
		model.put("cuenta", cuenta);
		model.put("titulo", "Edite la cuenta");
		return "form-cuenta";
	}
	
	@RequestMapping(value = "form-cuenta", method = RequestMethod.POST)
	public String guardar(@Valid Cuenta cuenta, BindingResult result, Model model, SessionStatus status, RedirectAttributes flash) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Llene correctamente los campos");
			model.addAttribute("result", result.hasErrors());
			model.addAttribute("mensaje", "Error al enviar los datos, por favor escriba correctamente los campos");
			System.out.println(result.toString());
			return "form-cuenta";
		} else {
			model.addAttribute("result", false);
		}
		
		model.addAttribute("titulo", "Formulario de cuenta");
		model.addAttribute("mensaje", "Se envio la informacion correctamente");
		
		try {
			cuentaDao.save(cuenta);
		} catch (Exception e) {
			e.printStackTrace();
			flash.addFlashAttribute("mensaje", e.getMessage());
		}
		status.setComplete();
		
		return "redirect:form-cuenta";
	}
	
	@RequestMapping(value = "eliminarcuenta/{id}")
	public String eliminar(@PathVariable(value = "id") Long id) {
		if(id > 0) {
			cuentaDao.delete(id);
		}
		return "index";
	}

}
