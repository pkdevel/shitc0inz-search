package de.pkdevel.shitc0inz.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VolumeController {
	
	private final VolumeService service;
	
	@Autowired
	public VolumeController(final VolumeService service) {
		this.service = service;
	}
	
	@RequestMapping("/volume")
	public String volume(
			@RequestParam(value = "sorting", required = false, defaultValue = "1h") final String sorting,
			@RequestParam(value = "order", required = false, defaultValue = "desc") final String order,
			final Model model) {
		final boolean asc = "asc".equals(order);
		
		final Collection<Volume> volumes = this.service.findAllSymbols()
				.stream()
				.filter(v -> v.getVolumeIncrease1h() != null)
				.filter(v -> v.getVolumeIncrease3h() != null)
				.filter(v -> v.getVolumeIncrease12h() != null)
				.sorted((v1, v2) -> {
					if ("12h".equals(sorting)) {
						return Float.valueOf(v2.getVolumeIncrease12h()).compareTo(Float.valueOf(v1.getVolumeIncrease12h()));
					}
					else if ("3h".equals(sorting)) {
						return Float.valueOf(v2.getVolumeIncrease3h()).compareTo(Float.valueOf(v1.getVolumeIncrease3h()));
					}
					else {
						if (asc) {
							return Float.valueOf(v1.getVolumeIncrease1h()).compareTo(Float.valueOf(v2.getVolumeIncrease1h()));
						}
						return Float.valueOf(v2.getVolumeIncrease1h()).compareTo(Float.valueOf(v1.getVolumeIncrease1h()));
					}
				})
				.collect(Collectors.toList());
		
		model.addAttribute("sorting", sorting);
		model.addAttribute("order", order);
		model.addAttribute("volumes", volumes);
		
		return "volume";
	}
}
