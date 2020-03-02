import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Aluno } from '../../../../shared/model/usuarios/aluno.model';
// import { User } from 'app/core';

@Component({
    selector: 'jhi-aluno-detail',
    templateUrl: './aluno-detail.component.html'
})
export class AlunoDetailComponent implements OnInit {
    /*Objetos*/
    aluno: Aluno;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ aluno }) => {
            this.aluno = aluno.body ? aluno.body : aluno;
        });
    }
}
