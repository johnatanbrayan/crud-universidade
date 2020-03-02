import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { AlunoService } from './aluno.service';
import { AlunoComponent } from './aluno.component';
import { AlunoDetailComponent } from './detail/aluno-detail.component';
import { AlunoUpdateComponent } from './update/aluno-update.component';
import { Aluno } from '../../../shared/model/usuarios/aluno.model';

@Injectable({ providedIn: 'root' })
export class AlunoResolve implements Resolve<any> {
    constructor(private service: AlunoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const login = route.params['login'] ? route.params['login'] : null;
        if (login) {
            return this.service.find(login);
        }
        return new Aluno();
    }
}

export const alunoRoute: Routes = [
    {
        path: '',
        component: AlunoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN', 'ALUNO'],
            defaultSort: 'id,asc',
            pageTitle: 'crudUniversidadeApp.titulos.aluno'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':login/view',
        component: AlunoDetailComponent,
        resolve: {
            aluno: AlunoResolve
        },
        data: {
            pageTitle: 'Alunos'
        }
    },
    {
        path: 'new',
        component: AlunoUpdateComponent,
        resolve: {
            aluno: AlunoResolve
        }
    },
    {
        path: ':login/edit',
        component: AlunoUpdateComponent,
        resolve: {
            aluno: AlunoResolve
        }
    }
];
