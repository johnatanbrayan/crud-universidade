import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { ProfessorService } from '../professor.service';
import { Professor } from 'app/shared/model/usuarios/professor.model';

@Component({
    selector: 'jhi-professor-delete-dialog',
    templateUrl: './professor-delete-dialog.component.html'
})
export class ProfessorDeleteDialogComponent {
    /*Objetos*/
    professor: Professor;

    constructor(private professorService: ProfessorService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    /*--------CONFIGURACAO---------*/
    confirmDelete(login) {
        this.professorService.delete(login).subscribe(response => {
            this.eventManager.broadcast({
                name: 'professorListModification',
                content: 'Deleted a professor'
            });
            this.activeModal.dismiss(true);
        });
    }
    /*-----------------------------*/

    /*-------CARREGAMENTO------*/
    clear() {
        this.activeModal.dismiss('cancel');
    }
    /*-------------------------*/
}
