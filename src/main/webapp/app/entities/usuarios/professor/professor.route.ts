import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { ProfessorService } from './professor.service';
import { ProfessorComponent } from './professor.component';
import { ProfessorDetailComponent } from './detail/professor-detail.component';
import { ProfessorUpdateComponent } from './update/professor-update.component';
import { Professor } from 'app/shared/model/usuarios/professor.model';

@Injectable({ providedIn: 'root' })
export class ProfessorResolve implements Resolve<any> {
    constructor(private service: ProfessorService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const login = route.params['login'] ? route.params['login'] : null;
        if (login) {
            return this.service.find(login);
        }
        return new Professor();
    }
}

export const professorRoute: Routes = [
    {
        path: '',
        component: ProfessorComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'PROFESSOR'],
            defaultSort: 'id,asc',
            pageTitle: 'crudUniversidadeApp.titulos.professor'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':login/view',
        component: ProfessorDetailComponent,
        resolve: {
            professor: ProfessorResolve
        },
        data: {
            pageTitle: 'Professores'
        }
    },
    {
        path: 'new',
        component: ProfessorUpdateComponent,
        resolve: {
            professor: ProfessorResolve
        }
    },
    {
        path: ':login/edit',
        component: ProfessorUpdateComponent,
        resolve: {
            professor: ProfessorResolve
        }
    }
];
