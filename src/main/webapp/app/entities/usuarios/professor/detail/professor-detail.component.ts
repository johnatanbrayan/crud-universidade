import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Professor } from 'app/shared/model/usuarios/professor.model';

@Component({
    selector: 'jhi-professor-detail',
    templateUrl: './professor-detail.component.html'
})
export class ProfessorDetailComponent implements OnInit {
    /*Objetos*/
    professor: Professor;

    constructor(private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.data.subscribe(({ professor }) => {
            this.professor = professor.body ? professor.body : professor;
        });
    }
}
