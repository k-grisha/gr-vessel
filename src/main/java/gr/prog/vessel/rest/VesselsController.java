package gr.prog.vessel.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/vessel")
public class VesselsController {

	@GetMapping
	public String hello(){
		return "Hello";
	}

}
