import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { AlunoService } from '../aluno.service';
import { Aluno } from '../../../../shared/model/usuarios/aluno.model';

@Component({
    selector: 'jhi-aluno-delete-dialog',
    templateUrl: './aluno-delete-dialog.component.html'
})
export class AlunoDeleteDialogComponent {
    /*Objetos*/
    aluno: Aluno;

    constructor(private alunoService: AlunoService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    /*--------CONFIGURACAO---------*/
    confirmDelete(login) {
        this.alunoService.delete(login).subscribe(response => {
            this.eventManager.broadcast({
                name: 'alunoListModification',
                content: 'Deleted a aluno'
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
