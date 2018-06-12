package gr.prog.vessel.rest;

import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/rest/vessel")
public class VesselsController {

	@GetMapping
	public String hello(@RequestParam Integer port, @RequestParam Timestamp t) {
		System.out.println(t);
		return "Hello";
	}

}
