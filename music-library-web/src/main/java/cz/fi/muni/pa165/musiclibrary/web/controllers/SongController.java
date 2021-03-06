package cz.fi.muni.pa165.musiclibrary.web.controllers;

import cz.fi.muni.pa165.musiclibrary.dto.*;
import cz.fi.muni.pa165.musiclibrary.facade.AlbumFacade;
import cz.fi.muni.pa165.musiclibrary.facade.GenreFacade;
import cz.fi.muni.pa165.musiclibrary.facade.MusicianFacade;
import cz.fi.muni.pa165.musiclibrary.facade.SongFacade;
import cz.fi.muni.pa165.musiclibrary.web.forms.SongCreateDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

/**
 * @author David
 */

@Controller
@RequestMapping("/song")
public class SongController {

	@Autowired
	private SongFacade songFacade;

	@Autowired
	private GenreFacade genreFacade;

	@Autowired
	private AlbumFacade albumFacade;

	@Autowired
	private MusicianFacade musicianFacade;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("songs", songFacade.findAll());
		return "song/list";
	}

	/**
	 * Prepares an empty form.
	 *
	 * @param model data to be displayed
	 * @return JSP page
	 */
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newSong(Model model) {
		List<AlbumDTO> albums = albumFacade.findAll();
		List<GenreDTO> genres = genreFacade.findAll();
		List<MusicianDTO> musicians = musicianFacade.findAll();

		model.addAttribute("songCreate", new SongCreateDTO());
		model.addAttribute("genres", genres);
		model.addAttribute("albums", albums);
		model.addAttribute("musicians", musicians);
		return "song/new";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@Valid @ModelAttribute("songCreate") SongCreateDTO formBean, BindingResult bindingResult,
						 Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach((fe) -> {
				model.addAttribute(fe.getField() + "_error", true);
			});
			return "song/new";
		}

		Long id = songFacade.create(formBean);
		redirectAttributes.addFlashAttribute("alert_success", "Song " + id + " was created");
		return "redirect:" + uriBuilder.path("/song/list").toUriString();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public String delete(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes) {
		SongDTO song = songFacade.findById(id);
		songFacade.delete(id);
		redirectAttributes.addFlashAttribute("alert_success", "Song " + song.getTitle() + " was deleted");
		return "redirect:" + uriBuilder.path("/song/list").toUriString();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable long id, Model model) {
		model.addAttribute("song", songFacade.findById(id));
		return "song/view";
	}

	/**
	 * Spring Validator added to JSR-303 Validator for this @Controller only.
	 * It is useful  for custom validations that are not defined on the form bean by annotations.
	 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html#validation-mvc-configuring
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		if (binder.getTarget() instanceof SongCreateDTO) {
			binder.addValidators(new SongCreateDTOValidator());
		}
	}

}
