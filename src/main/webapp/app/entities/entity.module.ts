import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'aluno',
                loadChildren: './usuarios/aluno/aluno.module#CrudUniversidadeAlunoModule'
            },
            {
                path: 'professor',
                loadChildren: './usuarios/professor/professor.module#CrudUniversidadeProfessorModule'
            },
            {
                path: 'tela-inicial',
                loadChildren: './tela-inicial/tela-inicial.module#CrudUniversidadeTelaInicialModule'
            },
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CrudUniversidadeEntityModule {}
