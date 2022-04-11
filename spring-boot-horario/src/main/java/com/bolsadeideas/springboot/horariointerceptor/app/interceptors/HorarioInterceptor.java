package com.bolsadeideas.springboot.horariointerceptor.app.interceptors;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component("HorarioInterceptor")
public class HorarioInterceptor implements HandlerInterceptor {

	@Value("${config.horario.apertura}")
	private Integer apertura;

	@Value("${config.horario.cierre}")
	private Integer cierre;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Obtenemos la hora actual
		Calendar calendar = Calendar.getInstance();
		int hora = calendar.get(Calendar.HOUR_OF_DAY);

		if (hora >= apertura && hora < cierre) {
			StringBuilder mensaje = new StringBuilder("Bienvenido al horario de atención a clientes");
			mensaje.append(", atendemos desde las ");
			mensaje.append(apertura);
			mensaje.append("hrs. hasta las ");
			mensaje.append(cierre).append("hrs. Gracias por tu visita");

			// Pasamos el mensaje en el request
			request.setAttribute("mensaje", mensaje.toString());
			return true;
		}
		// Si es falso lo redirigimos a otro sitio
		response.sendRedirect(request.getContextPath().concat("/cerrado"));
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// Obtenemos el mensaje del request
		String mensaje = (String) request.getAttribute("mensaje");

		// Pasamos el mensaje a la vista
		if(modelAndView != null && handler instanceof HandlerMethod) {				
			modelAndView.addObject("mensajeHorario", mensaje);
		}	
	}

}
