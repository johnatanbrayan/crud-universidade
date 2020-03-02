import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { TelaInicialComponent } from './tela-inicial.component';

export const telaInicialRoute: Routes = [
    {
        path: '',
        component: TelaInicialComponent,
        canActivate: [UserRouteAccessService]
    }
];
