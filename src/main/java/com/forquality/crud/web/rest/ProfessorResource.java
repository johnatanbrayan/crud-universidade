package com.forquality.crud.web.rest;

import com.forquality.crud.config.Constants;
import com.forquality.crud.domain.User;
import com.forquality.crud.repository.UserRepository;
import com.forquality.crud.service.ProfessorService;
import com.forquality.crud.service.MailService;
import com.forquality.crud.service.dto.ProfessorDTO;
import com.forquality.crud.web.rest.errors.BadRequestAlertException;
import com.forquality.crud.web.rest.errors.EmailAlreadyUsedException;
import com.forquality.crud.web.rest.errors.LoginAlreadyUsedException;
import com.forquality.crud.web.rest.util.HeaderUtil;
import com.forquality.crud.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing professores.
 * <p>
 * This class accesses the Professor entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between Professor and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the professor and the authorities, because people will
 * quite often do relationships with the professor, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our professores'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages professores, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class ProfessorResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final ProfessorService professorService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public ProfessorResource(ProfessorService professorService, UserRepository userRepository, MailService mailService) {
        this.professorService = professorService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * POST  /professores  : Creates a new professor.
     * <p>
     * Creates a new professor if the login and email are not already used, and sends an
     * mail with an activation link.
     * The professor needs to be activated on creation.
     *
     * @param professorDTO the professor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new professor, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/professores")
    public ResponseEntity<User> createProfessor(@Valid @RequestBody ProfessorDTO professorDTO) throws URISyntaxException {
        log.debug("REST request to save Professor : {}", professorDTO);

        if (professorDTO.getId() != null) {
            throw new BadRequestAlertException("A new professor cannot already have an ID", "professorManagement", "idexists");
            // Lowercase the professor login before comparing with database
        } else if (userRepository.findOneByLogin(professorDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(professorDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newProfessor = professorService.createProfessor(professorDTO);
            mailService.sendCreationEmail(newProfessor);
            return ResponseEntity.created(new URI("/api/professores/" + newProfessor.getLogin()))
                .headers(HeaderUtil.createAlert( "A professor is created with identifier " + newProfessor.getLogin(), newProfessor.getLogin()))
                .body(newProfessor);
        }
    }

    /**
     * PUT /professoress : Updates an existing Professor.
     *
     * @param professorDTO the professor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated professor
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/professores")
    public ResponseEntity<ProfessorDTO> updateProfessor(@Valid @RequestBody ProfessorDTO professorDTO) {
        log.debug("REST request to update Professor : {}", professorDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(professorDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(professorDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(professorDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(professorDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<ProfessorDTO> updatedProfessor = professorService.updateProfessor(professorDTO);

        return ResponseUtil.wrapOrNotFound(updatedProfessor,
            HeaderUtil.createAlert("A professor is updated with identifier " + professorDTO.getLogin(), professorDTO.getLogin()));
    }

    /**
     * GET /professores : get all professores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all professores
     */
    @GetMapping("/professores")
    public ResponseEntity<List<User>> getAllProfessor(Pageable pageable) {
        final Page<User> page = professorService.getManagedProfessores(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/professores/authorities")
    public List<String> getAuthorities() {
        return professorService.getAuthorities();
    }

    /**
     * GET /professores/:login : get the "login" professor.
     *
     * @param login the login of the professor to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" professor, or with status 404 (Not Found)
     */
    @GetMapping("/professores/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<ProfessorDTO> getProfessor(@PathVariable String login) {
        log.debug("REST request to get Professor : {}", login);
        return ResponseUtil.wrapOrNotFound(
            professorService.getUserWithAuthoritiesByLogin(login)
                .map(ProfessorDTO::new));
    }

    /**
     * DELETE /professores/:login : delete the "login" Professor.
     *
     * @param login the login of the professor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/professores/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable String login) {
        log.debug("REST request to delete Professor: {}", login);
        professorService.deleteProfessor(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A professor is deleted with identifier " + login, login)).build();
    }
}
