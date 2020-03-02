package com.forquality.crud.web.rest;

import com.forquality.crud.config.Constants;
import com.forquality.crud.domain.User;
import com.forquality.crud.repository.UserRepository;
import com.forquality.crud.service.AlunoService;
import com.forquality.crud.service.MailService;
import com.forquality.crud.service.dto.AlunoDTO;
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
 * REST controller for managing alunos.
 * <p>
 * This class accesses the Aluno entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between Aluno and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the aluno and the authorities, because people will
 * quite often do relationships with the aluno, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our alunos'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages alunos, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class AlunoResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final AlunoService alunoService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public AlunoResource(AlunoService alunoService, UserRepository userRepository, MailService mailService) {
        this.alunoService = alunoService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * POST  /alunos  : Creates a new aluno.
     * <p>
     * Creates a new aluno if the login and email are not already used, and sends an
     * mail with an activation link.
     * The aluno needs to be activated on creation.
     *
     * @param alunoDTO the aluno to create
     * @return the ResponseEntity with status 201 (Created) and with body the new aluno, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/alunos")
    public ResponseEntity<User> createAluno(@Valid @RequestBody AlunoDTO alunoDTO) throws URISyntaxException {
        log.debug("REST request to save Aluno : {}", alunoDTO);

        if (alunoDTO.getId() != null) {
            throw new BadRequestAlertException("A new aluno cannot already have an ID", "alunoManagement", "idexists");
            // Lowercase the aluno login before comparing with database
        } else if (userRepository.findOneByLogin(alunoDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(alunoDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newAluno = alunoService.createAluno(alunoDTO);
            mailService.sendCreationEmail(newAluno);
            return ResponseEntity.created(new URI("/api/alunos/" + newAluno.getLogin()))
                .headers(HeaderUtil.createAlert( "A aluno is created with identifier " + newAluno.getLogin(), newAluno.getLogin()))
                .body(newAluno);
        }
    }

    /**
     * PUT /alunos : Updates an existing Aluno.
     *
     * @param alunoDTO the aluno to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated aluno
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/alunos")
    public ResponseEntity<AlunoDTO> updateAluno(@Valid @RequestBody AlunoDTO alunoDTO) {
        log.debug("REST request to update Aluno : {}", alunoDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(alunoDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(alunoDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(alunoDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(alunoDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<AlunoDTO> updatedAluno = alunoService.updateAluno(alunoDTO);

        return ResponseUtil.wrapOrNotFound(updatedAluno,
            HeaderUtil.createAlert("A aluno is updated with identifier " + alunoDTO.getLogin(), alunoDTO.getLogin()));
    }

    /**
     * GET /alunos : get all alunos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all alunos
     */
    @GetMapping("/alunos")
    public ResponseEntity<List<User>> getAllAluno(Pageable pageable) {
        final Page<User> page = alunoService.getManagedAlunos(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/alunos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/alunos/authorities")
    public List<String> getAuthorities() {
        return alunoService.getAuthorities();
    }

    /**
     * GET /alunos/:login : get the "login" aluno.
     *
     * @param login the login of the aluno to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" aluno, or with status 404 (Not Found)
     */
    @GetMapping("/alunos/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<AlunoDTO> getAluno(@PathVariable String login) {
        log.debug("REST request to get Aluno : {}", login);
        return ResponseUtil.wrapOrNotFound(
            alunoService.getUserWithAuthoritiesByLogin(login)
                .map(AlunoDTO::new));
    }

    /**
     * DELETE /alunos/:login : delete the "login" Aluno.
     *
     * @param login the login of the aluno to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/alunos/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<Void> deleteAluno(@PathVariable String login) {
        log.debug("REST request to delete Aluno: {}", login);
        alunoService.deleteAluno(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A aluno is deleted with identifier " + login, login)).build();
    }
}
